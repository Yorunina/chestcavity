package net.tigereye.chestcavity.mob_effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class OrganRejection extends CCStatusEffect {
    public OrganRejection() {
        super(MobEffectCategory.NEUTRAL, 13172480);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration <= 1;
    }

    public void applyEffectTick(LivingEntity entity, int amplifier) {
    }
}
