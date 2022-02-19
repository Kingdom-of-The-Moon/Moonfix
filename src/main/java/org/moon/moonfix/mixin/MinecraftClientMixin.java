package org.moon.moonfix.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
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
        int config = (int) Config.HIGHLIGHT.value;

        //return
        if (config == 0 || cir.getReturnValue() || !this.options.keySpectatorOutlines.isPressed() || entity.getType() != EntityType.PLAYER)
            return;

        GameMode gamemode = this.getNetworkHandler().getPlayerListEntry(this.player.getUuid()).getGameMode();
        if (config == 1 && gamemode == GameMode.CREATIVE || config == 2) {
            cir.setReturnValue(true);
        }
    }

    @Shadow @Nullable public abstract ClientPlayNetworkHandler getNetworkHandler();
}
