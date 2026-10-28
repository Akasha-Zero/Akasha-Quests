package akasha.questui.client.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import akasha.questui.client.data.UiProps;
import betterquesting.api.api.QuestingAPI;
import betterquesting.api.enums.EnumLogic;
import betterquesting.api.enums.EnumQuestVisibility;
import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.IQuestLine;
import betterquesting.api.questing.IQuestLineEntry;
import betterquesting.api.storage.BQ_Settings;
import betterquesting.api2.cache.QuestCache;
import betterquesting.api2.client.gui.resources.textures.OreDictTexture;
import betterquesting.api2.utils.QuestTranslation;
import betterquesting.questing.QuestDatabase;
import betterquesting.questing.QuestLineDatabase;

/**
 * One chapter row of the chapter panel. Visibility, badges and progress follow BQ's GuiQuestLines exactly
 * (refreshChapterVisibility / refreshQuestCompletion), so both screens list the same chapters with the same numbers.
 */
public class ChapterInfo {

    public final UUID id;
    public final IQuestLine line;
    /** Full BQ name (tooltips). */
    public final String name;
    /** Name shown in the panel: the short title when the chapter has one. */
    public final String label;
    public final String group;
    public final OreDictTexture icon;
    /** At least one quest has rewards waiting. */
    public boolean pendingClaim;
    /** Every counted quest is done. */
    public boolean allComplete;
    /** No visible quest for this player (BQ shows the row disabled). */
    public boolean locked;
    public int done;
    public int total;

    private ChapterInfo(UUID id, IQuestLine line) {
        this.id = id;
        this.line = line;
        this.name = QuestTranslation.translateQuestLineName(id, line);
        String shortTitle = line.getProperty(UiProps.TITLE)
            .trim();
        // formatting stripped: in the panel, colour encodes state (selected, complete, locked)
        this.label = net.minecraft.util.EnumChatFormatting
            .getTextWithoutFormattingCodes(shortTitle.isEmpty() ? name : QuestTranslation.translate(shortTitle));
        this.group = line.getProperty(UiProps.GROUP)
            .trim();
        this.icon = new OreDictTexture(1F, line.getProperty(NativeProps.ICON), false, true);
    }

    public double percent() {
        return total > 0 ? done * 100.0 / total : 0;
    }

    public static List<ChapterInfo> collect(boolean canEdit) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        UUID pid = QuestingAPI.getQuestingUUID(player);
        List<ChapterInfo> out = new ArrayList<>();

        for (Map.Entry<UUID, IQuestLine> entry : QuestLineDatabase.INSTANCE.getOrderedEntries()) {
            IQuestLine ql = entry.getValue();
            EnumQuestVisibility vis = ql.getProperty(NativeProps.VISIBILITY);
            if (!canEdit && vis == EnumQuestVisibility.HIDDEN) continue;

            boolean show = canEdit || BQ_Settings.viewMode;
            boolean unlocked = canEdit || BQ_Settings.viewModeAllQuestLine;
            boolean complete = canEdit;
            boolean allComplete = true;
            boolean pending = false;

            for (Map.Entry<UUID, IQuestLineEntry> qe : ql.entrySet()) {
                IQuest q = QuestDatabase.INSTANCE.get(qe.getKey());
                if (q == null) continue;
                if (allComplete && !countsAsDone(pid, q)) allComplete = false;
                if (!pending && q.canClaimBasically(player)) pending = true;
                if (!unlocked && q.isUnlocked(pid)) unlocked = true;
                if (!complete && q.isComplete(pid)) complete = true;
                if (!show && QuestCache.isQuestShown(q, pid, player)) show = true;
            }

            if (vis == EnumQuestVisibility.COMPLETED && !complete) continue;
            if (vis == EnumQuestVisibility.UNLOCKED && !unlocked) continue;
            if (!show && BQ_Settings.hideLockedQuestLines) continue;

            ChapterInfo info = new ChapterInfo(entry.getKey(), ql);
            info.pendingClaim = pending;
            info.allComplete = allComplete && !ql.isEmpty();
            info.locked = !show;
            info.count(pid);
            out.add(info);
        }
        return out;
    }

    /** BQ's completion count: hidden/secret and COUNT_AS_QUEST=false quests are skipped. */
    private void count(UUID pid) {
        for (Map.Entry<UUID, IQuestLineEntry> qe : line.entrySet()) {
            IQuest q = QuestDatabase.INSTANCE.get(qe.getKey());
            if (q == null) continue;
            EnumQuestVisibility v = q.getProperty(NativeProps.VISIBILITY);
            if (v == EnumQuestVisibility.HIDDEN || v == EnumQuestVisibility.SECRET) continue;
            if (!q.getProperty(NativeProps.COUNT_AS_QUEST)) continue;
            total++;
            if (q.isComplete(pid) || !q.isUnlockable(pid)) done++;
        }
    }

    /** Mirrors GuiQuestLines.isQuestCompletedForQuestline. */
    private static boolean countsAsDone(UUID pid, IQuest q) {
        if (q.isComplete(pid)) return true;
        EnumQuestVisibility vis = q.getProperty(NativeProps.VISIBILITY);
        if (vis == EnumQuestVisibility.HIDDEN || vis == EnumQuestVisibility.SECRET) return true;
        if (!q.getProperty(NativeProps.COUNT_AS_QUEST)) return true;
        if (q.getProperty(NativeProps.LOGIC_QUEST) == EnumLogic.XOR) {
            int n = 0;
            for (UUID r : q.getRequirements()) {
                IQuest rq = QuestDatabase.INSTANCE.get(r);
                if (rq != null && rq.isComplete(pid) && ++n == 2) return true;
            }
        }
        return false;
    }
}
