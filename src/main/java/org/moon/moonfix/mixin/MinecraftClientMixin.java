package org.moon.moonfix.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.Nullable;
import org.moon.moonfix.config.ConfigManager.Config;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Final public GameOptions options;
    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(at = @At("RETURN"), method = "hasOutline", cancellable = true)
    private void hasOutline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        //button not pressed or already true, return
        if (!this.options.spectatorOutlinesKey.isPressed() || cir.getReturnValue())
            return;

        //entity detection
        int config = (int) Config.HIGHLIGHT_ENTITY.value;
        if (config == 0 && entity.getType() != EntityType.PLAYER || config == 1 && !(entity instanceof LivingEntity))
            return;

        //gamemode detection and return
        config = (int) Config.HIGHLIGHT.value;
        GameMode gamemode = this.getNetworkHandler().getPlayerListEntry(this.player.getUuid()).getGameMode();
        if (config == 2 || (gamemode == GameMode.CREATIVE && config == 1) || gamemode == GameMode.SPECTATOR) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("RETURN"), method = "hasReducedDebugInfo", cancellable = true)
    private void hasReducedDebugInfo(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && !(boolean) Config.DEBUG_INFO.value);
    }

    @Shadow @Nullable public abstract ClientPlayNetworkHandler getNetworkHandler();
}
