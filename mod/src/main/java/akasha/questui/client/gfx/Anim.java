package akasha.questui.client.gfx;

import net.minecraft.client.Minecraft;

/** Time base and easing curves (DESIGN §1, §6, §7). */
public final class Anim {

    private Anim() {}

    /** Wall-clock seconds, used for pulses and scrolling chevrons. */
    public static double seconds() {
        return Minecraft.getSystemTime() / 1000.0;
    }

    public static long millis() {
        return Minecraft.getSystemTime();
    }

    public static double clamp01(double t) {
        return t < 0 ? 0 : (t > 1 ? 1 : t);
    }

    /** 1 - (1 - t)^3. */
    public static double easeOutCubic(double t) {
        double u = 1 - clamp01(t);
        return 1 - u * u * u;
    }

    /** Smooth 0..1..0 pulse of the given period: 1/2 - 1/2 cos(2 pi t / T). */
    public static double pulse(double periodSeconds) {
        return 0.5 - 0.5 * Math.cos(2 * Math.PI * seconds() / periodSeconds);
    }

    /** Exponential approach factor for a frame of dt milliseconds with time constant tau. */
    public static double approach(double dtMillis, double tauMillis) {
        return 1 - Math.exp(-dtMillis / tauMillis);
    }
}
