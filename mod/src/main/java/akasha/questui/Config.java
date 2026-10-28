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
        pulseAvailable = cfg.getBoolean("pulseAvailable", UI, true, "Soft glow pulse on quests that can be started.");

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

    public static void saveState() {
        if (cfg == null) return;
        cfg.get(STATE, "sidebarPinned", true)
            .set(sidebarPinned);
        cfg.get(STATE, "collapsedGroups", new String[0])
            .set(collapsedGroups.toArray(new String[0]));
        cfg.save();
    }
}
