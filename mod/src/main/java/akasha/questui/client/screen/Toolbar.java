package akasha.questui.client.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import akasha.questui.client.gfx.Draw;
import akasha.questui.client.gfx.Palette;
import akasha.questui.client.ui.Widget;
import betterquesting.api2.client.gui.resources.textures.IGuiTexture;

/** Vertical icon bar on the right edge (DESIGN §6). Buttons stack from the top or from the bottom. */
public class Toolbar extends Widget {

    public static final int WIDTH = 20;
    private static final int CELL = 18;
    private static final int ICON = 16;

    public interface Action {

        /** Called with the button's screen position so menus can open next to it. */
        void run(int bx, int by);
    }

    public static final class Button {

        final IGuiTexture icon;
        final Supplier<String> tooltip;
        final Action action;
        BooleanSupplier visible = () -> true;
        BooleanSupplier enabled = () -> true;
        /** Tint while drawn, ARGB (lets claim-all pulse). */
        IntSupplier tint = () -> 0xFFFFFFFF;
        final boolean bottom;
        int by;

        Button(IGuiTexture icon, Supplier<String> tooltip, Action action, boolean bottom) {
            this.icon = icon;
            this.tooltip = tooltip;
            this.action = action;
            this.bottom = bottom;
        }

        public Button visible(BooleanSupplier v) {
            this.visible = v;
            return this;
        }

        public Button enabled(BooleanSupplier e) {
            this.enabled = e;
            return this;
        }

        public Button tint(IntSupplier t) {
            this.tint = t;
            return this;
        }
    }

    private final List<Button> buttons = new ArrayList<>();

    public Button top(IGuiTexture icon, Supplier<String> tooltip, Action action) {
        Button b = new Button(icon, tooltip, action, false);
        buttons.add(b);
        return b;
    }

    public Button bottom(IGuiTexture icon, Supplier<String> tooltip, Action action) {
        Button b = new Button(icon, tooltip, action, true);
        buttons.add(b);
        return b;
    }

    private void layout() {
        int ty = y + 2;
        int byy = y + h - 2 - CELL;
        List<Button> bottoms = new ArrayList<>();
        for (Button b : buttons) {
            if (!b.visible.getAsBoolean()) {
                b.by = Integer.MIN_VALUE;
                continue;
            }
            if (b.bottom) bottoms.add(b);
            else {
                b.by = ty;
                ty += CELL;
            }
        }
        // bottom buttons keep their declared order reading downwards
        Collections.reverse(bottoms);
        for (Button b : bottoms) {
            b.by = byy;
            byy -= CELL;
        }
    }

    private Button at(int mx, int my) {
        if (!inside(mx, my)) return null;
        for (Button b : buttons) {
            if (b.by != Integer.MIN_VALUE && my >= b.by && my < b.by + CELL) return b;
        }
        return null;
    }

    @Override
    public void drawPanel(int mx, int my, float partialTick) {
        layout();
        Draw.rect(x, y, x + w, y + h, Palette.PANEL);
        Draw.rect(x, y, x + 1, y + h, Palette.PANEL_EDGE);
        Button hot = at(mx, my);
        int ix = x + (w - ICON) / 2 + 1;
        for (Button b : buttons) {
            if (b.by == Integer.MIN_VALUE) continue;
            boolean en = b.enabled.getAsBoolean();
            if (b == hot && en) Draw.rect(x + 2, b.by, x + w - 1, b.by + CELL, Palette.ROW_SELECTED);
            int tint = en ? b.tint.getAsInt() : 0xFF55555E;
            b.icon.drawTexture(
                ix,
                b.by + (CELL - ICON) / 2,
                ICON,
                ICON,
                0F,
                partialTick,
                new betterquesting.api2.client.gui.resources.colors.GuiColorStatic(tint));
        }
    }

    @Override
    public boolean onMouseClick(int mx, int my, int button) {
        if (!inside(mx, my)) return false;
        Button b = at(mx, my);
        if (b != null && button == 0 && b.enabled.getAsBoolean()) b.action.run(x, b.by);
        return true;
    }

    @Override
    public List<String> getTooltip(int mx, int my) {
        Button b = at(mx, my);
        if (b == null) return null;
        List<String> tt = new ArrayList<>();
        for (String s : b.tooltip.get()
            .split("\n")) tt.add(s);
        return tt;
    }
}
