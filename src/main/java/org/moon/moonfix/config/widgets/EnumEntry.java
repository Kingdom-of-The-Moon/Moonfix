package org.moon.moonfix.config.widgets;

import org.moon.moonfix.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Arrays;
import java.util.List;

public class EnumEntry extends ConfigListWidget.Entry {
    private final int initValue;
    private final List<Text> states;

    //buttons
    private final ButtonWidget toggle;
    private final ButtonWidget reset;

    public EnumEntry(MinecraftClient client, Text title, Text tooltip, ConfigManager.Config config, List<Text> states) {
        super(client, config, title, tooltip);
        this.initValue = (int) config.value;
        this.states = states;

        //toggle button
        this.toggle = new ButtonWidget(0, 0, 80, 20, this.title, (button) -> config.configValue = (int) config.configValue + 1);

        //reset button
        this.reset = new ButtonWidget(0, 0, 40, 20, new TranslatableText("controls.reset"), (button) -> config.configValue = config.defaultValue);
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        //text
        TextRenderer textRenderer = client.textRenderer;
        int posY = y + entryHeight / 2;
        textRenderer.draw(matrices, this.title, (float) x, (float)(posY - 9 / 2), 0xFFFFFF);

        //reset button
        this.reset.x = x + 260;
        this.reset.y = y;
        this.reset.active = !this.config.configValue.equals(this.config.defaultValue);
        this.reset.render(matrices, mouseX, mouseY, tickDelta);

        //toggle button
        this.toggle.x = x + 175;
        this.toggle.y = y;
        this.toggle.setMessage(states.get((int) this.config.configValue % states.size()));

        //if setting is changed
        if ((int) this.config.configValue != this.initValue)
            this.toggle.setMessage(new LiteralText("").styled(ConfigManager.ACCENT_COLOR).append(this.toggle.getMessage()));

        this.toggle.render(matrices, mouseX, mouseY, tickDelta);
    }

    @Override
    public List<? extends Element> children() {
        return Arrays.asList(this.toggle, this.reset);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return Arrays.asList(this.toggle, this.reset);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.toggle.mouseClicked(mouseX, mouseY, button) || this.reset.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.toggle.mouseReleased(mouseX, mouseY, button) || this.reset.mouseReleased(mouseX, mouseY, button);
    }
}