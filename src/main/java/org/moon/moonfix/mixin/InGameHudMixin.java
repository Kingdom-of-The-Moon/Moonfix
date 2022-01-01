package org.moon.moonfix.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.Perspective;
import org.moon.moonfix.config.ConfigManager.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Redirect(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/Perspective;isFirstPerson()Z"))
    private boolean renderCrosshair(Perspective instance) {
        return (boolean) Config.THIRD_PERSON_CROSSHAIR.value || instance.isFirstPerson();
    }
}
