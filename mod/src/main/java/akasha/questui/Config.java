package akasha.questui;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraftforge.common.config.Configuration;

import akasha.questui.client.gfx.Shapes;

/** config/akashaquestui.cfg (client only). */
public final class Config {

    private static final String UI = "ui";
    private static final String STATE = "state";

    private static Configuration cfg;

    /** Replace BetterQuesting's quest screens with the FTB-style screen. */
    public static boolean enabled = true;
    /** Activate the matching BQ theme so BQ's own panels (tasks, rewards, editors) match the dark look. */
    public static boolean applyTheme = true;
    public static String defaultShape = "circle";
    public static String mainShape = "hexagon";
    public static boolean animateLines = true;
    public static double lineSpeed = 0.5;
    public static boolean pulseAvailable = true;
    /** Scroll the selected chapter's name in the side panel when it is too long for its row. */
    public static boolean scrollChapterName = true;
    /**
     * Opacity of the map background; below 1 the world shows through like in FTB Quests. Side panels use
     * min(1, a + 0.15) so their text stays readable (DESIGN §10).
     */
    public static double backgroundOpacity = 0.72;

    // Client view state, saved across sessions
    public static boolean sidebarPinned = true;
    public static final Set<String> collapsedGroups = new LinkedHashSet<>();

    private Config() {}

    public static void load(File file) {
        cfg = new Configuration(file);
        sync();
    }

    private static void sync() {
        enabled = cfg.getBoolean(
            "enabled",
            UI,
            true,
            "Replace BetterQuesting's quest screens with the FTB Quests style screen.");
        applyTheme = cfg.getBoolean(
            "applyTheme",
            UI,
            true,
            "Select the 'Akasha FTB' BetterQuesting theme at startup so BQ panels match the dark look.");
        defaultShape = shape(
            cfg.getString(
                "defaultShape",
                UI,
                "circle",
                "Shape of quests without an explicit shape. One of: " + Shapes.ORDER));
        mainShape = shape(
            cfg.getString("mainShape", UI, "hexagon", "Shape of BQ 'main' quests without an explicit shape."));
        animateLines = cfg.getBoolean("animateLines", UI, true, "Move the chevrons along dependency lines.");
        lineSpeed = cfg.get(UI, "lineSpeed", 0.5, "Chevron speed in tiles per second.", 0.0, 4.0)
            .getDouble();
        // Forge does not enforce the min/max it writes in the comment
        lineSpeed = Double.isNaN(lineSpeed) ? 0.5 : Math.max(0.0, Math.min(4.0, lineSpeed));
        pulseAvailable = cfg.getBoolean("pulseAvailable", UI, true, "Soft glow pulse on quests that can be started.");
        scrollChapterName = cfg.getBoolean(
            "scrollChapterName",
            UI,
            true,
            "Scroll the selected chapter's name in the side panel (marquee) when it does not fit; off: cut with '...'.");
        backgroundOpacity = cfg
            .get(
                UI,
                "backgroundOpacity",
                0.72,
                "Map background opacity (1 = solid, lower shows the world behind).",
                0.2,
                1.0)
            .getDouble();
        backgroundOpacity = Double.isNaN(backgroundOpacity) ? 0.72 : Math.max(0.2, Math.min(1.0, backgroundOpacity));

        sidebarPinned = cfg
            .getBoolean("sidebarPinned", STATE, true, "Chapter panel pinned open (toggled by the pin button).");
        collapsedGroups.clear();
        collapsedGroups.addAll(
            Arrays.asList(cfg.getStringList("collapsedGroups", STATE, new String[0], "Collapsed chapter groups.")));
        if (cfg.hasChanged()) cfg.save();
    }

    private static String shape(String s) {
        return Shapes.exists(s) ? s : "circle";
    }

    public static void setBackgroundOpacity(double a) {
        backgroundOpacity = Math.max(0.2, Math.min(1.0, a));
        if (cfg == null) return;
        cfg.get(UI, "backgroundOpacity", 0.72)
            .set(backgroundOpacity);
        cfg.save();
    }

    /** View options toggles, saved like the opacity. */
    public static void setAnimation(boolean lines, boolean pulse) {
        animateLines = lines;
        pulseAvailable = pulse;
        if (cfg == null) return;
        cfg.get(UI, "animateLines", true)
            .set(lines);
        cfg.get(UI, "pulseAvailable", true)
            .set(pulse);
        cfg.save();
    }

    public static void saveState() {
        if (cfg == null) return;
        cfg.get(STATE, "sidebarPinned", true)
            .set(sidebarPinned);
        cfg.get(STATE, "collapsedGroups", new String[0])
            .set(collapsedGroups.toArray(new String[0]));
        cfg.save();
    }
}
