package net.tigereye.chestcavity.interfaces;

import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;

public interface ChestCavityEntity {
    static Optional<ChestCavityEntity> of(Entity entity) {
        return entity instanceof ChestCavityEntity ? Optional.of((ChestCavityEntity)entity) : Optional.empty();
    }

    ChestCavityInstance getChestCavityInstance();

    void setChestCavityInstance(ChestCavityInstance var1);

    int getAdditionalSlot();
}
