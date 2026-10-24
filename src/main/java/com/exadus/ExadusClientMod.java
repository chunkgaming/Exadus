package com.exadus;

import com.exadus.config.ExadusConfig;
import com.exadus.util.ExadusTargetHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;

public class ExadusClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExadusConfig.load();

        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof JoinMultiplayerScreen
                    || screen instanceof PauseScreen
                    || screen instanceof OptionsScreen) {
                Button toggleButton = Button.builder(
                        ExadusTargetHelper.getToggleButtonText(),
                        btn -> {
                            ExadusConfig.toggle();
                            btn.setMessage(ExadusTargetHelper.getToggleButtonText());
                        }
                ).bounds(scaledWidth - 108, 8, 100, 20).build();

                Screens.getWidgets(screen).add(toggleButton);
            }
        });

        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            if (ExadusTargetHelper.isHeldByTargetPlayer(stack, null) && !lines.isEmpty()) {
                lines.set(0, ExadusTargetHelper.getExadusDisplayName());
            }
        });
    }
}
