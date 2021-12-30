package org.moon.moonfix;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moon.moonfix.config.ConfigManager;

public class Moonfix implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        //init config
        ConfigManager.initialize();
    }
}
