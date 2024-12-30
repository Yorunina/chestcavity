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

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % ChestCavity.config.RUMINATION_TIME == 1;
    }

    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player && !entity.level().isClientSide) {
            FoodData hungerManager = ((Player)entity).getFoodData();
            hungerManager.eat(CCItems.CUD.get(), new ItemStack(CCItems.CUD.get()), entity);
        }

    }
}
