package akasha.questui.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import akasha.questui.client.screen.QuestScreen;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * The map background is translucent, so the HUD shows through it. Hotbar and chat are useful context (as in FTB
 * Quests), but the crosshair lands in the middle of the map and reads as a stray "+" marker: hide it while the book
 * is open.
 */
public class HudFilter {

    @SubscribeEvent
    public void onOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS
            && Minecraft.getMinecraft().currentScreen instanceof QuestScreen) {
            event.setCanceled(true);
        }
    }
}
