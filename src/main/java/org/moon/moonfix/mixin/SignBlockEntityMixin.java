package org.moon.moonfix.mixin;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

    //edit signs on right click
    @Inject(at = @At("HEAD"), method = "onActivate", cancellable = true)
    public void onActivate(ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = player.getStackInHand(player.getActiveHand());

        if (!(boolean) Config.SIGN_EDIT.value || itemStack.getItem() instanceof DyeItem || itemStack.isOf(Items.GLOW_INK_SAC) || itemStack.isOf(Items.INK_SAC))
            return;

        this.editable = true;
        SignBlockEntity sign = (SignBlockEntity) (Object) this;
        player.openEditSignScreen(sign);

        cir.cancel();
    }
}
