package org.moon.moonfix.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import org.moon.moonfix.config.ConfigManager;
import org.moon.moonfix.accessors.EntityAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    //no fire overlay - first person
    @Inject(at = @At("HEAD"), method = "renderFireOverlay", cancellable = true)
    private static void renderFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
        if ((boolean) ConfigManager.Config.FIRE_OVERLAY.value && client.player != null && client.player.getActiveStatusEffects().containsKey(StatusEffects.FIRE_RESISTANCE))
            ci.cancel();
    }

    //soul fire overlay - first person
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SpriteIdentifier;getSprite()Lnet/minecraft/client/texture/Sprite;"), method = "renderFireOverlay")
    private static Sprite getFireSprite(SpriteIdentifier instance) {
        Entity player = MinecraftClient.getInstance().player;

        if ((boolean) ConfigManager.Config.SOUL_FIRE.value && player != null && ((EntityAccessor) MinecraftClient.getInstance().player).hasSoulFire())
            return new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier("block/soul_fire_1")).getSprite();

        return instance.getSprite();
    }
}
