package akasha.questui.client.ui;

import java.util.List;

import betterquesting.api2.client.gui.misc.GuiRectangle;
import betterquesting.api2.client.gui.misc.IGuiRect;
import betterquesting.api2.client.gui.panels.IGuiPanel;

/**
 * Base for the screen's own panels. Layout is absolute (screen GUI pixels, set by the owner on resize), so the
 * geometry in DESIGN §6-§7 maps one to one onto {@link #x}, {@link #y}, {@link #w}, {@link #h}.
 */
public abstract class Widget implements IGuiPanel {

    protected final GuiRectangle rect = new GuiRectangle(0, 0, 0, 0, 0);
    protected int x, y, w, h;
    private boolean enabled = true;

    public void setBounds(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        rect.x = x;
        rect.y = y;
        rect.w = w;
        rect.h = h;
        onResize();
    }

    protected void onResize() {}

    public boolean inside(int mx, int my) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    @Override
    public IGuiRect getTransform() {
        return rect;
    }

    @Override
    public void initPanel() {}

    @Override
    public void setEnabled(boolean state) {
        enabled = state;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean onMouseClick(int mx, int my, int button) {
        return false;
    }

    @Override
    public boolean onMouseRelease(int mx, int my, int button) {
        return false;
    }

    @Override
    public boolean onMouseScroll(int mx, int my, int scroll) {
        return false;
    }

    @Override
    public boolean onKeyTyped(char c, int keycode) {
        return false;
    }

    @Override
    public List<String> getTooltip(int mx, int my) {
        return null;
    }
}
