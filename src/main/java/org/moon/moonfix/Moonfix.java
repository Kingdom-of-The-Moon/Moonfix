package org.moon.moonfix;

import net.fabricmc.api.ClientModInitializer;
import org.moon.moonfix.config.ConfigManager;

public class Moonfix implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //init config
        ConfigManager.initialize();

        //commands
        Commands.initCommands();
    }
}
