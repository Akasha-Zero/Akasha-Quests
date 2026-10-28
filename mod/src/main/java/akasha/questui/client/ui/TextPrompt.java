package akasha.questui.client.ui;

import java.util.function.Consumer;

import betterquesting.api2.client.gui.controls.PanelTextField;
import betterquesting.api2.client.gui.controls.filters.FieldFilterString;
import betterquesting.api2.client.gui.misc.GuiRectangle;
import betterquesting.api2.utils.QuestTranslation;

/** One-line text input (group names). */
public class TextPrompt extends Dialog {

    private final String initial;
    private final Consumer<String> done;
    private PanelTextField<String> field;

    public TextPrompt(String title, String initial, Runnable close, Consumer<String> done) {
        super(title, close);
        this.initial = initial;
        this.done = done;
        addButton(QuestTranslation.translate("gui.cancel"), close);
        addButton(QuestTranslation.translate("gui.done"), this::onEnter);
    }

    @Override
    protected int contentW() {
        return 200;
    }

    @Override
    protected int contentH() {
        return 14;
    }

    @Override
    protected void build(int cx, int cy) {
        field = new PanelTextField<>(new GuiRectangle(cx, cy, contentW(), 14), initial, FieldFilterString.INSTANCE);
        field.setMaxLength(64);
        field.lockFocus(true);
        fields.add(field);
    }

    @Override
    protected void onEnter() {
        String v = field.getRawText();
        close.run();
        done.accept(v);
    }
}
