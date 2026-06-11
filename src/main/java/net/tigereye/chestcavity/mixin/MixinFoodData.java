package net.tigereye.chestcavity.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.interfaces.CCFoodData;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class MixinFoodData implements CCFoodData {
    @Shadow
    private int tickTimer;
    @Unique
    public ChestCavityInstance ccIns = null;
    @Shadow
    private int foodLevel;
    @Shadow
    private float saturationLevel;

    public MixinFoodData() {}

    @Inject(
            at = {@At("HEAD")},
            method = {"tick"}
    )
    public void chestCavityUpdateMixin(Player player, CallbackInfo info) {
        if (this.ccIns == null) {
            ChestCavityEntity.of(player).ifPresent((ccPlayerEntityInterface) -> {
                this.ccIns = ccPlayerEntityInterface.getChestCavityInstance();
            });
        }

        if (this.ccIns != null) {
            this.tickTimer = ChestCavityUtil.applySpleenMetabolism(this.ccIns, this.tickTimer);
        }
    }

    @Inject(method = "eat(IF)V", at = @At("HEAD"), cancellable = true)
    public void chestCavityEatMixin(int pFoodLevelModifier, float pSaturationLevelModifier, CallbackInfo ci) {
        if (this.ccIns != null) {
            float saturationGain = ChestCavityUtil.applyNutrition(this.ccIns, pFoodLevelModifier, pSaturationLevelModifier);
            int hungerGain = ChestCavityUtil.applyDigestion(this.ccIns, pFoodLevelModifier, pSaturationLevelModifier);
            this.foodLevel = Math.min(hungerGain + this.foodLevel, 20);
            this.saturationLevel = Math.min(this.saturationLevel + (float) hungerGain * saturationGain, (float) this.foodLevel);
            ci.cancel();
        }
    }

    @ModifyVariable(
            at = @At("HEAD"),
            ordinal = 0,
            method = {"addExhaustion"},
            argsOnly = true
    )
    public float chestCavityAddExhaustionMixin(float exhaustion) {
        if (this.ccIns != null) {
            float enduranceDiff = this.ccIns.getOrganScore(CCOrganScores.ENDURANCE) - this.ccIns.getChestCavityType().getDefaultOrganScore(CCOrganScores.ENDURANCE);
            float out;
            if (enduranceDiff > 0.0F) {
                out = exhaustion / (1.0F + enduranceDiff / 2.0F);
            } else {
                out = exhaustion * (1.0F - enduranceDiff / 2.0F);
            }
            return out;
        } else {
            return exhaustion;
        }
    }

    public void updateCCInstance(ChestCavityInstance ccIns) {
        this.ccIns = ccIns;
        ChestCavity.LOGGER.info("CCFoodData updateCCInstance");
    }
}