package net.tigereye.chestcavity.service;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.compat.kubejs.CCEvents;
import net.tigereye.chestcavity.listeners.OrganAddStatusEffectListeners;
import net.tigereye.chestcavity.registration.CCOrganScores;

/**
 * Applies organ-derived gameplay effects. This class owns effect formulas and
 * does not evaluate inventory contents or perform network synchronization.
 */
public final class OrganEffectService {
    private OrganEffectService() {
    }

    public static int applyBreathInWater(ChestCavityInstance cc, int oldAir, int newAir) {
        if (!cc.opened) {
            return newAir;
        }
        var ccType = cc.getChestCavityType();
        if (ccType.getDefaultOrganScore(CCOrganScores.BREATH_CAPACITY) <= 0
                && ccType.getDefaultOrganScore(CCOrganScores.WATERBREATH) <= 0) {
            return newAir;
        }
        if (ccType.getDefaultOrganScore(CCOrganScores.BREATH_CAPACITY) == cc.getOrganScore(CCOrganScores.BREATH_CAPACITY)
                && ccType.getDefaultOrganScore(CCOrganScores.WATERBREATH) == cc.getOrganScore(CCOrganScores.WATERBREATH)) {
            return newAir;
        }

        float airLoss = 1;
        float waterBreath = cc.getOrganScore(CCOrganScores.WATERBREATH);
        if (cc.owner.isSprinting()) {
            waterBreath /= 4;
        }
        if (waterBreath > 0) {
            airLoss += (-2 * waterBreath);
        }

        if (airLoss > 0) {
            if (oldAir == newAir) {
                airLoss = 0;
            } else {
                float capacity = cc.getOrganScore(CCOrganScores.BREATH_CAPACITY);
                airLoss *= (oldAir - newAir);
                if (airLoss > 0) {
                    float lungRatio = 5f;
                    if (capacity > 0.2f) {
                        lungRatio = Math.min(1 / capacity, 5f);
                    }
                    airLoss = (airLoss * lungRatio) + cc.lungRemainder;
                }
            }
        }

        cc.lungRemainder = airLoss % 1;
        int airResult = Math.min(oldAir - ((int) airLoss), cc.owner.getMaxAirSupply());
        if (airResult <= -20) {
            airResult = 0;
            cc.lungRemainder = 0;
            cc.owner.hurt(cc.owner.damageSources().drown(), 2.0F);
        }
        return airResult;
    }

    public static int applyBreathOnLand(ChestCavityInstance cc, int oldAir, int airGain) {
        if (!cc.opened) {
            return oldAir;
        }
        var ccType = cc.getChestCavityType();
        if (ccType.getDefaultOrganScore(CCOrganScores.BREATH_CAPACITY) <= 0
                && ccType.getDefaultOrganScore(CCOrganScores.WATERBREATH) <= 0
                && ccType.getDefaultOrganScore(CCOrganScores.BREATH_RECOVERY) <= 0) {
            return oldAir;
        }
        if (ccType.getDefaultOrganScore(CCOrganScores.BREATH_CAPACITY) == cc.getOrganScore(CCOrganScores.BREATH_CAPACITY)
                && ccType.getDefaultOrganScore(CCOrganScores.WATERBREATH) == cc.getOrganScore(CCOrganScores.WATERBREATH)
                && ccType.getDefaultOrganScore(CCOrganScores.BREATH_RECOVERY) == cc.getOrganScore(CCOrganScores.BREATH_RECOVERY)) {
            return oldAir;
        }

        float airLoss = cc.owner.hasEffect(MobEffects.WATER_BREATHING)
                || cc.owner.hasEffect(MobEffects.CONDUIT_POWER) ? 0 : 1;

        float breath = cc.getOrganScore(CCOrganScores.BREATH_RECOVERY);
        if (cc.owner.isSprinting()) {
            breath /= 4;
        }
        if (cc.owner.isInWaterOrRain()) {
            breath += cc.getOrganScore(CCOrganScores.WATERBREATH) / 4;
        }
        if (breath > 0) {
            airLoss += (-airGain * breath / 2);
        }

        if (airLoss > 0) {
            int respiration = EnchantmentHelper.getRespiration(cc.owner);
            if (cc.owner.getRandom().nextInt(respiration + 1) != 0) {
                airLoss = 0;
            } else {
                float capacity = cc.getOrganScore(CCOrganScores.BREATH_CAPACITY);
                float breathRatio = 5f;
                if (capacity > 0.2f) {
                    breathRatio = Math.min(1 / capacity, 5f);
                }
                airLoss = (airLoss * breathRatio) + cc.lungRemainder;
            }
        } else if (oldAir == cc.owner.getMaxAirSupply()) {
            return oldAir;
        }

        cc.lungRemainder = airLoss % 1;
        int airResult = Math.min(oldAir - ((int) airLoss) - airGain, cc.owner.getMaxAirSupply());
        if (airResult <= -20) {
            airResult = 0;
            cc.lungRemainder = 0;
            cc.owner.hurt(cc.owner.damageSources().drown(), 2.0F);
        }
        return airResult;
    }

    public static float applyDefenses(ChestCavityInstance cc, DamageSource source, float damage) {
        if (!cc.opened) {
            return damage;
        }
        if (source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL)) {
            damage = applyImpactResistant(cc, damage);
        }
        if (source.is(DamageTypeTags.IS_FIRE)) {
            damage = applyFireResistant(cc, damage);
        }
        return damage;
    }

    public static float applyFireResistant(ChestCavityInstance cc, float damage) {
        float fireproof = cc.getOrganScore(CCOrganScores.FIRE_RESISTANT);
        return fireproof > 0.0F
                ? (float) ((double) damage * Math.pow(1.0F - ChestCavity.config.FIREPROOF_DEFENSE, fireproof / 4.0F))
                : damage;
    }

    public static float applyImpactResistant(ChestCavityInstance cc, float damage) {
        float impactResistant = cc.getOrganScore(CCOrganScores.IMPACT_RESISTANT);
        return impactResistant > 0.0F
                ? (float) ((double) damage * Math.pow(1.0F - ChestCavity.config.IMPACT_DEFENSE, impactResistant / 4.0F))
                : damage;
    }

    public static int applyDigestion(ChestCavityInstance cc, int hunger, float saturation) {
        float defaultDigestion = cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.DIGESTION);
        float digestionDiff = cc.getOrganScore(CCOrganScores.DIGESTION) - defaultDigestion;
        if (digestionDiff == 0) {
            return hunger;
        } else if (digestionDiff < 0) {
            return Math.abs(digestionDiff) < hunger ? 1 : 0;
        } else {
            return Math.max((int) (hunger * (1 + digestionDiff / 4)), 1);
        }
    }

    public static float applyNutrition(ChestCavityInstance cc, int hunger, float saturation) {
        float defaultNutrition = cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.NUTRITION);
        float nutritionDiff = cc.getOrganScore(CCOrganScores.NUTRITION) - defaultNutrition;
        if (nutritionDiff == 0) {
            return saturation;
        } else if (nutritionDiff < 0) {
            return saturation * Math.max(1 + nutritionDiff / 2, 0.1F);
        } else {
            return saturation * (1 + nutritionDiff / 4);
        }
    }

    public static float applyNervesToMining(ChestCavityInstance cc, float miningProgress) {
        float defaultNerves = cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.NERVES);
        if (defaultNerves == 0.0F) {
            return miningProgress;
        }
        float nervesDiff = cc.getOrganScore(CCOrganScores.NERVES) - defaultNerves;
        return miningProgress * (1.0F + ChestCavity.config.NERVES_HASTE * nervesDiff);
    }

    public static int applySpleenMetabolism(ChestCavityInstance cc, int foodStarvationTimer) {
        if (!cc.opened) {
            return foodStarvationTimer;
        }

        float defaultMetabolism = cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.METABOLISM);
        float metabolismDiff = cc.getOrganScoreOrDefault(CCOrganScores.METABOLISM, defaultMetabolism) - defaultMetabolism;
        if (metabolismDiff != 0.0F) {
            if (metabolismDiff > 0.0F) {
                cc.metabolismRemainder += metabolismDiff;
                foodStarvationTimer += (int) cc.metabolismRemainder;
            } else {
                cc.metabolismRemainder += 1.0F - 1.0F / (-metabolismDiff + 1.0F);
                foodStarvationTimer -= (int) cc.metabolismRemainder;
            }
            cc.metabolismRemainder %= 1.0F;
        }
        return foodStarvationTimer;
    }

    public static float applySwimSpeedInWater(ChestCavityInstance cc) {
        if (cc.opened && cc.owner.isInWaterOrRain()) {
            float speedDiff = cc.getOrganScore(CCOrganScores.SWIM_SPEED)
                    - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.SWIM_SPEED);
            return speedDiff == 0.0F
                    ? 1.0F
                    : Math.max(0.0F, 1.0F + speedDiff * ChestCavity.config.SWIMSPEED_FACTOR / 8.0F);
        }
        return 1.0F;
    }

    public static MobEffectInstance onAddStatusEffect(ChestCavityInstance cc, MobEffectInstance effect) {
        if (!cc.opened) {
            return effect;
        }

        effect = OrganAddStatusEffectListeners.call(cc.owner, cc, effect);

        if (cc.owner != null && !cc.owner.level().isClientSide()) {
            return CCEvents.postOpenedEntityAddStatus(cc, effect);
        }

        return effect;
    }
}
