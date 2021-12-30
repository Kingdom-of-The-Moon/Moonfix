package org.moon.moonfix.mixin;

import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.moon.moonfix.config.ConfigManager.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(SignBlockEntityRenderer.class)
public class SignBlockEntityRendererMixin {

    //remove text limit when rendering signs
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/SignBlockEntity;updateSign(ZLjava/util/function/Function;)[Lnet/minecraft/text/OrderedText;"), method = "render(Lnet/minecraft/block/entity/SignBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V")
    private OrderedText[] updateSign(SignBlockEntity signBlockEntity, boolean filterText, Function<Text, OrderedText> textOrderingFunction) {
        if ((boolean) Config.SIGN_TEXT_RENDER.value)
            return signBlockEntity.updateSign(filterText, Text::asOrderedText);

        return signBlockEntity.updateSign(filterText, textOrderingFunction);
    }
}
