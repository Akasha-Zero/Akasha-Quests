package akasha.questui.client.map;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagList;

import org.lwjgl.input.Keyboard;

import akasha.questui.client.data.ChapterImage;
import akasha.questui.client.data.ImageSources;
import akasha.questui.client.data.UiProps;
import akasha.questui.client.gfx.Draw;
import akasha.questui.client.gfx.Palette;
import akasha.questui.client.ui.Lang;
import betterquesting.api.questing.IQuestLineEntry;
import betterquesting.network.handlers.NetChapterEdit;

/**
 * Edit mode on the map (DESIGN §8): select, move and nudge quests; move, resize, rotate, duplicate and delete
 * images; box selection; undo. Every finished gesture is applied locally and sent with BQ's own chapter packet.
 */
public class MapEditor {

    public static final double SNAP = 4;
    private static final double HANDLE = 3; // half side of a resize handle, screen px
    private static final double ROT_OFFSET = 14; // rotation handle distance above the top edge, screen px
    private static final int UNDO_LIMIT = 64;

    private enum Mode {
        NONE,
        MOVE_NODES,
        MOVE_IMAGE,
        RESIZE_IMAGE,
        ROTATE_IMAGE,
        BOX
    }

    private final QuestMap map;
    private final Set<UUID> selected = new LinkedHashSet<>();
    private int selectedImage = -1;

    private Mode mode = Mode.NONE;
    private double pressWx, pressWy;
    private int handleSx, handleSy;
    private final Map<UUID, double[]> origin = new HashMap<>();
    private ChapterImage imageOrigin;
    private NBTTagList imagesBefore;
    private boolean ctrlPress;
    private int boxX0, boxY0, boxX1, boxY1;

    private final Deque<Runnable> undo = new ArrayDeque<>();

    MapEditor(QuestMap map) {
        this.map = map;
    }

    void reset() {
        selected.clear();
        selectedImage = -1;
        mode = Mode.NONE;
        undo.clear();
    }

    void onModelRebuilt() {
        ChapterModel m = map.getModel();
        selected.retainAll(m.nodes.keySet());
        if (selectedImage >= m.images.size()) selectedImage = -1;
    }

    public boolean isSelected(QuestNode n) {
        return selected.contains(n.id);
    }

    public Set<UUID> getSelection() {
        return selected;
    }

    public ChapterImage getSelectedImage() {
        ChapterModel m = map.getModel();
        return m != null && selectedImage >= 0 && selectedImage < m.images.size() ? m.images.get(selectedImage) : null;
    }

    public void selectImage(ChapterImage img) {
        selectedImage = map.getModel().images.indexOf(img);
        if (selectedImage >= 0) selected.clear();
    }

    public void selectOnly(QuestNode n) {
        selected.clear();
        selected.add(n.id);
        selectedImage = -1;
    }

    private static boolean shift() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }

    // ------------------------------------------------------------------------------------------
    // Pointer
    // ------------------------------------------------------------------------------------------

    /** Left press. Returns true when the press belongs to the editor (the map must not pan). */
    boolean onMouseClick(int mx, int my, int button) {
        if (button != 0) return false;
        Camera cam = map.camera;
        ChapterModel m = map.getModel();
        pressWx = cam.toWorldX(mx);
        pressWy = cam.toWorldY(my);
        ctrlPress = GuiScreen.isCtrlKeyDown();
        mode = Mode.NONE;

        ChapterImage simg = getSelectedImage();
        if (simg != null) {
            int[] handle = handleAt(simg, mx, my);
            if (handle != null) {
                beginImage(simg, handle[0] == 2 ? Mode.ROTATE_IMAGE : Mode.RESIZE_IMAGE);
                handleSx = handle[0] == 2 ? 0 : handle[0];
                handleSy = handle[1];
                return true;
            }
        }

        QuestNode node = map.nodeAt(mx, my);
        if (node != null) {
            if (ctrlPress) {
                if (!selected.remove(node.id)) selected.add(node.id);
            } else if (!selected.contains(node.id)) {
                selectOnly(node);
            }
            selectedImage = -1;
            mode = Mode.MOVE_NODES;
            origin.clear();
            for (UUID id : selected) {
                QuestNode n = m.nodes.get(id);
                if (n != null) origin.put(id, new double[] { n.x, n.y });
            }
            return true;
        }

        ChapterImage img = map.imageAt(mx, my, true);
        if (img != null) {
            selected.clear();
            selectImage(img);
            beginImage(img, Mode.MOVE_IMAGE);
            return true;
        }

        if (shift()) {
            mode = Mode.BOX;
            boxX0 = boxX1 = mx;
            boxY0 = boxY1 = my;
            return true;
        }
        if (!ctrlPress) {
            selected.clear();
            selectedImage = -1;
        }
        return false;
    }

    private void beginImage(ChapterImage img, Mode m) {
        mode = m;
        imageOrigin = img.copy();
        imagesBefore = ChapterImage.writeList(map.getModel().images);
    }

    /** Returns true while the editor owns the drag. */
    boolean onDrag(int mx, int my, int lastX, int lastY, int button) {
        if (button != 0 || mode == Mode.NONE) return false;
        Camera cam = map.camera;
        double wx = cam.toWorldX(mx), wy = cam.toWorldY(my);
        double dx = wx - pressWx, dy = wy - pressWy;
        double unit = shift() ? 1 : SNAP;
        ChapterImage img = getSelectedImage();
        switch (mode) {
            case MOVE_NODES:
                for (Map.Entry<UUID, double[]> e : origin.entrySet()) {
                    QuestNode n = map.getModel().nodes.get(e.getKey());
                    if (n == null) continue;
                    n.x = Geometry.snap(e.getValue()[0] + dx, unit);
                    n.y = Geometry.snap(e.getValue()[1] + dy, unit);
                }
                return true;
            case MOVE_IMAGE:
                if (img == null) return false;
                img.x = Geometry.snap(imageOrigin.x + dx, unit);
                img.y = Geometry.snap(imageOrigin.y + dy, unit);
                return true;
            case RESIZE_IMAGE:
                if (img == null) return false;
                resize(img, dx, dy);
                return true;
            case ROTATE_IMAGE:
                if (img == null) return false;
                img.rot = Geometry.rotation(img.x, img.y, wx, wy, shift());
                return true;
            case BOX:
                boxX1 = mx;
                boxY1 = my;
                return true;
            default:
                return false;
        }
    }

    /** Resize keeping the opposite handle fixed (DESIGN §8, {@link Geometry#resize}). */
    private void resize(ChapterImage img, double dx, double dy) {
        ChapterImage o = imageOrigin;
        boolean keep = shift() || ImageSources.resolve(o.image).item != null;
        double[] r = Geometry.resize(o.x, o.y, o.w, o.h, o.rot, handleSx, handleSy, dx, dy, keep);
        img.x = r[0];
        img.y = r[1];
        img.w = r[2];
        img.h = r[3];
    }

    /** True when the release finished an editor gesture (the map must not treat it as a click). */
    boolean onMouseRelease(int mx, int my, int button, boolean wasDrag) {
        if (button != 0) return false;
        Mode m = mode;
        mode = Mode.NONE;
        switch (m) {
            case MOVE_NODES:
                if (wasDrag) {
                    commitNodes(origin);
                    return true;
                }
                return ctrlPress; // plain click on a quest still opens it
            case MOVE_IMAGE:
            case RESIZE_IMAGE:
            case ROTATE_IMAGE:
                if (wasDrag) commitImages(imagesBefore);
                return true;
            case BOX:
                boxSelect();
                return true;
            default:
                return false;
        }
    }

    private void boxSelect() {
        Camera cam = map.camera;
        double x0 = cam.toWorldX(Math.min(boxX0, boxX1)), x1 = cam.toWorldX(Math.max(boxX0, boxX1));
        double y0 = cam.toWorldY(Math.min(boxY0, boxY1)), y1 = cam.toWorldY(Math.max(boxY0, boxY1));
        if (!ctrlPress) selected.clear();
        selectedImage = -1;
        for (QuestNode n : map.getModel().nodes.values()) {
            if (n.x + n.w >= x0 && n.x <= x1 && n.y + n.h >= y0 && n.y <= y1) selected.add(n.id);
        }
    }

    /**
     * Handle under the cursor as {sx, sy} with sx, sy in {-1, 0, 1}; {2, 2} is the rotation handle. Null if none.
     */
    private int[] handleAt(ChapterImage img, int mx, int my) {
        double[][] pts = handlePoints(img);
        for (double[] p : pts) {
            if (Math.abs(mx - p[0]) <= HANDLE + 1 && Math.abs(my - p[1]) <= HANDLE + 1) {
                return new int[] { (int) p[2], (int) p[3] };
            }
        }
        return null;
    }

    /** Screen positions of the 8 resize handles and the rotation handle: {sx, sy, hx, hy}. */
    private double[][] handlePoints(ChapterImage img) {
        Camera cam = map.camera;
        double cx = cam.toScreenX(img.x), cy = cam.toScreenY(img.y);
        double hw = img.w / 2 * cam.zoom, hh = img.h / 2 * cam.zoom;
        double t = img.theta(), c = Math.cos(t), s = Math.sin(t);
        List<double[]> out = new ArrayList<>();
        int[][] hs = { { -1, -1 }, { 0, -1 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 }, { -1, 0 } };
        for (int[] h : hs) {
            double lx = h[0] * hw, ly = h[1] * hh;
            out.add(new double[] { cx + lx * c - ly * s, cy + lx * s + ly * c, h[0], h[1] });
        }
        double ly = -hh - ROT_OFFSET;
        out.add(new double[] { cx - ly * s, cy + ly * c, 2, 2 });
        return out.toArray(new double[0][]);
    }

    // ------------------------------------------------------------------------------------------
    // Keyboard
    // ------------------------------------------------------------------------------------------

    boolean onKeyTyped(char c, int key) {
        boolean ctrl = GuiScreen.isCtrlKeyDown();
        ChapterImage img = getSelectedImage();
        if (ctrl && key == Keyboard.KEY_Z) {
            undo();
            return true;
        }
        if (ctrl && key == Keyboard.KEY_A) {
            selected.clear();
            selected.addAll(map.getModel().nodes.keySet());
            selectedImage = -1;
            return true;
        }
        if (img != null && (key == Keyboard.KEY_DELETE || key == Keyboard.KEY_BACK)) {
            deleteImage(img);
            return true;
        }
        if (img != null && ctrl && key == Keyboard.KEY_D) {
            duplicateImage(img);
            return true;
        }
        int dx = 0, dy = 0;
        int step = shift() ? 8 : 1;
        if (key == Keyboard.KEY_LEFT) dx = -step;
        else if (key == Keyboard.KEY_RIGHT) dx = step;
        else if (key == Keyboard.KEY_UP) dy = -step;
        else if (key == Keyboard.KEY_DOWN) dy = step;
        if (dx == 0 && dy == 0) return false;
        if (img != null) {
            NBTTagList before = ChapterImage.writeList(map.getModel().images);
            img.x += dx;
            img.y += dy;
            commitImages(before);
            return true;
        }
        if (!selected.isEmpty()) {
            Map<UUID, double[]> before = new HashMap<>();
            for (UUID id : selected) {
                QuestNode n = map.getModel().nodes.get(id);
                if (n == null) continue;
                before.put(id, new double[] { n.x, n.y });
                n.x += dx;
                n.y += dy;
            }
            commitNodes(before);
            return true;
        }
        return false;
    }

    // ------------------------------------------------------------------------------------------
    // Image operations (also used by the context menus and the image dialog)
    // ------------------------------------------------------------------------------------------

    public ChapterImage addImage(double wx, double wy) {
        ChapterModel m = map.getModel();
        NBTTagList before = ChapterImage.writeList(m.images);
        ChapterImage img = new ChapterImage();
        img.image = "akashaquestui:textures/gui/placeholder.png";
        img.x = Geometry.snap(wx, SNAP);
        img.y = Geometry.snap(wy, SNAP);
        m.images.add(img);
        selectImage(img);
        commitImages(before);
        return img;
    }

    public void deleteImage(ChapterImage img) {
        ChapterModel m = map.getModel();
        NBTTagList before = ChapterImage.writeList(m.images);
        if (m.images.remove(img)) {
            selectedImage = -1;
            commitImages(before);
        }
    }

    public void duplicateImage(ChapterImage img) {
        ChapterModel m = map.getModel();
        NBTTagList before = ChapterImage.writeList(m.images);
        ChapterImage copy = img.copy();
        copy.x += 8;
        copy.y += 8;
        m.images.add(copy);
        selectImage(copy);
        commitImages(before);
    }

    /** Moves the image one step in draw order (+1 towards the front). */
    public void reorderImage(ChapterImage img, int delta) {
        List<ChapterImage> list = map.getModel().images;
        int i = list.indexOf(img);
        int j = Math.max(0, Math.min(list.size() - 1, i + delta));
        if (i < 0 || i == j) return;
        NBTTagList before = ChapterImage.writeList(list);
        list.remove(i);
        list.add(j, img);
        selectImage(img);
        commitImages(before);
    }

    /** Replace an image with an edited copy (image dialog). */
    public void replaceImage(ChapterImage old, ChapterImage edited) {
        List<ChapterImage> list = map.getModel().images;
        int i = list.indexOf(old);
        if (i < 0) return;
        NBTTagList before = ChapterImage.writeList(list);
        list.set(i, edited);
        selectedImage = i;
        commitImages(before);
    }

    // ------------------------------------------------------------------------------------------
    // Commit and undo
    // ------------------------------------------------------------------------------------------

    private void pushUndo(Runnable r) {
        undo.push(r);
        while (undo.size() > UNDO_LIMIT) undo.removeLast();
    }

    public void undo() {
        Runnable r = undo.poll();
        if (r != null) r.run();
    }

    /** Writes the image list into the chapter and sends it; `before` becomes the undo state. */
    public void commitImages(NBTTagList before) {
        ChapterModel m = map.getModel();
        m.line.setProperty(UiProps.IMAGES, ChapterImage.writeList(m.images));
        NetChapterEdit.requestEdit(m.lineId, m.line);
        if (before != null) {
            pushUndo(() -> {
                m.images.clear();
                m.images.addAll(ChapterImage.readList(before));
                selectedImage = -1;
                commitImages(null);
            });
        }
    }

    /** Writes moved quest positions into the chapter entries and sends the chapter. */
    private void commitNodes(Map<UUID, double[]> before) {
        ChapterModel m = map.getModel();
        Map<UUID, double[]> prev = new HashMap<>();
        boolean changed = false;
        for (Map.Entry<UUID, double[]> e : before.entrySet()) {
            QuestNode n = m.nodes.get(e.getKey());
            if (n == null) continue;
            prev.put(e.getKey(), e.getValue().clone());
            IQuestLineEntry entry = n.entry;
            int nx = (int) Math.round(n.x), ny = (int) Math.round(n.y);
            if (entry.getPosX() != nx || entry.getPosY() != ny) changed = true;
            entry.setPosition(nx, ny);
        }
        if (!changed) return;
        NetChapterEdit.requestEdit(m.lineId, m.line);
        pushUndo(() -> {
            for (Map.Entry<UUID, double[]> e : prev.entrySet()) {
                QuestNode n = m.nodes.get(e.getKey());
                if (n == null) continue;
                n.x = e.getValue()[0];
                n.y = e.getValue()[1];
                n.entry.setPosition((int) Math.round(n.x), (int) Math.round(n.y));
            }
            NetChapterEdit.requestEdit(m.lineId, m.line);
        });
    }

    // ------------------------------------------------------------------------------------------
    // Overlay
    // ------------------------------------------------------------------------------------------

    void drawOverlay(int mx, int my) {
        Camera cam = map.camera;
        ChapterImage img = getSelectedImage();
        ChapterImage hov = mode == Mode.NONE ? map.imageAt(mx, my, true) : null;
        if (hov != null && hov != img) outline(hov, 0x80FFFFFF);
        if (img != null) {
            outline(img, Palette.SELECT);
            double[][] pts = handlePoints(img);
            // stem to the rotation handle
            double[] top = pts[1], rot = pts[8];
            Draw.line(top[0], top[1], rot[0], rot[1], 1, Palette.SELECT);
            for (int i = 0; i < pts.length; i++) {
                double[] p = pts[i];
                double hsz = HANDLE;
                Draw.rect(p[0] - hsz - 1, p[1] - hsz - 1, p[0] + hsz + 1, p[1] + hsz + 1, 0xFF000000);
                Draw.rect(p[0] - hsz, p[1] - hsz, p[0] + hsz, p[1] + hsz, i == 8 ? Palette.SELECT : Palette.HANDLE);
            }
        }
        if (mode == Mode.BOX) {
            int x0 = Math.min(boxX0, boxX1), x1 = Math.max(boxX0, boxX1);
            int y0 = Math.min(boxY0, boxY1), y1 = Math.max(boxY0, boxY1);
            Draw.rect(x0, y0, x1, y1, Palette.SELECT_FILL);
            Draw.border(x0, y0, x1, y1, 1, Palette.SELECT);
        }

        // Status bar, bottom-left of the map: cursor in world units, selection, snap
        ChapterModel m = map.getModel();
        String status = Lang.get(
            "status",
            (long) Math.floor(cam.toWorldX(mx)),
            (long) Math.floor(cam.toWorldY(my)),
            selected.size(),
            m.nodes.size(),
            shift() ? 1 : (int) SNAP);
        int sw = Draw.font()
            .getStringWidth(status) + 8;
        int sy = map.getY() + map.getH() - 12;
        Draw.rect(map.getX(), sy, map.getX() + sw, sy + 12, 0xC0000000);
        Draw.text(status, map.getX() + 4, sy + 2, Palette.TEXT_2, false);
    }

    private void outline(ChapterImage img, int color) {
        double[][] p = handlePoints(img);
        // corners are indices 0, 2, 4, 6
        int[] k = { 0, 2, 4, 6, 0 };
        for (int i = 0; i < 4; i++) {
            Draw.line(p[k[i]][0], p[k[i]][1], p[k[i + 1]][0], p[k[i + 1]][1], 1, color);
        }
    }
}
