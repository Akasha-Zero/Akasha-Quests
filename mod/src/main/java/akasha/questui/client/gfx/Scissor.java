package akasha.questui.client.gfx;

import java.util.ArrayDeque;
import java.util.Deque;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

/**
 * Nested scissor rectangles in GUI coordinates (untransformed modelview).
 * <p>
 * BQ's RenderUtils.startScissor flips Y with the rounded-up scaled height, ceil(H / f) * f, while the GUI projection
 * uses H / f exactly; when H is not a multiple of f the clip lands up to f - 1 device px too high. Here GUI y maps to
 * device y = H - y * f, the same transform the projection applies.
 */
public final class Scissor {

    private static final Deque<int[]> STACK = new ArrayDeque<>();

    private Scissor() {}

    public static void push(int x, int y, int w, int h) {
        Minecraft mc = Minecraft.getMinecraft();
        int f = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
        int x0 = x * f, x1 = (x + Math.max(0, w)) * f;
        int y0 = mc.displayHeight - (y + Math.max(0, h)) * f, y1 = mc.displayHeight - y * f;
        int[] top = STACK.peek();
        if (top != null) {
            x0 = Math.max(x0, top[0]);
            y0 = Math.max(y0, top[1]);
            x1 = Math.min(x1, top[2]);
            y1 = Math.min(y1, top[3]);
        }
        int[] r = { x0, y0, Math.max(x0, x1), Math.max(y0, y1) };
        STACK.push(r);
        apply(r);
    }

    public static void pop() {
        STACK.pop();
        int[] top = STACK.peek();
        if (top == null) GL11.glDisable(GL11.GL_SCISSOR_TEST);
        else apply(top);
    }

    private static void apply(int[] r) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(r[0], r[1], r[2] - r[0], r[3] - r[1]);
    }
}
