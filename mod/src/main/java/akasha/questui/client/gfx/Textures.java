package akasha.questui.client.gfx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;

/** Generated line tiles and quest badges (tools/gen_textures.py). */
public final class Textures {

    public static MipTexture LINE_CHEVRON;
    public static MipTexture LINE_PLAIN;
    public static MipTexture LINE_DASHED;
    public static MipTexture BADGE_CHECK;
    public static MipTexture BADGE_ALERT;
    public static MipTexture BADGE_REPEAT;
    public static MipTexture BADGE_LOCK;
    public static MipTexture BADGE_PIN;

    private Textures() {}

    public static void load() {
        TextureManager tm = Minecraft.getMinecraft()
            .getTextureManager();
        LINE_CHEVRON = Shapes.texture(tm, "textures/lines/chevron.png", true);
        LINE_PLAIN = Shapes.texture(tm, "textures/lines/plain.png", true);
        LINE_DASHED = Shapes.texture(tm, "textures/lines/dashed.png", true);
        BADGE_CHECK = Shapes.texture(tm, "textures/badges/check.png", false);
        BADGE_ALERT = Shapes.texture(tm, "textures/badges/alert.png", false);
        BADGE_REPEAT = Shapes.texture(tm, "textures/badges/repeat.png", false);
        BADGE_LOCK = Shapes.texture(tm, "textures/badges/lock.png", false);
        BADGE_PIN = Shapes.texture(tm, "textures/badges/pin.png", false);
        Shapes.load();
    }
}
