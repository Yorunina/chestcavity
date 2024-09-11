package net.tigereye.chestcavity.mob_effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.registration.CCItems;

public class Ruminating extends CCStatusEffect {
    public Ruminating() {
        super(MobEffectCategory.BENEFICIAL, 13172480);
    }

    public boolean m_6584_(int duration, int amplifier) {
        return duration % ChestCavity.config.RUMINATION_TIME == 1;
    }

    public void m_6742_(LivingEntity entity, int amplifier) {
        if (entity instanceof Player && !entity.level().f_46443_) {
            FoodData hungerManager = ((Player)entity).m_36324_();
            hungerManager.eat((Item)CCItems.CUD.get(), new ItemStack((ItemLike)CCItems.CUD.get()), entity);
        }

    }
}
