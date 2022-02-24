package org.moon.moonfix.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.SignType;
import org.moon.moonfix.config.ConfigManager.Config;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Function;

@Mixin(SignBlockEntityRenderer.class)
public class SignBlockEntityRendererMixin {

    @Shadow @Final private TextRenderer textRenderer;

    //remove text limit when rendering signs
    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/SignBlockEntity;updateSign(ZLjava/util/function/Function;)[Lnet/minecraft/text/OrderedText;"), method = "render(Lnet/minecraft/block/entity/SignBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V")
    private OrderedText[] updateSign(SignBlockEntity signBlockEntity, boolean filterText, Function<Text, OrderedText> textOrderingFunction) {
        if ((boolean) Config.SIGN_TEXT_RENDER.value)
            return signBlockEntity.updateSign(filterText, Text::asOrderedText);

        return signBlockEntity.updateSign(filterText, textOrderingFunction);
    }

    //sign scaling
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", shift = At.Shift.BEFORE), method = "render(Lnet/minecraft/block/entity/SignBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderModel(SignBlockEntity signBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo ci, BlockState blockState, float g, SignType signType, SignBlockEntityRenderer.SignModel signModel, SpriteIdentifier h, VertexConsumer vertexConsumer) {
        if (!(boolean) Config.SIGN_SCALE.value)
            return;

        //text len
        float max = 90;
        OrderedText[] texts = signBlockEntity.updateSign(MinecraftClient.getInstance().shouldFilterText(), Text::asOrderedText);

        for (OrderedText text: texts)
            max = Math.max(this.textRenderer.getWidth(text), max);

        max = (max + 6) / 96f;

        if (max <= 1.01f)
            return;

        //stick
        boolean wasStickVisible = signModel.stick.visible;
        signModel.stick.visible = false;

        //scale
        matrixStack.push();
        matrixStack.scale(max, 1.01f, 1.01f);

        //render
        signModel.root.render(matrixStack, vertexConsumer, i, j);
        matrixStack.pop();

        signModel.stick.visible = wasStickVisible;
    }
}
