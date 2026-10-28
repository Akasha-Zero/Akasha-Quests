package akasha.questui.client.ui;

import net.minecraft.util.StatCollector;

/** Translations under "akashaquestui." (assets/akashaquestui/lang). */
public final class Lang {

    private Lang() {}

    public static String get(String key, Object... args) {
        return StatCollector.translateToLocalFormatted("akashaquestui." + key, args);
    }
}
