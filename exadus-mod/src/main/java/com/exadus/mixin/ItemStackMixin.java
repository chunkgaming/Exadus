package com.exadus.mixin;

import com.exadus.util.ExadusTargetHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    private void exadus$overrideMaceName(CallbackInfoReturnable<Text> cir) {
        ItemStack self = (ItemStack) (Object) this;
        if (ExadusTargetHelper.isHeldByTargetPlayer(self, null)) {
            cir.setReturnValue(ExadusTargetHelper.getExadusDisplayName());
        }
    }
}
