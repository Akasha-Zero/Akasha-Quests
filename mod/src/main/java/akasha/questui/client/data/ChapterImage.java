package akasha.questui.client.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

/**
 * One image placed on a chapter map (DESIGN §8). Position is the centre in world units; rotation in degrees,
 * clockwise on screen.
 */
public class ChapterImage {

    public static final int LAYER_BACK = 0;
    public static final int LAYER_MIDDLE = 1;
    public static final int LAYER_FRONT = 2;

    public String image = "";
    public double x;
    public double y;
    public double w = 32;
    public double h = 32;
    public double rot;
    public float alpha = 1F;
    public int tint = 0xFFFFFFFF;
    public int layer = LAYER_BACK;
    public final List<String> hover = new ArrayList<>();
    public String click = "";
    public String dep = "";
    public boolean editOnly;

    public ChapterImage() {}

    public ChapterImage copy() {
        return read(write());
    }

    public static ChapterImage read(NBTTagCompound tag) {
        ChapterImage img = new ChapterImage();
        img.image = tag.getString("image");
        img.x = tag.getDouble("x");
        img.y = tag.getDouble("y");
        img.w = tag.hasKey("w") ? tag.getDouble("w") : 32;
        img.h = tag.hasKey("h") ? tag.getDouble("h") : 32;
        img.rot = tag.getDouble("rot");
        img.alpha = tag.hasKey("alpha") ? tag.getFloat("alpha") : 1F;
        img.tint = tag.hasKey("tint") ? tag.getInteger("tint") : 0xFFFFFFFF;
        img.layer = Math.max(LAYER_BACK, Math.min(LAYER_FRONT, tag.getInteger("layer")));
        NBTTagList hov = tag.getTagList("hover", Constants.NBT.TAG_STRING);
        for (int i = 0; i < hov.tagCount(); i++) img.hover.add(hov.getStringTagAt(i));
        img.click = tag.getString("click");
        img.dep = tag.getString("dep");
        img.editOnly = tag.getBoolean("edit");
        // NaN or a zero size would poison resize math (k = n / 0) and BQ's JSON writer refuses NaN, which stops
        // the chapter file from saving
        img.x = finite(img.x, 0);
        img.y = finite(img.y, 0);
        img.w = Math.max(1, finite(img.w, 32));
        img.h = Math.max(1, finite(img.h, 32));
        img.rot = finite(img.rot, 0);
        img.alpha = Float.isNaN(img.alpha) ? 1F : Math.max(0F, Math.min(1F, img.alpha));
        return img;
    }

    private static double finite(double v, double fallback) {
        return Double.isNaN(v) || Double.isInfinite(v) ? fallback : v;
    }

    public NBTTagCompound write() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("image", image);
        tag.setDouble("x", x);
        tag.setDouble("y", y);
        tag.setDouble("w", w);
        tag.setDouble("h", h);
        if (rot != 0) tag.setDouble("rot", rot);
        if (alpha != 1F) tag.setFloat("alpha", alpha);
        if (tint != 0xFFFFFFFF) tag.setInteger("tint", tint);
        if (layer != LAYER_BACK) tag.setInteger("layer", layer);
        if (!hover.isEmpty()) {
            NBTTagList hov = new NBTTagList();
            for (String s : hover) hov.appendTag(new NBTTagString(s));
            tag.setTag("hover", hov);
        }
        if (!click.isEmpty()) tag.setString("click", click);
        if (!dep.isEmpty()) tag.setString("dep", dep);
        if (editOnly) tag.setBoolean("edit", true);
        return tag;
    }

    public static List<ChapterImage> readList(NBTTagList list) {
        List<ChapterImage> out = new ArrayList<>();
        for (int i = 0; i < list.tagCount(); i++) out.add(read(list.getCompoundTagAt(i)));
        return out;
    }

    public static NBTTagList writeList(List<ChapterImage> images) {
        NBTTagList list = new NBTTagList();
        for (ChapterImage img : images) list.appendTag(img.write());
        return list;
    }

    /** Rotation in radians. */
    public double theta() {
        return Math.toRadians(rot);
    }

    /** Hit test: u = R(-theta)(p - c), inside if |u_x| <= w/2 and |u_y| <= h/2 (DESIGN §8). */
    public boolean contains(double px, double py) {
        double t = theta();
        double c = Math.cos(t);
        double s = Math.sin(t);
        double dx = px - x;
        double dy = py - y;
        double ux = dx * c + dy * s;
        double uy = -dx * s + dy * c;
        return Math.abs(ux) <= w / 2 && Math.abs(uy) <= h / 2;
    }
}
