package akasha.questui.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import net.minecraft.client.Minecraft;

import akasha.questui.client.gfx.Draw;
import akasha.questui.client.gfx.Palette;
import akasha.questui.client.gfx.Scissor;
import betterquesting.api2.client.gui.resources.textures.IGuiTexture;
import betterquesting.api2.client.gui.themes.presets.PresetIcon;

/**
 * Flat context menu in the FTB Quests style: icon + label rows, toggles with a check, hover submenus.
 * Shown through BQ's popup slot, so it receives every input event while open.
 */
public class ContextMenu extends Widget {

    public static final int ROW = 14;
    private static final int PAD = 3;
    private static final int ICON = 8;
    /** Longest label drawn in full; longer ones (quest names in the dependency lists) end in "...". */
    private static final int MAX_LABEL = 200;

    public static class Item {

        final String label;
        final IGuiTexture icon;
        final Runnable action;
        final List<Item> sub;
        final BooleanSupplier checked;
        boolean enabled = true;
        final boolean separator;
        int color = Palette.TEXT;

        private Item(String label, IGuiTexture icon, Runnable action, List<Item> sub, BooleanSupplier checked,
            boolean separator) {
            this.label = label;
            this.icon = icon;
            this.action = action;
            this.sub = sub;
            this.checked = checked;
            this.separator = separator;
        }

        public Item enabled(boolean e) {
            this.enabled = e;
            return this;
        }

        public Item color(int argb) {
            this.color = argb;
            return this;
        }
    }

    private final List<Item> items = new ArrayList<>();
    private final Runnable close;
    private int hoverRow = -1;
    private ContextMenu child;
    private int childRow = -1;
    /** Full content height and the scroll offset when the menu is taller than the screen. */
    private int contentH;
    private int scrollY;

    public ContextMenu(Runnable close) {
        this.close = close;
    }

    public Item add(String label, IGuiTexture icon, Runnable action) {
        Item it = new Item(label, icon, action, null, null, false);
        items.add(it);
        return it;
    }

    public Item add(String label, PresetIcon icon, Runnable action) {
        return add(label, icon == null ? null : icon.getTexture(), action);
    }

    public Item toggle(String label, BooleanSupplier state, Runnable action) {
        Item it = new Item(label, null, action, null, state, false);
        items.add(it);
        return it;
    }

    public Item submenu(String label, PresetIcon icon, List<Item> sub) {
        Item it = new Item(label, icon == null ? null : icon.getTexture(), null, sub, null, false);
        it.enabled = !sub.isEmpty();
        items.add(it);
        return it;
    }

    public void separator() {
        if (!items.isEmpty() && !items.get(items.size() - 1).separator) {
            items.add(new Item("", null, null, null, null, true));
        }
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    /** Builds a detached item list for {@link #submenu}. */
    public static Item item(String label, Runnable action) {
        return new Item(label, null, action, null, null, false);
    }

    public static Item check(String label, BooleanSupplier state, Runnable action) {
        return new Item(label, null, action, null, state, false);
    }

    /** Size from content; placed at (mx, my) and kept on screen. */
    public ContextMenu openAt(int mx, int my) {
        Minecraft mc = Minecraft.getMinecraft();
        net.minecraft.client.gui.ScaledResolution sr = new net.minecraft.client.gui.ScaledResolution(
            mc,
            mc.displayWidth,
            mc.displayHeight);
        int maxText = 0;
        boolean anySub = false;
        for (Item it : items) {
            maxText = Math.max(
                maxText,
                Math.min(
                    MAX_LABEL,
                    Draw.font()
                        .getStringWidth(it.label)));
            anySub |= it.sub != null;
        }
        int width = PAD + ICON + 4 + maxText + (anySub ? 10 : 0) + PAD + 2;
        int height = 2;
        for (Item it : items) height += it.separator ? 3 : ROW;
        contentH = height;
        scrollY = 0;
        height = Math.min(height, sr.getScaledHeight() - 8); // taller lists scroll with the wheel
        int x0 = Math.max(0, Math.min(mx, sr.getScaledWidth() - width));
        int y0 = Math.max(0, Math.min(my, sr.getScaledHeight() - height));
        setBounds(x0, y0, width, height);
        return this;
    }

    private int rowAt(int mx, int my) {
        if (!inside(mx, my) || my < y + 1 || my >= y + h - 1) return -1;
        int yy = y + 1 - scrollY;
        for (int i = 0; i < items.size(); i++) {
            int rh = items.get(i).separator ? 3 : ROW;
            if (my >= yy && my < yy + rh) return items.get(i).separator ? -1 : i;
            yy += rh;
        }
        return -1;
    }

    private int rowY(int row) {
        int yy = y + 1 - scrollY;
        for (int i = 0; i < row; i++) yy += items.get(i).separator ? 3 : ROW;
        return yy;
    }

    private boolean insideTree(int mx, int my) {
        return inside(mx, my) || (child != null && child.insideTree(mx, my));
    }

    @Override
    public void drawPanel(int mx, int my, float partialTick) {
        // soft drop shadow, then the panel
        Draw.rect(x + 2, y + 2, x + w + 2, y + h + 2, 0x60000000);
        Draw.panel(x, y, x + w, y + h, 0xF4141418, Palette.PANEL_EDGE);

        boolean inChild = child != null && child.insideTree(mx, my);
        if (!inChild) hoverRow = rowAt(mx, my);

        // open/close the hover submenu
        if (!inChild && hoverRow != childRow) {
            child = null;
            childRow = -1;
            if (hoverRow >= 0) {
                Item it = items.get(hoverRow);
                if (it.sub != null && it.enabled) {
                    child = new ContextMenu(close);
                    child.items.addAll(it.sub);
                    int ry = rowY(hoverRow);
                    child.openAt(x + w - 1, ry - 1);
                    if (child.x < x + w - 1) child.openAt(x - child.w + 1, ry - 1); // flip left
                    childRow = hoverRow;
                }
            }
        }

        boolean scrolls = contentH > h;
        if (scrolls) Scissor.push(x, y + 1, w, h - 2);
        int yy = y + 1 - scrollY;
        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            if (yy >= y + h || yy + ROW <= y) {
                yy += it.separator ? 3 : ROW;
                continue;
            }
            if (it.separator) {
                Draw.rect(x + PAD, yy + 1, x + w - PAD, yy + 2, Palette.DIVIDER);
                yy += 3;
                continue;
            }
            boolean hot = (i == hoverRow || i == childRow) && it.enabled;
            if (hot) Draw.rect(x + 1, yy, x + w - 1, yy + ROW, Palette.ROW_SELECTED);
            int ix = x + PAD;
            int iy = yy + (ROW - ICON) / 2;
            if (it.checked != null) {
                boolean on = it.checked.getAsBoolean();
                Draw.border(ix, iy, ix + ICON, iy + ICON, 1, on ? Palette.ACCENT : Palette.TEXT_OFF);
                if (on) Draw.rect(ix + 2, iy + 2, ix + ICON - 2, iy + ICON - 2, Palette.ACCENT);
            } else if (it.icon != null) {
                it.icon.drawTexture(ix, iy, ICON, ICON, 0F, partialTick);
            }
            int tc = it.enabled ? it.color : Palette.TEXT_OFF;
            int room = w - (ix + ICON + 4 - x) - PAD - 2 - (it.sub != null ? 10 : 0);
            Draw.text(Draw.ellipsize(it.label, room), ix + ICON + 4, yy + (ROW - 8) / 2 + 1, tc, false);
            if (it.sub != null) Draw.triangle(x + w - PAD - 6, yy + (ROW - 6) / 2.0, 6, Draw.RIGHT, tc);
            yy += ROW;
        }
        if (scrolls) {
            Scissor.pop();
            // 2 px scroll bar, handle proportional to the visible fraction
            int hh = Math.max(8, (h - 2) * (h - 2) / contentH);
            int hy = y + 1 + (int) ((h - 2 - hh) * (scrollY / (double) (contentH - h)));
            Draw.rect(x + w - 3, hy, x + w - 1, hy + hh, 0x80FFFFFF);
        }

        if (child != null) child.drawPanel(mx, my, partialTick);
    }

    @Override
    public boolean onMouseClick(int mx, int my, int button) {
        if (child != null && child.insideTree(mx, my)) return child.onMouseClick(mx, my, button);
        int row = rowAt(mx, my);
        if (row < 0) {
            if (!inside(mx, my)) close.run(); // click outside dismisses
            return true;
        }
        Item it = items.get(row);
        if (!it.enabled || it.sub != null) return true;
        if (it.checked != null) {
            if (it.action != null) it.action.run(); // toggles stay open so several can be flipped
            return true;
        }
        close.run();
        if (it.action != null) it.action.run();
        return true;
    }

    /** Activates the first enabled item whose label contains the text, searching open submenus too. */
    public boolean activate(String labelPart) {
        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            if (it.separator || !it.enabled || !it.label.contains(labelPart)) continue;
            if (it.sub != null) continue;
            if (it.checked == null) close.run();
            if (it.action != null) it.action.run();
            return true;
        }
        for (Item it : items) {
            if (it.sub == null || !it.enabled) continue;
            ContextMenu sub = new ContextMenu(close);
            sub.items.addAll(it.sub);
            if (sub.activate(labelPart)) return true;
        }
        return false;
    }

    @Override
    public boolean onMouseRelease(int mx, int my, int button) {
        return true;
    }

    @Override
    public boolean onMouseScroll(int mx, int my, int scroll) {
        if (child != null && child.insideTree(mx, my)) return child.onMouseScroll(mx, my, scroll);
        if (contentH > h && inside(mx, my)) {
            scrollY = Math.max(0, Math.min(contentH - h, scrollY + (scroll > 0 ? 1 : -1) * 2 * ROW));
            child = null;
            childRow = -1;
        }
        return true;
    }

    @Override
    public boolean onKeyTyped(char c, int keycode) {
        if (keycode == org.lwjgl.input.Keyboard.KEY_ESCAPE) close.run();
        return true;
    }

    @Override
    public List<String> getTooltip(int mx, int my) {
        return null;
    }
}
