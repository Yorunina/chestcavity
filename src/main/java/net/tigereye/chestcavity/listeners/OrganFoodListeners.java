package net.tigereye.chestcavity.listeners;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCItems;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCStatusEffects;
import net.tigereye.chestcavity.registration.CCTags;

public class OrganFoodListeners {
    public OrganFoodListeners() {
    }

    public static EffectiveFoodScores call(Item food, FoodProperties foodComponent, ChestCavityEntity cce, EffectiveFoodScores efs) {
        efs = applyHerbivorousCarnivorous(food, foodComponent, cce, efs);
        efs = applyRot(food, foodComponent, cce, efs);
        efs = applyFurnacePower(food, foodComponent, cce, efs);
        return efs;
    }

    private static EffectiveFoodScores applyHerbivorousCarnivorous(Item food, FoodProperties foodComponent, ChestCavityEntity cce, EffectiveFoodScores efs) {
        if (!foodComponent.isMeat() && !food.getDefaultInstance().is(CCTags.CARNIVORE_FOOD)) {
            efs.digestion += cce.getChestCavityInstance().getOrganScore(CCOrganScores.HERBIVOROUS_DIGESTION);
            efs.nutrition += cce.getChestCavityInstance().getOrganScore(CCOrganScores.HERBIVOROUS_NUTRITION);
        } else {
            efs.digestion += cce.getChestCavityInstance().getOrganScore(CCOrganScores.CARNIVOROUS_DIGESTION);
            efs.nutrition += cce.getChestCavityInstance().getOrganScore(CCOrganScores.CARNIVOROUS_NUTRITION);
        }

        return efs;
    }

    private static EffectiveFoodScores applyRot(Item food, FoodProperties foodComponent, ChestCavityEntity cce, EffectiveFoodScores efs) {
        if (food.getDefaultInstance().is(CCTags.ROTTEN_FOOD)) {
            efs.digestion += cce.getChestCavityInstance().getOrganScore(CCOrganScores.ROT_DIGESTION);
            efs.nutrition += cce.getChestCavityInstance().getOrganScore(CCOrganScores.ROTGUT);
        }

        return efs;
    }

    private static EffectiveFoodScores applyFurnacePower(Item food, FoodProperties foodComponent, ChestCavityEntity cce, EffectiveFoodScores efs) {
        if (food == CCItems.FURNACE_POWER.get()) {
            int power = 0;
            if (cce.getChestCavityInstance().owner.hasEffect((MobEffect)CCStatusEffects.FURNACE_POWER.get())) {
                power = cce.getChestCavityInstance().owner.getEffect((MobEffect)CCStatusEffects.FURNACE_POWER.get()).getAmplifier() + 1;
            }

            efs.digestion -= cce.getChestCavityInstance().getOrganScore(CCOrganScores.HERBIVOROUS_DIGESTION);
            efs.nutrition -= cce.getChestCavityInstance().getOrganScore(CCOrganScores.HERBIVOROUS_NUTRITION);
            efs.nutrition += (float)power;
        }

        return efs;
    }
}
