package org.moon.moonfix.config.widgets;

import org.moon.moonfix.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.List;

public class InputEntry extends ConfigListWidget.Entry {
    private final Object initValue;
    private final ConfigManager.InputType inputType;

    //buttons
    public final TextFieldWidget field;
    private final ButtonWidget reset;

    public InputEntry(MinecraftClient client, Text title, Text tooltip, ConfigManager.Config config, ConfigManager.InputType inputType) {
        super(client, config, title, tooltip);
        this.initValue = config.value;
        this.inputType = inputType;

        //field
        Text fieldText;
        if (inputType == ConfigManager.InputType.HEX_COLOR)
            fieldText = new LiteralText(String.format("#%06X", config.configValue));
        else
            fieldText = new LiteralText(config.configValue + "");

        this.field = new TextFieldWidget(client.textRenderer, 0, 0, 76, 16, fieldText);
        this.field.setChangedListener((text) -> {
            // Only write config value if it's valid
            if (inputType.validator.test(text)) {
                if (inputType == ConfigManager.InputType.HEX_COLOR)
                    config.configValue = hexToInt(text);
                else
                    config.configValue = text;
            }
        });
        this.field.setMaxLength(1000);
        this.field.setText(fieldText.asString());
        this.field.setCursorToStart();

        //reset button
        this.reset = new ButtonWidget(0, 0, 40, 20, new TranslatableText("controls.reset"), (button) -> {
            config.configValue = config.defaultValue;
            if (inputType == ConfigManager.InputType.HEX_COLOR)
                this.field.setText(String.format("#%06X", config.configValue));
            else
                this.field.setText(config.configValue + "");
        });
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        //text
        TextRenderer textRenderer = client.textRenderer;
        int posY = y + entryHeight / 2;

        if (this.field.isFocused() && this.inputType == ConfigManager.InputType.HEX_COLOR) {
            Text text = new LiteralText("").append(this.title).append(" (").append(this.field.getText()).append(")");
            textRenderer.draw(matrices, text, (float) x, (float) (posY - 9 / 2), 0xFFFFFF);
        }
        else {
            textRenderer.draw(matrices, this.title, (float) x, (float) (posY - 9 / 2), 0xFFFFFF);
        }

        //reset button
        this.reset.x = x + 260;
        this.reset.y = y;
        this.reset.active = !this.config.configValue.equals(this.config.defaultValue + "");
        this.reset.render(matrices, mouseX, mouseY, tickDelta);

        //text field
        this.field.y = y + 2;

        //focused size
        int extraWidth = 0;
        if (this.field.isFocused() && !field.getText().isBlank())
            extraWidth = MathHelper.clamp(textRenderer.getWidth(field.getText()) - 50, 0, 177);

        //set size
        this.field.setWidth(76 + extraWidth);
        this.field.x = x + 177 - extraWidth;

        //set text color
        int color = 0xFFFFFF;

        if (!this.config.configValue.equals(this.initValue + ""))
            if (this.inputType == ConfigManager.InputType.HEX_COLOR)
                color = hexToInt(this.field.getText());
            else
                color = ConfigManager.ACCENT_COLOR.apply(Style.EMPTY).getColor().getRgb();

        if (!inputType.validator.test(field.getText())) {
            color = 0xFF5555;
        }
        this.field.setEditableColor(color);

        //render
        this.field.render(matrices, mouseX, mouseY, tickDelta);
    }

    @Override
    public List<? extends Element> children() {
        return Arrays.asList(this.field, this.reset);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return Arrays.asList(this.field, this.reset);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.field.mouseClicked(mouseX, mouseY, button) || this.reset.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.field.mouseReleased(mouseX, mouseY, button) || this.reset.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers) || this.field.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return this.field.charTyped(chr, modifiers);
    }

    public static int hexToInt(String hexString) {
        //parse hex color
        StringBuilder hex = new StringBuilder(hexString);

        if (hex.toString().startsWith("#")) hex = new StringBuilder(hex.substring(1));
        if (hex.length() < 6) {
            char[] bgChar = hex.toString().toCharArray();

            //special catch for 3
            if (hex.length() == 3)
                hex = new StringBuilder("" + bgChar[0] + bgChar[0] + bgChar[1] + bgChar[1] + bgChar[2] + bgChar[2]);
            else
                hex.append("0".repeat(Math.max(0, 6 - hex.toString().length())));
        }

        try {
            return Integer.parseInt(hex.toString(), 16);
        } catch (Exception ignored) {
            return ConfigManager.ACCENT_COLOR.apply(Style.EMPTY).getColor().getRgb();
        }
    }
}