package com.exadus.mixin;

import com.exadus.util.ExadusTargetHelper;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {
    @Unique
    private static final ThreadLocal<Identifier> EXADUS_PREV_MODEL = new ThreadLocal<>();

    @Unique
    private static final ThreadLocal<Boolean> EXADUS_MODEL_SWAPPED = ThreadLocal.withInitial(() -> false);

    @Inject(method = "appendItemLayers", at = @At("HEAD"), require = 0)
    private void exadus$beforeAppendItemLayers(
            ItemStackRenderState renderState,
            ItemStack stack,
            ItemDisplayContext displayContext,
            Level level,
            ItemOwner owner,
            int seed,
            CallbackInfo ci
    ) {
        LivingEntity entity = owner != null ? owner.asLivingEntity() : null;
        if (ExadusTargetHelper.isHeldByTargetPlayer(stack, entity, displayContext)) {
            EXADUS_PREV_MODEL.set(stack.get(DataComponents.ITEM_MODEL));
            EXADUS_MODEL_SWAPPED.set(true);
            stack.set(DataComponents.ITEM_MODEL, ExadusTargetHelper.SCYTHE_ITEM_MODEL_ID);
        }
    }

    @Inject(method = "appendItemLayers", at = @At("RETURN"), require = 0)
    private void exadus$afterAppendItemLayers(
            ItemStackRenderState renderState,
            ItemStack stack,
            ItemDisplayContext displayContext,
            Level level,
            ItemOwner owner,
            int seed,
            CallbackInfo ci
    ) {
        if (Boolean.TRUE.equals(EXADUS_MODEL_SWAPPED.get())) {
            Identifier prev = EXADUS_PREV_MODEL.get();
            if (prev != null) {
                stack.set(DataComponents.ITEM_MODEL, prev);
            } else {
                stack.remove(DataComponents.ITEM_MODEL);
            }
            EXADUS_PREV_MODEL.remove();
            EXADUS_MODEL_SWAPPED.set(false);
        }
    }
}
