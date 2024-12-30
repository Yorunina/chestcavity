package net.tigereye.chestcavity.mob_effect;

import java.util.Optional;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCItems;

public class FurnacePower extends CCStatusEffect {
    public FurnacePower() {
        super(MobEffectCategory.BENEFICIAL, 13172480);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player && !entity.level().isClientSide) {
            Optional<ChestCavityEntity> optional = ChestCavityEntity.of(entity);
            if (optional.isPresent()) {
                ChestCavityEntity cce = optional.get();
                ChestCavityInstance cc = cce.getChestCavityInstance();
                ++cc.furnaceProgress;
                if (cc.furnaceProgress >= 200) {
                    cc.furnaceProgress = 0;
                    FoodData hungerManager = ((Player)entity).getFoodData();
                    ItemStack furnaceFuel = new ItemStack(CCItems.FURNACE_POWER.get());

                    for(int i = 0; i <= amplifier; ++i) {
                        hungerManager.eat(CCItems.FURNACE_POWER.get(), furnaceFuel, entity);
                    }
                }
            }
        }

    }
}
