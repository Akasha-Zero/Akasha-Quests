package akasha.questui.client.screen;

import java.net.URI;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.config.Configuration;

import org.lwjgl.input.Keyboard;

import akasha.questui.Config;
import akasha.questui.client.data.ChapterImage;
import akasha.questui.client.data.ImageSources;
import akasha.questui.client.data.UiProps;
import akasha.questui.client.gfx.Anim;
import akasha.questui.client.gfx.Draw;
import akasha.questui.client.gfx.Palette;
import akasha.questui.client.gfx.Shapes;
import akasha.questui.client.map.ChapterModel;
import akasha.questui.client.map.Ids;
import akasha.questui.client.map.MapHost;
import akasha.questui.client.map.QuestMap;
import akasha.questui.client.map.QuestNode;
import akasha.questui.client.ui.ContextMenu;
import akasha.questui.client.ui.Dialog;
import akasha.questui.client.ui.Lang;
import akasha.questui.client.ui.TextPrompt;
import akasha.questui.client.ui.Widget;
import betterquesting.api.api.QuestingAPI;
import betterquesting.api.client.gui.misc.INeedsRefresh;
import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.IQuestLine;
import betterquesting.api.questing.IQuestLineEntry;
import betterquesting.api.storage.BQ_Settings;
import betterquesting.api.utils.UuidConverter;
import betterquesting.api2.cache.QuestCache;
import betterquesting.api2.client.gui.GuiScreenCanvas;
import betterquesting.api2.client.gui.context.IQuestContextMenuEntry;
import betterquesting.api2.client.gui.context.QuestContextMenuRegistry;
import betterquesting.api2.client.gui.panels.IGuiPanel;
import betterquesting.api2.client.gui.popups.PopChoice;
import betterquesting.api2.client.gui.themes.presets.PresetIcon;
import betterquesting.api2.utils.QuestTranslation;
import betterquesting.client.BookmarkHandler;
import betterquesting.client.gui2.GuiBookmarks;
import betterquesting.client.gui2.GuiHome;
import betterquesting.client.gui2.GuiNotificationSettings;
import betterquesting.client.gui2.GuiQuestHelp;
import betterquesting.client.gui2.GuiQuestSearch;
import betterquesting.client.gui2.editors.GuiQuestEditor;
import betterquesting.client.gui2.editors.GuiQuestLinesEditor;
import betterquesting.client.gui2.editors.designer.GuiDesigner;
import betterquesting.client.gui2.party.GuiPartyManage;
import betterquesting.handlers.ConfigHandler;
import betterquesting.network.handlers.NetChapterEdit;
import betterquesting.network.handlers.NetQuestAction;
import betterquesting.network.handlers.NetQuestEdit;
import betterquesting.questing.QuestDatabase;
import betterquesting.questing.QuestLineDatabase;
import betterquesting.questing.QuestLineEntry;
import betterquesting.storage.QuestSettings;

/**
 * The FTB Quests style quest book: chapter panel | map | toolbar, with the quest view on top (DESIGN §6).
 * Replaces BQ's GuiQuestLines and GuiQuest (see ScreenInterceptor) while every action goes through BQ.
 */
public class QuestScreen extends GuiScreenCanvas implements INeedsRefresh, MapHost, ChapterPanel.Host {

    private static UUID selectedChapter;
    private static boolean editing;

    private final ChapterPanel chapters = new ChapterPanel(this);
    private final QuestMap map = new QuestMap(this);
    private final Toolbar toolbar = new Toolbar();
    private final QuestView view = new QuestView(this);
    private final Root root = new Root();
    private ChapterModel model;

    private UUID pendingFocus;
    private boolean pendingOpen;
    /** Popup currently shown (BQ keeps it private). */
    private IGuiPanel popupRef;
    /** Dialog to restore after a BQ picker screen (file browser, item picker, text editor) returns here. */
    private Dialog activeDialog;

    public static QuestScreen create(GuiScreen parent) {
        return new QuestScreen(parent);
    }

    private QuestScreen(GuiScreen parent) {
        super(parent);
        useMargins(false);
        buildToolbar();
    }

    // ------------------------------------------------------------------------------------------
    // Lifecycle
    // ------------------------------------------------------------------------------------------

    @Override
    public void initPanel() {
        super.initPanel();
        popupRef = null;
        GuiHome.bookmark = this;
        addPanel(root);
        reloadChapters();
        // syncs that arrived while a BQ screen (designer, editors, pickers) was open refreshed that screen, not
        // this one, and BQ replaces the chapter's entry objects on sync
        map.refresh();
        view.refresh();
        layout();
        if (pendingFocus != null) applyFocus();
        if (activeDialog != null) openPopup(activeDialog.layoutCentered(width, height));
    }

    @Override
    public void openPopup(IGuiPanel panel) {
        super.openPopup(panel);
        popupRef = panel;
    }

    @Override
    public void closePopup() {
        super.closePopup();
        popupRef = null;
    }

    /** The game screen was shown: forget a dialog that waited for a picker to return here. */
    public static void droppedToGame() {
        if (GuiHome.bookmark instanceof QuestScreen) ((QuestScreen) GuiHome.bookmark).activeDialog = null;
    }

    public void closeDialog() {
        activeDialog = null;
        closePopup();
    }

    /** Leaves for a BQ picker screen; the dialog is reopened when that screen returns here. */
    public void leaveForPicker(Dialog dialog, GuiScreen picker) {
        if (picker == null) return;
        activeDialog = dialog;
        mc.displayGuiScreen(picker);
    }

    /** The popup currently shown, if any (menus, dialogs). */
    public IGuiPanel currentPopup() {
        return popupRef;
    }

    /** Screen centre of a quest on the current map, or null when it is not on it. */
    public int[] questScreenPos(UUID questId) {
        QuestNode n = model == null ? null : model.nodes.get(questId);
        if (n == null) return null;
        return new int[] { (int) Math.round(map.camera.toScreenX(n.cx())),
            (int) Math.round(map.camera.toScreenY(n.cy())) };
    }

    /** Screen centre of the i-th image of the current chapter. */
    public int[] imageScreenPos(int index) {
        if (model == null || index < 0 || index >= model.images.size()) return null;
        ChapterImage img = model.images.get(index);
        return new int[] { (int) Math.round(map.camera.toScreenX(img.x)),
            (int) Math.round(map.camera.toScreenY(img.y)) };
    }

    /** Half width, half height (screen px) and rotation (degrees) of the i-th image. */
    public double[] imageScreenGeometry(int index) {
        if (model == null || index < 0 || index >= model.images.size()) return null;
        ChapterImage img = model.images.get(index);
        return new double[] { img.w / 2 * map.camera.zoom, img.h / 2 * map.camera.zoom, img.rot };
    }

    public void deleteImage(ChapterImage image) {
        map.editor.deleteImage(image);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        map.saveCamera();
    }

    @Override
    public void refreshGui() {
        reloadChapters();
        map.refresh();
        view.refresh();
    }

    /** Map-only refresh (bookmarks, local edits). */
    public void refreshMapOnly() {
        map.refresh();
    }

    private void reloadChapters() {
        List<ChapterInfo> list = ChapterInfo.collect(canEdit());
        chapters.setChapters(list);
        ChapterInfo sel = selectedChapter == null ? null : chapters.find(selectedChapter);
        if (sel == null) {
            // first visible, unlocked chapter
            for (ChapterInfo c : list) {
                if (!c.locked) {
                    sel = c;
                    break;
                }
            }
        }
        UUID id = sel == null ? null : sel.id;
        if (model == null || id == null || !id.equals(model.lineId) || model.line != sel.line) {
            selectedChapter = id;
            model = id == null ? null : new ChapterModel(id, sel.line);
            map.setChapter(model);
        }
    }

    private void layout() {
        int fw = chapters.preferredWidth(width);
        chapters.setFullWidth(fw);
        chapters.setBounds(0, 0, fw, height);
        int left = Config.sidebarPinned ? fw : ChapterPanel.STRIP;
        int right = width - Toolbar.WIDTH;
        map.setBounds(left, 0, right - left, height);
        toolbar.setBounds(right, 0, Toolbar.WIDTH, height);
        view.layoutIn(left, 0, right - left, height);
        root.setBounds(0, 0, width, height);
    }

    // ------------------------------------------------------------------------------------------
    // Navigation
    // ------------------------------------------------------------------------------------------

    /** Show the chapter containing the quest, centre it and optionally open its view. */
    public void focusQuest(UUID questId, boolean open) {
        pendingFocus = questId;
        pendingOpen = open;
        if (width > 0) applyFocus();
    }

    private void applyFocus() {
        UUID qid = pendingFocus;
        boolean open = pendingOpen;
        pendingFocus = null;
        closePopup();
        UUID line = chapterOf(qid);
        if (line != null && !line.equals(selectedChapter)) selectChapter(line);
        // only what the map itself would show and let you click: links and image actions must not reveal
        // hidden, secret or locked quests (the chapter model applies BQ's visibility rules)
        QuestNode node = model == null || !model.lineId.equals(line) ? null : model.nodes.get(qid);
        if (node == null) return;
        map.focus(qid);
        if (open && node.clickable) openQuest(qid);
    }

    /** A chapter the player can open that contains the quest: the current one first, then the panel's order. */
    private UUID chapterOf(UUID questId) {
        if (model != null && model.line.containsKey(questId)) return model.lineId;
        for (ChapterInfo c : chapters.getChapters()) {
            if (c.line.containsKey(questId) && (!c.locked || canEdit())) return c.id;
        }
        return null;
    }

    @Override
    public void selectChapter(UUID id) {
        if (id.equals(selectedChapter) && model != null) return;
        ChapterInfo info = chapters.find(id);
        // only chapters the panel lists (hidden ones are not in it) and, outside edit mode, not locked ones
        if (info == null || info.locked && !canEdit()) return;
        IQuestLine line = info.line;
        closeQuestView();
        selectedChapter = id;
        model = new ChapterModel(id, line);
        map.setChapter(model);
        chapters.reveal(id);
    }

    @Override
    public UUID selectedChapter() {
        return selectedChapter;
    }

    @Override
    public void openQuest(UUID questId) {
        view.open(questId);
        map.setSelected(questId);
    }

    public void closeQuestView() {
        view.close();
        map.setSelected(null);
    }

    // ------------------------------------------------------------------------------------------
    // Shared state
    // ------------------------------------------------------------------------------------------

    public boolean canEdit() {
        return ChapterModel.canEdit();
    }

    @Override
    public boolean isEditing() {
        return editing && canEdit();
    }

    /** Edit mode toggle (toolbar pencil); only effective with BQ edit permission. */
    public void setEditing(boolean state) {
        editing = state;
        map.refresh();
        layout();
    }

    @Override
    public boolean mapInteractive() {
        return !view.isEnabled() && popupRef == null;
    }

    @Override
    public boolean covered(int mx, int my) {
        return chapters.covers(mx, my) || mx >= toolbar.getX();
    }

    UUID playerId() {
        return QuestingAPI.getQuestingUUID(mc.thePlayer);
    }

    @Override
    public String packTitle() {
        String name = QuestSettings.INSTANCE.getProperty(NativeProps.PACK_NAME);
        return name == null || name.trim()
            .isEmpty() ? Lang.get("title") : QuestTranslation.translate(name);
    }

    List<Map.Entry<UUID, IQuest>> visibleDependencies(IQuest quest) {
        List<Map.Entry<UUID, IQuest>> out = new ArrayList<>();
        UUID pid = playerId();
        for (UUID req : quest.getRequirements()) {
            IQuest rq = QuestDatabase.INSTANCE.get(req);
            if (rq != null && chapterOf(req) != null && QuestCache.isQuestShown(rq, pid, mc.thePlayer)) {
                out.add(new AbstractMap.SimpleEntry<>(req, rq));
            }
        }
        return out;
    }

    List<Map.Entry<UUID, IQuest>> visibleDependants(UUID questId) {
        List<Map.Entry<UUID, IQuest>> out = new ArrayList<>();
        UUID pid = playerId();
        for (Map.Entry<UUID, IQuest> e : QuestDatabase.INSTANCE.entrySet()) {
            IQuest q = e.getValue();
            if (q != null && q.getRequirements()
                .contains(questId) && chapterOf(e.getKey()) != null && QuestCache.isQuestShown(q, pid, mc.thePlayer)) {
                out.add(e);
            }
        }
        return out;
    }

    // ------------------------------------------------------------------------------------------
    // Menus
    // ------------------------------------------------------------------------------------------

    private ContextMenu menu() {
        return new ContextMenu(this::closePopup);
    }

    private void show(ContextMenu m, int mx, int my) {
        if (m.isEmpty()) return;
        openPopup(m.openAt(mx, my));
    }

    /** Dropdown of quests (dependencies / dependants) that navigates on click. */
    void questListMenu(List<Map.Entry<UUID, IQuest>> quests, String title, int mx, int my) {
        ContextMenu m = menu();
        m.add(title, (betterquesting.api2.client.gui.resources.textures.IGuiTexture) null, null)
            .enabled(false);
        m.separator();
        for (Map.Entry<UUID, IQuest> e : quests) {
            UUID id = e.getKey();
            m.add(
                QuestTranslation.translateQuestName(e),
                (betterquesting.api2.client.gui.resources.textures.IGuiTexture) null,
                () -> focusQuest(id, true));
        }
        show(m, mx, my);
    }

    @Override
    public void questContextMenu(QuestNode node, int mx, int my) {
        UUID qid = node.id;
        IQuest quest = node.quest;
        ContextMenu m = menu();
        boolean marked = BookmarkHandler.isBookmarked(qid);
        m.add(
            QuestTranslation
                .translate(marked ? "betterquesting.btn.unbookmark_quest" : "betterquesting.btn.bookmark_quest"),
            PresetIcon.ICON_BOOKMARK,
            () -> {
                BookmarkHandler.bookmarkQuest(qid);
                map.refresh();
            });
        m.add(
            QuestTranslation.translate("betterquesting.btn.share_quest"),
            PresetIcon.ICON_PARTY,
            () -> mc.thePlayer.sendChatMessage("betterquesting.msg.sharequest:" + UuidConverter.encodeUuid(qid)));
        m.add(QuestTranslation.translate("betterquesting.btn.copy_quest"), PresetIcon.ICON_COPY, () -> {
            String s = UuidConverter.encodeUuid(qid);
            setClipboardString(s);
            mc.thePlayer.addChatMessage(
                new ChatComponentText(QuestTranslation.translate("betterquesting.msg.copy_quest_copied")));
            mc.thePlayer.addChatMessage(new ChatComponentText("  " + EnumChatFormatting.AQUA + s));
        });
        m.submenu(
            QuestTranslation.translate("betterquesting.btn.view_dependencies"),
            PresetIcon.ICON_LEFT,
            navItems(visibleDependencies(quest)));
        m.submenu(
            QuestTranslation.translate("betterquesting.btn.view_dependants"),
            PresetIcon.ICON_RIGHT,
            navItems(visibleDependants(qid)));
        for (IQuestContextMenuEntry ext : QuestContextMenuRegistry.getEntries()) {
            m.add(
                ext.getLabel(qid, quest),
                (betterquesting.api2.client.gui.resources.textures.IGuiTexture) null,
                ext.getAction(qid, quest));
        }

        if (isEditing()) {
            if (!map.editor.isSelected(node)) map.editor.selectOnly(node);
            Set<UUID> sel = new LinkedHashSet<>(map.editor.getSelection());
            m.separator();
            m.add(Lang.get("edit_quest"), PresetIcon.ICON_GEAR, () -> editQuest(qid));
            m.submenu(Lang.get("shape"), PresetIcon.ICON_PAINT, shapeItems(sel));
            m.submenu(Lang.get("size"), PresetIcon.ICON_SCALE, sizeItems(sel));
            UUID other = null;
            for (UUID s : sel) if (!s.equals(qid)) other = s;
            if (sel.size() == 2 && other != null) {
                UUID from = other;
                boolean linked = quest.getRequirements()
                    .contains(from);
                String otherName = QuestTranslation.translateQuestName(from, QuestDatabase.INSTANCE.get(from));
                m.add(
                    Lang.get(linked ? "remove_dep" : "add_dep", otherName),
                    PresetIcon.ICON_LINK,
                    () -> toggleDependency(qid, from));
            }
            m.add(Lang.get("complete"), PresetIcon.ICON_TICK, () -> NetQuestEdit.requestComplete(sel, true));
            m.add(Lang.get("reset"), PresetIcon.ICON_REFRESH, () -> NetQuestEdit.requestComplete(sel, false));
            m.add(Lang.get("remove_from_chapter"), PresetIcon.ICON_NEGATIVE, () -> removeFromChapter(sel));
            m.add(
                Lang.get("delete_quest"),
                PresetIcon.ICON_TRASH,
                () -> confirm(Lang.get("confirm_delete", sel.size()), () -> NetQuestEdit.requestDelete(sel)))
                .color(0xFFFF7070);
        }
        show(m, mx, my);
    }

    private List<ContextMenu.Item> navItems(List<Map.Entry<UUID, IQuest>> quests) {
        List<ContextMenu.Item> items = new ArrayList<>();
        for (Map.Entry<UUID, IQuest> e : quests) {
            UUID id = e.getKey();
            items.add(ContextMenu.item(QuestTranslation.translateQuestName(e), () -> focusQuest(id, true)));
        }
        return items;
    }

    private List<ContextMenu.Item> shapeItems(Set<UUID> quests) {
        List<ContextMenu.Item> items = new ArrayList<>();
        IQuest single = quests.size() == 1 ? QuestDatabase.INSTANCE.get(
            quests.iterator()
                .next())
            : null;
        String current = single != null ? single.getProperty(UiProps.SHAPE) : null;
        items.add(
            ContextMenu.check(
                Lang.get("shape.default"),
                () -> current != null && current.isEmpty(),
                () -> setQuestShape(quests, "")));
        for (String s : Shapes.ORDER) {
            items.add(
                ContextMenu.check(Lang.get("shape." + s), () -> s.equals(current), () -> setQuestShape(quests, s)));
        }
        return items;
    }

    private List<ContextMenu.Item> sizeItems(Set<UUID> quests) {
        List<ContextMenu.Item> items = new ArrayList<>();
        int current = -1;
        if (quests.size() == 1 && model != null) {
            IQuestLineEntry e = model.line.get(
                quests.iterator()
                    .next());
            if (e != null) current = e.getSizeX();
        }
        final int cur = current;
        for (int s : new int[] { 16, 20, 24, 28, 32, 40, 48, 64 }) {
            items.add(ContextMenu.check(s + " px", () -> s == cur, () -> setQuestSize(quests, s)));
        }
        return items;
    }

    private void setQuestShape(Set<UUID> quests, String shape) {
        closePopup();
        Map<UUID, IQuest> edits = new java.util.HashMap<>();
        for (UUID id : quests) {
            IQuest q = QuestDatabase.INSTANCE.get(id);
            if (q == null) continue;
            if (shape.isEmpty()) q.removeProperty(UiProps.SHAPE);
            else q.setProperty(UiProps.SHAPE, shape);
            edits.put(id, q);
        }
        NetQuestEdit.requestEdit(edits);
        map.refresh();
    }

    /** Resizes about the quest centre so the layout does not drift. */
    private void setQuestSize(Set<UUID> quests, int size) {
        closePopup();
        if (model == null) return;
        for (UUID id : quests) {
            IQuestLineEntry e = model.line.get(id);
            if (e == null) continue;
            int cx = e.getPosX() + e.getSizeX() / 2;
            int cy = e.getPosY() + e.getSizeY() / 2;
            e.setSize(size, size);
            e.setPosition(cx - size / 2, cy - size / 2);
        }
        NetChapterEdit.requestEdit(model.lineId, model.line);
        map.refresh();
    }

    private void toggleDependency(UUID questId, UUID requirement) {
        IQuest q = QuestDatabase.INSTANCE.get(questId);
        if (q == null) return;
        Set<UUID> reqs = new LinkedHashSet<>(q.getRequirements());
        if (!reqs.remove(requirement)) reqs.add(requirement);
        q.setRequirements(reqs);
        NetQuestEdit.requestEdit(Collections.singletonMap(questId, q));
        map.refresh();
    }

    private void removeFromChapter(Set<UUID> quests) {
        if (model == null) return;
        for (UUID id : quests) model.line.remove(id);
        NetChapterEdit.requestEdit(model.lineId, model.line);
        map.refresh();
    }

    void editQuest(UUID questId) {
        mc.displayGuiScreen(new GuiQuestEditor(this, questId));
    }

    private void confirm(String message, Runnable yes) {
        openPopup(
            new PopChoice(
                message,
                PresetIcon.ICON_NOTICE.getTexture(),
                choice -> { if (choice == 0) yes.run(); },
                QuestTranslation.translate("gui.yes"),
                QuestTranslation.translate("gui.no")));
    }

    @Override
    public void canvasContextMenu(double wx, double wy, int mx, int my) {
        if (!isEditing() || model == null) return;
        ContextMenu m = menu();
        m.add(Lang.get("add_image"), PresetIcon.ICON_IMAGE, () -> {
            ChapterImage img = map.editor.addImage(wx, wy);
            openImageDialog(img);
        });
        m.add(Lang.get("new_quest"), PresetIcon.ICON_NEW, () -> newQuestAt(wx, wy));
        m.submenu(Lang.get("chapter_shape"), PresetIcon.ICON_PAINT, chapterShapeItems(model.lineId, model.line));
        m.separator();
        m.add(Lang.get("fit"), PresetIcon.ICON_BOX_FIT, map::fit);
        m.add(
            QuestTranslation.translate("betterquesting.btn.designer"),
            PresetIcon.ICON_SORT,
            () -> mc.displayGuiScreen(new GuiDesigner(this, model.line)));
        m.add(Lang.get("reload_images"), PresetIcon.ICON_REFRESH, ImageSources::clear);
        show(m, mx, my);
    }

    private void newQuestAt(double wx, double wy) {
        UUID qid = QuestDatabase.INSTANCE.generateKey();
        int size = 24;
        int x = (int) (Math.round((wx - size / 2.0) / 4) * 4);
        int y = (int) (Math.round((wy - size / 2.0) / 4) * 4);
        IQuestLineEntry e = model.line.get(qid);
        if (e == null) model.line.put(qid, new QuestLineEntry(x, y, size, size));
        NetQuestEdit.requestCreate(qid);
        NetChapterEdit.requestEdit(model.lineId, model.line);
    }

    private List<ContextMenu.Item> chapterShapeItems(UUID lineId, IQuestLine line) {
        List<ContextMenu.Item> items = new ArrayList<>();
        String cur = line.getProperty(UiProps.SHAPE);
        items.add(ContextMenu.check(Lang.get("shape.default"), cur::isEmpty, () -> setChapterShape(lineId, line, "")));
        for (String s : Shapes.ORDER) {
            items.add(
                ContextMenu.check(Lang.get("shape." + s), () -> s.equals(cur), () -> setChapterShape(lineId, line, s)));
        }
        return items;
    }

    private void setChapterShape(UUID lineId, IQuestLine line, String shape) {
        closePopup();
        if (shape.isEmpty()) line.removeProperty(UiProps.SHAPE);
        else line.setProperty(UiProps.SHAPE, shape);
        NetChapterEdit.requestEdit(lineId, line);
        map.refresh();
    }

    @Override
    public void imageContextMenu(ChapterImage image, int mx, int my) {
        map.editor.selectImage(image);
        ContextMenu m = menu();
        m.add(Lang.get("edit_image"), PresetIcon.ICON_PROPS, () -> openImageDialog(image));
        m.add(Lang.get("duplicate"), PresetIcon.ICON_COPY, () -> map.editor.duplicateImage(image));
        m.add(Lang.get("bring_forward"), PresetIcon.ICON_UP, () -> map.editor.reorderImage(image, 1));
        m.add(Lang.get("send_backward"), PresetIcon.ICON_DOWN, () -> map.editor.reorderImage(image, -1));
        List<ContextMenu.Item> layers = new ArrayList<>();
        for (int l = 0; l <= 2; l++) {
            int layer = l;
            layers.add(ContextMenu.check(Lang.get("layer." + l), () -> image.layer == layer, () -> {
                ChapterImage copy = image.copy();
                copy.layer = layer;
                map.editor.replaceImage(image, copy);
                closePopup();
            }));
        }
        m.submenu(Lang.get("layer"), PresetIcon.ICON_SORT, layers);
        m.separator();
        m.add(Lang.get("delete"), PresetIcon.ICON_TRASH, () -> map.editor.deleteImage(image))
            .color(0xFFFF7070);
        show(m, mx, my);
    }

    private void openImageDialog(ChapterImage image) {
        closePopup();
        activeDialog = new ImageDialog(this, image, edited -> map.editor.replaceImage(image, edited));
        openPopup(activeDialog.layoutCentered(width, height));
    }

    @Override
    public void imageClicked(ChapterImage image) {
        String c = image.click.trim();
        String lower = c.toLowerCase(java.util.Locale.ROOT);
        if (lower.startsWith("quest:")) {
            UUID id = Ids.parse(c.substring(6));
            if (id != null) focusQuest(id, true);
        } else if (lower.startsWith("chapter:")) {
            UUID id = Ids.parse(c.substring(8));
            if (id != null) selectChapter(id);
        } else if (lower.startsWith("url:")) {
            openLink(
                c.substring(4)
                    .trim());
        }
    }

    /** Vanilla's link confirmation, same as chat links. */
    private void openLink(String url) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) return;
            GuiScreen self = this;
            mc.displayGuiScreen(new GuiConfirmOpenLink(new GuiYesNoCallback() {

                @Override
                public void confirmClicked(boolean yes, int id) {
                    if (yes) betterquesting.api2.client.gui.panels.content.PanelTextBox.openURL(uri);
                    mc.displayGuiScreen(self);
                }
            }, url, 0, false));
        } catch (Exception ignored) {}
    }

    // Chapter panel host -----------------------------------------------------------------------

    @Override
    public void chapterContextMenu(ChapterInfo info, int mx, int my) {
        ContextMenu m = menu();
        m.add(
            QuestTranslation.translate("betterquesting.btn.designer"),
            PresetIcon.ICON_SORT,
            () -> mc.displayGuiScreen(new GuiDesigner(this, info.line)));
        m.add(
            Lang.get("edit_chapters"),
            PresetIcon.ICON_BOOKS,
            () -> mc.displayGuiScreen(new GuiQuestLinesEditor(this)));
        List<ContextMenu.Item> groupItems = new ArrayList<>();
        groupItems.add(
            ContextMenu
                .check(Lang.get("no_group"), info.group::isEmpty, () -> setGroup(Collections.singletonList(info), "")));
        Set<String> groups = new LinkedHashSet<>();
        for (ChapterInfo c : chapters.getChapters()) if (!c.group.isEmpty()) groups.add(c.group);
        for (String g : groups) {
            groupItems.add(
                ContextMenu.check(
                    QuestTranslation.translate(g),
                    () -> g.equals(info.group),
                    () -> setGroup(Collections.singletonList(info), g)));
        }
        groupItems.add(
            ContextMenu.item(
                Lang.get("new_group"),
                () -> prompt(
                    Lang.get("new_group"),
                    "",
                    name -> setGroup(Collections.singletonList(info), name.trim()))));
        m.submenu(Lang.get("group"), PresetIcon.ICON_FOLDER_CLOSED, groupItems);
        m.submenu(Lang.get("chapter_shape"), PresetIcon.ICON_PAINT, chapterShapeItems(info.id, info.line));
        m.add(Lang.get("move_up"), PresetIcon.ICON_UP, () -> moveChapter(info, -1))
            .enabled(neighbour(info, -1) != null);
        m.add(Lang.get("move_down"), PresetIcon.ICON_DOWN, () -> moveChapter(info, 1))
            .enabled(neighbour(info, 1) != null);
        show(m, mx, my);
    }

    @Override
    public void groupContextMenu(String group, int mx, int my) {
        ContextMenu m = menu();
        m.add(Lang.get("rename_group"), PresetIcon.ICON_PROPS, () -> prompt(Lang.get("rename_group"), group, name -> {
            List<ChapterInfo> members = new ArrayList<>();
            for (ChapterInfo c : chapters.getChapters()) if (c.group.equals(group)) members.add(c);
            if (Config.collapsedGroups.remove(group)) {
                Config.collapsedGroups.add(name.trim());
                Config.saveState();
            }
            setGroup(members, name.trim());
        }));
        m.add(Lang.get("ungroup"), PresetIcon.ICON_NEGATIVE, () -> {
            List<ChapterInfo> members = new ArrayList<>();
            for (ChapterInfo c : chapters.getChapters()) if (c.group.equals(group)) members.add(c);
            setGroup(members, "");
        });
        show(m, mx, my);
    }

    private void setGroup(List<ChapterInfo> infos, String group) {
        for (ChapterInfo c : infos) {
            if (group.isEmpty()) c.line.removeProperty(UiProps.GROUP);
            else c.line.setProperty(UiProps.GROUP, group);
            NetChapterEdit.requestEdit(c.id, c.line);
        }
        reloadChapters();
        layout();
    }

    /**
     * The chapter shown next to this one inside its group in the panel (the panel lists ungrouped chapters first,
     * then each group, so BQ's global neighbour is often in another group or hidden).
     */
    private ChapterInfo neighbour(ChapterInfo info, int delta) {
        List<ChapterInfo> siblings = new ArrayList<>();
        for (ChapterInfo c : chapters.getChapters()) if (c.group.equals(info.group)) siblings.add(c);
        int i = siblings.indexOf(info);
        int j = i + delta;
        return i < 0 || j < 0 || j >= siblings.size() ? null : siblings.get(j);
    }

    /**
     * Swaps the chapter with its panel neighbour in BQ's global order (NetChapterEdit reorder). Only the two
     * positions exchange, so every other chapter, and each group's first appearance, keeps its place.
     */
    private void moveChapter(ChapterInfo info, int delta) {
        ChapterInfo other = neighbour(info, delta);
        if (other == null) return;
        List<UUID> order = new ArrayList<>();
        for (Map.Entry<UUID, IQuestLine> e : QuestLineDatabase.INSTANCE.getOrderedEntries()) order.add(e.getKey());
        int i = order.indexOf(info.id), j = order.indexOf(other.id);
        if (i < 0 || j < 0) return;
        Collections.swap(order, i, j);
        NetChapterEdit.requestReorder(order);
    }

    private void prompt(String title, String initial, java.util.function.Consumer<String> done) {
        closePopup();
        openPopup(
            new TextPrompt(
                title,
                initial,
                this::closePopup,
                s -> {
                    if (!s.trim()
                        .isEmpty()) done.accept(s);
                }).layoutCentered(width, height));
    }

    @Override
    public void headerAddChapter() {
        NetChapterEdit.requestCreate();
    }

    @Override
    public void pinChanged() {
        layout();
    }

    // ------------------------------------------------------------------------------------------
    // Toolbar
    // ------------------------------------------------------------------------------------------

    private void buildToolbar() {
        toolbar.top(PresetIcon.ICON_CHEST_ALL.getTexture(), () -> Lang.get("claim_all"), (bx, by) -> claimAll(bx, by))
            .enabled(() -> !claimable().isEmpty())
            .tint(() -> Draw.lerpColor(0xFFFFFFFF, 0xFFFFD34A, (float) Anim.pulse(1.6)));
        toolbar.top(
            PresetIcon.ICON_BOOKMARK.getTexture(),
            () -> QuestTranslation.translate("betterquesting.gui.bookmarks"),
            (bx, by) -> {
                GuiBookmarks g = new GuiBookmarks(this);
                g.setCallback(
                    entry -> focusQuest(
                        entry.getQuest()
                            .getKey(),
                        false));
                mc.displayGuiScreen(g);
            });
        toolbar.top(
            PresetIcon.ICON_ZOOM.getTexture(),
            () -> QuestTranslation.translate("betterquesting.gui.search"),
            (bx, by) -> {
                GuiQuestSearch g = new GuiQuestSearch(this);
                g.setCallback(
                    entry -> focusQuest(
                        entry.getQuest()
                            .getKey(),
                        false));
                mc.displayGuiScreen(g);
            });
        toolbar.top(PresetIcon.ICON_BOX_FIT.getTexture(), () -> Lang.get("fit"), (bx, by) -> map.fit());

        toolbar.bottom(
            PresetIcon.ICON_VISIBILITY_NORMAL.getTexture(),
            () -> Lang.get("view_options"),
            (bx, by) -> viewOptions(bx, by));
        toolbar.bottom(
            PresetIcon.ICON_PARTY.getTexture(),
            () -> QuestTranslation.translate("betterquesting.home.party"),
            (bx, by) -> mc.displayGuiScreen(new GuiPartyManage(this)));
        toolbar.bottom(
            PresetIcon.ICON_NOTICE.getTexture(),
            () -> QuestTranslation.translate("betterquesting.notification.settings"),
            (bx, by) -> mc.displayGuiScreen(new GuiNotificationSettings(this)));
        toolbar
            .bottom(
                PresetIcon.ICON_GRAB.getTexture(),
                () -> Lang.get(editing ? "edit_mode_off" : "edit_mode_on"),
                (bx, by) -> setEditing(!editing))
            .visible(this::canEdit)
            .tint(() -> editing ? Palette.ACCENT : 0xFFFFFFFF);
        toolbar
            .bottom(
                PresetIcon.ICON_BOOKS.getTexture(),
                () -> Lang.get("edit_chapters"),
                (bx, by) -> mc.displayGuiScreen(new GuiQuestLinesEditor(this)))
            .visible(this::isEditing);
        toolbar.bottom(
            PresetIcon.ICON_INFO.getTexture(),
            () -> Lang.get("help"),
            (bx, by) -> mc.displayGuiScreen(new GuiQuestHelp(this)));
        toolbar.bottom(
            PresetIcon.ICON_EXIT.getTexture(),
            () -> QuestTranslation.translate("gui.done"),
            (bx, by) -> doClose());
    }

    private List<UUID> claimable() {
        List<UUID> ids = new ArrayList<>();
        if (model == null) return ids;
        for (QuestNode n : model.nodes.values()) {
            if (n.quest.getRewards()
                .size() > 0 && n.quest.canClaim(mc.thePlayer, true)) ids.add(n.id);
        }
        return ids;
    }

    /** BQ's claim-all with its confirmation and "force choice" settings. */
    private void claimAll(int bx, int by) {
        List<UUID> ids = claimable();
        if (ids.isEmpty()) return;
        Runnable go = () -> {
            if (BQ_Settings.claimAllRandomChoice) NetQuestAction.requestClaimForceChoice(ids);
            else NetQuestAction.requestClaim(ids);
        };
        // BQ: the confirmation shows when enabled in its settings, and Shift forces it
        if (!BQ_Settings.claimAllConfirmation && !isShiftKeyDown()) {
            go.run();
            return;
        }
        ContextMenu m = menu();
        m.add(Lang.get("claim_n", ids.size()), PresetIcon.ICON_CHEST_ALL, go);
        m.toggle(
            QuestTranslation.translate(
                BQ_Settings.claimAllRandomChoice ? "betterquesting.gui.force_choice_yes"
                    : "betterquesting.gui.force_choice_no"),
            () -> BQ_Settings.claimAllRandomChoice,
            () -> {
                BQ_Settings.claimAllRandomChoice = !BQ_Settings.claimAllRandomChoice;
                saveBq("Claim all random select choice rewards", BQ_Settings.claimAllRandomChoice);
            });
        m.add(QuestTranslation.translate("gui.cancel"), PresetIcon.ICON_CROSS, null);
        show(m, bx - 4, by);
        // keep the menu left of the toolbar
        ContextMenu shown = m;
        shown.setBounds(Math.max(0, bx - shown.getW() - 2), shown.getY(), shown.getW(), shown.getH());
    }

    /** BQ's quest-book toggles, persisted in BQ's own config like its buttons do. */
    private void viewOptions(int bx, int by) {
        ContextMenu m = menu();
        m.toggle(
            QuestTranslation.translate("betterquesting.btn.hide_locked_quest_lines"),
            () -> BQ_Settings.hideLockedQuestLines,
            () -> {
                BQ_Settings.hideLockedQuestLines = !BQ_Settings.hideLockedQuestLines;
                saveBq("Hide locked quest lines", BQ_Settings.hideLockedQuestLines);
                reloadChapters();
                layout();
            });
        m.toggle(
            QuestTranslation.translate("betterquesting.btn.always_draw_implicit"),
            () -> BQ_Settings.alwaysDrawImplicit,
            () -> {
                BQ_Settings.alwaysDrawImplicit = !BQ_Settings.alwaysDrawImplicit;
                saveBq("Always draw implicit dependency", BQ_Settings.alwaysDrawImplicit);
            });
        m.toggle(
            QuestTranslation.translate("betterquesting.btn.show_dependency_arrows"),
            () -> BQ_Settings.showDependencyArrows,
            () -> {
                BQ_Settings.showDependencyArrows = !BQ_Settings.showDependencyArrows;
                saveBq("Show dependency arrows", BQ_Settings.showDependencyArrows);
            });
        // BQ labels this "Show Quest Colors": checked means colours are shown, i.e. NOT forced monochrome
        m.toggle(
            QuestTranslation.translate("betterquesting.btn.force_monochrome"),
            () -> !BQ_Settings.forceMonochromeText,
            () -> {
                BQ_Settings.forceMonochromeText = !BQ_Settings.forceMonochromeText;
                saveBq("Force Monochrome Text", BQ_Settings.forceMonochromeText);
                view.refresh();
            });
        if (BQ_Settings.viewModeBtn) {
            m.toggle(QuestTranslation.translate("betterquesting.btn.view_mode"), () -> BQ_Settings.viewMode, () -> {
                BQ_Settings.viewMode = !BQ_Settings.viewMode;
                saveBq("View mode", BQ_Settings.viewMode);
                refreshGui();
            });
        }
        List<ContextMenu.Item> opacity = new ArrayList<>();
        for (int pct : new int[] { 100, 90, 80, 72, 60, 50, 40, 30 }) {
            opacity.add(
                ContextMenu.check(
                    pct + "%",
                    () -> Math.round(Config.backgroundOpacity * 100) == pct,
                    () -> Config.setBackgroundOpacity(pct / 100.0)));
        }
        m.submenu(Lang.get("bg_opacity"), PresetIcon.ICON_VISIBILITY_IMPLICIT, opacity);
        m.toggle(
            Lang.get("animate_lines"),
            () -> Config.animateLines,
            () -> Config.setAnimation(!Config.animateLines, Config.pulseAvailable));
        m.toggle(
            Lang.get("pulse_available"),
            () -> Config.pulseAvailable,
            () -> Config.setAnimation(Config.animateLines, !Config.pulseAvailable));
        show(m, bx, by);
        m.setBounds(Math.max(0, bx - m.getW() - 2), Math.max(0, Math.min(by, height - m.getH())), m.getW(), m.getH());
    }

    private static void saveBq(String key, boolean value) {
        if (ConfigHandler.config == null) return;
        ConfigHandler.config.get(Configuration.CATEGORY_GENERAL, key, value)
            .set(value);
        ConfigHandler.config.save();
    }

    // ------------------------------------------------------------------------------------------
    // Input: ESC closes the quest view first
    // ------------------------------------------------------------------------------------------

    @Override
    public void keyTyped(char c, int keyCode) {
        if (keyCode == Keyboard.KEY_BACK) {
            // text fields get it first; otherwise it closes the innermost layer like ESC but never leaves the
            // book (BQ's canvas would jump to the parent screen, usually its home screen)
            if (!onKeyTyped(c, keyCode)) closeLayer();
            return;
        }
        if (keyCode == Keyboard.KEY_ESCAPE && closeLayer()) return;
        super.keyTyped(c, keyCode);
    }

    /** Closes the innermost layer: popup/dialog, then the quest view, then the edit selection. */
    private boolean closeLayer() {
        if (popupRef != null) {
            if (popupRef == activeDialog) closeDialog();
            else closePopup();
            return true;
        }
        if (view.isEnabled()) {
            closeQuestView();
            return true;
        }
        return isEditing() && map.editor.clearSelection();
    }

    /**
     * Chapter title and progress over the top-left of the map (DESIGN §6): a pill of panel colour, the name, then
     * "done/total (p%)" in the secondary colour, and a 2 px bar of width p under it. Display only, no input.
     */
    private void drawChapterTitle() {
        ChapterInfo info = selectedChapter == null ? null : chapters.find(selectedChapter);
        if (info == null) return;
        String prog = Lang.get("progress", info.done, info.total, String.format("%.0f", info.percent()));
        // follows the right edge of the chapter panel, which slides over the map when unpinned
        int left = Math.max(map.getX(), chapters.getX() + chapters.drawnWidth());
        int px = left + 6, py = 6;
        int maxW = map.getX() + map.getW() - left - 12 - 8;
        if (maxW < 60) return;
        int progW = Draw.font()
            .getStringWidth(prog);
        String name = Draw
            .ellipsize(EnumChatFormatting.getTextWithoutFormattingCodes(info.name), Math.max(20, maxW - progW - 8));
        int nameW = Draw.font()
            .getStringWidth(name);
        int pw = Math.min(maxW, nameW + 8 + progW) + 8;
        Draw.panel(px, py, px + pw, py + 16, Palette.panelBg(), Palette.PANEL_EDGE);
        Draw.text(name, px + 4, py + 4, Palette.TEXT, false);
        if (nameW + 8 + progW <= maxW) Draw.text(prog, px + 4 + nameW + 8, py + 4, Palette.TEXT_2, false);
        if (info.total > 0) {
            int bw = (int) Math.round((pw - 2) * info.done / (double) info.total);
            Draw.rect(px + 1, py + 14, px + 1 + bw, py + 15, Palette.ACCENT);
        }
    }

    /** Composes the screen's widgets in a fixed order: map, panel, toolbar, then the modal quest view. */
    private final class Root extends Widget {

        private Widget[] order() {
            return new Widget[] { map, chapters, toolbar, view };
        }

        @Override
        public void drawPanel(int mx, int my, float pt) {
            // depth cleared between layers: item icons write depth (Draw.clearDepth)
            // while the quest view is up nothing behind it may show hover feedback (menus keep the real cursor so
            // the slid-out chapter panel stays open under a menu opened from it)
            boolean modal = view.isEnabled();
            int hx = modal ? -10000 : mx, hy = modal ? -10000 : my;
            map.drawPanel(mx, my, pt);
            Draw.clearDepth();
            drawChapterTitle();
            chapters.drawPanel(hx, hy, pt);
            Draw.clearDepth();
            toolbar.drawPanel(hx, hy, pt);
            Draw.clearDepth();
            if (view.isEnabled()) {
                Draw.rect(0, 0, width, height, Palette.DIM);
                view.drawPanel(mx, my, pt);
                Draw.clearDepth();
            }
        }

        @Override
        public boolean onMouseClick(int mx, int my, int button) {
            // always consumed: BQ's canvas treats an unused right click as "go back to the parent screen"
            if (view.isEnabled()) view.onMouseClick(mx, my, button);
            else if (!chapters.onMouseClick(mx, my, button) && !toolbar.onMouseClick(mx, my, button)) {
                map.onMouseClick(mx, my, button);
            }
            return true;
        }

        @Override
        public boolean onMouseRelease(int mx, int my, int button) {
            boolean used = false;
            for (Widget w : order()) if (w.isEnabled()) used |= w.onMouseRelease(mx, my, button);
            return used;
        }

        @Override
        public boolean onMouseScroll(int mx, int my, int scroll) {
            if (view.isEnabled()) return view.onMouseScroll(mx, my, scroll);
            if (chapters.onMouseScroll(mx, my, scroll)) return true;
            return map.onMouseScroll(mx, my, scroll);
        }

        @Override
        public boolean onKeyTyped(char c, int keycode) {
            if (view.isEnabled()) return view.onKeyTyped(c, keycode);
            return map.onKeyTyped(c, keycode);
        }

        @Override
        public List<String> getTooltip(int mx, int my) {
            if (popupRef != null) return null; // menus and dialogs own the tooltip layer
            if (view.isEnabled()) return view.getTooltip(mx, my);
            List<String> tt = toolbar.getTooltip(mx, my);
            if (tt == null) tt = chapters.getTooltip(mx, my);
            if (tt == null && !chapters.covers(mx, my)) tt = map.getTooltip(mx, my);
            return tt;
        }
    }
}
