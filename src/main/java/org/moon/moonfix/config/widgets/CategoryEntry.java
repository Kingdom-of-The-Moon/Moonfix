package org.moon.moonfix.config.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;

public class CategoryEntry extends ConfigListWidget.Entry {

    public CategoryEntry(MinecraftClient client, Text title) {
        super(client, null, title, null);
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        //render text
        TextRenderer textRenderer = client.textRenderer;
        int textWidth = client.textRenderer.getWidth(this.title);
        float xPos = client.currentScreen.width / 2f - textWidth / 2f;
        int yPos = y + entryHeight;
        textRenderer.draw(matrices, this.title, xPos, (float)(yPos - 9 - 1), 0xFFFFFF);
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        return false;
    }

    @Override
    public List<? extends Element> children() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return Collections.emptyList();
    }
}