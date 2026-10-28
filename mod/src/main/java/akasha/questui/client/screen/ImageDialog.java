package akasha.questui.client.screen;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import net.minecraft.item.Item;

import akasha.questui.client.data.ChapterImage;
import akasha.questui.client.data.ImageSources;
import akasha.questui.client.gfx.Draw;
import akasha.questui.client.gfx.Palette;
import akasha.questui.client.ui.Dialog;
import akasha.questui.client.ui.Lang;
import betterquesting.api.utils.BigItemStack;
import betterquesting.api2.client.gui.controls.PanelTextField;
import betterquesting.api2.client.gui.controls.filters.FieldFilterNumber;
import betterquesting.api2.client.gui.controls.filters.FieldFilterString;
import betterquesting.api2.client.gui.misc.GuiRectangle;
import betterquesting.api2.client.gui.themes.gui_args.GArgsCallback;
import betterquesting.api2.client.gui.themes.gui_args.GArgsFileBrowser;
import betterquesting.api2.client.gui.themes.presets.PresetGUIs;
import betterquesting.api2.utils.QuestTranslation;
import betterquesting.client.themes.ThemeRegistry;

/**
 * Image properties (DESIGN §8). Edits a working copy; BQ's own file browser, item picker and text editor are used
 * for the image source and the hover text. The screen keeps this dialog alive while those screens are open.
 */
public class ImageDialog extends Dialog {

    private static final int LABEL = 76;
    private static final int ROW = 18;
    private static final int W = 284;

    private final QuestScreen screen;
    private final ChapterImage original;
    private final ChapterImage work;
    private final Consumer<ChapterImage> save;

    private PanelTextField<String> fSource, fClick, fDep, fTint;
    private PanelTextField<Double> fW, fH, fRot, fAlpha;
    private int cx, cy;

    public ImageDialog(QuestScreen screen, ChapterImage image, Consumer<ChapterImage> save) {
        super(Lang.get("image.title"), screen::closeDialog);
        this.screen = screen;
        this.original = image;
        this.work = image.copy();
        this.save = save;
        addButton(Lang.get("delete"), () -> {
            screen.closeDialog();
            screen.deleteImage(original);
        });
        addButton(QuestTranslation.translate("gui.cancel"), screen::closeDialog);
        addButton(QuestTranslation.translate("gui.done"), this::onEnter);
    }

    @Override
    protected int contentW() {
        return W;
    }

    @Override
    protected int contentH() {
        return 7 * ROW;
    }

    private int rowY(int r) {
        return cy + r * ROW;
    }

    private static String num(double v) {
        return v == Math.rint(v) ? String.valueOf((long) v) : String.format(Locale.ROOT, "%.2f", v);
    }

    @Override
    protected void build(int cx, int cy) {
        this.cx = cx;
        this.cy = cy;
        int fx = cx + LABEL;
        fSource = text(fx, rowY(0), W - LABEL - 2 * 32, work.image, 256);
        fW = number(fx, rowY(1), 44, work.w);
        fH = number(fx + 58, rowY(1), 44, work.h);
        fRot = number(fx, rowY(2), 44, work.rot);
        fAlpha = number(fx + 130, rowY(2), 36, Math.round(work.alpha * 100));
        fTint = text(fx, rowY(3), 56, String.format("%06X", work.tint & 0xFFFFFF), 6);
        fClick = text(fx, rowY(4), W - LABEL, work.click, 256);
        fDep = text(fx, rowY(5), W - LABEL, work.dep, 64);
    }

    private PanelTextField<String> text(int x, int y, int w, String value, int max) {
        PanelTextField<String> f = new PanelTextField<>(new GuiRectangle(x, y, w, 14), value, FieldFilterString.INSTANCE);
        f.setMaxLength(max);
        fields.add(f);
        return f;
    }

    private PanelTextField<Double> number(int x, int y, int w, double value) {
        PanelTextField<Double> f = new PanelTextField<>(new GuiRectangle(x, y, w, 14), num(value), FieldFilterNumber.DOUBLE);
        fields.add(f);
        return f;
    }

    /** Reads every field back into the working copy (also before leaving for a BQ picker screen). */
    private void pull() {
        work.image = fSource.getRawText()
            .trim();
        work.w = Math.max(1, value(fW, work.w));
        work.h = Math.max(1, value(fH, work.h));
        work.rot = value(fRot, work.rot);
        work.alpha = (float) Math.max(0, Math.min(1, value(fAlpha, work.alpha * 100) / 100.0));
        try {
            work.tint = 0xFF000000 | Integer.parseInt(fTint.getRawText()
                .trim(), 16);
        } catch (NumberFormatException ignored) {}
        work.click = fClick.getRawText()
            .trim();
        work.dep = fDep.getRawText()
            .trim();
    }

    private static double value(PanelTextField<Double> f, double fallback) {
        try {
            return Double.parseDouble(f.getRawText()
                .trim()
                .replace(',', '.'));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    @Override
    protected void onEnter() {
        pull();
        screen.closeDialog();
        save.accept(work.copy());
    }

    // Small inline buttons: {x0, y0, x1, y1}
    private int[] btnFile() {
        return new int[] { cx + W - 62, rowY(0), cx + W - 32, rowY(0) + 14 };
    }

    private int[] btnItem() {
        return new int[] { cx + W - 30, rowY(0), cx + W, rowY(0) + 14 };
    }

    private int[] btnAspect() {
        return new int[] { cx + LABEL + 108, rowY(1), cx + W, rowY(1) + 14 };
    }

    private int[] btnLayer() {
        return new int[] { cx + LABEL + 130, rowY(3), cx + W, rowY(3) + 14 };
    }

    private int[] btnHover() {
        return new int[] { cx + LABEL, rowY(6), cx + LABEL + 110, rowY(6) + 14 };
    }

    private int[] btnEditOnly() {
        return new int[] { cx + LABEL + 116, rowY(6), cx + W, rowY(6) + 14 };
    }

    @Override
    protected void drawContent(int mx, int my, float pt) {
        label(0, Lang.get("image.source"));
        label(1, Lang.get("image.size"));
        Draw.text("×", cx + LABEL + 48, rowY(1) + 3, Palette.TEXT_2, false);
        label(2, Lang.get("image.rotation"));
        Draw.text("°", cx + LABEL + 46, rowY(2) + 3, Palette.TEXT_2, false);
        Draw.text(Lang.get("image.opacity"), cx + LABEL + 62, rowY(2) + 3, Palette.TEXT_2, false);
        Draw.text("%", cx + LABEL + 168, rowY(2) + 3, Palette.TEXT_2, false);
        label(3, Lang.get("image.tint"));
        label(4, Lang.get("image.click"));
        label(5, Lang.get("image.dep"));
        label(6, Lang.get("image.hover"));
        small(btnFile(), Lang.get("image.file"), mx, my);
        small(btnItem(), Lang.get("image.item"), mx, my);
        small(btnAspect(), Lang.get("image.aspect"), mx, my);
        small(btnLayer(), Lang.get("layer." + work.layer), mx, my);
        small(btnHover(), Lang.get("image.hover_edit", work.hover.size()), mx, my);
        int[] e = btnEditOnly();
        Draw.border(e[0], e[1] + 3, e[0] + 8, e[1] + 11, 1, work.editOnly ? Palette.ACCENT : Palette.TEXT_OFF);
        if (work.editOnly) Draw.rect(e[0] + 2, e[1] + 5, e[0] + 6, e[1] + 9, Palette.ACCENT);
        Draw.text(Draw.ellipsize(Lang.get("image.edit_only"), e[2] - e[0] - 12), e[0] + 12, e[1] + 3, Palette.TEXT_2, false);
        // live preview swatch of the tint
        int sw = cx + LABEL + 60;
        Draw.rect(sw, rowY(3) + 2, sw + 10, rowY(3) + 12, work.tint | 0xFF000000);
    }

    private void label(int row, String s) {
        Draw.text(Draw.ellipsize(s, LABEL - 4), cx, rowY(row) + 3, Palette.TEXT_2, false);
    }

    private static void small(int[] r, String s, int mx, int my) {
        boolean hot = mx >= r[0] && mx < r[2] && my >= r[1] && my < r[3];
        Draw.panel(r[0], r[1], r[2], r[3], hot ? Palette.BTN_HOVER : Palette.BTN, hot ? Palette.BTN_EDGE_HOVER : Palette.BTN_EDGE);
        Draw.textCentered(Draw.ellipsize(s, r[2] - r[0] - 4), (r[0] + r[2]) / 2.0, r[1] + 3, Palette.TEXT, false);
    }

    private static boolean in(int[] r, int mx, int my) {
        return mx >= r[0] && mx < r[2] && my >= r[1] && my < r[3];
    }

    @Override
    protected void clickContent(int mx, int my, int button) {
        if (button != 0) return;
        if (in(btnFile(), mx, my)) {
            pull();
            File dir = ImageSources.imageDir();
            if (!dir.exists()) dir.mkdirs();
            screen.leaveForPicker(this, ThemeRegistry.INSTANCE.getGui(PresetGUIs.FILE_EXPLORE, new GArgsFileBrowser(screen, dir,
                files -> {
                    if (files == null || files.length == 0) return;
                    String rel = relative(dir, files[0]);
                    if (rel != null) {
                        work.image = "file:" + rel;
                        fitAspect();
                    }
                }, f -> f.isDirectory() || f.getName()
                    .toLowerCase(Locale.ROOT)
                    .endsWith(".png"), false)));
        } else if (in(btnItem(), mx, my)) {
            pull();
            ImageSources.Resolved r = ImageSources.resolve(work.image);
            BigItemStack start = r.item != null ? new BigItemStack(r.item) : new BigItemStack(net.minecraft.init.Items.diamond);
            screen.leaveForPicker(this, ThemeRegistry.INSTANCE.getGui(PresetGUIs.EDIT_ITEM, new GArgsCallback<>(screen, start,
                stack -> {
                    if (stack == null || stack.getBaseStack() == null) return;
                    String id = Item.itemRegistry.getNameForObject(stack.getBaseStack()
                        .getItem());
                    work.image = "item:" + id + (stack.getBaseStack()
                        .getItemDamage() != 0 ? ":" + stack.getBaseStack()
                            .getItemDamage() : "");
                    work.h = work.w;
                })));
        } else if (in(btnAspect(), mx, my)) {
            pull();
            fitAspect();
            relayout();
        } else if (in(btnLayer(), mx, my)) {
            pull();
            work.layer = (work.layer + 1) % 3;
            relayout();
        } else if (in(btnHover(), mx, my)) {
            pull();
            String joined = String.join("\n", work.hover);
            screen.leaveForPicker(this, ThemeRegistry.INSTANCE.getGui(PresetGUIs.EDIT_TEXT, new GArgsCallback<>(screen, joined,
                text -> {
                    work.hover.clear();
                    if (text != null && !text.trim()
                        .isEmpty()) work.hover.addAll(Arrays.asList(text.split("\n")));
                })));
        } else if (in(btnEditOnly(), mx, my)) {
            pull();
            work.editOnly = !work.editOnly;
            relayout();
        }
    }

    /** h = w / aspect of the source (items are square). */
    private void fitAspect() {
        ImageSources.Resolved r = ImageSources.resolve(work.image);
        double a = r.aspect();
        if (a > 0) work.h = Math.max(1, Math.round(work.w / a * 2) / 2.0);
    }

    private void relayout() {
        layoutCentered(screen.width, screen.height);
    }

    private static String relative(File dir, File f) {
        try {
            String d = dir.getCanonicalPath() + File.separator;
            String p = f.getCanonicalPath();
            return p.startsWith(d) ? p.substring(d.length())
                .replace(File.separatorChar, '/') : null;
        } catch (java.io.IOException e) {
            return null;
        }
    }

    @Override
    protected List<String> contentTooltip(int mx, int my) {
        List<String> tt = new ArrayList<>();
        if (in(btnFile(), mx, my)) tt.add(Lang.get("image.file_tip", ImageSources.FILE_DIR));
        else if (in(btnItem(), mx, my)) tt.add(Lang.get("image.item_tip"));
        else if (my >= rowY(0) && my < rowY(1) && mx < cx + LABEL) tt.addAll(Arrays.asList(Lang.get("image.source_tip").split("\\\\n")));
        else if (my >= rowY(4) && my < rowY(5) && mx < cx + LABEL) tt.addAll(Arrays.asList(Lang.get("image.click_tip").split("\\\\n")));
        else if (my >= rowY(5) && my < rowY(6) && mx < cx + LABEL) tt.add(Lang.get("image.dep_tip"));
        return tt.isEmpty() ? null : tt;
    }
}
