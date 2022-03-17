package org.moon.moonfix.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.world.World;
import org.moon.moonfix.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RabbitEntity.class)
public abstract class RabbitEntityMixin extends AnimalEntity {

    protected RabbitEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    //rabbit fall damage immunity
    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return (boolean) ConfigManager.Config.RABBIT_FALL_DAMAGE.value && super.handleFallDamage(fallDistance, damageMultiplier, damageSource);
    }
}
