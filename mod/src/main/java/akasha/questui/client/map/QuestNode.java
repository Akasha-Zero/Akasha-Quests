package akasha.questui.client.map;

import java.util.UUID;

import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.IQuestLineEntry;
import betterquesting.api2.client.gui.resources.textures.OreDictTexture;

/** One quest placed on the current chapter, with its derived visual state (DESIGN §3). */
public class QuestNode {

    public enum State {
        LOCKED,
        AVAILABLE,
        PROGRESS,
        CLAIM,
        DONE,
        REPEAT
    }

    public final UUID id;
    public final IQuest quest;
    public final IQuestLineEntry entry;
    /** World rectangle (BQ quest-line pixels). */
    public double x, y, w, h;
    public String shape;
    public State state;
    public int tasksDone;
    public int tasksTotal;
    public boolean main;
    public boolean bookmarked;
    /** Locked quests only open for editors or in BQ view mode. */
    public boolean clickable;
    public OreDictTexture icon;

    public QuestNode(UUID id, IQuest quest, IQuestLineEntry entry) {
        this.id = id;
        this.quest = quest;
        this.entry = entry;
        this.x = entry.getPosX();
        this.y = entry.getPosY();
        this.w = entry.getSizeX();
        this.h = entry.getSizeY();
    }

    public double cx() {
        return x + w / 2;
    }

    public double cy() {
        return y + h / 2;
    }

    public double size() {
        return Math.min(w, h);
    }
}
