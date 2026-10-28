package akasha.questui.client.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Smooth texture with a full mipmap chain built at load time (DESIGN §2).
 * <p>
 * Levels are box-filtered 2x2 in premultiplied alpha, so transparent texels never bleed dark fringes, and
 * then stored back as straight alpha for the regular SRC_ALPHA / ONE_MINUS_SRC_ALPHA blend.
 */
public class MipTexture extends AbstractTexture {

    private final ResourceLocation location;
    private final boolean repeatU;
    private boolean pixelMagnify;
    private int width;
    private int height;

    public MipTexture(ResourceLocation location, boolean repeatU) {
        this.location = location;
        this.repeatU = repeatU;
    }

    /**
     * Magnify with GL_NEAREST (pixel art stays crisp when zoomed in) while still minifying through the mip chain.
     */
    public MipTexture pixelMagnify() {
        this.pixelMagnify = true;
        return this;
    }

    /** Source of the image bytes; resource packs by default, overridden for files on disk. */
    protected InputStream open(IResourceManager manager) throws IOException {
        return manager.getResource(location)
            .getInputStream();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void loadTexture(IResourceManager manager) throws IOException {
        deleteGlTexture();
        BufferedImage img;
        try (InputStream in = open(manager)) {
            img = ImageIO.read(in);
        }
        if (img == null) throw new IOException("Unsupported image: " + location);
        width = img.getWidth();
        height = img.getHeight();

        float[] level = toPremultiplied(img);
        int w = width;
        int h = height;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getGlTextureId());
        int lvl = 0;
        while (true) {
            upload(lvl, level, w, h);
            if (w == 1 && h == 1) break;
            // GL sizes each level as floor(prev / 2); for odd sizes the last row/column (one texel) is dropped
            level = halve(level, w, h);
            w = Math.max(1, w / 2);
            h = Math.max(1, h / 2);
            lvl++;
        }

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_BASE_LEVEL, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, lvl);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(
            GL11.GL_TEXTURE_2D,
            GL11.GL_TEXTURE_MAG_FILTER,
            pixelMagnify ? GL11.GL_NEAREST : GL11.GL_LINEAR);
        GL11.glTexParameteri(
            GL11.GL_TEXTURE_2D,
            GL11.GL_TEXTURE_WRAP_S,
            repeatU ? GL11.GL_REPEAT : GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
    }

    private static float[] toPremultiplied(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        int[] argb = img.getRGB(0, 0, w, h, null, 0, w);
        float[] out = new float[w * h * 4];
        for (int i = 0; i < argb.length; i++) {
            int c = argb[i];
            float a = ((c >>> 24) & 0xFF) / 255F;
            out[i * 4] = ((c >> 16) & 0xFF) / 255F * a;
            out[i * 4 + 1] = ((c >> 8) & 0xFF) / 255F * a;
            out[i * 4 + 2] = (c & 0xFF) / 255F * a;
            out[i * 4 + 3] = a;
        }
        return out;
    }

    private static float[] halve(float[] src, int w, int h) {
        int nw = Math.max(1, w / 2);
        int nh = Math.max(1, h / 2);
        float[] dst = new float[nw * nh * 4];
        for (int y = 0; y < nh; y++) {
            for (int x = 0; x < nw; x++) {
                int x0 = Math.min(w - 1, x * 2);
                int x1 = Math.min(w - 1, x * 2 + 1);
                int y0 = Math.min(h - 1, y * 2);
                int y1 = Math.min(h - 1, y * 2 + 1);
                for (int c = 0; c < 4; c++) {
                    dst[(y * nw + x) * 4 + c] = 0.25F * (src[(y0 * w + x0) * 4 + c] + src[(y0 * w + x1) * 4 + c]
                        + src[(y1 * w + x0) * 4 + c]
                        + src[(y1 * w + x1) * 4 + c]);
                }
            }
        }
        return dst;
    }

    private static void upload(int lvl, float[] px, int w, int h) {
        IntBuffer buf = ByteBuffer.allocateDirect(w * h * 4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer();
        for (int i = 0; i < w * h; i++) {
            float a = px[i * 4 + 3];
            float inv = a > 1e-6F ? 1F / a : 0F;
            int r = clamp255(px[i * 4] * inv);
            int g = clamp255(px[i * 4 + 1] * inv);
            int b = clamp255(px[i * 4 + 2] * inv);
            int al = clamp255(a);
            buf.put((al << 24) | (r << 16) | (g << 8) | b);
        }
        buf.flip();
        GL11.glTexImage2D(
            GL11.GL_TEXTURE_2D,
            lvl,
            GL11.GL_RGBA8,
            w,
            h,
            0,
            GL12.GL_BGRA,
            GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
            buf);
    }

    private static int clamp255(float v) {
        int i = Math.round(v * 255F);
        return i < 0 ? 0 : Math.min(i, 255);
    }
}
