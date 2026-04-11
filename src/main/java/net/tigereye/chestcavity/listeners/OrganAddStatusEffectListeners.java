package net.tigereye.chestcavity.listeners;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.interfaces.CCStatusEffect;
import net.tigereye.chestcavity.interfaces.CCStatusEffectInstance;
import net.tigereye.chestcavity.registration.CCOrganScores;

public class OrganAddStatusEffectListeners {
    public OrganAddStatusEffectListeners() {
    }

    public static MobEffectInstance call(LivingEntity entity, ChestCavityInstance cc, MobEffectInstance instance) {
        instance = ApplyDetoxification(entity, cc, instance);
        instance = ApplyFiltration(entity, cc, instance);
        return instance;
    }


    private static MobEffectInstance ApplyDetoxification(LivingEntity entity, ChestCavityInstance cc, MobEffectInstance instance) {
        if (cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.DETOXIFICATION) > 0.0F && cc.getOrganScoreOrDefault(CCOrganScores.DETOXIFICATION, 1F) != cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.DETOXIFICATION)) {
            CCStatusEffect ccStatusEffect = (CCStatusEffect) instance.getEffect();
            if (ccStatusEffect.isHarmful() && instance.getEffect() != MobEffects.POISON) {
                CCStatusEffectInstance ccInstance = (CCStatusEffectInstance) instance;
                float detoxRatio = Math.max(0, cc.getOrganScore(CCOrganScores.DETOXIFICATION) / cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.DETOXIFICATION));
                ccInstance.CC_setDuration((int) Math.max(1.0F, (float) (instance.getDuration() * 2) / (1.0F + detoxRatio)));
            }

            return instance;
        } else {
            return instance;
        }
    }

    private static MobEffectInstance ApplyFiltration(LivingEntity entity, ChestCavityInstance cc, MobEffectInstance instance) {
        float filtrationDiff = cc.getOrganScore(CCOrganScores.FILTRATION) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.FILTRATION);
        if (filtrationDiff > 0.0F && instance.getEffect() == MobEffects.POISON) {
            CCStatusEffectInstance ccInstance = (CCStatusEffectInstance) instance;
            ccInstance.CC_setDuration((int) ((float) instance.getDuration() / (1.0F + ChestCavity.config.FILTRATION_DURATION_FACTOR * cc.getOrganScore(CCOrganScores.FILTRATION))));
        }
        return instance;
    }
}
