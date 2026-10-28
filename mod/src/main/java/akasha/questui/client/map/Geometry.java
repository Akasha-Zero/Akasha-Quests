package akasha.questui.client.map;

/** Pure editing geometry (DESIGN §8), kept free of Minecraft state so it can be unit tested. */
public final class Geometry {

    private Geometry() {}

    public static double snap(double v, double unit) {
        return Math.round(v / unit) * unit;
    }

    /**
     * Resize a rotated rectangle by dragging one handle while the opposite one stays fixed.
     * <p>
     * (sx, sy) in {-1, 0, 1}^2 picks the handle (0 = edge handle, that axis is untouched). The world drag (dx, dy) is
     * taken into the rectangle's frame, d = R(-theta) D; half sizes become h' = max(1, h + s d / 2), and the centre
     * moves by R(theta) (s_x (h'_x - h_x), s_y (h'_y - h_y)). With keepAspect the scale factor of the dragged axes is
     * applied to both. Sizes are rounded to half units.
     *
     * @return {cx, cy, w, h}
     */
    public static double[] resize(double cx, double cy, double w, double h, double rotDeg, int sx, int sy, double dx,
        double dy, boolean keepAspect) {
        double t = Math.toRadians(rotDeg);
        double c = Math.cos(t), s = Math.sin(t);
        double lx = dx * c + dy * s;
        double ly = -dx * s + dy * c;
        // half sizes below 0.5 (bad data) would make the keep-aspect ratio k = n / 0 infinite and the result NaN
        double hx = Math.max(0.5, w / 2), hy = Math.max(0.5, h / 2);
        double nhx = sx == 0 ? hx : Math.max(1, hx + sx * lx / 2);
        double nhy = sy == 0 ? hy : Math.max(1, hy + sy * ly / 2);
        if (keepAspect) {
            double k;
            if (sx != 0 && sy != 0) k = Math.max(nhx / hx, nhy / hy);
            else if (sx != 0) k = nhx / hx;
            else k = nhy / hy;
            nhx = hx * k;
            nhy = hy * k;
        }
        nhx = Math.max(1, Math.round(nhx * 2) / 2.0);
        nhy = Math.max(1, Math.round(nhy * 2) / 2.0);
        // an axis without a handle grows symmetrically (keep-aspect on an edge handle), so its centre shift is 0
        double mxl = sx * (nhx - hx), myl = sy * (nhy - hy);
        return new double[] { cx + mxl * c - myl * s, cy + mxl * s + myl * c, nhx * 2, nhy * 2 };
    }

    /**
     * Rotation for a pointer at (px, py) around the centre (cx, cy): theta = atan2(p - c) + 90 deg, so the handle
     * above the top edge maps to 0. Snapped to 15 deg unless free; result in (-180, 180].
     */
    public static double rotation(double cx, double cy, double px, double py, boolean free) {
        double deg = Math.toDegrees(Math.atan2(py - cy, px - cx)) + 90;
        if (!free) deg = Math.round(deg / 15) * 15;
        deg = ((deg % 360) + 360) % 360;
        return deg > 180 ? deg - 360 : deg;
    }
}
