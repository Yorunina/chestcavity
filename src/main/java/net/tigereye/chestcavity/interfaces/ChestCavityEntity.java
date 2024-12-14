package net.tigereye.chestcavity.interfaces;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;

import java.util.Optional;

public interface ChestCavityEntity {
    static Optional<ChestCavityEntity> of(Entity entity) {
        return entity instanceof ChestCavityEntity ? Optional.of((ChestCavityEntity)entity) : Optional.empty();
    }

    ChestCavityInstance getChestCavityInstance();

    void setChestCavityInstance(ChestCavityInstance var1);

    InventoryTypeData getInventoryTypeData();
    void setInventoryTypeData(ResourceLocation id);
}
