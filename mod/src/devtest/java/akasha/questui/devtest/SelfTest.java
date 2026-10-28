package akasha.questui.devtest;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;

import akasha.questui.client.screen.QuestScreen;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.IQuestLine;
import betterquesting.api.utils.UuidConverter;
import betterquesting.api2.client.gui.GuiScreenCanvas;
import betterquesting.api2.utils.QuestTranslation;
import betterquesting.client.gui2.GuiQuestLines;
import betterquesting.questing.QuestDatabase;
import betterquesting.questing.QuestLineDatabase;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

/**
 * Development-only scripted UI test (never part of the released jar). Enabled with
 * -Dakashaquestui.selftest=path/to/script.txt. One command per line:
 *
 * <pre>
 * world                      create/load a flat creative world with cheats
 * wait N                     wait N client ticks
 * waitquests                 wait until the player exists and quests are synced
 * cmd /text                  send a chat command
 * complete title             /bq_admin complete for the quest whose name contains `title`
 * open                       open BQ's quest book (intercepted by the mod)
 * chapter title              select a chapter by (partial) name
 * quest title                focus + open a quest by (partial) name
 * close                      close the quest view
 * edit on|off                toggle the mod's edit mode
 * mouse X Y                  move the cursor (GUI coordinates)
 * click X Y [button]         press + release at (X, Y)
 * press X Y [button] / release X Y [button]
 * scroll X Y N               wheel N notches (positive = zoom out)
 * key CODE [char]            keyTyped on the current screen
 * hoverquest title           move the cursor onto a quest of the current chapter
 * clickquest title [button]  click a quest (button 1 = context menu)
 * clickimage I [button]      click the I-th chapter image
 * pin on|off                 pin or collapse the chapter panel
 * menu label                 activate the item of the open context menu containing `label`
 * fps                        log the current frame rate
 * shot NAME                  save run/screenshots/NAME.png after the next frame
 * exit                       quit the game
 * </pre>
 */
@Mod(modid = "akashaquestui_selftest", name = "Akasha Quests UI self test", version = "dev",
    dependencies = "required-after:akashaquestui")
public class SelfTest {

    private static final Logger LOG = LogManager.getLogger("AkashaSelfTest");

    private final List<String> lines = new ArrayList<>();
    private int pc;
    private int waitTicks;
    private String pendingShot;
    private boolean waitingQuests;

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) throws Exception {
        String path = System.getProperty("akashaquestui.selftest");
        if (path == null) return;
        for (String l : Files.readAllLines(new File(path).toPath(), StandardCharsets.UTF_8)) {
            l = l.trim();
            if (!l.isEmpty() && !l.startsWith("#")) lines.add(l);
        }
        LOG.info("Self test with {} steps from {}", lines.size(), path);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent e) {
        if (e.phase != TickEvent.Phase.END || pendingShot == null) return;
        Minecraft mc = Minecraft.getMinecraft();
        ScreenShotHelper.saveScreenshot(mc.mcDataDir, pendingShot + ".png", mc.displayWidth, mc.displayHeight,
            mc.getFramebuffer());
        LOG.info("screenshot {}", pendingShot);
        pendingShot = null;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (pendingShot != null) return;
        if (waitTicks > 0) {
            waitTicks--;
            return;
        }
        if (waitingQuests) {
            if (mc.thePlayer == null || QuestDatabase.INSTANCE.isEmpty() || QuestLineDatabase.INSTANCE.isEmpty()) return;
            waitingQuests = false;
            waitTicks = 20;
            return;
        }
        if (pc == 0 && !(mc.currentScreen instanceof GuiMainMenu) && mc.theWorld == null) return; // still loading
        while (pc < lines.size() && waitTicks == 0 && !waitingQuests && pendingShot == null) {
            String line = lines.get(pc++);
            try {
                run(mc, line);
            } catch (Exception ex) {
                LOG.error("step '{}' failed", line, ex);
            }
        }
    }

    private static int[] toDevice(Minecraft mc, int gx, int gy) {
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int f = sr.getScaleFactor();
        return new int[] { gx * f, mc.displayHeight - gy * f - 1 };
    }

    private void run(Minecraft mc, String line) throws Exception {
        LOG.info("> {}", line);
        String[] a = line.split("\\s+", 2);
        String arg = a.length > 1 ? a[1] : "";
        String[] n = arg.isEmpty() ? new String[0] : arg.split("\\s+");
        GuiScreen s = mc.currentScreen;
        switch (a[0]) {
            case "world":
                WorldSettings ws = new WorldSettings(4242L, WorldSettings.GameType.CREATIVE, false, false, WorldType.FLAT);
                ws.enableCommands();
                mc.launchIntegratedServer("selftest", "selftest", ws);
                break;
            case "wait":
                waitTicks = Integer.parseInt(arg);
                break;
            case "waitquests":
                waitingQuests = true;
                break;
            case "cmd":
                mc.thePlayer.sendChatMessage(arg);
                waitTicks = 5;
                break;
            case "complete":
                UUID q = questByName(arg);
                if (q != null) mc.thePlayer.sendChatMessage("/bq_admin complete " + UuidConverter.encodeUuid(q));
                else LOG.warn("no quest named {}", arg);
                waitTicks = 5;
                break;
            case "open":
                mc.displayGuiScreen(new GuiQuestLines(null));
                break;
            case "chapter":
                if (s instanceof QuestScreen) {
                    for (Map.Entry<UUID, IQuestLine> en : QuestLineDatabase.INSTANCE.entrySet()) {
                        if (QuestTranslation.translateQuestLineName(en)
                            .contains(arg)) ((QuestScreen) s).selectChapter(en.getKey());
                    }
                }
                break;
            case "quest":
                UUID qq = questByName(arg);
                if (s instanceof QuestScreen && qq != null) ((QuestScreen) s).focusQuest(qq, true);
                break;
            case "close":
                if (s instanceof QuestScreen) ((QuestScreen) s).closeQuestView();
                break;
            case "edit":
                if (s instanceof QuestScreen) ((QuestScreen) s).setEditing("on".equals(arg));
                break;
            case "mouse": {
                int[] d = toDevice(mc, Integer.parseInt(n[0]), Integer.parseInt(n[1]));
                Mouse.setCursorPosition(d[0], d[1]);
                break;
            }
            case "click":
            case "press":
            case "release": {
                int x = Integer.parseInt(n[0]), y = Integer.parseInt(n[1]);
                int b = n.length > 2 ? Integer.parseInt(n[2]) : 0;
                int[] d = toDevice(mc, x, y);
                Mouse.setCursorPosition(d[0], d[1]);
                if (s instanceof GuiScreenCanvas) {
                    GuiScreenCanvas c = (GuiScreenCanvas) s;
                    if (!"release".equals(a[0])) c.onMouseClick(x, y, b);
                    if (!"press".equals(a[0])) c.onMouseRelease(x, y, b);
                }
                break;
            }
            case "scroll":
                if (s instanceof GuiScreenCanvas) {
                    int sc = Integer.parseInt(n[2]);
                    for (int i = 0; i < Math.abs(sc); i++) {
                        ((GuiScreenCanvas) s).onMouseScroll(Integer.parseInt(n[0]), Integer.parseInt(n[1]), Integer.signum(sc));
                    }
                }
                break;
            case "key":
                if (s instanceof GuiScreenCanvas) {
                    char ch = n.length > 1 ? n[1].charAt(0) : 0;
                    ((GuiScreenCanvas) s).keyTyped(ch, Integer.parseInt(n[0]));
                }
                break;
            case "hoverquest":
            case "clickquest": {
                String[] parts = arg.split("\\s+(?=\\d$)");
                UUID id = questByName(parts[0]);
                int[] pos = s instanceof QuestScreen && id != null ? ((QuestScreen) s).questScreenPos(id) : null;
                if (pos == null) {
                    LOG.warn("quest {} not on screen", arg);
                    break;
                }
                int[] d = toDevice(mc, pos[0], pos[1]);
                Mouse.setCursorPosition(d[0], d[1]);
                if ("clickquest".equals(a[0])) {
                    int b = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                    ((GuiScreenCanvas) s).onMouseClick(pos[0], pos[1], b);
                    ((GuiScreenCanvas) s).onMouseRelease(pos[0], pos[1], b);
                }
                break;
            }
            case "clickimage": {
                int[] pos = s instanceof QuestScreen ? ((QuestScreen) s).imageScreenPos(Integer.parseInt(n[0])) : null;
                if (pos == null) break;
                int b = n.length > 1 ? Integer.parseInt(n[1]) : 0;
                int[] d = toDevice(mc, pos[0], pos[1]);
                Mouse.setCursorPosition(d[0], d[1]);
                ((GuiScreenCanvas) s).onMouseClick(pos[0], pos[1], b);
                ((GuiScreenCanvas) s).onMouseRelease(pos[0], pos[1], b);
                break;
            }
            case "menu":
                if (s instanceof QuestScreen && ((QuestScreen) s).currentPopup() instanceof akasha.questui.client.ui.ContextMenu) {
                    if (!((akasha.questui.client.ui.ContextMenu) ((QuestScreen) s).currentPopup()).activate(arg)) LOG.warn("no menu item {}", arg);
                } else LOG.warn("no menu open");
                break;
            case "pin":
                akasha.questui.Config.sidebarPinned = "on".equals(arg);
                if (s instanceof QuestScreen) ((QuestScreen) s).pinChanged();
                break;
            case "fps":
                LOG.info("fps: {}", mc.debug);
                break;
            case "shot":
                pendingShot = arg;
                break;
            case "exit":
                mc.shutdown();
                break;
            default:
                LOG.warn("unknown step {}", line);
        }
    }

    private static UUID questByName(String part) {
        for (Map.Entry<UUID, IQuest> e : QuestDatabase.INSTANCE.entrySet()) {
            if (QuestTranslation.translateQuestName(e)
                .contains(part)) return e.getKey();
        }
        return null;
    }
}
