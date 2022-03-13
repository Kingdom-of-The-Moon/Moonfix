package org.moon.moonfix;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.moon.moonfix.config.ConfigManager;

public class MoonfixServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        //init config
        ConfigManager.initialize();
    }
}
