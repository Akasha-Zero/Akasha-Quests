package akasha.questui.client.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import akasha.questui.Config;
import akasha.questui.client.data.ChapterImage;
import akasha.questui.client.data.ImageSources;
import akasha.questui.client.gfx.Anim;
import akasha.questui.client.gfx.Draw;
import akasha.questui.client.gfx.MipTexture;
import akasha.questui.client.gfx.Palette;
import akasha.questui.client.gfx.Scissor;
import akasha.questui.client.gfx.Shapes;
import akasha.questui.client.gfx.Textures;
import akasha.questui.client.ui.Widget;
import betterquesting.api.storage.BQ_Settings;
import betterquesting.api.utils.RenderUtils;
import betterquesting.api2.client.gui.controls.PanelButtonQuest;
import betterquesting.api2.client.gui.misc.GuiRectangle;
import betterquesting.api2.utils.QuestTranslation;

/**
 * The chapter map: camera, background grid, images, dependency lines and quests (DESIGN §1-§5, §8).
 */
public class QuestMap extends Widget {

    /** Camera per chapter, kept for the session like FTB Quests does. */
    private static final Map<UUID, double[]> CAMERAS = new HashMap<>();

    private static final double GRID = 8;
    private static final double DRAG_SLOP = 3;
    private static final double ICON_FRAC = 0.62;
    private static final double BADGE_FRAC = 0.46;
    private static final double BADGE_OFFSET = 0.72;
    private static final double LINE_FRAC = 0.11;
    /** Item icons smaller than this (device px) are skipped: unreadable, and RenderItem is the costliest call. */
    private static final double ICON_MIN_DEVICE_PX = 6;

    /** Chevron phase per link, advanced by v * dt so a speed change (hover) never makes the chevrons jump. */
    /** Keyed by quest ids: the model (and its QuestLink objects) is rebuilt on every sync. */
    private final Map<String, Double> linkPhase = new HashMap<>();
    private double lastLineTime = -1;

    /** Mouse button state source; replaceable so scripted UI tests can perform drags. */
    public static java.util.function.IntPredicate buttonDown = Mouse::isButtonDown;

    private final MapHost host;
    public final Camera camera = new Camera();
    public final MapEditor editor;
    private ChapterModel model;

    private QuestNode hovered;
    private ChapterImage hoveredImage;
    private PanelButtonQuest tooltipButton;
    private QuestNode tooltipFor;
    private UUID selected;
    private UUID highlight;
    private long highlightUntil;

    // Pointer state
    private int pressButton = -1;
    private int pressX, pressY, lastX, lastY;
    private boolean dragging;
    private boolean editorOwnsPress;
    private QuestNode pressNode;
    private ChapterImage pressImage;
    private boolean pendingFit;

    public QuestMap(MapHost host) {
        this.host = host;
        this.editor = new MapEditor(this);
    }

    public ChapterModel getModel() {
        return model;
    }

    public MapHost getHost() {
        return host;
    }

    /** Shows a chapter, restoring its last camera or fitting it on first view. */
    public void setChapter(ChapterModel m) {
        saveCamera();
        this.model = m;
        hovered = null;
        hoveredImage = null;
        tooltipFor = null;
        linkPhase.clear();
        editor.reset();
        if (m == null) return;
        camera.setBounds(m.bounds(host.isEditing()));
        double[] cam = CAMERAS.get(m.lineId);
        if (cam != null) camera.set(cam[0], cam[1], (int) cam[2]);
        else if (w > 0) camera.fit();
        else pendingFit = true;
    }

    /** Rebuilds the model in place (after a sync), keeping the camera. */
    public void refresh() {
        if (model == null) return;
        model.rebuild();
        camera.setBounds(model.bounds(host.isEditing()));
        editor.onModelRebuilt();
        tooltipFor = null;
    }

    public void saveCamera() {
        if (model != null && w > 0) CAMERAS.put(model.lineId, camera.snapshot());
    }

    public void fit() {
        if (model == null) return;
        camera.setBounds(model.bounds(host.isEditing()));
        camera.fit();
    }

    public void setSelected(UUID questId) {
        this.selected = questId;
    }

    /** Pulse a quest for a few seconds and centre the camera on it (navigation, search, bookmarks). */
    public void focus(UUID questId) {
        if (model == null) return;
        QuestNode n = model.nodes.get(questId);
        if (n == null) return;
        if (camera.getLevel() < 0) camera.set(n.cx(), n.cy(), 0);
        else camera.centerOn(n.cx(), n.cy());
        highlight = questId;
        highlightUntil = Anim.millis() + 3000;
    }

    @Override
    protected void onResize() {
        camera.setViewport(x, y, w, h);
        if (pendingFit && model != null && w > 0) {
            pendingFit = false;
            camera.fit();
        }
    }

    // ------------------------------------------------------------------------------------------
    // Geometry helpers
    // ------------------------------------------------------------------------------------------

    private static double deviceScale() {
        Minecraft mc = Minecraft.getMinecraft();
        return new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
    }

    /** Snap a GUI coordinate to the device pixel grid (DESIGN §1, pixel snapping). */
    private static double snap(double v, double f) {
        return Math.round(v * f) / f;
    }

    public QuestNode nodeAt(int mx, int my) {
        if (model == null || !inside(mx, my)) return null;
        double wx = camera.toWorldX(mx);
        double wy = camera.toWorldY(my);
        QuestNode best = null;
        for (QuestNode n : model.nodes.values()) {
            if (wx < n.x || wy < n.y || wx > n.x + n.w || wy > n.y + n.h) continue;
            Shapes.Shape s = Shapes.get(n.shape);
            double u = (wx - n.cx()) / (n.w / 2);
            double v = (wy - n.cy()) / (n.h / 2);
            if (s.isNone() ? Math.max(Math.abs(u), Math.abs(v)) <= ICON_FRAC : s.contains(u, v)) best = n; // topmost
        }
        return best;
    }

    public ChapterImage imageAt(int mx, int my, boolean editing) {
        if (model == null || !inside(mx, my)) return null;
        double wx = camera.toWorldX(mx);
        double wy = camera.toWorldY(my);
        List<ChapterImage> imgs = model.images;
        for (int i = imgs.size() - 1; i >= 0; i--) {
            ChapterImage img = imgs.get(i);
            if (!imageVisible(img, editing)) continue;
            if (img.contains(wx, wy)) return img;
        }
        return null;
    }

    boolean imageVisible(ChapterImage img, boolean editing) {
        if (editing) return true;
        if (img.editOnly) return false;
        if (!img.dep.isEmpty()) {
            UUID dep = Ids.parse(img.dep);
            if (dep != null) {
                betterquesting.api.questing.IQuest q = betterquesting.questing.QuestDatabase.INSTANCE.get(dep);
                return q != null && q
                    .isComplete(betterquesting.api.api.QuestingAPI.getQuestingUUID(Minecraft.getMinecraft().thePlayer));
            }
        }
        return true;
    }

    // ------------------------------------------------------------------------------------------
    // Rendering
    // ------------------------------------------------------------------------------------------

    @Override
    public void drawPanel(int mx, int my, float partialTick) {
        camera.setViewport(x, y, w, h);
        camera.tick();
        processDrag(mx, my);

        boolean interactive = host.mapInteractive();
        boolean editing = host.isEditing();
        interactive &= !host.covered(mx, my);
        hovered = interactive && !dragging ? nodeAt(mx, my) : null;
        hoveredImage = interactive && !dragging && hovered == null ? imageAt(mx, my, editing) : null;

        Scissor.push(x, y, w, h);
        GL11.glPushMatrix();
        Draw.rect(x, y, x + w, y + h, Palette.mapBg());
        if (model != null) {
            drawBgImage();
            drawGrid();
            drawImages(ChapterImage.LAYER_BACK, editing);
            drawLines();
            drawImages(ChapterImage.LAYER_MIDDLE, editing);
            drawNodes(partialTick);
            drawImages(ChapterImage.LAYER_FRONT, editing);
            if (editing) editor.drawOverlay(mx, my);
        }
        GL11.glPopMatrix();
        Scissor.pop();
    }

    private ResourceLocation bgLocation;

    /** BQ's chapter background, on the same device-snapped grid as the quests so it does not slide while zooming. */
    private void drawBgImage() {
        if (model.bgImage == null) return;
        if (bgLocation == null || !model.bgImage.equals(bgLocation.toString())) {
            bgLocation = new ResourceLocation(model.bgImage);
        }
        double f = deviceScale();
        double s = model.bgSize;
        Draw.bind(bgLocation);
        Draw.tex(
            snap(camera.toScreenX(0), f),
            snap(camera.toScreenY(0), f),
            snap(camera.toScreenX(s), f),
            snap(camera.toScreenY(s), f),
            0xFFFFFFFF);
    }

    /** Two grids at g = 8 and 4g with opacity fading as lines get dense (DESIGN §5). */
    private void drawGrid() {
        double f = deviceScale();
        double px = 1 / f;
        double z = camera.zoom;
        double aMinor = 0.035 * Anim.clamp01((GRID * z - 5) / 7);
        double aMajor = 0.07 * Anim.clamp01((4 * GRID * z - 5) / 7);
        drawGridLines(GRID, aMinor, px, f, 4);
        drawGridLines(4 * GRID, aMajor, px, f, 0);
    }

    private void drawGridLines(double step, double alpha, double px, double f, int skipEvery) {
        if (alpha < 0.002) return;
        int col = Draw.withAlpha(Palette.GRID, (float) alpha);
        double wx0 = camera.toWorldX(x), wx1 = camera.toWorldX(x + w);
        double wy0 = camera.toWorldY(y), wy1 = camera.toWorldY(y + h);
        long i0 = (long) Math.floor(wx0 / step), i1 = (long) Math.ceil(wx1 / step);
        Draw.beginRects(col);
        for (long i = i0; i <= i1; i++) {
            if (skipEvery > 0 && i % skipEvery == 0) continue; // major line drawn by the other pass
            double sx = snap(camera.toScreenX(i * step), f);
            Draw.batchRect(sx, y, sx + px, y + h);
        }
        long j0 = (long) Math.floor(wy0 / step), j1 = (long) Math.ceil(wy1 / step);
        for (long j = j0; j <= j1; j++) {
            if (skipEvery > 0 && j % skipEvery == 0) continue;
            double sy = snap(camera.toScreenY(j * step), f);
            Draw.batchRect(x, sy, x + w, sy + px);
        }
        Draw.endRects();
    }

    private void drawLines() {
        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        QuestNode focus = hovered != null ? hovered : (selected != null ? model.nodes.get(selected) : null);
        double t = Anim.seconds();
        double dt = lastLineTime < 0 ? 0 : Math.max(0, Math.min(0.25, t - lastLineTime));
        lastLineTime = t;
        if (linkPhase.size() > 4 * model.links.size() + 16) linkPhase.clear(); // removed links
        double speed = Config.animateLines ? Config.lineSpeed : 0;
        for (QuestLink link : model.links) {
            boolean touches = focus != null && (link.from == focus || link.to == focus);
            if (link.implicit && !BQ_Settings.alwaysDrawImplicit && !touches && !shift) continue;

            int col = lineColor(link.to.state);
            if (focus != null) {
                if (link.to == focus) col = Palette.L_REQUIRES;
                else if (link.from == focus) col = Palette.L_REQUIRED_FOR;
                else col = Draw.mulAlpha(col, 0.35F);
            }

            double ww = LINE_FRAC * Math.min(link.from.size(), link.to.size()) * (link.to.main ? 1.25 : 1);
            double sw = Math.max(1, ww * camera.zoom);
            MipTexture tex = link.dashed ? Textures.LINE_DASHED
                : (BQ_Settings.showDependencyArrows ? Textures.LINE_CHEVRON : Textures.LINE_PLAIN);
            double v = touches && BQ_Settings.animateDependencyArrows ? Math.max(speed, 0.5) * 3 : speed;
            String key = link.from.id + ">" + link.to.id;
            double phase = (linkPhase.getOrDefault(key, 0.0) + v * dt) % 1.0;
            linkPhase.put(key, phase);
            double ax = camera.toScreenX(link.from.cx()), ay = camera.toScreenY(link.from.cy());
            double bx = camera.toScreenX(link.to.cx()), by = camera.toScreenY(link.to.cy());
            double m = sw + 2;
            if (Math.max(ax, bx) + m < x || Math.min(ax, bx) - m > x + w
                || Math.max(ay, by) + m < y
                || Math.min(ay, by) - m > y + h) continue;
            // quests without a shape have no fill to hide the line: stop it at the icon edge
            double len = Math.hypot(bx - ax, by - ay);
            if (len < 1e-6) continue;
            double ta = trim(link.from) / len, tb = trim(link.to) / len;
            if (ta + tb >= 1) continue;
            double ax2 = ax + (bx - ax) * ta, ay2 = ay + (by - ay) * ta;
            bx = bx - (bx - ax) * tb;
            by = by - (by - ay) * tb;
            ax = ax2;
            ay = ay2;
            // dark casing 1 px each side keeps the line >= 3:1 against any world behind a translucent map (DESIGN §4)
            Draw.line(ax, ay, bx, by, sw + 2, Draw.mulAlpha(Palette.LINE_CASING, ((col >>> 24) & 0xFF) / 255F));
            Draw.bind(tex);
            Draw.band(ax, ay, bx, by, sw, 2 * sw, phase, col);
        }
    }

    /** Screen distance cut from a line end at a quest: the icon half side for shapeless quests, else 0. */
    private double trim(QuestNode n) {
        if (!Shapes.get(n.shape)
            .isNone()) return 0;
        return ICON_FRAC * Math.min(n.w, n.h) / 2 * camera.zoom;
    }

    private static int lineColor(QuestNode.State s) {
        switch (s) {
            case LOCKED:
                return Palette.L_LOCKED;
            case CLAIM:
            case DONE:
                return Palette.L_DONE;
            case REPEAT:
                return Palette.L_REPEAT;
            default:
                return Palette.L_OPEN;
        }
    }

    private void drawNodes(float partialTick) {
        double f = deviceScale();
        boolean editing = host.isEditing();
        for (QuestNode n : model.nodes.values()) {
            double sx0 = snap(camera.toScreenX(n.x), f), sx1 = snap(camera.toScreenX(n.x + n.w), f);
            double sy0 = snap(camera.toScreenY(n.y), f), sy1 = snap(camera.toScreenY(n.y + n.h), f);
            // margin = half size: the glow reaches 2h from the centre, badges about 1.18h
            double mg = Math.max(sx1 - sx0, sy1 - sy0) / 2;
            if (sx1 + mg < x || sy1 + mg < y || sx0 - mg > x + w || sy0 - mg > y + h) continue;
            boolean sel = n.id.equals(selected) || (editing && editor.isSelected(n));
            drawNode(
                n,
                (sx0 + sx1) / 2,
                (sy0 + sy1) / 2,
                (sx1 - sx0) / 2,
                (sy1 - sy0) / 2,
                n == hovered,
                sel,
                f,
                partialTick);
        }
    }

    /** Layered quest drawing, DESIGN §3 steps 1-7. */
    private void drawNode(QuestNode n, double cx, double cy, double hx, double hy, boolean hover, boolean sel, double f,
        float partialTick) {
        Shapes.Shape shape = Shapes.get(n.shape);
        int fill;
        int edge;
        switch (n.state) {
            case LOCKED:
                fill = Palette.Q_LOCKED_FILL;
                edge = Palette.Q_LOCKED_EDGE;
                break;
            case PROGRESS:
                fill = Palette.Q_FILL;
                edge = Palette.Q_PROGRESS_EDGE;
                break;
            case CLAIM:
                fill = Palette.Q_FILL;
                edge = Palette.Q_DONE_EDGE;
                break;
            case DONE:
                fill = Palette.Q_DONE_FILL;
                edge = Palette.Q_DONE_EDGE;
                break;
            case REPEAT:
                fill = Palette.Q_FILL;
                edge = Palette.Q_REPEAT_EDGE;
                break;
            case AVAILABLE:
            default:
                fill = Palette.Q_FILL;
                edge = Palette.Q_AVAILABLE_EDGE;
        }

        boolean highlighted = n.id.equals(highlight) && Anim.millis() < highlightUntil;

        if (!shape.isNone()) {
            // 1. glow: [-2h, 2h]
            if (sel || highlighted) {
                int c = highlighted ? 0xFFFFBF00 : Palette.ACCENT;
                float a = highlighted ? (float) (0.35 + 0.55 * Anim.pulse(0.7)) : 0.85F;
                Draw.bind(shape.glow);
                Draw.tex(cx - 2 * hx, cy - 2 * hy, cx + 2 * hx, cy + 2 * hy, Draw.withAlpha(c, a));
            } else if (n.state == QuestNode.State.AVAILABLE && Config.pulseAvailable) {
                Draw.bind(shape.glow);
                Draw.tex(
                    cx - 2 * hx,
                    cy - 2 * hy,
                    cx + 2 * hx,
                    cy + 2 * hy,
                    Draw.withAlpha(edge, (float) (0.10 + 0.20 * Anim.pulse(2.4))));
            }
            // 2. fill
            Draw.bind(shape.fill);
            Draw.tex(cx - hx, cy - hy, cx + hx, cy + hy, fill);
        } else if (hover || sel) {
            Draw.rect(cx - hx, cy - hy, cx + hx, cy + hy, sel ? Palette.SELECT_FILL : 0x18FFFFFF);
        }

        // 3. icon, side 0.62 * 2h
        double side = ICON_FRAC * 2 * Math.min(hx, hy);
        if (n.icon != null && side * f >= ICON_MIN_DEVICE_PX) {
            Draw.clearDepthIcon(cx, cy, side);
            GL11.glPushMatrix();
            // origin on the device pixel grid so the nearest-sampled item atlas keeps even texel widths
            GL11.glTranslated(snap(cx - side / 2, f), snap(cy - side / 2, f), 0);
            // uniform: Z shrinks with X/Y (depth extent) and GL_RESCALE_NORMAL stays valid (block shading)
            // Z capped: the GUI projection keeps modelview z in [-1000, 1000] and a block icon spans about 4..32
            GL11.glScaled(side / 16.0, side / 16.0, Math.min(side / 16.0, 8));
            n.icon.drawTexture(0, 0, 16, 16, 0F, partialTick);
            GL11.glPopMatrix();
            Draw.afterItem();
        }

        if (!shape.isNone()) {
            Draw.bind(shape.mask);
            // 4. dim locked quests
            if (n.state == QuestNode.State.LOCKED) Draw.tex(cx - hx, cy - hy, cx + hx, cy + hy, 0x8C000000);
            // 5. hover
            if (hover) Draw.tex(cx - hx, cy - hy, cx + hx, cy + hy, 0x1FFFFFFF);
            // 6. outline
            Draw.bind(shape.outline);
            Draw.tex(cx - hx, cy - hy, cx + hx, cy + hy, edge);
        }

        // 7. badges
        double h = Math.min(hx, hy);
        double bs = BADGE_FRAC * 2 * h;
        MipTexture badge = null;
        double scale = 1;
        if (n.state == QuestNode.State.DONE) badge = Textures.BADGE_CHECK;
        else if (n.state == QuestNode.State.CLAIM) {
            badge = Textures.BADGE_ALERT;
            scale = 1 + 0.08 * (2 * Anim.pulse(1.2) - 1);
        } else if (n.state == QuestNode.State.REPEAT) badge = Textures.BADGE_REPEAT;
        if (badge != null && bs >= 3) {
            double r = bs / 2 * scale;
            double bx = cx + BADGE_OFFSET * h, by = cy - BADGE_OFFSET * h;
            Draw.bind(badge);
            Draw.tex(bx - r, by - r, bx + r, by + r, 0xFFFFFFFF);
        }
        if (n.bookmarked && bs >= 3) {
            double r = bs / 2;
            double bx = cx - BADGE_OFFSET * h, by = cy - BADGE_OFFSET * h;
            Draw.bind(Textures.BADGE_PIN);
            Draw.tex(bx - r, by - r, bx + r, by + r, 0xFFFFFFFF);
        }
    }

    private void drawImages(int layer, boolean editing) {
        for (ChapterImage img : model.images) {
            if (img.layer != layer || !imageVisible(img, editing)) continue;
            drawImage(img, editing);
        }
    }

    void drawImage(ChapterImage img, boolean editing) {
        double cx = camera.toScreenX(img.x), cy = camera.toScreenY(img.y);
        double hw = img.w / 2 * camera.zoom, hh = img.h / 2 * camera.zoom;
        double rad = Math.hypot(hw, hh);
        if (cx + rad < x || cx - rad > x + w || cy + rad < y || cy - rad > y + h) return;
        float alpha = img.alpha * (img.editOnly ? 0.5F : 1F);
        int tint = Draw.mulAlpha(img.tint, alpha);
        ImageSources.Resolved r = ImageSources.resolve(img.image);
        if (r.texture != null) {
            Draw.bind(r.texture);
            Draw.texRotated(cx, cy, hw, hh, img.theta(), tint);
        } else if (r.item != null) {
            double side = 2 * Math.min(hw, hh);
            // the rotated square's screen extent is at most side * sqrt(2); clearDepthIcon covers 2 * side
            Draw.clearDepthIcon(cx, cy, side);
            GL11.glPushMatrix();
            GL11.glTranslated(cx, cy, 0);
            GL11.glRotated(img.rot, 0, 0, 1);
            GL11.glScaled(side / 16.0, side / 16.0, Math.min(side / 16.0, 8)); // Z capped, see drawNode
            // RenderItem has no alpha: tint applies, opacity does not (documented in ImageDialog's tooltip)
            RenderUtils.RenderItemStack(Minecraft.getMinecraft(), r.item, -8, -8, "", img.tint | 0xFF000000);
            GL11.glPopMatrix();
            Draw.afterItem();
        } else if (editing) {
            // Missing source: magenta/black checker like vanilla's missing texture, only for editors
            GL11.glPushMatrix();
            GL11.glTranslated(cx, cy, 0);
            GL11.glRotated(img.rot, 0, 0, 1);
            Draw.rect(-hw, -hh, 0, 0, 0xC0F800F8);
            Draw.rect(0, 0, hw, hh, 0xC0F800F8);
            Draw.rect(0, -hh, hw, 0, 0xC0000000);
            Draw.rect(-hw, 0, 0, hh, 0xC0000000);
            GL11.glPopMatrix();
        }
    }

    // ------------------------------------------------------------------------------------------
    // Input
    // ------------------------------------------------------------------------------------------

    @Override
    public boolean onMouseClick(int mx, int my, int button) {
        if (!inside(mx, my) || model == null) return false;
        pressButton = button;
        pressX = lastX = mx;
        pressY = lastY = my;
        dragging = false;
        pressNode = nodeAt(mx, my);
        pressImage = pressNode == null ? imageAt(mx, my, host.isEditing()) : null;
        editorOwnsPress = host.isEditing() && editor.onMouseClick(mx, my, button);
        return true; // consume, including right clicks: BQ would otherwise go "back" on an unused right click
    }

    /** Per-frame drag handling (GuiScreenCanvas has no drag event). */
    private void processDrag(int mx, int my) {
        if (pressButton < 0) return;
        if (!buttonDown.test(pressButton)) {
            // the release went to another panel: finish the gesture here so edits are still committed
            onMouseRelease(mx, my, pressButton);
            return;
        }
        if (!dragging && Math.hypot(mx - pressX, my - pressY) > DRAG_SLOP) dragging = true;
        if (dragging) {
            if (editorOwnsPress && editor.onDrag(mx, my, lastX, lastY, pressButton)) {
                // editor consumed the drag
            } else if (pressButton == 0 || pressButton == 2) {
                camera.pan(mx - lastX, my - lastY);
            }
        }
        lastX = mx;
        lastY = my;
    }

    @Override
    public boolean onMouseRelease(int mx, int my, int button) {
        if (button != pressButton) return false;
        boolean wasDrag = dragging;
        pressButton = -1;
        dragging = false;
        if (editorOwnsPress) {
            editorOwnsPress = false;
            if (editor.onMouseRelease(mx, my, button, wasDrag)) return true;
        }
        if (wasDrag) return true;
        if (button == 0) {
            if (pressNode != null && pressNode == nodeAt(mx, my)) {
                if (pressNode.clickable) host.openQuest(pressNode.id);
            } else if (pressImage != null && !pressImage.click.isEmpty()) {
                host.imageClicked(pressImage);
            }
        } else if (button == 1) {
            if (pressNode != null) host.questContextMenu(pressNode, mx, my);
            else if (pressImage != null && host.isEditing()) host.imageContextMenu(pressImage, mx, my);
            else host.canvasContextMenu(camera.toWorldX(mx), camera.toWorldY(my), mx, my);
        }
        return true;
    }

    @Override
    public boolean onMouseScroll(int mx, int my, int scroll) {
        if (!inside(mx, my) || model == null) return false;
        // BQ passes -sign(dwheel): scroll < 0 is wheel up = zoom in
        camera.zoomStep(scroll < 0 ? 1 : -1, mx, my);
        return true;
    }

    @Override
    public boolean onKeyTyped(char c, int keycode) {
        if (model == null) return false;
        if (host.isEditing() && editor.onKeyTyped(c, keycode)) return true;
        double step = 48;
        switch (keycode) {
            case Keyboard.KEY_LEFT:
                camera.pan(step, 0);
                return true;
            case Keyboard.KEY_RIGHT:
                camera.pan(-step, 0);
                return true;
            case Keyboard.KEY_UP:
                camera.pan(0, step);
                return true;
            case Keyboard.KEY_DOWN:
                camera.pan(0, -step);
                return true;
            case Keyboard.KEY_HOME:
                fit();
                return true;
            default:
                return false;
        }
    }

    @Override
    public List<String> getTooltip(int mx, int my) {
        if (!host.mapInteractive() || dragging) return null;
        if (hovered != null) {
            if (tooltipFor != hovered || tooltipButton == null) {
                tooltipFor = hovered;
                tooltipButton = new PanelButtonQuest(
                    new GuiRectangle(0, 0, 1, 1),
                    -1,
                    "",
                    new java.util.AbstractMap.SimpleEntry<>(hovered.id, hovered.quest));
            }
            // PanelButtonQuest only answers for points inside its own rectangle
            GuiRectangle r = (GuiRectangle) tooltipButton.getTransform();
            r.x = mx;
            r.y = my;
            return tooltipButton.getTooltip(mx, my);
        }
        if (hoveredImage != null) {
            List<String> tt = new ArrayList<>();
            for (String s : hoveredImage.hover) tt.add(QuestTranslation.translate(s));
            if (host.isEditing()) {
                tt.add("§7" + hoveredImage.image);
                if (!hoveredImage.click.isEmpty()) tt.add("§7> " + hoveredImage.click); // ASCII: the default font has
                                                                                        // no arrow glyphs
            }
            return tt.isEmpty() ? null : tt;
        }
        return null;
    }

    public QuestNode getHovered() {
        return hovered;
    }

    public List<QuestNode> nodesSnapshot() {
        return model == null ? Collections.emptyList() : new ArrayList<>(model.nodes.values());
    }
}
