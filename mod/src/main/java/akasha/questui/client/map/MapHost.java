package akasha.questui.client.map;

import java.util.UUID;

import akasha.questui.client.data.ChapterImage;

/** What the quest map asks of the screen that owns it. */
public interface MapHost {

    /** Left click on a quest outside edit mode: open the quest view. */
    void openQuest(UUID questId);

    void questContextMenu(QuestNode node, int mx, int my);

    void imageContextMenu(ChapterImage image, int mx, int my);

    /** Right click on empty map; world coordinates of the click are given for "add here" actions. */
    void canvasContextMenu(double wx, double wy, int mx, int my);

    /** Click on an image with a click action (DESIGN §8). */
    void imageClicked(ChapterImage image);

    /** True while the editor mode is active (requires BQ edit permission). */
    boolean isEditing();

    /** False while a modal (quest view, dialog) covers the map. */
    boolean mapInteractive();
}
