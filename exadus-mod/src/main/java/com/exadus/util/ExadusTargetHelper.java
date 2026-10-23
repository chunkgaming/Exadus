package com.exadus.util;

import com.exadus.config.ExadusConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class ExadusTargetHelper {
    public static final String TARGET_PLAYER_NAME = "609593e7-debc-43e2-967f-25762ac564d5";
    public static final Identifier SCYTHE_MODEL_ID = Identifier.of("exadus", "item/exadus_scythe");

    private ExadusTargetHelper() {}

    public static Text getExadusDisplayName() {
        return Text.empty()
                .append(Text.literal("XX").formatted(Formatting.DARK_RED, Formatting.BOLD, Formatting.OBFUSCATED))
                .append(Text.literal(" ").formatted(Formatting.RESET))
                .append(Text.literal("Exadus ").formatted(Formatting.RED, Formatting.BOLD))
                .append(Text.literal("XX").formatted(Formatting.DARK_RED, Formatting.BOLD, Formatting.OBFUSCATED));
    }

    public static boolean isTargetPlayer(@Nullable LivingEntity entity) {
        if (!(entity instanceof PlayerEntity player)) {
            return false;
        }
        String uuidString = player.getUuidAsString();
        String nameString = player.getName().getString();
        return TARGET_PLAYER_NAME.equalsIgnoreCase(uuidString)
                || TARGET_PLAYER_NAME.replace("-", "").equalsIgnoreCase(uuidString.replace("-", ""))
                || TARGET_PLAYER_NAME.equalsIgnoreCase(nameString);
    }

    public static boolean isHeldByTargetPlayer(ItemStack stack, @Nullable LivingEntity contextEntity) {
        if (!ExadusConfig.isExadusEnabled() || stack == null || !stack.isOf(Items.MACE)) {
            return false;
        }

        if (contextEntity != null) {
            return isTargetPlayer(contextEntity)
                    && (contextEntity.getMainHandStack() == stack || contextEntity.getOffHandStack() == stack);
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.world == null) {
            return false;
        }

        for (PlayerEntity player : client.world.getPlayers()) {
            if (isTargetPlayer(player)) {
                if (player.getMainHandStack() == stack || player.getOffHandStack() == stack) {
                    return true;
                }
            }
        }
        return false;
    }
}
