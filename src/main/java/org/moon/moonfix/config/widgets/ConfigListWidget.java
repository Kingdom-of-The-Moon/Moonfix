package org.moon.moonfix.config.widgets;

import org.moon.moonfix.config.ConfigManager.Config;
import org.moon.moonfix.config.ConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

public class ConfigListWidget extends ElementListWidget<ConfigListWidget.Entry> {

    //focused binding
    public KeyBinding focusedBinding;

    public ConfigListWidget(ConfigScreen parent, MinecraftClient client) {
        super(client, parent.width + 45, parent.height, 43, parent.height - 32, 20);
    }

    public void addEntries(Config[] configs) {
        for (Config config : configs) {
            switch (config.type) {
                case CATEGORY -> addEntry(new CategoryEntry(client, config.name));
                case BOOLEAN -> addEntry(new BooleanEntry(client, config.name, config.tooltip, config));
                case ENUM -> addEntry(new EnumEntry(client, config.name, config.tooltip, config, config.enumList));
                case INPUT -> addEntry(new InputEntry(client, config.name, config.tooltip, config, config.inputType));
                case KEYBIND -> addEntry(new KeyBindEntry(client, config.name, config.tooltip, config, config.keyBind, this));
            }
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 15;
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 150;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.children().forEach(entry -> {
            if (entry instanceof InputEntry inputEntry) {
                inputEntry.field.setTextFieldFocused(inputEntry.field.isMouseOver(mouseX, mouseY));
                if (inputEntry.field.isFocused())
                    inputEntry.field.setSelectionEnd(0);
            }
        });
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public abstract static class Entry extends ElementListWidget.Entry<Entry> {
        public final MinecraftClient client;
        public final Config config;
        public final Text title;
        public final Text tooltip;

        public Entry(MinecraftClient client, Config config, Text title, Text tooltip) {
            this.client = client;
            this.config = config;
            this.title = title;
            this.tooltip = tooltip;
        }
    }
}