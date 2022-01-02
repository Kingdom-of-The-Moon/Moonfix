package org.moon.moonfix.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import org.moon.moonfix.accessors.EntityAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements EntityAccessor {

    @Unique private boolean hasSoulFire = false;

    @Override @Unique
    public boolean hasSoulFire() {
        return hasSoulFire;
    }

    @Inject(at = @At("HEAD"), method = "checkBlockCollision")
    private void checkBlockCollision(CallbackInfo ci) {
        this.hasSoulFire = false;
    }

    @Inject(at = @At("HEAD"), method = "onBlockCollision")
    private void onBlockCollision(BlockState state, CallbackInfo ci) {
        this.hasSoulFire = hasSoulFire || state.isOf(Blocks.SOUL_FIRE) || state.isOf(Blocks.SOUL_CAMPFIRE);
    }
}
