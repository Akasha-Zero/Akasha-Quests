package akasha.questui.client.map;

import akasha.questui.client.gfx.Anim;

/**
 * Map camera (DESIGN §1). Screen s = v0 + z (w - c), world w = c + (s - v0) / z, where v0 is the viewport centre.
 * <p>
 * Zoom is quantized to z = 2^(k/4), k in [K_MIN, K_MAX], and the displayed zoom approaches the target with
 * time constant TAU while the world point under the anchor stays fixed on screen.
 */
public class Camera {

    public static final int K_MIN = -8;
    public static final int K_MAX = 8;
    private static final double TAU_MS = 45;
    private static final double MARGIN = 24;
    /** Screen pixels of content that must stay visible when panning (DESIGN §1, camera bounds). */
    private static final double KEEP_VISIBLE = 32;

    public double cx, cy;
    public double zoom = 1;
    private int level;
    private double targetZoom = 1;

    // Zoom anchor: screen point and the world point that must stay under it
    private double anchorSx, anchorSy, anchorWx, anchorWy;
    private boolean anchored;
    private long lastMillis = -1;

    // Viewport
    public double vx, vy, vw = 1, vh = 1;
    // Content bounds {x0, y0, x1, y1} or null
    private double[] bounds;

    public static double zoomOf(int k) {
        return Math.pow(2, k / 4.0);
    }

    public void setViewport(double x, double y, double w, double h) {
        vx = x;
        vy = y;
        vw = Math.max(1, w);
        vh = Math.max(1, h);
    }

    public void setBounds(double[] b) {
        this.bounds = b;
    }

    public double v0x() {
        return vx + vw / 2;
    }

    public double v0y() {
        return vy + vh / 2;
    }

    public double toScreenX(double wx) {
        return v0x() + zoom * (wx - cx);
    }

    public double toScreenY(double wy) {
        return v0y() + zoom * (wy - cy);
    }

    public double toWorldX(double sx) {
        return cx + (sx - v0x()) / zoom;
    }

    public double toWorldY(double sy) {
        return cy + (sy - v0y()) / zoom;
    }

    public int getLevel() {
        return level;
    }

    /** One wheel notch: k += dk, anchored at the screen point (sx, sy). */
    public void zoomStep(int dk, double sx, double sy) {
        int k = Math.max(K_MIN, Math.min(K_MAX, level + dk));
        if (k == level) return;
        level = k;
        targetZoom = zoomOf(k);
        anchorSx = sx;
        anchorSy = sy;
        anchorWx = toWorldX(sx);
        anchorWy = toWorldY(sy);
        anchored = true;
    }

    /** Jump without animation. */
    public void set(double cx, double cy, int k) {
        this.cx = cx;
        this.cy = cy;
        this.level = Math.max(K_MIN, Math.min(K_MAX, k));
        this.zoom = this.targetZoom = zoomOf(level);
        this.anchored = false;
        clamp();
    }

    /** Fit the content box: z_fit = min(w_v / (B_w + 2m), h_v / (B_h + 2m)), level rounded down. */
    public void fit() {
        if (bounds == null) {
            set(0, 0, 0);
            return;
        }
        double bw = bounds[2] - bounds[0] + 2 * MARGIN;
        double bh = bounds[3] - bounds[1] + 2 * MARGIN;
        double zFit = Math.min(vw / bw, vh / bh);
        int k = (int) Math.floor(4 * Math.log(zFit) / Math.log(2) + 1e-9);
        // capped at k = 2 (z = sqrt 2) so a chapter with two quests does not fill the screen
        set((bounds[0] + bounds[2]) / 2, (bounds[1] + bounds[3]) / 2, Math.min(k, 2));
    }

    /** Pan by a screen-space delta: c' = c - delta / z. */
    public void pan(double dsx, double dsy) {
        cx -= dsx / zoom;
        cy -= dsy / zoom;
        if (anchored) {
            // keep zoom animation coherent with the new position
            anchorWx = toWorldX(anchorSx);
            anchorWy = toWorldY(anchorSy);
        }
        clamp();
    }

    /** Centre on a world point, keeping the zoom level. */
    public void centerOn(double wx, double wy) {
        cx = wx;
        cy = wy;
        anchored = false;
        clamp();
    }

    /** Advance the zoom animation (call once per frame). */
    public void tick() {
        tick(Anim.millis());
    }

    void tick(long now) {
        double dt = lastMillis < 0 ? 16 : Math.min(100, now - lastMillis);
        lastMillis = now;
        boolean settled = Math.abs(zoom - targetZoom) < 1e-4 * targetZoom;
        zoom = settled ? targetZoom : zoom + (targetZoom - zoom) * Anim.approach(dt, TAU_MS);
        if (anchored) {
            // c' = w* - (s* - v0) / z'
            cx = anchorWx - (anchorSx - v0x()) / zoom;
            cy = anchorWy - (anchorSy - v0y()) / zoom;
            if (settled) anchored = false;
        }
        clamp();
    }

    /** c in [B0 - (v/2 - keep)/z, B1 + (v/2 - keep)/z] on each axis. */
    private void clamp() {
        if (bounds == null) return;
        double mx = Math.max(0, vw / 2 - KEEP_VISIBLE) / zoom;
        double my = Math.max(0, vh / 2 - KEEP_VISIBLE) / zoom;
        cx = Math.max(bounds[0] - mx, Math.min(bounds[2] + mx, cx));
        cy = Math.max(bounds[1] - my, Math.min(bounds[3] + my, cy));
    }

    public double[] snapshot() {
        return new double[] { cx, cy, level };
    }
}
