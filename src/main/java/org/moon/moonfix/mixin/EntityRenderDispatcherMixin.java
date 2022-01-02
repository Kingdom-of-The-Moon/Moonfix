package org.moon.moonfix.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.moon.moonfix.accessors.EntityAccessor;
import org.moon.moonfix.config.ConfigManager.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Unique private Entity fireRenderingEntity;

    //no fire overlay - third person
    @Inject(at = @At("HEAD"), method = "renderFire", cancellable = true)
    private void renderFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, CallbackInfo ci) {
        fireRenderingEntity = entity;

        if (!(boolean) Config.FIRE_OVERLAY.value || entity == null)
            return;

        if (entity instanceof LivingEntity liv && liv.getActiveStatusEffects().containsKey(StatusEffects.FIRE_RESISTANCE))
            ci.cancel();
    }

    //soul fire overlay - third person
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SpriteIdentifier;getSprite()Lnet/minecraft/client/texture/Sprite;"), method = "renderFire")
    private Sprite getFireSprite(SpriteIdentifier instance) {
        if ((boolean) Config.SOUL_FIRE.value && fireRenderingEntity != null && ((EntityAccessor) fireRenderingEntity).hasSoulFire()) {
            Identifier id;
            if (instance.getTextureId() == ModelLoader.FIRE_0.getTextureId())
                id = new Identifier("block/soul_fire_0");
            else
                id = new Identifier("block/soul_fire_1");

            return new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id).getSprite();
        }

        return instance.getSprite();
    }
}
