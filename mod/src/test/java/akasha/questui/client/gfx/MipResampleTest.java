package akasha.questui.client.gfx;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** DESIGN §2: mip levels are area averages; odd sizes keep every texel's contribution. */
public class MipResampleTest {

    private static double sum(float[] a, int ch, int c) {
        double s = 0;
        for (int i = c; i < a.length; i += ch) s += a[i];
        return s;
    }

    @Test
    public void evenSizeIsTwoByTwoBox() {
        float[] src = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 }; // 4x4, 1 channel
        float[] dst = MipTexture.resample(src, 1, 4, 4, 2, 2);
        assertEquals((1 + 2 + 5 + 6) / 4F, dst[0], 1e-6);
        assertEquals((11 + 12 + 15 + 16) / 4F, dst[3], 1e-6);
    }

    /** Mean is preserved exactly (area weights sum to 1), so nothing is dropped or shifted for odd sizes. */
    @Test
    public void oddSizeConservesMean() {
        int w = 7, h = 5, nw = 3, nh = 2;
        float[] src = new float[w * h * 2];
        for (int i = 0; i < src.length; i++) src[i] = (float) Math.sin(i * 1.7) + 1;
        float[] dst = MipTexture.resample(src, 2, w, h, nw, nh);
        for (int c = 0; c < 2; c++) {
            assertEquals(sum(src, 2, c) / (w * h), sum(dst, 2, c) / (nw * nh), 1e-5);
        }
    }

    /** The last column of an odd image still contributes (the old 2x2 filter dropped it). */
    @Test
    public void lastColumnContributes() {
        float[] src = { 0, 0, 3 }; // 3x1
        float[] dst = MipTexture.resample(src, 1, 3, 1, 1, 1);
        assertEquals(1F, dst[0], 1e-6);
    }
}
