package com.exadus.mixin;

import com.exadus.util.ExadusTargetHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Inject(
            method = "getModel(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)Lnet/minecraft/client/render/model/BakedModel;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void exadus$redirectMaceModel(
            ItemStack stack,
            @Nullable World world,
            @Nullable LivingEntity entity,
            int seed,
            CallbackInfoReturnable<BakedModel> cir
    ) {
        if (ExadusTargetHelper.isHeldByTargetPlayer(stack, entity)) {
            BakedModel customModel = MinecraftClient.getInstance()
                    .getBakedModelManager()
                    .getModel(ExadusTargetHelper.SCYTHE_MODEL_ID);
            if (customModel != null && customModel != MinecraftClient.getInstance().getBakedModelManager().getMissingModel()) {
                cir.setReturnValue(customModel);
            }
        }
    }
}
