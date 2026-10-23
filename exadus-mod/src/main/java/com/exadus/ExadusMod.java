package com.exadus;

import com.exadus.config.ExadusConfig;
import net.fabricmc.api.ModInitializer;

public class ExadusMod implements ModInitializer {
    @Override
    public void onInitialize() {
        ExadusConfig.load();
    }
}
