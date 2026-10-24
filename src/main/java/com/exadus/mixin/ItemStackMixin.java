package com.exadus.mixin;

import com.exadus.util.ExadusTargetHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getHoverName", at = @At("HEAD"), cancellable = true, require = 0)
    private void exadus$overrideHoverName(CallbackInfoReturnable<Component> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (ExadusTargetHelper.isHeldByTargetPlayer(self, null)) {
            cir.setReturnValue(ExadusTargetHelper.getExadusDisplayName());
        }
    }

    @Inject(method = "getItemName", at = @At("HEAD"), cancellable = true, require = 0)
    private void exadus$overrideItemName(CallbackInfoReturnable<Component> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (ExadusTargetHelper.isHeldByTargetPlayer(self, null)) {
            cir.setReturnValue(ExadusTargetHelper.getExadusDisplayName());
        }
    }
}
