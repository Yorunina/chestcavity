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

    public boolean m_6584_(int duration, int amplifier) {
        return true;
    }

    public void m_6742_(LivingEntity entity, int amplifier) {
        if (entity instanceof Player && !entity.level().f_46443_) {
            Optional<ChestCavityEntity> optional = ChestCavityEntity.of(entity);
            if (optional.isPresent()) {
                ChestCavityEntity cce = (ChestCavityEntity)optional.get();
                ChestCavityInstance cc = cce.getChestCavityInstance();
                ++cc.furnaceProgress;
                if (cc.furnaceProgress >= 200) {
                    cc.furnaceProgress = 0;
                    FoodData hungerManager = ((Player)entity).m_36324_();
                    ItemStack furnaceFuel = new ItemStack((ItemLike)CCItems.FURNACE_POWER.get());

                    for(int i = 0; i <= amplifier; ++i) {
                        hungerManager.eat((Item)CCItems.FURNACE_POWER.get(), furnaceFuel, entity);
                    }
                }
            }
        }

    }
}
