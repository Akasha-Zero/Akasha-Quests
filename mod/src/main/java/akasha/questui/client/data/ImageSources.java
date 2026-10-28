package akasha.questui.client.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import akasha.questui.AkashaQuestUI;
import akasha.questui.client.gfx.MipTexture;

/**
 * Resolves the "image" field of a chapter image (DESIGN §8):
 * <ul>
 * <li>{@code item:mod:name[:meta]} - an item icon</li>
 * <li>{@code file:sub/dir/name.png} - a PNG in config/betterquesting/akasha_images/</li>
 * <li>{@code domain:path.png} - a texture from a resource pack or mod</li>
 * </ul>
 */
public final class ImageSources {

    public static final String FILE_DIR = "config/betterquesting/akasha_images";

    private static final Map<String, Resolved> CACHE = new HashMap<>();

    private ImageSources() {}

    public static final class Resolved {

        /** Texture to draw, or null for item / missing sources. */
        public final MipTexture texture;
        public final ItemStack item;
        public final boolean missing;
        final long created = System.currentTimeMillis();
        long lastUse = created;

        Resolved(MipTexture texture, ItemStack item, boolean missing) {
            this.texture = texture;
            this.item = item;
            this.missing = missing;
        }

        /** Natural aspect ratio w/h of the source (1 for items and missing images). */
        public double aspect() {
            if (texture == null || texture.getHeight() == 0) return 1;
            return texture.getWidth() / (double) texture.getHeight();
        }
    }

    public static File imageDir() {
        return new File(Minecraft.getMinecraft().mcDataDir, FILE_DIR);
    }

    /** A missing source is looked up again after this long (a PNG copied into the folder later appears). */
    private static final long MISSING_RETRY_MS = 5000;
    /** Textures unused this long are freed (images of chapters no longer viewed). */
    private static final long EVICT_MS = 120_000;
    private static long lastSweep;

    public static Resolved resolve(String source) {
        if (source == null) source = "";
        long now = System.currentTimeMillis();
        Resolved r = CACHE.get(source);
        if (r == null || r.missing && now - r.created > MISSING_RETRY_MS) {
            r = load(source.trim());
            CACHE.put(source, r);
        }
        r.lastUse = now;
        if (now - lastSweep > 10_000) sweep(now);
        return r;
    }

    private static void sweep(long now) {
        lastSweep = now;
        java.util.Iterator<Resolved> it = CACHE.values()
            .iterator();
        while (it.hasNext()) {
            Resolved r = it.next();
            if (now - r.lastUse > EVICT_MS) {
                if (r.texture != null) r.texture.deleteGlTexture();
                it.remove();
            }
        }
    }

    /** Drops cached images; file images are re-read from disk on next use. */
    public static void clear() {
        for (Resolved r : CACHE.values()) {
            if (r.texture != null) r.texture.deleteGlTexture();
        }
        CACHE.clear();
    }

    private static Resolved load(String src) {
        if (src.isEmpty()) return new Resolved(null, null, true);
        String lower = src.toLowerCase(Locale.ROOT);
        try {
            if (lower.startsWith("item:")) return new Resolved(null, parseItem(src.substring(5)), false);
            MipTexture tex;
            if (lower.startsWith("file:")) {
                File f = safeFile(src.substring(5));
                if (f == null || !f.isFile()) return new Resolved(null, null, true);
                tex = new FileTexture(f);
            } else {
                tex = new MipTexture(new ResourceLocation(src), false).pixelMagnify();
            }
            tex.loadTexture(
                Minecraft.getMinecraft()
                    .getResourceManager());
            return new Resolved(tex, null, false);
        } catch (Exception e) {
            AkashaQuestUI.LOG.warn("Chapter image '{}' could not be loaded: {}", src, e.toString());
            return new Resolved(null, null, true);
        }
    }

    /** Resolves a path inside the image folder, refusing anything that escapes it. */
    public static File safeFile(String rel) throws IOException {
        File dir = imageDir().getCanonicalFile();
        File f = new File(dir, rel.replace('\\', '/')).getCanonicalFile();
        return f.getPath()
            .startsWith(dir.getPath() + File.separator) ? f : null;
    }

    private static ItemStack parseItem(String spec) {
        // mod:name or mod:name:meta
        String[] parts = spec.split(":");
        if (parts.length < 2) return null;
        String id = parts[0] + ":" + parts[1];
        int meta = 0;
        if (parts.length >= 3) {
            try {
                meta = Integer.parseInt(parts[2]);
            } catch (NumberFormatException ignored) {}
        }
        Item item = (Item) Item.itemRegistry.getObject(id);
        return item == null ? null : new ItemStack(item, 1, meta);
    }

    private static final class FileTexture extends MipTexture {

        private final File file;

        FileTexture(File file) {
            super(new ResourceLocation("akashaquestui", "file/" + file.getName()), false);
            this.file = file;
            pixelMagnify();
        }

        @Override
        protected InputStream open(IResourceManager manager) throws IOException {
            return new FileInputStream(file);
        }
    }
}
