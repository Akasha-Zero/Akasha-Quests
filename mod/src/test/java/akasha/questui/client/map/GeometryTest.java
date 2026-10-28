package akasha.questui.client.map;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** DESIGN §8: resize with a fixed opposite handle, aspect lock and rotation handle. */
public class GeometryTest {

    /** World position of the local point (lx, ly) of a rectangle {cx, cy, w, h} rotated by deg. */
    private static double[] corner(double[] r, double deg, double ux, double uy) {
        double t = Math.toRadians(deg), c = Math.cos(t), s = Math.sin(t);
        double lx = ux * r[2] / 2, ly = uy * r[3] / 2;
        return new double[] { r[0] + lx * c - ly * s, r[1] + lx * s + ly * c };
    }

    @Test
    public void cornerResizeUnrotated() {
        double[] r = Geometry.resize(0, 0, 40, 20, 0, 1, 1, 10, 6, false);
        assertEquals(50, r[2], 1e-9);
        assertEquals(26, r[3], 1e-9);
        assertEquals(5, r[0], 1e-9);
        assertEquals(3, r[1], 1e-9);
    }

    @Test
    public void oppositeCornerStaysFixedWhenRotated() {
        double rot = 30;
        double[] before = { 10, -5, 64, 32 };
        for (int sx = -1; sx <= 1; sx += 2) {
            for (int sy = -1; sy <= 1; sy += 2) {
                double[] r = Geometry.resize(10, -5, 64, 32, rot, sx, sy, 7, -3, false);
                double[] a = corner(before, rot, -sx, -sy);
                double[] b = corner(r, rot, -sx, -sy);
                assertEquals(a[0], b[0], 0.75); // sizes round to half units
                assertEquals(a[1], b[1], 0.75);
            }
        }
    }

    @Test
    public void edgeHandleChangesOneAxis() {
        double[] r = Geometry.resize(0, 0, 40, 20, 0, 0, 1, 99, 8, false);
        assertEquals(40, r[2], 1e-9);
        assertEquals(28, r[3], 1e-9);
        assertEquals(0, r[0], 1e-9);
        assertEquals(4, r[1], 1e-9);
    }

    @Test
    public void keepAspectPreservesRatio() {
        double[] r = Geometry.resize(0, 0, 40, 20, 0, 1, 1, 20, 2, true);
        assertEquals(2.0, r[2] / r[3], 1e-9);
        assertEquals(60, r[2], 1e-9);
    }

    @Test
    public void sizeNeverCollapses() {
        double[] r = Geometry.resize(0, 0, 10, 10, 0, 1, 1, -500, -500, false);
        assertEquals(2, r[2], 1e-9);
        assertEquals(2, r[3], 1e-9);
    }

    @Test
    public void rotationHandle() {
        assertEquals(0, Geometry.rotation(0, 0, 0, -10, false), 1e-9); // straight above
        assertEquals(90, Geometry.rotation(0, 0, 10, 0, false), 1e-9); // to the right (clockwise on screen)
        assertEquals(180, Geometry.rotation(0, 0, 0, 10, false), 1e-9);
        assertEquals(-90, Geometry.rotation(0, 0, -10, 0, false), 1e-9);
        assertEquals(
            15,
            Geometry.rotation(0, 0, Math.sin(Math.toRadians(17)), -Math.cos(Math.toRadians(17)), false),
            1e-9);
        assertEquals(
            17,
            Geometry.rotation(0, 0, Math.sin(Math.toRadians(17)), -Math.cos(Math.toRadians(17)), true),
            1e-9);
    }

    /** Bad data (w = 0) must not produce NaN: BQ's JSON writer refuses NaN and the chapter would stop saving. */
    @Test
    public void zeroSizeKeepAspectStaysFinite() {
        for (int sx = -1; sx <= 1; sx++) {
            for (int sy = -1; sy <= 1; sy++) {
                if (sx == 0 && sy == 0) continue;
                double[] r = Geometry.resize(0, 0, 0, 0, 30, sx, sy, 5, 7, true);
                for (double v : r) {
                    org.junit.Assert.assertFalse(Double.isNaN(v) || Double.isInfinite(v));
                }
                org.junit.Assert.assertTrue(r[2] >= 2 && r[3] >= 2);
            }
        }
    }
}
