package akasha.questui.client.gfx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/** DESIGN §2: the hit-test polygons in shapes.json match the analytic shapes they were generated from. */
public class ShapesTest {

    private static JsonObject meta() throws Exception {
        try (InputStreamReader r = new InputStreamReader(
            ShapesTest.class.getResourceAsStream("/assets/akashaquestui/shapes/shapes.json"),
            StandardCharsets.UTF_8)) {
            return new JsonParser().parse(r)
                .getAsJsonObject();
        }
    }

    private static Shapes.Shape shape(JsonObject meta, String id) {
        JsonArray pts = meta.getAsJsonObject("shapes")
            .getAsJsonArray(id);
        float[] xs = new float[pts.size()], ys = new float[pts.size()];
        for (int i = 0; i < xs.length; i++) {
            xs[i] = pts.get(i)
                .getAsJsonArray()
                .get(0)
                .getAsFloat();
            ys[i] = pts.get(i)
                .getAsJsonArray()
                .get(1)
                .getAsFloat();
        }
        return new Shapes.Shape(id, xs, ys, null, null, null, null);
    }

    /** Monte Carlo area over [-1, 1]^2 with a fixed seed. */
    private static double area(Shapes.Shape s) {
        Random rnd = new Random(7);
        int in = 0, n = 200000;
        for (int i = 0; i < n; i++) if (s.contains(rnd.nextDouble() * 2 - 1, rnd.nextDouble() * 2 - 1)) in++;
        return 4.0 * in / n;
    }

    @Test
    public void everyShapeIsCentredAndInside() throws Exception {
        JsonObject m = meta();
        for (String id : Shapes.ORDER) {
            if (Shapes.NONE.equals(id)) continue;
            Shapes.Shape s = shape(m, id);
            assertTrue(id + " contains its centre", s.contains(0, 0) || "heart".equals(id));
            assertFalse(id + " stays inside the unit square", s.contains(0.999, 0.999) && !"square".equals(id));
        }
    }

    @Test
    public void areasMatchTheAnalyticShapes() throws Exception {
        JsonObject m = meta();
        double f = m.get("fit")
            .getAsDouble();
        // circle: pi F^2 (96-gon hit polygon loses 1 - sin(2pi/96)/(2pi/96) ~ 0.07 %)
        assertEquals(Math.PI * f * f, area(shape(m, "circle")), 0.02);
        assertEquals(4 * f * f, area(shape(m, "square")), 0.02);
        assertEquals(2 * f * f, area(shape(m, "diamond")), 0.02);
        // pointy-top hexagon fitted by height: R = F, area = (3 sqrt 3 / 2) R^2
        assertEquals(1.5 * Math.sqrt(3) * f * f, area(shape(m, "hexagon")), 0.02);
    }

    @Test
    public void hexagonCornersAreOutside() throws Exception {
        Shapes.Shape hex = shape(meta(), "hexagon");
        assertTrue(hex.contains(0, -0.9));
        assertFalse(hex.contains(0.95, -0.9)); // corner of the bounding box
    }
}
