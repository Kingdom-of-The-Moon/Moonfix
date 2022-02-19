package org.moon.moonfix.mixin;

import net.minecraft.resource.ResourcePackProfile;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourcePackProfile.class)
public class ResourcePackProfileMixin {

    @Shadow @Final private String name;

    @Inject(at = @At("HEAD"), method = "isPinned", cancellable = true)
    private void isPinned(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.name.equals("vanilla"));
    }

    @Inject(at = @At("HEAD"), method = "isAlwaysEnabled", cancellable = true)
    private void isAlwaysEnabled(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.name.equals("vanilla"));
    }
}
