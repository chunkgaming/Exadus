package com.exadus;

import com.exadus.config.ExadusConfig;
import com.exadus.util.ExadusTargetHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public class ExadusClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExadusConfig.load();

        ModelLoadingPlugin.register(pluginContext ->
                pluginContext.addModels(ExadusTargetHelper.SCYTHE_MODEL_ID)
        );

        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (ExadusTargetHelper.isHeldByTargetPlayer(stack, null) && !lines.isEmpty()) {
                lines.set(0, ExadusTargetHelper.getExadusDisplayName());
            }
        });
    }
}
