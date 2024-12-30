package net.tigereye.chestcavity.mixin;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCOrganScores;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class})
public class MixinEntity {
    public MixinEntity() {
    }

    @ModifyVariable(
            at = @At("HEAD"),
            ordinal = 0,
            method = {"checkFallDamage"},
            argsOnly = true
    )
    public double chestCavityEntityFallMixin(double finalHeightDifference, double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
        if (heightDifference < 0.0) {
            Optional<ChestCavityEntity> cce = ChestCavityEntity.of((Entity)(Object)this);
            if (cce.isPresent()) {
                finalHeightDifference = heightDifference * (double)(1.0F - ((ChestCavityEntity)cce.get()).getChestCavityInstance().getOrganScore(CCOrganScores.BUOYANT) / 3.0F);
            }
        }

        return finalHeightDifference;
    }
}
