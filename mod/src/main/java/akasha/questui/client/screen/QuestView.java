package akasha.questui.client.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import com.google.common.collect.Maps;

import akasha.questui.client.gfx.Anim;
import akasha.questui.client.gfx.Draw;
import akasha.questui.client.gfx.Palette;
import akasha.questui.client.gfx.Textures;
import akasha.questui.client.ui.Lang;
import akasha.questui.client.ui.Widget;
import betterquesting.api.enums.EnumLogic;
import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.api.questing.rewards.IReward;
import betterquesting.api.questing.tasks.ITask;
import betterquesting.api.utils.BigItemStack;
import betterquesting.api2.client.gui.misc.GuiAlign;
import betterquesting.api2.client.gui.misc.GuiPadding;
import betterquesting.api2.client.gui.misc.GuiRectangle;
import betterquesting.api2.client.gui.misc.GuiTransform;
import betterquesting.api2.client.gui.misc.IGuiRect;
import betterquesting.api2.client.gui.misc.TextureSizeHelper;
import betterquesting.api2.client.gui.panels.CanvasTextured;
import betterquesting.api2.client.gui.panels.IGuiPanel;
import betterquesting.api2.client.gui.panels.bars.PanelVScrollBar;
import betterquesting.api2.client.gui.panels.content.PanelGeneric;
import betterquesting.api2.client.gui.panels.content.PanelTextBox;
import betterquesting.api2.client.gui.panels.lists.CanvasScrolling;
import betterquesting.api2.client.gui.resources.colors.GuiColorStatic;
import betterquesting.api2.client.gui.resources.textures.OreDictTexture;
import betterquesting.api2.client.gui.resources.textures.SimpleNoUVTexture;
import betterquesting.api2.client.gui.themes.presets.PresetIcon;
import betterquesting.api2.client.gui.themes.presets.PresetTexture;
import betterquesting.api2.storage.DBEntry;
import betterquesting.api2.utils.QuestTranslation;
import betterquesting.client.BookmarkHandler;
import betterquesting.client.util.GuiTextToggles;
import betterquesting.network.handlers.NetQuestAction;
import betterquesting.questing.QuestDatabase;

/**
 * The quest view in FTB Quests' layout (DESIGN §7): header, Tasks | Rewards columns, description, and the BQ
 * actions (detect/submit, claim). Task and reward contents are BQ's own panels, so every task type, NEI lookup,
 * choice reward and checkbox behaves exactly as in BetterQuesting.
 */
public class QuestView extends Widget {

    private static final int PAD = 6;
    private static final int HEADER = 20;
    private static final int FOOTER = 18;
    private static final int COL_HEADER = 12;
    private static final int SCROLL_W = 8;
    private static final long OPEN_MS = 120;
    private static final Pattern IMG = Pattern.compile("\\[img height=([1-9]\\d*)] *(.*?:.*?) *\\[/img]");
    /** Scroll positions per quest {tasks, rewards, description}, like BQ's GuiQuest. */
    private static final Map<UUID, int[]> SCROLLS = new HashMap<>();

    private final QuestScreen screen;
    private UUID questId;
    private IQuest quest;
    private long openedAt;

    // regions
    private final GuiRectangle rTask = new GuiRectangle(0, 0, 0, 0);
    private final GuiRectangle rReward = new GuiRectangle(0, 0, 0, 0);
    private final GuiRectangle rDesc = new GuiRectangle(0, 0, 0, 0);
    private int colY, colH, descY, descH, footY, midX;
    private int vx, vy, vw, vh;
    private boolean hasRewards;

    // BQ content
    private CanvasScrolling csTask, csReward, csDesc;
    private PanelVScrollBar sbTask, sbReward, sbDesc;
    private final List<IGuiPanel> children = new ArrayList<>();
    private OreDictTexture icon;
    private String title = "";
    private List<java.util.Map.Entry<UUID, IQuest>> deps = new ArrayList<>();
    private List<java.util.Map.Entry<UUID, IQuest>> dependants = new ArrayList<>();

    public QuestView(QuestScreen screen) {
        this.screen = screen;
        setEnabled(false);
    }

    public UUID getQuestId() {
        return isEnabled() ? questId : null;
    }

    public void open(UUID id) {
        IQuest q = QuestDatabase.INSTANCE.get(id);
        if (q == null) return;
        saveScroll();
        questId = id;
        quest = q;
        openedAt = Anim.millis();
        setEnabled(true);
        rebuild();
    }

    public void close() {
        saveScroll();
        setEnabled(false);
        questId = null;
        quest = null;
        children.clear();
    }

    /** Called by the screen after a quest sync. */
    public void refresh() {
        if (!isEnabled()) return;
        quest = QuestDatabase.INSTANCE.get(questId);
        if (quest == null) {
            close();
            return;
        }
        saveScroll();
        rebuild();
    }

    /** Remembers the map viewport the view is centred in (DESIGN §7). */
    public void layoutIn(int vx, int vy, int vw, int vh) {
        this.vx = vx;
        this.vy = vy;
        this.vw = vw;
        this.vh = vh;
        if (isEnabled()) {
            saveScroll();
            rebuild();
        }
    }

    private static int clamp(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    private void saveScroll() {
        if (questId == null || csTask == null) return;
        SCROLLS.put(
            questId,
            new int[] { csTask.getScrollY(), csReward == null ? 0 : csReward.getScrollY(),
                csDesc == null ? 0 : csDesc.getScrollY() });
    }

    // ------------------------------------------------------------------------------------------
    // Build
    // ------------------------------------------------------------------------------------------

    private void rebuild() {
        children.clear();
        if (quest == null || vw <= 0) return;

        title = GuiTextToggles.applyMonochromeIfEnabled(QuestTranslation.translateQuestName(questId, quest));
        BigItemStack ico = quest.getProperty(NativeProps.ICON);
        icon = ico == null ? null : new OreDictTexture(1F, ico, false, true);
        deps = screen.visibleDependencies(quest);
        dependants = screen.visibleDependants(questId);
        hasRewards = quest.getRewards()
            .size() > 0;

        boolean hasTasks = quest.getTasks()
            .size() > 0;
        boolean columns = hasTasks || hasRewards;

        // Width: W_p = clamp(0.78 w_v, 300, 480 with rewards | 360 without) (DESIGN §7); BQ task panels need ~200 px
        int pw = vw < 316 ? vw - 16 : clamp((int) Math.round(0.78 * vw), 300, hasRewards ? 480 : 360);
        int hMax = vh < 206 ? vh - 16 : (int) Math.round(0.82 * vh);
        int innerW = pw - 2 * PAD;
        int colWT = hasRewards ? (innerW - 6) / 2 : innerW;
        int colWR = innerW - 6 - colWT;

        // Measure: task/reward panels and the wrapped description decide the height (shrink to fit)
        rTask.h = rReward.h = hMax;
        int needT = hasTasks ? buildTasks(measureCanvas(), colWT - SCROLL_W) : 0;
        int needR = hasRewards ? buildRewards(measureCanvas(), colWR - SCROLL_W) : 0;
        List<IGuiPanel> descPanels = new ArrayList<>();
        int textH = buildDescription(descPanels, innerW - SCROLL_W - 2);
        int colNeed = columns ? COL_HEADER + 2 + Math.max(needT, needR) : 0;
        int descNeed = textH > 0 ? textH + 4 : 0;
        int gap = columns && descNeed > 0 ? 4 : 0;
        int ph = clamp(HEADER + FOOTER + 2 * PAD + colNeed + gap + descNeed, 150, Math.max(150, hMax));
        setBounds(vx + (vw - pw) / 2, vy + (vh - ph) / 2, pw, ph);

        // Split the body: both fit -> each gets its need, spare space to the columns; otherwise the description
        // gets h_d = min(need, max(36, 0.42 avail, avail - columns need))
        int bodyH = h - HEADER - FOOTER - 2 * PAD;
        int avail = bodyH - gap;
        if (!columns) {
            descH = avail;
            colH = 0;
        } else if (colNeed + descNeed <= avail) {
            descH = descNeed;
            colH = avail - descH;
        } else {
            descH = descNeed == 0 ? 0
                : Math.min(descNeed, Math.max(36, Math.max((int) (0.42 * avail), avail - colNeed)));
            colH = avail - descH;
        }
        int innerX = x + PAD;
        colY = y + HEADER + 2;
        descY = colY + colH + gap;
        footY = y + h - FOOTER - PAD + 4;
        midX = hasRewards ? innerX + colWT + 3 : x + w - PAD;
        rDesc.x = innerX;
        rDesc.w = innerW;

        int listY = colY + COL_HEADER + 2;
        int listH = Math.max(0, colH - COL_HEADER - 2);
        rTask.x = innerX;
        rTask.y = listY;
        rTask.w = colWT;
        rTask.h = listH;
        rReward.x = midX + 3;
        rReward.y = listY;
        rReward.w = colWR;
        rReward.h = listH;
        rDesc.y = descY;
        rDesc.h = descH;

        int[] sc = SCROLLS.getOrDefault(questId, new int[3]);

        if (colH > 0) {
            csTask = scroller(rTask);
            sbTask = scrollBar(rTask, csTask);
            buildTasks(csTask, rTask.w - SCROLL_W);
            restore(csTask, sbTask, sc[0]);
            if (hasRewards) {
                csReward = scroller(rReward);
                sbReward = scrollBar(rReward, csReward);
                buildRewards(csReward, rReward.w - SCROLL_W);
                restore(csReward, sbReward, sc[1]);
            } else {
                csReward = null;
                sbReward = null;
            }
        } else {
            csTask = csReward = null;
            sbTask = sbReward = null;
        }

        if (descH > 0) {
            csDesc = scroller(rDesc);
            sbDesc = scrollBar(rDesc, csDesc);
            for (IGuiPanel p : descPanels) csDesc.addCulledPanel(p, false);
            restore(csDesc, sbDesc, sc[2]);
        } else {
            csDesc = null;
            sbDesc = null;
        }
    }

    private static CanvasScrolling measureCanvas() {
        CanvasScrolling cs = new CanvasScrolling(new GuiRectangle(0, 0, 100, 100));
        cs.initPanel();
        return cs;
    }

    private CanvasScrolling scroller(GuiRectangle region) {
        CanvasScrolling cs = new CanvasScrolling(
            new GuiTransform(GuiAlign.FULL_BOX, new GuiPadding(0, 0, SCROLL_W + 1, 0), 0));
        cs.getTransform()
            .setParent(region);
        cs.initPanel();
        children.add(cs);
        return cs;
    }

    private PanelVScrollBar scrollBar(GuiRectangle region, CanvasScrolling cs) {
        PanelVScrollBar sb = new PanelVScrollBar(
            new GuiTransform(GuiAlign.RIGHT_EDGE, new GuiPadding(-SCROLL_W, 0, 0, 0), 0));
        sb.getTransform()
            .setParent(region);
        sb.initPanel();
        cs.setScrollDriverY(sb);
        children.add(sb);
        return sb;
    }

    private static void restore(CanvasScrolling cs, PanelVScrollBar sb, int scrollY) {
        cs.refreshScrollBounds();
        cs.setScrollY(scrollY);
        cs.updatePanelScroll();
        sb.setEnabled(
            cs.getScrollBounds()
                .getHeight() > 0);
    }

    /** Same structure as BQ's GuiQuest.refreshTaskPanel: numbered title, task panel on a framed canvas, OR marks. */
    private int buildTasks(CanvasScrolling cs, int width) {
        int yy = 0;
        EnumLogic logic = quest.getProperty(NativeProps.LOGIC_TASK);
        List<DBEntry<ITask>> entries = quest.getTasks()
            .getEntries();
        for (int i = 0; i < entries.size(); i++) {
            ITask task = entries.get(i)
                .getValue();
            boolean done = task.isComplete(screen.playerId());
            String name = (i + 1) + ". " + QuestTranslation.translate(task.getUnlocalisedName());
            PanelTextBox label = new PanelTextBox(
                new GuiTransform(new Vector4f(), 0, yy, width, 10, 0),
                (done ? "§a✔ " : "") + name);
            label.setColor(new GuiColorStatic(done ? 0xFF7CD977 : 0xFFC9C9D1));
            cs.addPanel(label);
            yy += 10;

            IGuiPanel gui = task.getTaskGui(
                new GuiTransform(GuiAlign.FULL_BOX, 10, 10, width, rTask.h, 0),
                Maps.immutableEntry(questId, quest));
            if (gui != null) {
                gui.initPanel();
                int ph = gui.getTransform()
                    .getHeight() + 20
                    - gui.getTransform()
                        .getY();
                CanvasTextured frame = new CanvasTextured(
                    new GuiTransform(GuiAlign.TOP_LEFT, 0, yy, width, ph, 1),
                    PresetTexture.PANEL_INNER.getTexture());
                cs.addPanel(frame);
                frame.addPanel(gui);
                yy += ph + 4;
            }
            if (logic == EnumLogic.OR && i < entries.size() - 1) {
                PanelTextBox or = new PanelTextBox(
                    new GuiTransform(new Vector4f(), 0, yy, width, 10, 0),
                    QuestTranslation.translate("betterquesting.gui.logic.or")).setAlignment(1);
                or.setColor(new GuiColorStatic(Palette.ACCENT));
                cs.addPanel(or);
                yy += 12;
            }
            yy += 4;
        }
        return yy;
    }

    /** Same structure as BQ's GuiQuest.refreshRewardPanel. */
    private int buildRewards(CanvasScrolling cs, int width) {
        int yy = 0;
        for (DBEntry<IReward> entry : quest.getRewards()
            .getEntries()) {
            IReward rew = entry.getValue();
            PanelTextBox label = new PanelTextBox(
                new GuiTransform(new Vector4f(), 0, yy, width, 10, 0),
                QuestTranslation.translate(rew.getUnlocalisedName()));
            label.setColor(new GuiColorStatic(0xFFC9C9D1));
            cs.addPanel(label);
            yy += 10;
            IGuiPanel gui = rew.getRewardGui(
                new GuiTransform(GuiAlign.FULL_BOX, 0, 0, width, rReward.h, 111),
                Maps.immutableEntry(questId, quest));
            if (gui != null) {
                gui.initPanel();
                int ph = gui.getTransform()
                    .getHeight()
                    - gui.getTransform()
                        .getY();
                betterquesting.api2.client.gui.panels.CanvasEmpty holder = new betterquesting.api2.client.gui.panels.CanvasEmpty(
                    new GuiTransform(GuiAlign.TOP_LEFT, 0, yy, width, ph, 1));
                cs.addPanel(holder);
                holder.addPanel(gui);
                yy += ph + 4;
            }
        }
        return yy;
    }

    /** BQ description with [img height=N]domain:path[/img] segments; returns the total height. */
    private int buildDescription(List<IGuiPanel> out, int width) {
        String text = QuestTranslation.translateQuestDescription(questId, quest);
        if (text == null || StringUtils.isBlank(text)) return 0;
        Matcher m = IMG.matcher(text);
        int last = 0;
        int yy = 0;
        LinkBridge bridge = new LinkBridge(screen, questId);
        while (m.find()) {
            yy += descText(out, yy, width, text.substring(last, m.start()), bridge);
            last = m.end();
            int hImg = Integer.parseInt(m.group(1));
            ResourceLocation res = new ResourceLocation(m.group(2));
            IGuiRect dim = TextureSizeHelper.getDimension(res);
            if (dim.getWidth() > 0 && dim.getHeight() > 0) {
                // fit width keeping aspect: if the requested height would overflow the width, shrink it
                if ((float) width / dim.getWidth() < (float) hImg / dim.getHeight()) {
                    hImg = -Math.floorDiv(-dim.getHeight() * width, dim.getWidth());
                }
                out.add(
                    new PanelGeneric(
                        new GuiRectangle(0, yy + 2, width, hImg),
                        new SimpleNoUVTexture(res, dim).maintainAspect(true)));
                yy += hImg + 4;
            }
        }
        if (last < text.length() && !StringUtils.isBlank(text.substring(last))) {
            yy += descText(out, yy, width, text.substring(last), bridge);
        }
        return yy;
    }

    private static int descText(List<IGuiPanel> out, int yy, int width, String s, LinkBridge bridge) {
        if (s.isEmpty()) return 0;
        PanelTextBox p = new PanelTextBox(
            new GuiRectangle(0, yy, width, 0),
            GuiTextToggles.applyMonochromeIfEnabled(s),
            true,
            true);
        p.setColor(new GuiColorStatic(0xFFC8C8CE));
        p.setGUI(bridge);
        p.initPanel();
        out.add(p);
        return p.getTransform()
            .getHeight();
    }

    // ------------------------------------------------------------------------------------------
    // Draw
    // ------------------------------------------------------------------------------------------

    private double openT() {
        return Anim.easeOutCubic((Anim.millis() - openedAt) / (double) OPEN_MS);
    }

    @Override
    public void drawPanel(int mx, int my, float partialTick) {
        if (quest == null) return;
        double t = openT();
        // frame: scale 0.92 -> 1 and fade in around the centre
        GL11.glPushMatrix();
        double s = 0.92 + 0.08 * t;
        double cx = x + w / 2.0, cy = y + h / 2.0;
        GL11.glTranslated(cx, cy, 0);
        GL11.glScaled(s, s, 1);
        GL11.glTranslated(-cx, -cy, 0);
        Draw.rect(x + 3, y + 3, x + w + 3, y + h + 3, Draw.mulAlpha(0x70000000, (float) t));
        Draw.rect(x, y, x + w, y + h, Draw.mulAlpha(0xF6141418, (float) t));
        Draw.border(x, y, x + w, y + h, 1, Draw.mulAlpha(Palette.PANEL_EDGE, (float) t));
        GL11.glPopMatrix();
        if (t < 1) return; // BQ panels scissor in screen space, so content waits for the frame to settle

        drawHeader(mx, my, partialTick);
        if (colH > 0) drawColumnHeaders(mx, my);
        if (descH > 0 && colH > 0) Draw.rect(x + PAD, descY - 3, x + w - PAD, descY - 2, Palette.DIVIDER);
        if (hasRewards && colH > 0) Draw.rect(midX, colY, midX + 1, colY + colH, Palette.DIVIDER);
        for (IGuiPanel p : children) if (p.isEnabled()) p.drawPanel(mx, my, partialTick);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        drawFooter(mx, my);
    }

    // header icons, right to left: close, pin, edit (editors), copy
    private int[] headerButtons() {
        List<Integer> xs = new ArrayList<>();
        int bx = x + w - PAD - 10;
        xs.add(bx);
        xs.add(bx -= 13);
        if (screen.canEdit()) xs.add(bx -= 13);
        xs.add(bx - 13);
        int[] out = new int[xs.size()];
        for (int i = 0; i < out.length; i++) out[i] = xs.get(i);
        return out;
    }

    private String headerAction(int i) {
        if (i == 0) return "close";
        if (i == 1) return "pin";
        if (screen.canEdit() && i == 2) return "edit";
        return "copy";
    }

    private void drawHeader(int mx, int my, float pt) {
        Draw.rect(x + 1, y + 1, x + w - 1, y + HEADER, Palette.PANEL_HEADER);
        Draw.rect(x + 1, y + HEADER, x + w - 1, y + HEADER + 1, Palette.PANEL_EDGE);
        if (icon != null) {
            icon.drawTexture(x + PAD, y + 2, 16, 16, 0F, pt);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
        int[] bx = headerButtons();
        int leftLimit = x + PAD + 20;
        int rightLimit = bx[bx.length - 1] - 4;
        int avail = 2 * Math.min(x + w / 2 - leftLimit, rightLimit - (x + w / 2));
        Draw.textCentered(Draw.ellipsize(title, Math.max(20, avail)), x + w / 2.0, y + 6, Palette.TEXT, true);
        for (int i = 0; i < bx.length; i++) {
            String a = headerAction(i);
            boolean hot = mx >= bx[i] - 1 && mx < bx[i] + 11 && my >= y + 4 && my < y + 16;
            if (hot) Draw.rect(bx[i] - 1, y + 4, bx[i] + 11, y + 16, 0x30FFFFFF);
            switch (a) {
                case "close":
                    Draw.textCentered("✕", bx[i] + 5, y + 6, hot ? 0xFFFF6B6B : Palette.TEXT_2, false);
                    break;
                case "pin":
                    Draw.bind(Textures.BADGE_PIN);
                    Draw.tex(
                        bx[i],
                        y + 5,
                        bx[i] + 10,
                        y + 15,
                        BookmarkHandler.isBookmarked(questId) ? 0xFFFFFFFF : 0x60FFFFFF);
                    break;
                case "edit":
                    PresetIcon.ICON_GEAR.getTexture()
                        .drawTexture(bx[i] + 1, y + 6, 8, 8, 0F, pt, new GuiColorStatic(hot ? 0xFFFFFFFF : 0xFFA6A6B0));
                    break;
                default:
                    PresetIcon.ICON_COPY.getTexture()
                        .drawTexture(bx[i] + 1, y + 6, 8, 8, 0F, pt, new GuiColorStatic(hot ? 0xFFFFFFFF : 0xFFA6A6B0));
            }
        }
    }

    private void drawColumnHeaders(int mx, int my) {
        int hy = colY + 2;
        int tCx = (x + PAD + midX) / 2;
        Draw.textCentered(Lang.get("tasks"), tCx, hy, Palette.TASKS, true);
        if (!deps.isEmpty()) arrow(Draw.LEFT, x + PAD, hy, mx, my);
        if (hasRewards) {
            int rCx = (midX + x + w - PAD) / 2;
            Draw.textCentered(Lang.get("rewards"), rCx, hy, Palette.REWARDS, true);
        }
        if (!dependants.isEmpty()) arrow(Draw.RIGHT, x + w - PAD - 8, hy, mx, my);
    }

    private static void arrow(int dir, int ax, int ay, int mx, int my) {
        boolean hot = mx >= ax - 2 && mx < ax + 10 && my >= ay - 2 && my < ay + 10;
        if (hot) Draw.rect(ax - 2, ay - 2, ax + 10, ay + 10, 0x30FFFFFF);
        Draw.triangle(ax, ay, 8, dir, hot ? Palette.TEXT : Palette.TEXT_2);
    }

    private int[] detectBtn() {
        return new int[] { x + PAD, footY, midX - (hasRewards ? 3 : 0), footY + 14 };
    }

    private int[] claimBtn() {
        return new int[] { midX + 3, footY, x + w - PAD, footY + 14 };
    }

    private void drawFooter(int mx, int my) {
        if (quest.getTasks()
            .size() > 0 || !hasRewards) {
            button(detectBtn(), Lang.get("detect"), quest.canSubmit(Minecraft.getMinecraft().thePlayer), mx, my);
        }
        if (hasRewards) {
            button(claimBtn(), Lang.get("claim"), quest.canClaim(Minecraft.getMinecraft().thePlayer), mx, my);
        }
    }

    private static void button(int[] r, String label, boolean active, int mx, int my) {
        boolean hot = active && mx >= r[0] && mx < r[2] && my >= r[1] && my < r[3];
        int edge = !active ? Palette.DIVIDER
            : (hot ? Palette.BTN_EDGE_HOVER
                : Draw.lerpColor(Palette.BTN_EDGE, Palette.Q_DONE_EDGE, (float) Anim.pulse(1.6)));
        Draw.panel(r[0], r[1], r[2], r[3], hot ? Palette.BTN_HOVER : Palette.BTN, edge);
        Draw.textCentered(
            Draw.ellipsize(label, r[2] - r[0] - 6),
            (r[0] + r[2]) / 2.0,
            r[1] + 3,
            active ? Palette.TEXT : Palette.TEXT_OFF,
            false);
    }

    private static boolean in(int[] r, int mx, int my) {
        return mx >= r[0] && mx < r[2] && my >= r[1] && my < r[3];
    }

    // ------------------------------------------------------------------------------------------
    // Input
    // ------------------------------------------------------------------------------------------

    @Override
    public boolean onMouseClick(int mx, int my, int button) {
        if (quest == null) return false;
        if (!inside(mx, my)) {
            screen.closeQuestView(); // click outside dismisses, like FTB Quests
            return true;
        }
        if (openT() < 1) return true;
        if (button == 0 && my >= y + 4 && my < y + 16) {
            int[] bx = headerButtons();
            for (int i = 0; i < bx.length; i++) {
                if (mx >= bx[i] - 1 && mx < bx[i] + 11) {
                    headerClick(headerAction(i));
                    return true;
                }
            }
        }
        if (button == 0 && colH > 0) {
            int hy = colY + 2;
            if (!deps.isEmpty() && mx >= x + PAD - 2 && mx < x + PAD + 10 && my >= hy - 2 && my < hy + 10) {
                screen.questListMenu(deps, Lang.get("view_deps"), x + PAD, hy + 10);
                return true;
            }
            int ax = x + w - PAD - 8;
            if (!dependants.isEmpty() && mx >= ax - 2 && mx < ax + 10 && my >= hy - 2 && my < hy + 10) {
                screen.questListMenu(dependants, Lang.get("view_dependants"), ax, hy + 10);
                return true;
            }
        }
        if (button == 0) {
            if ((quest.getTasks()
                .size() > 0 || !hasRewards) && in(detectBtn(), mx, my)) {
                if (quest.canSubmit(Minecraft.getMinecraft().thePlayer))
                    NetQuestAction.requestDetect(java.util.Collections.singletonList(questId));
                return true;
            }
            if (hasRewards && in(claimBtn(), mx, my)) {
                if (quest.canClaim(Minecraft.getMinecraft().thePlayer))
                    NetQuestAction.requestClaim(java.util.Collections.singletonList(questId));
                return true;
            }
        }
        for (int i = children.size() - 1; i >= 0; i--) {
            IGuiPanel p = children.get(i);
            if (p.isEnabled() && p.onMouseClick(mx, my, button)) return true;
        }
        if (button == 1) screen.closeQuestView();
        return true;
    }

    private void headerClick(String action) {
        switch (action) {
            case "close":
                screen.closeQuestView();
                break;
            case "pin":
                BookmarkHandler.bookmarkQuest(questId);
                screen.refreshMapOnly();
                break;
            case "edit":
                screen.editQuest(questId);
                break;
            default:
                String txt = QuestTranslation.translateQuestDescription(questId, quest);
                GuiScreen.setClipboardString(stripFormatting(txt));
        }
    }

    /** Plain text for the clipboard: formatting codes and BQ tags removed (as BQ's copy button does). */
    static String stripFormatting(String s) {
        return s.replaceAll(
            "§.|&#[0-9a-fA-F]{6}|&[0-9a-fA-Fk-orqgzv]|\\[/?(url|warn|note|quest|questlink)]|\\[img.*?]|\\[/img]",
            "");
    }

    @Override
    public boolean onMouseRelease(int mx, int my, int button) {
        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i)
                .onMouseRelease(mx, my, button)) return true;
        }
        saveScroll();
        return inside(mx, my);
    }

    @Override
    public boolean onMouseScroll(int mx, int my, int scroll) {
        if (!inside(mx, my)) return true;
        for (int i = children.size() - 1; i >= 0; i--) {
            IGuiPanel p = children.get(i);
            if (p.isEnabled() && p.onMouseScroll(mx, my, scroll)) {
                saveScroll();
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyTyped(char c, int keycode) {
        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i)
                .onKeyTyped(c, keycode)) return true;
        }
        return false;
    }

    @Override
    public List<String> getTooltip(int mx, int my) {
        if (!inside(mx, my) || openT() < 1) return null;
        for (int i = children.size() - 1; i >= 0; i--) {
            IGuiPanel p = children.get(i);
            if (!p.isEnabled()) continue;
            List<String> tt = p.getTooltip(mx, my);
            if (tt != null && !tt.isEmpty()) return tt;
        }
        if (my >= y + 4 && my < y + 16) {
            int[] bx = headerButtons();
            for (int i = 0; i < bx.length; i++) {
                if (mx >= bx[i] - 1 && mx < bx[i] + 11) {
                    String a = headerAction(i);
                    if ("pin".equals(a)) a = BookmarkHandler.isBookmarked(questId) ? "unpin" : "pin";
                    List<String> tt = new ArrayList<>();
                    tt.add(Lang.get("btn." + a));
                    return tt;
                }
            }
        }
        if (colH > 0 && my >= colY && my < colY + 12) {
            List<String> tt = new ArrayList<>();
            if (!deps.isEmpty() && mx < x + PAD + 12) tt.add(Lang.get("view_deps"));
            else if (!dependants.isEmpty() && mx >= x + w - PAD - 10) tt.add(Lang.get("view_dependants"));
            return tt.isEmpty() ? null : tt;
        }
        return null;
    }

    /**
     * BQ's PanelTextBox resolves [quest] links through a GuiQuest; this subclass sends them to our screen.
     * Never displayed.
     */
    public static final class LinkBridge extends betterquesting.client.gui2.GuiQuest {

        private final QuestScreen target;

        LinkBridge(QuestScreen target, UUID questId) {
            super(target, questId);
            this.target = target;
        }

        @Override
        public void navigateToQuest(UUID targetId) {
            target.focusQuest(targetId, true);
        }
    }
}
