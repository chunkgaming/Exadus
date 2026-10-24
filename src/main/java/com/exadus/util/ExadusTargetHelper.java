package com.exadus.util;

import com.exadus.config.ExadusConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public final class ExadusTargetHelper {
    public static final String TARGET_PLAYER_NAME = "609593e7-debc-43e2-967f-25762ac564d5";
    private static final String TARGET_USERNAME = "Chnks_";
    private static final String TARGET_OFFLINE_UUID = UUID.nameUUIDFromBytes(
            ("OfflinePlayer:" + TARGET_USERNAME).getBytes(StandardCharsets.UTF_8)
    ).toString();

    public static final Identifier SCYTHE_ITEM_MODEL_ID = Identifier.fromNamespaceAndPath("exadus", "exadus_scythe");

    private ExadusTargetHelper() {}

    public static Component getExadusDisplayName() {
        return Component.empty()
                .append(Component.literal("XX").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD, ChatFormatting.OBFUSCATED))
                .append(Component.literal(" ").withStyle(ChatFormatting.RESET))
                .append(Component.literal("Exadus ").withStyle(ChatFormatting.RED, ChatFormatting.BOLD))
                .append(Component.literal("XX").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD, ChatFormatting.OBFUSCATED));
    }

    public static Component getToggleButtonText() {
        boolean enabled = ExadusConfig.isExadusEnabled();
        return Component.literal("Exadus: ")
                .append(Component.literal(enabled ? "ON" : "OFF")
                        .withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED, ChatFormatting.BOLD));
    }

    public static boolean isTargetPlayer(@Nullable LivingEntity entity) {
        if (!(entity instanceof Player player)) {
            return false;
        }
        String uuidString = player.getUUID().toString();
        String cleanUuid = uuidString.replace("-", "");
        String cleanTarget = TARGET_PLAYER_NAME.replace("-", "");
        String nameString = player.getName().getString();
        String profileName = player.getGameProfile().name();

        return TARGET_PLAYER_NAME.equalsIgnoreCase(uuidString)
                || cleanTarget.equalsIgnoreCase(cleanUuid)
                || TARGET_OFFLINE_UUID.equalsIgnoreCase(uuidString)
                || TARGET_PLAYER_NAME.equalsIgnoreCase(nameString)
                || TARGET_USERNAME.equalsIgnoreCase(nameString)
                || TARGET_USERNAME.equalsIgnoreCase(profileName);
    }

    private static boolean isStackInPlayerInventoryOrHands(Player player, ItemStack stack) {
        if (player.getMainHandItem() == stack || player.getOffhandItem() == stack) {
            return true;
        }

        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i) == stack) {
                return true;
            }
        }

        if (player.containerMenu != null) {
            if (player.containerMenu.getCarried() == stack) {
                return true;
            }
            for (Slot slot : player.containerMenu.slots) {
                if (slot.getItem() == stack) {
                    return true;
                }
            }
        }

        if (player.inventoryMenu != null) {
            if (player.inventoryMenu.getCarried() == stack) {
                return true;
            }
            for (Slot slot : player.inventoryMenu.slots) {
                if (slot.getItem() == stack) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isHeldByTargetPlayer(ItemStack stack, @Nullable LivingEntity contextEntity) {
        return isHeldByTargetPlayer(stack, contextEntity, null);
    }

    public static boolean isHeldByTargetPlayer(
            ItemStack stack,
            @Nullable LivingEntity contextEntity,
            @Nullable ItemDisplayContext displayContext
    ) {
        if (!ExadusConfig.isExadusEnabled() || stack == null || stack.isEmpty() || !stack.is(Items.MACE)) {
            return false;
        }

        Minecraft client = Minecraft.getInstance();

        if (contextEntity != null) {
            if (!isTargetPlayer(contextEntity)) {
                return false;
            }
            if (displayContext == ItemDisplayContext.GUI || displayContext == null) {
                return true;
            }
            if (contextEntity instanceof Player player && isStackInPlayerInventoryOrHands(player, stack)) {
                return true;
            }
            return contextEntity == (client != null ? client.player : null);
        }

        if (client == null || client.level == null) {
            return false;
        }

        if (client.player != null && isTargetPlayer(client.player)) {
            if (displayContext == ItemDisplayContext.GUI || displayContext == null) {
                return true;
            }
            if (isStackInPlayerInventoryOrHands(client.player, stack)) {
                return true;
            }
            ItemStack mainHand = client.player.getMainHandItem();
            ItemStack offHand = client.player.getOffhandItem();
            if ((mainHand.is(Items.MACE) && ItemStack.isSameItemSameComponents(stack, mainHand))
                    || (offHand.is(Items.MACE) && ItemStack.isSameItemSameComponents(stack, offHand))) {
                return true;
            }
        }

        for (Player player : client.level.players()) {
            if (isTargetPlayer(player) && isStackInPlayerInventoryOrHands(player, stack)) {
                return true;
            }
        }

        return false;
    }
}
