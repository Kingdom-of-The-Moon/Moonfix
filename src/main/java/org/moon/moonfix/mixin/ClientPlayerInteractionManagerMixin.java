package org.moon.moonfix.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.moon.moonfix.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    //makes log strips and grass path actions be ignored unless if player is pressing sneak
    @Inject(at = @At("HEAD"), method = "interactBlock", cancellable = true)
    private void interactBlock(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        Item item = player.getStackInHand(hand).getItem();
        BlockState block = world.getBlockState(hitResult.getBlockPos());

        boolean path = (boolean) ConfigManager.Config.DIRT_PATH.value && item instanceof ShovelItem && ShovelItemAccessor.getPathStates().containsKey(block.getBlock());
        boolean stripped = (boolean) ConfigManager.Config.STRIPPED_LOGS.value && item instanceof AxeItem && AxeItemAccessor.getStrippedBlocks().containsKey(block.getBlock());

        if (!player.isSneaking() && (path || stripped)) {
            cir.setReturnValue(ActionResult.PASS);
            cir.cancel();
        }
    }
}
