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
 * Levels are area-filtered in premultiplied alpha, so transparent texels never bleed dark fringes into the chain,
 * then stored back as straight alpha for the regular SRC_ALPHA / ONE_MINUS_SRC_ALPHA blend. Fully transparent
 * texels carry the colour of their visible neighbours (also filtered down the chain) so bilinear sampling at the
 * edge of a shape does not pull in black.
 */
public class MipTexture extends AbstractTexture {

    private final ResourceLocation location;
    private final boolean repeatU;
    private boolean pixelMagnify;
    private int width;
    private int height;
    private boolean loaded;

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

    /** Largest side kept on load; bigger images are downscaled (memory, and GL_MAX_TEXTURE_SIZE). */
    static final int MAX_SIDE = 2048;

    @Override
    public void loadTexture(IResourceManager manager) throws IOException {
        loaded = false;
        deleteGlTexture();
        BufferedImage img;
        try (InputStream in = open(manager)) {
            img = ImageIO.read(in);
        }
        if (img == null) throw new IOException("Unsupported image: " + location);
        int limit = Math.min(MAX_SIDE, Math.max(64, GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE)));
        img = fit(img, limit);
        width = img.getWidth();
        height = img.getHeight();

        int w = width;
        int h = height;
        int[] argb = img.getRGB(0, 0, w, h, null, 0, w);
        float[] level = toPremultiplied(argb);
        float[] cover = dilate(argb, w, h);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getGlTextureId());
        int lvl = 0;
        while (true) {
            upload(lvl, level, cover, w, h);
            if (w == 1 && h == 1) break;
            // GL sizes each level as floor(prev / 2); odd sizes are area-resampled so no texel is dropped
            int nw = Math.max(1, w / 2);
            int nh = Math.max(1, h / 2);
            level = resample(level, 4, w, h, nw, nh);
            cover = resample(cover, 3, w, h, nw, nh);
            w = nw;
            h = nh;
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
        loaded = true;
    }

    /** False until the image was read and uploaded (a missing resource leaves an empty texture object). */
    public boolean isLoaded() {
        return loaded;
    }

    /** Downscales (area average) so the longer side is at most limit. */
    private static BufferedImage fit(BufferedImage img, int limit) {
        int w = img.getWidth(), h = img.getHeight();
        if (w <= limit && h <= limit) return img;
        double k = limit / (double) Math.max(w, h);
        int nw = Math.max(1, (int) Math.round(w * k)), nh = Math.max(1, (int) Math.round(h * k));
        BufferedImage out = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = out.createGraphics();
        g.drawImage(img.getScaledInstance(nw, nh, java.awt.Image.SCALE_AREA_AVERAGING), 0, 0, null);
        g.dispose();
        return out;
    }

    private static float[] toPremultiplied(int[] argb) {
        float[] out = new float[argb.length * 4];
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

    /**
     * Straight RGB to store under fully transparent texels. Linear filtering mixes the colour of a=0 texels into
     * the edge, so they take the mean colour of their visible 8-neighbours (or keep their own colour when none is
     * visible) instead of becoming black.
     */
    private static float[] dilate(int[] argb, int w, int h) {
        float[] out = new float[w * h * 3];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int i = y * w + x;
                int c = argb[i];
                float r = ((c >> 16) & 0xFF) / 255F, g = ((c >> 8) & 0xFF) / 255F, b = (c & 0xFF) / 255F;
                if ((c >>> 24) == 0) {
                    float sr = 0, sg = 0, sb = 0, sa = 0;
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            int xx = x + dx, yy = y + dy;
                            if (xx < 0 || yy < 0 || xx >= w || yy >= h) continue;
                            int n = argb[yy * w + xx];
                            float a = ((n >>> 24) & 0xFF) / 255F;
                            sr += ((n >> 16) & 0xFF) / 255F * a;
                            sg += ((n >> 8) & 0xFF) / 255F * a;
                            sb += (n & 0xFF) / 255F * a;
                            sa += a;
                        }
                    }
                    if (sa > 0) {
                        r = sr / sa;
                        g = sg / sa;
                        b = sb / sa;
                    }
                }
                out[i * 3] = r;
                out[i * 3 + 1] = g;
                out[i * 3 + 2] = b;
            }
        }
        return out;
    }

    /**
     * Area-weighted box resampling from w x h to nw x nh (nw <= w, nh <= h): each destination texel averages the
     * exact source area it covers, so odd sizes (ratio between 2 and 3) lose nothing and do not shift.
     */
    static float[] resample(float[] src, int ch, int w, int h, int nw, int nh) {
        float[] tmp = new float[nw * h * ch];
        double sx = w / (double) nw;
        for (int x = 0; x < nw; x++) {
            double a0 = x * sx, a1 = a0 + sx;
            for (int i = (int) a0; i < Math.min(w, Math.ceil(a1)); i++) {
                double wt = (Math.min(a1, i + 1) - Math.max(a0, i)) / sx;
                if (wt <= 0) continue;
                for (int y = 0; y < h; y++) {
                    for (int c = 0; c < ch; c++) tmp[(y * nw + x) * ch + c] += (float) (wt * src[(y * w + i) * ch + c]);
                }
            }
        }
        float[] dst = new float[nw * nh * ch];
        double sy = h / (double) nh;
        for (int y = 0; y < nh; y++) {
            double a0 = y * sy, a1 = a0 + sy;
            for (int j = (int) a0; j < Math.min(h, Math.ceil(a1)); j++) {
                double wt = (Math.min(a1, j + 1) - Math.max(a0, j)) / sy;
                if (wt <= 0) continue;
                for (int x = 0; x < nw; x++) {
                    for (int c = 0; c < ch; c++)
                        dst[(y * nw + x) * ch + c] += (float) (wt * tmp[(j * nw + x) * ch + c]);
                }
            }
        }
        return dst;
    }

    private static void upload(int lvl, float[] px, float[] cover, int w, int h) {
        IntBuffer buf = ByteBuffer.allocateDirect(w * h * 4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer();
        for (int i = 0; i < w * h; i++) {
            float a = px[i * 4 + 3];
            int al = clamp255(a);
            int r, g, b;
            if (al > 0) {
                float inv = 1F / a;
                r = clamp255(px[i * 4] * inv);
                g = clamp255(px[i * 4 + 1] * inv);
                b = clamp255(px[i * 4 + 2] * inv);
            } else {
                r = clamp255(cover[i * 3]);
                g = clamp255(cover[i * 3 + 1]);
                b = clamp255(cover[i * 3 + 2]);
            }
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
