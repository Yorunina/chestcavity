package net.tigereye.chestcavity.compat.kubejs.events;

import dev.latvian.mods.kubejs.level.LevelEventJS;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;

/**
 * Server event fired after a living entity's initial chest cavity has been
 * created and before it is assigned to the entity.
 */
public class InitChestCavityJS extends LevelEventJS {

    private final Level level;
    private ChestCavityInstance chestCavity;
    private final LivingEntity entity;

    public InitChestCavityJS(ChestCavityInstance chestCavity, LivingEntity entity, Level level) {
        super();
        this.level = level;
        this.chestCavity = chestCavity;
        this.entity = entity;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    public ChestCavityInstance getChestCavity() {
        return chestCavity;
    }

    public void setChestCavity(ChestCavityInstance chestCavity) {
        this.chestCavity = chestCavity;
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
