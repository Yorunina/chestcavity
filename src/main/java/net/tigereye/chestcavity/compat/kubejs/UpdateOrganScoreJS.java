package net.tigereye.chestcavity.compat.kubejs;

import dev.latvian.mods.kubejs.level.LevelEventJS;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;

public class UpdateOrganScoreJS extends LevelEventJS {

    private final Level level;
    private final ChestCavityInstance cc;
    private final LivingEntity entity;


    public UpdateOrganScoreJS(ChestCavityInstance cc, LivingEntity entity, Level level) {
        super();
        this.level = level;
        this.cc = cc;
        this.entity = entity;
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

}
