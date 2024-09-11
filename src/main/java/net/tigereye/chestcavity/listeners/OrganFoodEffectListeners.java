package net.tigereye.chestcavity.listeners;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCTags;

public class OrganFoodEffectListeners {
    public OrganFoodEffectListeners() {
    }

    public static List<Pair<MobEffectInstance, Float>> call(List<Pair<MobEffectInstance, Float>> list, ItemStack itemStack, Level world, LivingEntity entity, ChestCavityInstance cc) {
        return applyRotgut(list, itemStack, world, entity, cc);
    }

    private static List<Pair<MobEffectInstance, Float>> applyRotgut(List<Pair<MobEffectInstance, Float>> list, ItemStack itemStack, Level world, LivingEntity entity, ChestCavityInstance cc) {
        float rotten = cc.getOrganScore(CCOrganScores.ROTGUT) + cc.getOrganScore(CCOrganScores.ROT_DIGESTION);
        if (rotten > 0.0F && itemStack.m_204117_(CCTags.ROTTEN_FOOD)) {
            list.removeIf((pair) -> {
                return ((MobEffectInstance)pair.getFirst()).m_19544_() == MobEffects.f_19612_;
            });
        }

        return list;
    }
}
