package net.tigereye.chestcavity.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCOrganScores;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class MixinCreeper extends Monster {
    @Shadow
    private int swell;

    protected MixinCreeper(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"tick"}
    )
    protected void chestCavityCreeperTickMixin(CallbackInfo info) {
        if (this.level().isClientSide) {
            return;
        }
        if (this.isAlive() && this.swell > 1) {
            ChestCavityEntity.of(this).ifPresent((cce) -> {
                if (cce.getChestCavityInstance().opened && cce.getChestCavityInstance().getOrganScore(CCOrganScores.LUCK) <= 0.0F) {
                    this.swell = 1;
                }
            });
        }
    }
}
