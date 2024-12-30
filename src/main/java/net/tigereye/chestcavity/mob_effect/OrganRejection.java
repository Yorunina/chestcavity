package net.tigereye.chestcavity.mob_effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.registration.CCDamageSources;

public class OrganRejection extends CCStatusEffect {
    public OrganRejection() {
        super(MobEffectCategory.NEUTRAL, 13172480);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration <= 1;
    }

    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide) {
            entity.hurt(CCDamageSources.of(entity.level(), CCDamageSources.ORGAN_REJECTION), (float)ChestCavity.config.ORGAN_REJECTION_DAMAGE);
        }

    }
}
