package akasha.questui.client.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** DESIGN §1: transforms, anchored zoom, fit and camera bounds. */
public class CameraTest {

    private static Camera camera() {
        Camera c = new Camera();
        c.setViewport(100, 20, 400, 300);
        c.setBounds(new double[] { -200, -100, 600, 300 });
        return c;
    }

    @Test
    public void screenWorldRoundTrip() {
        Camera c = camera();
        c.set(37, -12, 3);
        for (double w : new double[] { -150, 0, 33.3, 500 }) {
            assertEquals(w, c.toWorldX(c.toScreenX(w)), 1e-9);
            assertEquals(w, c.toWorldY(c.toScreenY(w)), 1e-9);
        }
        // viewport centre shows the camera centre
        assertEquals(37, c.toWorldX(300), 1e-9);
        assertEquals(-12, c.toWorldY(170), 1e-9);
    }

    @Test
    public void zoomLevelsAreQuarterOctaves() {
        assertEquals(1.0, Camera.zoomOf(0), 1e-12);
        assertEquals(2.0, Camera.zoomOf(4), 1e-12);
        assertEquals(Math.sqrt(2), Camera.zoomOf(2), 1e-12);
        assertEquals(0.25, Camera.zoomOf(Camera.K_MIN), 1e-12);
    }

    @Test
    public void zoomKeepsTheWorldPointUnderTheCursor() {
        Camera c = camera();
        c.set(50, 40, 0);
        double sx = 180, sy = 90;
        double wx = c.toWorldX(sx), wy = c.toWorldY(sy);
        c.zoomStep(3, sx, sy);
        long t = 0;
        for (int i = 0; i < 200; i++) {
            c.tick(t += 16);
            // the anchor holds during the animation, not only at the end
            assertEquals(wx, c.toWorldX(sx), 1e-6);
            assertEquals(wy, c.toWorldY(sy), 1e-6);
        }
        assertEquals(Camera.zoomOf(3), c.zoom, 1e-9);
    }

    @Test
    public void fitContainsTheContentWithMargin() {
        Camera c = camera();
        c.fit();
        // bounds 800 x 400 plus 2*24 margin into 400 x 300: z_fit = min(400/848, 300/448) = 0.4717 -> k = floor(4 log2)
        // = -5
        assertEquals(-5, c.getLevel());
        assertTrue(c.toScreenX(-200) >= c.vx && c.toScreenX(600) <= c.vx + c.vw);
        assertTrue(c.toScreenY(-100) >= c.vy && c.toScreenY(300) <= c.vy + c.vh);
    }

    @Test
    public void panningKeepsContentVisible() {
        Camera c = camera();
        c.set(0, 0, 0);
        c.pan(100000, 100000);
        // at least 32 px of the content box remain on screen
        assertTrue(c.toScreenX(600) >= c.vx + 32 - 1e-9);
        assertTrue(c.toScreenY(300) >= c.vy + 32 - 1e-9);
        c.pan(-1e7, -1e7);
        assertTrue(c.toScreenX(-200) <= c.vx + c.vw - 32 + 1e-9);
        assertTrue(c.toScreenY(-100) <= c.vy + c.vh - 32 + 1e-9);
    }
}
