package net.tigereye.chestcavity.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin({Entity.class})
public abstract class MixinEntity {

    @Shadow
    public abstract int getAirSupply();
    @Shadow
    public abstract int getMaxAirSupply();


    @ModifyVariable(
            at = @At("HEAD"),
            ordinal = 0,
            method = {"checkFallDamage"},
            argsOnly = true
    )
    public double chestCavityEntityFallMixin(double finalHeightDifference, double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
        if (heightDifference < 0.0) {
            Optional<ChestCavityEntity> cce = ChestCavityEntity.of((LivingEntity)(Object)this);
            if (cce.isPresent()) {
                finalHeightDifference = ChestCavityUtil.applyOrgansToFallDistance((LivingEntity)(Object)this,cce.get().getChestCavityInstance(),heightDifference);
            }
        }

        return finalHeightDifference;
    }
}
