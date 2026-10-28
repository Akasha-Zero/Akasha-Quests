package akasha.questui.client;

import java.lang.reflect.Field;
import java.util.UUID;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;

import akasha.questui.AkashaQuestUI;
import akasha.questui.Config;
import akasha.questui.client.screen.QuestScreen;
import betterquesting.api2.client.gui.GuiScreenCanvas;
import betterquesting.api2.client.gui.themes.IGuiTheme;
import betterquesting.client.gui2.GuiQuest;
import betterquesting.client.gui2.GuiQuestLines;
import betterquesting.client.themes.ThemeRegistry;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * Swaps BetterQuesting's chapter screen and quest screen for {@link QuestScreen} wherever they are opened from
 * (keybind, home screen, NEI, search, bookmarks, /bq show, quest links), so the BQ code paths stay untouched.
 */
public class ScreenInterceptor {

    private static final ResourceLocation THEME = new ResourceLocation("akashaquestui", "ftb_dark");

    private static Field questIdField;
    private static Field previousScreenField;
    private boolean themeApplied;

    static {
        try {
            questIdField = GuiQuest.class.getDeclaredField("questID");
            questIdField.setAccessible(true);
            previousScreenField = GuiScreenCanvas.class.getDeclaredField("previousScreen");
            previousScreenField.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            AkashaQuestUI.LOG.error("BetterQuesting internals changed; quest screens will not be replaced", e);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onGuiOpen(GuiOpenEvent event) {
        // back to the game (e.g. ESC inside a BQ picker): a dialog waiting for that picker must not reappear later
        if (event.gui == null) QuestScreen.droppedToGame();
        if (!Config.enabled || event.gui == null || event.gui instanceof QuestScreen) return;
        if (questIdField == null) return;
        try {
            if (event.gui instanceof GuiQuestLines) {
                applyTheme();
                GuiScreenCanvas lines = (GuiScreenCanvas) event.gui;
                QuestScreen screen = QuestScreen.create(lines.parent);
                transferPrevious(lines, screen);
                event.gui = screen;
            } else if (event.gui instanceof GuiQuest
                && !(event.gui instanceof akasha.questui.client.screen.QuestView.LinkBridge)) {
                    applyTheme();
                    GuiQuest gq = (GuiQuest) event.gui;
                    UUID questId = (UUID) questIdField.get(gq);
                    QuestScreen screen;
                    if (gq.parent instanceof QuestScreen) {
                        screen = (QuestScreen) gq.parent;
                    } else {
                        GuiScreen parent = gq.parent instanceof GuiQuestLines ? ((GuiScreenCanvas) gq.parent).parent
                            : gq.parent;
                        screen = QuestScreen.create(parent);
                    }
                    transferPrevious(gq, screen);
                    screen.focusQuest(questId, true);
                    event.gui = screen;
                }
        } catch (ReflectiveOperationException | RuntimeException e) {
            // never break BQ: on any surprise fall back to its own screen
            AkashaQuestUI.LOG.error("Could not open the Akasha quest screen, using BetterQuesting's", e);
        }
    }

    private static void transferPrevious(GuiScreenCanvas from, GuiScreenCanvas to) throws IllegalAccessException {
        GuiScreen prev = (GuiScreen) previousScreenField.get(from);
        if (prev != null) to.setPreviousScreen(prev);
    }

    /** Selects the matching BQ theme once per session so BQ's own panels share the dark look (Config). */
    private void applyTheme() {
        if (themeApplied || !Config.applyTheme) return;
        themeApplied = true;
        IGuiTheme theme = ThemeRegistry.INSTANCE.getTheme(THEME);
        IGuiTheme current = ThemeRegistry.INSTANCE.getCurrentTheme();
        // only over BQ's default: a theme the player picked in BQ's own theme screen is left alone
        String cur = current == null ? ""
            : current.getID()
                .toString();
        if (theme != null && (cur.isEmpty() || "betterquesting:light".equals(cur))) {
            ThemeRegistry.INSTANCE.setTheme(THEME);
        }
    }
}
