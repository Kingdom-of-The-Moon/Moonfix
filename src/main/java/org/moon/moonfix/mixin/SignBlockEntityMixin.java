package org.moon.moonfix.mixin;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.moon.moonfix.config.ConfigManager.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignBlockEntity.class)
public class SignBlockEntityMixin {

    @Shadow private boolean editable;

    //edit signs on right click with an empty hand while sneaking
    @Inject(at = @At("HEAD"), method = "onActivate", cancellable = true)
    public void onActivate(ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (!(boolean) Config.SIGN_EDIT.value || !player.isSneaking() || !player.getStackInHand(player.getActiveHand()).isEmpty())
            return;

        this.editable = true;
        SignBlockEntity sign = (SignBlockEntity) (Object) this;
        player.openEditSignScreen(sign);

        cir.setReturnValue(true);
    }
}
