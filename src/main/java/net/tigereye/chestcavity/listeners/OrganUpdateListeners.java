package net.tigereye.chestcavity.listeners;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.instance.OrganScoreState;
import net.tigereye.chestcavity.interfaces.CCFoodData;
import net.tigereye.chestcavity.registration.CCAttributes;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCStatusEffects;

import java.util.UUID;

public class OrganUpdateListeners {
    private static final UUID LUCK_ID = UUID.fromString("ac606ec3-4cc3-42b5-9399-7fa8ceba8722");
    private static final UUID HEALTH_ID = UUID.fromString("edb1e124-a951-48bd-b711-782ec1364722");
    private static final UUID STRENGTH_ID = UUID.fromString("bf560396-9855-496e-a942-99824467e1ad");
    private static final UUID SPEED_ID = UUID.fromString("979aa156-3f01-45d3-8784-56185eeef96d");
    private static final UUID ATTACK_SPEED_ID = UUID.fromString("709e3e77-0586-4304-80b5-d28bc477e947");
    private static final UUID MOVEMENT_ID = UUID.fromString("8f56feed-589f-416f-86c5-315765d41f57");
    private static final UUID KNOCKBACK_RESISTANCE_ID = UUID.fromString("673566d3-5daa-40d7-955f-cbabc27a84cf");

    private static final UUID DEFENSE_ID = UUID.fromString("3737d5eb-2a47-42e2-8e70-14552dd706b2");
    private static final UUID CLIMB_SPEED_ID = UUID.fromString("bca95213-7672-424c-b75b-c8ad88b22b28");

    public OrganUpdateListeners() {
    }

    public static void call(LivingEntity entity, ChestCavityInstance cc) {
        UpdateLuck(entity, cc);
        UpdateHealth(entity, cc);
        UpdateStrength(entity, cc);
        UpdateSpeed(entity, cc);
        UpdateNerves(entity, cc);
        UpdateKnockBackResistance(entity, cc);
        UpdateIncompatibility(entity, cc);
        UpdateDefense(entity, cc);
        UpdateClimbing(entity, cc);
        if (entity instanceof Player player) {
            ((CCFoodData)player.getFoodData()).updateCCInstance(cc);
        }
    }

    public static void UpdateClimbing(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.CLIMBING) > 0) return;
        OrganScoreState state = cc.getOrganScoreState();
        if (!state.hasScoreChanged(CCOrganScores.CLIMBING)) return;
        float climbingScore = state.getScore(CCOrganScores.CLIMBING, 0.0F);
        updateAttributeModifier(entity.getAttribute(CCAttributes.CLIMB_SPEED.get()), CLIMB_SPEED_ID, "ChestCavityClimbingSpeed", Math.max(climbingScore / 10.0F, 0), Operation.ADDITION);
    }

    public static void UpdateDefense(LivingEntity entity, ChestCavityInstance cc) {
        updateAttributeModifier(cc, CCOrganScores.DEFENSE, entity.getAttribute(Attributes.ARMOR), DEFENSE_ID, "ChestCavityDefenseArmor", ChestCavity.config.BONE_DEFENSE, Operation.ADDITION);
    }

    public static void UpdateLuck(LivingEntity entity, ChestCavityInstance cc) {
        updateAttributeModifier(cc, CCOrganScores.LUCK, entity.getAttribute(Attributes.LUCK), LUCK_ID, "ChestCavityLuck", ChestCavity.config.APPENDIX_LUCK, Operation.ADDITION);
    }

    public static void UpdateHealth(LivingEntity entity, ChestCavityInstance cc) {
        updateAttributeModifier(cc, CCOrganScores.HEALTH, entity.getAttribute(Attributes.MAX_HEALTH), HEALTH_ID, "ChestCavityHealth", ChestCavity.config.HEART_HP, Operation.ADDITION);
    }

    public static void UpdateStrength(LivingEntity entity, ChestCavityInstance cc) {
        updateAttributeModifier(cc, CCOrganScores.STRENGTH, entity.getAttribute(Attributes.ATTACK_DAMAGE), STRENGTH_ID, "ChestCavityMuscleAttackDamage", ChestCavity.config.MUSCLE_STRENGTH, Operation.ADDITION);
    }

    public static void UpdateSpeed(LivingEntity entity, ChestCavityInstance cc) {
        updateAttributeModifier(cc, CCOrganScores.SPEED, entity.getAttribute(Attributes.MOVEMENT_SPEED), SPEED_ID, "ChestCavityMovementSpeed", ChestCavity.config.MUSCLE_SPEED / 8.0F, Operation.MULTIPLY_BASE);
    }

    public static void UpdateNerves(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.NERVES) == 0.0F) return;
        OrganScoreState state = cc.getOrganScoreState();
        float nervesScore = state.getScore(CCOrganScores.NERVES, 0.0F);
        if (!state.hasScoreChanged(CCOrganScores.NERVES)) return;

        updateAttributeModifier(entity.getAttribute(Attributes.MOVEMENT_SPEED), MOVEMENT_ID, "ChestCavityMovement", nervesScore > 0.0F ? 0.0 : -1.0, Operation.MULTIPLY_TOTAL);
        updateAttributeModifier(entity.getAttribute(Attributes.ATTACK_SPEED), ATTACK_SPEED_ID, "ChestCavityAttackSpeed", getScoreDelta(cc, CCOrganScores.NERVES) * ChestCavity.config.NERVES_HASTE, Operation.MULTIPLY_BASE);
    }

    public static void UpdateKnockBackResistance(LivingEntity entity, ChestCavityInstance cc) {
        updateAttributeModifier(cc, CCOrganScores.KNOCKBACK_RESISTANT, entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE), KNOCKBACK_RESISTANCE_ID, "ChestCavityKnockbackResistance", 0.1, Operation.ADDITION);
    }

    public static void UpdateIncompatibility(LivingEntity entity, ChestCavityInstance cc) {
        if (!cc.getOrganScoreState().hasScoreChanged(CCOrganScores.INCOMPATIBILITY)) return;
        try {
            entity.removeEffect(CCStatusEffects.ORGAN_REJECTION.get());
        } catch (Exception ignored) {
        }
    }

    private static float getScoreDelta(ChestCavityInstance cc, ResourceLocation scoreId) {
        OrganScoreState state = cc.getOrganScoreState();
        return state.getScore(scoreId, 0.0F) - cc.getChestCavityType().getDefaultOrganScore(scoreId);
    }

    private static void updateAttributeModifier(ChestCavityInstance cc, ResourceLocation scoreId, AttributeInstance attribute, UUID modifierId, String modifierName, double factor, Operation operation) {
        OrganScoreState state = cc.getOrganScoreState();
        if (attribute == null || !state.hasScoreChanged(scoreId)) return;
        updateAttributeModifier(attribute, modifierId, modifierName, getScoreDelta(cc, scoreId) * factor, operation);
    }

    private static void updateAttributeModifier(AttributeInstance attribute, UUID modifierId, String modifierName, double amount, Operation operation) {
        if (attribute == null) return;
        ReplaceAttributeModifier(attribute, new AttributeModifier(modifierId, modifierName, amount, operation));
    }

    private static void ReplaceAttributeModifier(AttributeInstance att, AttributeModifier mod) {
        att.removeModifier(mod);
        att.addPermanentModifier(mod);
    }
}
