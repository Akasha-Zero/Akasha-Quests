package akasha.questui.client.map;

import static betterquesting.api.placeholders.ItemPlaceholder.placeholder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;

import akasha.questui.Config;
import akasha.questui.client.data.ChapterImage;
import akasha.questui.client.data.UiProps;
import akasha.questui.client.gfx.Shapes;
import betterquesting.api.api.QuestingAPI;
import betterquesting.api.enums.EnumLogic;
import betterquesting.api.enums.EnumQuestState;
import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.IQuest.RequirementType;
import betterquesting.api.questing.IQuestLine;
import betterquesting.api.questing.IQuestLineEntry;
import betterquesting.api.questing.tasks.ITask;
import betterquesting.api.storage.BQ_Settings;
import betterquesting.api.utils.BigItemStack;
import betterquesting.api2.cache.QuestCache;
import betterquesting.api2.client.gui.resources.textures.OreDictTexture;
import betterquesting.api2.storage.DBEntry;
import betterquesting.client.BookmarkHandler;
import betterquesting.questing.QuestDatabase;

/**
 * Derived view of one chapter for the current player: visible quests, their states and the dependency links.
 * Mirrors the visibility and line rules of BQ's CanvasQuestLine so both screens always agree.
 */
public class ChapterModel {

    public final UUID lineId;
    public final IQuestLine line;
    public final Map<UUID, QuestNode> nodes = new LinkedHashMap<>();
    public final List<QuestLink> links = new ArrayList<>();
    public final List<ChapterImage> images = new ArrayList<>();
    /** BQ's native chapter background image (NativeProps.BG_IMAGE), or null. */
    public String bgImage;
    public int bgSize;

    public ChapterModel(UUID lineId, IQuestLine line) {
        this.lineId = lineId;
        this.line = line;
        rebuild();
    }

    public void rebuild() {
        nodes.clear();
        links.clear();
        List<ChapterImage> previous = new ArrayList<>(images);
        images.clear();
        if (line == null) return;

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        UUID pid = QuestingAPI.getQuestingUUID(player);
        boolean canEdit = canEdit();

        String bg = line.getProperty(NativeProps.BG_IMAGE);
        bgImage = StringUtils.isNullOrEmpty(bg) ? null : bg;
        bgSize = line.getProperty(NativeProps.BG_SIZE);

        String chapterShape = line.getProperty(UiProps.SHAPE);

        for (Map.Entry<UUID, IQuestLineEntry> qle : line.entrySet()) {
            IQuest quest = QuestDatabase.INSTANCE.get(qle.getKey());
            if (quest == null || !QuestCache.isQuestShown(quest, pid, player)) continue;

            QuestNode node = new QuestNode(qle.getKey(), quest, qle.getValue());
            node.main = quest.getProperty(NativeProps.MAIN);
            node.shape = resolveShape(quest.getProperty(UiProps.SHAPE), chapterShape, node.main);
            node.tasksTotal = quest.getTasks()
                .size();
            for (DBEntry<ITask> t : quest.getTasks()
                .getEntries()) {
                if (t.getValue()
                    .isComplete(pid)) node.tasksDone++;
            }
            node.state = state(quest.getState(player), node.tasksDone);
            node.bookmarked = BookmarkHandler.isBookmarked(node.id);
            node.clickable = canEdit || node.state != QuestNode.State.LOCKED || BQ_Settings.viewMode;
            node.icon = new OreDictTexture(
                1F,
                quest.getOrDefault(NativeProps.ICON, new BigItemStack(placeholder)),
                false,
                true);
            nodes.put(node.id, node);
        }

        for (QuestNode node : nodes.values()) {
            EnumLogic logic = node.quest.getProperty(NativeProps.LOGIC_QUEST);
            boolean anyOf = logic != EnumLogic.AND;
            for (UUID req : node.quest.getRequirements()) {
                QuestNode parent = nodes.get(req);
                if (parent == null) continue;
                RequirementType type = node.quest.getRequirementType(req);
                if (type == RequirementType.HIDDEN) continue;
                boolean implicit = type == RequirementType.IMPLICIT;
                links.add(new QuestLink(parent, node, implicit, implicit || anyOf));
            }
        }

        // Unchanged images keep their object: dialogs, menus, the selection and undo hold references to them,
        // and every sync (any quest, party or progress update) rebuilds the model.
        for (ChapterImage img : ChapterImage.readList(line.getProperty(UiProps.IMAGES))) {
            NBTTagCompound tag = img.write();
            ChapterImage keep = null;
            for (java.util.Iterator<ChapterImage> it = previous.iterator(); it.hasNext();) {
                ChapterImage old = it.next();
                if (old.write()
                    .equals(tag)) {
                    keep = old;
                    it.remove();
                    break;
                }
            }
            images.add(keep != null ? keep : img);
        }
    }

    public static boolean canEdit() {
        return QuestingAPI.getAPI(betterquesting.api.api.ApiReference.SETTINGS)
            .canUserEdit(Minecraft.getMinecraft().thePlayer);
    }

    public static String resolveShape(String own, String chapterDefault, boolean main) {
        if (own != null && Shapes.exists(own)) return own;
        if (chapterDefault != null && Shapes.exists(chapterDefault)) return chapterDefault;
        return main ? Config.mainShape : Config.defaultShape;
    }

    private static QuestNode.State state(EnumQuestState s, int tasksDone) {
        switch (s) {
            case LOCKED:
                return QuestNode.State.LOCKED;
            case UNCLAIMED:
                return QuestNode.State.CLAIM;
            case COMPLETED:
                return QuestNode.State.DONE;
            case REPEATABLE:
                return QuestNode.State.REPEAT;
            case UNLOCKED:
            default:
                return tasksDone > 0 ? QuestNode.State.PROGRESS : QuestNode.State.AVAILABLE;
        }
    }

    /** Bounding box of quests and images in world units: {x0, y0, x1, y1}, or null when empty. */
    public double[] bounds(boolean includeEditOnly) {
        double x0 = Double.MAX_VALUE, y0 = Double.MAX_VALUE, x1 = -Double.MAX_VALUE, y1 = -Double.MAX_VALUE;
        boolean any = false;
        for (QuestNode n : nodes.values()) {
            x0 = Math.min(x0, n.x);
            y0 = Math.min(y0, n.y);
            x1 = Math.max(x1, n.x + n.w);
            y1 = Math.max(y1, n.y + n.h);
            any = true;
        }
        for (ChapterImage img : images) {
            if (img.editOnly && !includeEditOnly) continue;
            // Axis-aligned extent of the rotated rectangle: |w/2 cos| + |h/2 sin|, |w/2 sin| + |h/2 cos|
            double t = img.theta();
            double ex = Math.abs(img.w / 2 * Math.cos(t)) + Math.abs(img.h / 2 * Math.sin(t));
            double ey = Math.abs(img.w / 2 * Math.sin(t)) + Math.abs(img.h / 2 * Math.cos(t));
            x0 = Math.min(x0, img.x - ex);
            y0 = Math.min(y0, img.y - ey);
            x1 = Math.max(x1, img.x + ex);
            y1 = Math.max(y1, img.y + ey);
            any = true;
        }
        return any ? new double[] { x0, y0, x1, y1 } : null;
    }
}
