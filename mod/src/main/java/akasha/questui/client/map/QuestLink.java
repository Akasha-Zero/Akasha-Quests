package akasha.questui.client.map;

/** Dependency line from a requirement to its dependant (DESIGN §4). */
public class QuestLink {

    public final QuestNode from;
    public final QuestNode to;
    /** BQ "implicit" requirement: only drawn on hover, Shift or when "always draw implicit" is on. */
    public final boolean implicit;
    /** Dashed when implicit or when the dependant uses OR/XOR logic over its requirements. */
    public final boolean dashed;

    public QuestLink(QuestNode from, QuestNode to, boolean implicit, boolean dashed) {
        this.from = from;
        this.to = to;
        this.implicit = implicit;
        this.dashed = dashed;
    }
}
