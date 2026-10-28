package akasha.questui.client.ui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import akasha.questui.client.gfx.Draw;
import akasha.questui.client.gfx.Palette;
import betterquesting.api2.client.gui.panels.IGuiPanel;

/**
 * Modal dialog shown in BQ's popup slot: dimmed screen, titled panel, BQ child panels (text fields) and flat buttons.
 */
public abstract class Dialog extends Widget {

    protected static final int PAD = 6;
    protected static final int TITLE = 16;
    protected static final int BTN_H = 14;

    protected final String title;
    protected final Runnable close;
    protected final List<IGuiPanel> fields = new ArrayList<>();
    private final List<Btn> buttons = new ArrayList<>();
    private int screenW, screenH;

    private static final class Btn {

        final String label;
        final Runnable action;
        int x0, y0, x1, y1;

        Btn(String label, Runnable action) {
            this.label = label;
            this.action = action;
        }
    }

    protected Dialog(String title, Runnable close) {
        this.title = title;
        this.close = close;
    }

    /** Content size (without title and button row). */
    protected abstract int contentW();

    protected abstract int contentH();

    /** Create fields with absolute positions inside (cx, cy). */
    protected abstract void build(int cx, int cy);

    protected void drawContent(int mx, int my, float pt) {}

    /** Called before the fields are rebuilt (window resize, return from a picker): keep what was typed. */
    protected void beforeRelayout() {}

    protected void addButton(String label, Runnable action) {
        buttons.add(new Btn(label, action));
    }

    public Dialog layoutCentered(int sw, int sh) {
        if (!fields.isEmpty()) beforeRelayout();
        screenW = sw;
        screenH = sh;
        int dw = contentW() + 2 * PAD;
        int dh = TITLE + contentH() + BTN_H + 3 * PAD;
        setBounds((sw - dw) / 2, (sh - dh) / 2, dw, dh);
        fields.clear();
        build(x + PAD, y + TITLE + PAD);
        for (IGuiPanel f : fields) f.initPanel();
        // buttons right-aligned on the bottom row
        int bx = x + w - PAD;
        int by = y + h - PAD - BTN_H;
        for (int i = buttons.size() - 1; i >= 0; i--) {
            Btn b = buttons.get(i);
            int bw = Math.max(
                50,
                Draw.font()
                    .getStringWidth(b.label) + 12);
            b.x1 = bx;
            b.x0 = bx - bw;
            b.y0 = by;
            b.y1 = by + BTN_H;
            bx = b.x0 - 4;
        }
        return this;
    }

    @Override
    public void drawPanel(int mx, int my, float pt) {
        Draw.rect(0, 0, screenW, screenH, 0x80000000);
        Draw.rect(x + 3, y + 3, x + w + 3, y + h + 3, 0x70000000);
        Draw.panel(x, y, x + w, y + h, 0xF6141418, Palette.PANEL_EDGE);
        Draw.rect(x + 1, y + 1, x + w - 1, y + TITLE, Palette.PANEL_HEADER);
        Draw.rect(x + 1, y + TITLE, x + w - 1, y + TITLE + 1, Palette.PANEL_EDGE);
        Draw.text(title, x + PAD, y + 4, Palette.TEXT, true);
        drawContent(mx, my, pt);
        for (IGuiPanel f : fields) if (f.isEnabled()) f.drawPanel(mx, my, pt);
        for (Btn b : buttons) {
            boolean hot = mx >= b.x0 && mx < b.x1 && my >= b.y0 && my < b.y1;
            Draw.panel(
                b.x0,
                b.y0,
                b.x1,
                b.y1,
                hot ? Palette.BTN_HOVER : Palette.BTN,
                hot ? Palette.BTN_EDGE_HOVER : Palette.BTN_EDGE);
            Draw.textCentered(b.label, (b.x0 + b.x1) / 2.0, b.y0 + 3, Palette.TEXT, false);
        }
    }

    @Override
    public boolean onMouseClick(int mx, int my, int button) {
        for (Btn b : buttons) {
            if (button == 0 && mx >= b.x0 && mx < b.x1 && my >= b.y0 && my < b.y1) {
                b.action.run();
                return true;
            }
        }
        boolean used = false;
        for (IGuiPanel f : fields) used |= f.isEnabled() && f.onMouseClick(mx, my, button);
        if (!used) clickContent(mx, my, button);
        return true;
    }

    protected void clickContent(int mx, int my, int button) {}

    @Override
    public boolean onMouseRelease(int mx, int my, int button) {
        for (IGuiPanel f : fields) if (f.isEnabled()) f.onMouseRelease(mx, my, button);
        return true;
    }

    @Override
    public boolean onMouseScroll(int mx, int my, int scroll) {
        for (IGuiPanel f : fields) if (f.isEnabled() && f.onMouseScroll(mx, my, scroll)) return true;
        return true;
    }

    @Override
    public boolean onKeyTyped(char c, int keycode) {
        if (keycode == Keyboard.KEY_ESCAPE) {
            close.run();
            return true;
        }
        if (keycode == Keyboard.KEY_RETURN || keycode == Keyboard.KEY_NUMPADENTER) {
            onEnter(); // all dialog fields are single line
            return true;
        }
        for (IGuiPanel f : fields) if (f.isEnabled() && f.onKeyTyped(c, keycode)) return true;
        return true;
    }

    protected void onEnter() {}

    @Override
    public List<String> getTooltip(int mx, int my) {
        for (IGuiPanel f : fields) {
            if (!f.isEnabled()) continue;
            List<String> tt = f.getTooltip(mx, my);
            if (tt != null && !tt.isEmpty()) return tt;
        }
        return contentTooltip(mx, my);
    }

    protected List<String> contentTooltip(int mx, int my) {
        return null;
    }
}
