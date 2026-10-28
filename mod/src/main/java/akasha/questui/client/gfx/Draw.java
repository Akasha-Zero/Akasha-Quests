package akasha.questui.client.gfx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * Immediate-mode drawing primitives in GUI space (floats, so zoomed geometry stays sub-pixel exact).
 */
public final class Draw {

    private Draw() {}

    public static void blend() {
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
    }

    public static void restore() {
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    private static void color(Tessellator t, int argb) {
        t.setColorRGBA((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF, (argb >>> 24) & 0xFF);
    }

    /** Batch of same-colour rectangles in one draw call: beginRects, then batchRect per rectangle, then endRects. */
    public static void beginRects(int argb) {
        blend();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        color(t, argb);
    }

    public static void batchRect(double x0, double y0, double x1, double y1) {
        Tessellator t = Tessellator.instance;
        t.addVertex(x0, y1, 0);
        t.addVertex(x1, y1, 0);
        t.addVertex(x1, y0, 0);
        t.addVertex(x0, y0, 0);
    }

    public static void endRects() {
        Tessellator.instance.draw();
        restore();
    }

    /** Solid rectangle [x0, x1) x [y0, y1). */
    public static void rect(double x0, double y0, double x1, double y1, int argb) {
        if ((argb >>> 24) == 0) return;
        blend();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        color(t, argb);
        t.addVertex(x0, y1, 0);
        t.addVertex(x1, y1, 0);
        t.addVertex(x1, y0, 0);
        t.addVertex(x0, y0, 0);
        t.draw();
        restore();
    }

    /** Vertical gradient, top colour to bottom colour. */
    public static void gradient(double x0, double y0, double x1, double y1, int top, int bottom) {
        blend();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        color(t, bottom);
        t.addVertex(x0, y1, 0);
        t.addVertex(x1, y1, 0);
        color(t, top);
        t.addVertex(x1, y0, 0);
        t.addVertex(x0, y0, 0);
        t.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        restore();
    }

    /** One-unit border drawn inside the rectangle. */
    public static void border(double x0, double y0, double x1, double y1, double w, int argb) {
        rect(x0, y0, x1, y0 + w, argb);
        rect(x0, y1 - w, x1, y1, argb);
        rect(x0, y0 + w, x0 + w, y1 - w, argb);
        rect(x1 - w, y0 + w, x1, y1 - w, argb);
    }

    /** Panel: fill plus 1px border. */
    public static void panel(double x0, double y0, double x1, double y1, int fill, int edge) {
        rect(x0, y0, x1, y1, fill);
        border(x0, y0, x1, y1, 1, edge);
    }

    public static void bind(ResourceLocation tex) {
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(tex);
    }

    public static void bind(ITextureObject tex) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getGlTextureId());
    }

    /** Textured quad with explicit UVs; the texture must already be bound. */
    public static void tex(double x0, double y0, double x1, double y1, double u0, double v0, double u1, double v1,
        int argb) {
        if ((argb >>> 24) == 0) return;
        blend();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        color(t, argb);
        t.addVertexWithUV(x0, y1, 0, u0, v1);
        t.addVertexWithUV(x1, y1, 0, u1, v1);
        t.addVertexWithUV(x1, y0, 0, u1, v0);
        t.addVertexWithUV(x0, y0, 0, u0, v0);
        t.draw();
        restore();
    }

    public static void tex(double x0, double y0, double x1, double y1, int argb) {
        tex(x0, y0, x1, y1, 0, 0, 1, 1, argb);
    }

    /**
     * Textured rectangle of half-extents (hw, hh) centred on (cx, cy), rotated by theta radians clockwise on screen.
     * Corners are c + R(theta) * (+-hw, +-hh) (DESIGN §8).
     */
    public static void texRotated(double cx, double cy, double hw, double hh, double theta, int argb) {
        if ((argb >>> 24) == 0) return;
        double c = Math.cos(theta);
        double s = Math.sin(theta);
        blend();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        color(t, argb);
        corner(t, cx, cy, -hw, hh, c, s, 0, 1);
        corner(t, cx, cy, hw, hh, c, s, 1, 1);
        corner(t, cx, cy, hw, -hh, c, s, 1, 0);
        corner(t, cx, cy, -hw, -hh, c, s, 0, 0);
        t.draw();
        restore();
    }

    private static void corner(Tessellator t, double cx, double cy, double lx, double ly, double c, double s, double u,
        double v) {
        t.addVertexWithUV(cx + lx * c - ly * s, cy + lx * s + ly * c, 0, u, v);
    }

    /**
     * Textured band from A to B of width w (DESIGN §4). u runs along the band: u = dist / uLen - uPhase, v across.
     */
    public static void band(double ax, double ay, double bx, double by, double w, double uLen, double uPhase,
        int argb) {
        double dx = bx - ax;
        double dy = by - ay;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len < 1e-3 || (argb >>> 24) == 0) return;
        double nx = -dy / len * w / 2;
        double ny = dx / len * w / 2;
        double u0 = -uPhase;
        double u1 = len / uLen - uPhase;
        blend();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        color(t, argb);
        t.addVertexWithUV(ax + nx, ay + ny, 0, u0, 1);
        t.addVertexWithUV(bx + nx, by + ny, 0, u1, 1);
        t.addVertexWithUV(bx - nx, by - ny, 0, u1, 0);
        t.addVertexWithUV(ax - nx, ay - ny, 0, u0, 0);
        t.draw();
        restore();
    }

    /** Direction constants for {@link #triangle}. */
    public static final int RIGHT = 0, DOWN = 1, LEFT = 2, UP = 3;

    /**
     * Filled isosceles triangle inscribed in the square (x, y, s): base on one side, apex at the middle of the
     * opposite side. Used for disclosure and navigation arrows (the MC font has no such glyphs).
     */
    public static void triangle(double x, double y, double s, int dir, int argb) {
        double[][] p;
        double m = s / 2;
        double inset = s * 0.2; // narrower base reads better at 5-7 px
        switch (dir) {
            case DOWN:
                p = new double[][] { { x, y + inset }, { x + s, y + inset }, { x + m, y + s - inset } };
                break;
            case LEFT:
                p = new double[][] { { x + s - inset, y }, { x + s - inset, y + s }, { x + inset, y + m } };
                break;
            case UP:
                p = new double[][] { { x, y + s - inset }, { x + m, y + inset }, { x + s, y + s - inset } };
                break;
            default:
                p = new double[][] { { x + inset, y }, { x + s - inset, y + m }, { x + inset, y + s } };
        }
        blend();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator t = Tessellator.instance;
        // GUI quads are emitted with a negative signed area (y down); keep triangles the same way or they get culled
        double area2 = (p[1][0] - p[0][0]) * (p[2][1] - p[0][1]) - (p[2][0] - p[0][0]) * (p[1][1] - p[0][1]);
        if (area2 > 0) {
            double[] tmp = p[1];
            p[1] = p[2];
            p[2] = tmp;
        }
        t.startDrawing(GL11.GL_TRIANGLES);
        color(t, argb);
        for (double[] v : p) t.addVertex(v[0], v[1], 0);
        t.draw();
        restore();
    }

    /** Untextured line of width w between two points. */
    public static void line(double ax, double ay, double bx, double by, double w, int argb) {
        double dx = bx - ax;
        double dy = by - ay;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len < 1e-6) return;
        double nx = -dy / len * w / 2;
        double ny = dx / len * w / 2;
        blend();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        color(t, argb);
        t.addVertex(ax + nx, ay + ny, 0);
        t.addVertex(bx + nx, by + ny, 0);
        t.addVertex(bx - nx, by - ny, 0);
        t.addVertex(ax - nx, ay - ny, 0);
        t.draw();
        restore();
    }

    /**
     * Item icons (RenderItem) draw with depth test and depth writes; with no clear in between, a later icon fails
     * the depth test where an earlier 3D block icon was and gets holes cut out of it. Call before each item icon
     * that may overlap a previous one, and between screen layers. Honours the active scissor.
     */
    public static void clearDepth() {
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * {@link #clearDepth()} limited to the GUI rectangle [x0, x1) x [y0, y1), intersected with the active scissor.
     * For an icon drawn inside the rectangle the result is the same as a full clear: the depth it is tested against
     * there is cleared, and depth elsewhere only comes from earlier icons, which it does not touch. Under the map's
     * scissor a full clear per icon is a depth pass over the whole map for each of the ~200 icons of a large chapter,
     * which stalls integrated GPUs; the rectangle is a few icon areas.
     */
    public static void clearDepth(double x0, double y0, double x1, double y1) {
        int ix0 = (int) Math.floor(x0), iy0 = (int) Math.floor(y0);
        Scissor.push(ix0, iy0, (int) Math.ceil(x1) - ix0, (int) Math.ceil(y1) - iy0);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        Scissor.pop();
    }

    /**
     * Depth clear for an item icon of the given side centred on (cx, cy), with half a side of margin on every edge
     * for renderers that draw a little past the 16 x 16 box. Clearing more than needed is always the old behaviour.
     */
    public static void clearDepthIcon(double cx, double cy, double side) {
        clearDepth(cx - side, cy - side, cx + side, cy + side);
    }

    /** State left by RenderItem that our flat drawing must not inherit. */
    public static void afterItem() {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    public static FontRenderer font() {
        return Minecraft.getMinecraft().fontRenderer;
    }

    public static void text(String s, double x, double y, int argb, boolean shadow) {
        // set the function too: an enchanted item icon leaves the glint's additive (DST_ALPHA, ONE) behind
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        font().drawString(s, (int) Math.round(x), (int) Math.round(y), argb, shadow);
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    public static void textCentered(String s, double cx, double y, int argb, boolean shadow) {
        text(s, cx - font().getStringWidth(s) / 2.0, y, argb, shadow);
    }

    /** Trims the string to fit maxWidth, appending an ellipsis when cut. */
    public static String ellipsize(String s, int maxWidth) {
        FontRenderer fr = font();
        if (fr.getStringWidth(s) <= maxWidth) return s;
        String dots = "...";
        int dw = fr.getStringWidth(dots);
        if (maxWidth <= dw) return "";
        return fr.trimStringToWidth(s, maxWidth - dw) + dots;
    }

    public static int mulAlpha(int argb, float a) {
        int al = Math.round(((argb >>> 24) & 0xFF) * Math.max(0F, Math.min(1F, a)));
        return (al << 24) | (argb & 0xFFFFFF);
    }

    public static int withAlpha(int rgb, float a) {
        return (Math.round(Math.max(0F, Math.min(1F, a)) * 255F) << 24) | (rgb & 0xFFFFFF);
    }

    public static int lerpColor(int c0, int c1, float t) {
        int a = lerp8(c0 >>> 24, c1 >>> 24, t);
        int r = lerp8((c0 >> 16) & 0xFF, (c1 >> 16) & 0xFF, t);
        int g = lerp8((c0 >> 8) & 0xFF, (c1 >> 8) & 0xFF, t);
        int b = lerp8(c0 & 0xFF, c1 & 0xFF, t);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int lerp8(int a, int b, float t) {
        return Math.round(a + (b - a) * t) & 0xFF;
    }
}
