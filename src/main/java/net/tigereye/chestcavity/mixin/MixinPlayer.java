package net.tigereye.chestcavity.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
    protected MixinPlayer(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"getDestroySpeed"},
            cancellable = true
    )
    void chestCavityPlayerEntityGetBlockBreakingSpeedMixin(BlockState block, CallbackInfoReturnable<Float> cir) {
        if (this.level().isClientSide) return;
        cir.setReturnValue(ChestCavityUtil.applyNervesToMining(((ChestCavityEntity) this).getChestCavityInstance(), cir.getReturnValue()));
    }
}