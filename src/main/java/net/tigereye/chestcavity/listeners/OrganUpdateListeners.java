package net.tigereye.chestcavity.listeners;

import java.util.UUID;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCStatusEffects;

public class OrganUpdateListeners {
    private static final UUID APPENDIX_ID = UUID.fromString("ac606ec3-4cc3-42b5-9399-7fa8ceba8722");
    private static final UUID HEART_ID = UUID.fromString("edb1e124-a951-48bd-b711-782ec1364722");
    private static final UUID MUSCLE_STRENGTH_ID = UUID.fromString("bf560396-9855-496e-a942-99824467e1ad");
    private static final UUID MUSCLE_SPEED_ID = UUID.fromString("979aa156-3f01-45d3-8784-56185eeef96d");
    private static final UUID SPINE_ATTACK_SPEED_ID = UUID.fromString("709e3e77-0586-4304-80b5-d28bc477e947");
    private static final UUID SPINE_MOVEMENT_ID = UUID.fromString("8f56feed-589f-416f-86c5-315765d41f57");
    private static final UUID KNOCKBACK_RESISTANCE_ID = UUID.fromString("673566d3-5daa-40d7-955f-cbabc27a84cf");

    public OrganUpdateListeners() {
    }

    public static void call(LivingEntity entity, ChestCavityInstance cc) {
        UpdateAppendix(entity, cc);
        UpdateHeart(entity, cc);
        UpdateStrength(entity, cc);
        UpdateSpeed(entity, cc);
        UpdateSpine(entity, cc);
        UpdateKnockbackResistance(entity, cc);
        UpdateIncompatibility(entity, cc);
    }

    public static void UpdateAppendix(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getOldOrganScore(CCOrganScores.LUCK) != cc.getOrganScore(CCOrganScores.LUCK)) {
            AttributeInstance att = entity.getAttribute(Attributes.LUCK);
            if (att != null) {
                AttributeModifier mod = new AttributeModifier(APPENDIX_ID, "ChestCavityAppendixLuck", (double)((cc.getOrganScore(CCOrganScores.LUCK) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.LUCK)) * ChestCavity.config.APPENDIX_LUCK), Operation.ADDITION);
                ReplaceAttributeModifier(att, mod);
            }
        }

    }

    public static void UpdateHeart(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getOldOrganScore(CCOrganScores.HEALTH) != cc.getOrganScore(CCOrganScores.HEALTH)) {
            AttributeInstance att = entity.getAttribute(Attributes.MAX_HEALTH);
            if (att != null) {
                AttributeModifier mod = new AttributeModifier(HEART_ID, "ChestCavityHeartMaxHP", (double)((cc.getOrganScore(CCOrganScores.HEALTH) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.HEALTH)) * ChestCavity.config.HEART_HP), Operation.ADDITION);
                ReplaceAttributeModifier(att, mod);
            }
        }

    }

    public static void UpdateStrength(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getOldOrganScore(CCOrganScores.STRENGTH) != cc.getOrganScore(CCOrganScores.STRENGTH)) {
            AttributeInstance att = entity.getAttribute(Attributes.ATTACK_DAMAGE);
            if (att != null) {
                AttributeModifier mod = new AttributeModifier(MUSCLE_STRENGTH_ID, "ChestCavityMuscleAttackDamage", (double)((cc.getOrganScore(CCOrganScores.STRENGTH) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.STRENGTH)) * ChestCavity.config.MUSCLE_STRENGTH / 8.0F), Operation.MULTIPLY_BASE);
                ReplaceAttributeModifier(att, mod);
            }
        }

    }

    public static void UpdateSpeed(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getOldOrganScore(CCOrganScores.SPEED) != cc.getOrganScore(CCOrganScores.SPEED)) {
            AttributeInstance att = entity.getAttribute(Attributes.MOVEMENT_SPEED);
            if (att != null) {
                AttributeModifier mod = new AttributeModifier(MUSCLE_SPEED_ID, "ChestCavityMovementSpeed", (double)((cc.getOrganScore(CCOrganScores.SPEED) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.SPEED)) * ChestCavity.config.MUSCLE_SPEED / 8.0F), Operation.MULTIPLY_BASE);
                ReplaceAttributeModifier(att, mod);
            }
        }

    }

    public static void UpdateSpine(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getOldOrganScore(CCOrganScores.NERVES) != cc.getOrganScore(CCOrganScores.NERVES) && cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.NERVES) != 0.0F) {
            AttributeInstance att = entity.getAttribute(Attributes.MOVEMENT_SPEED);
            AttributeModifier mod;
            if (att != null) {
                mod = new AttributeModifier(SPINE_MOVEMENT_ID, "ChestCavitySpineMovement", cc.getOrganScore(CCOrganScores.NERVES) > 0.0F ? 0.0 : -1.0, Operation.MULTIPLY_TOTAL);
                ReplaceAttributeModifier(att, mod);
            }

            att = entity.getAttribute(Attributes.ATTACK_SPEED);
            if (att != null) {
                mod = new AttributeModifier(SPINE_ATTACK_SPEED_ID, "ChestCavitySpineAttackSpeed", (double)((cc.getOrganScore(CCOrganScores.NERVES) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.NERVES)) * ChestCavity.config.NERVES_HASTE), Operation.MULTIPLY_BASE);
                ReplaceAttributeModifier(att, mod);
            }
        }

    }

    public static void UpdateKnockbackResistance(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getOldOrganScore(CCOrganScores.KNOCKBACK_RESISTANT) != cc.getOrganScore(CCOrganScores.KNOCKBACK_RESISTANT)) {
            AttributeInstance att = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
            if (att != null) {
                AttributeModifier mod = new AttributeModifier(KNOCKBACK_RESISTANCE_ID, "ChestCavityKnockbackResistance", (double)(cc.getOrganScore(CCOrganScores.KNOCKBACK_RESISTANT) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.KNOCKBACK_RESISTANT)) * 0.1, Operation.ADDITION);
                ReplaceAttributeModifier(att, mod);
            }
        }

    }

    public static void UpdateIncompatibility(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getOldOrganScore(CCOrganScores.INCOMPATIBILITY) != cc.getOrganScore(CCOrganScores.INCOMPATIBILITY)) {
            try {
                entity.removeEffect((MobEffect)CCStatusEffects.ORGAN_REJECTION.get());
            } catch (Exception var3) {
            }
        }

    }

    private static void ReplaceAttributeModifier(AttributeInstance att, AttributeModifier mod) {
        att.removeModifier(mod);
        att.addPermanentModifier(mod);
    }
}
