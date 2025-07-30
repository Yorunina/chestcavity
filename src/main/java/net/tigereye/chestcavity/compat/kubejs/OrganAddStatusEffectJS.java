package net.tigereye.chestcavity.compat.kubejs;

import dev.latvian.mods.kubejs.level.LevelEventJS;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;

public class OrganAddStatusEffectJS extends LevelEventJS {

    private final Level level;
    private final ChestCavityInstance cc;
    private final LivingEntity entity;

    private MobEffectInstance effect;

    public OrganAddStatusEffectJS(ChestCavityInstance cc, LivingEntity entity, Level level, MobEffectInstance effect) {
        super();
        this.level = level;
        this.cc = cc;
        this.entity = entity;
        this.effect = effect;
    }


    @Override
    public Level getLevel() {
        return level;
    }

    public ChestCavityInstance getChestCavity() {
        return cc;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public MobEffectInstance getEffect() {
        return effect;
    }

    public void setEffect(MobEffectInstance effect) {
        this.effect = effect;
    }

}
