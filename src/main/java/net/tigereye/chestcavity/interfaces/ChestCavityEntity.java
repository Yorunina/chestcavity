package net.tigereye.chestcavity.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;

import java.util.Optional;

public interface ChestCavityEntity {
    static Optional<ChestCavityEntity> of(Entity entity) {
        return entity instanceof ChestCavityEntity ? Optional.of((ChestCavityEntity) entity) : Optional.empty();
    }


    @OnlyIn(Dist.CLIENT)
    static Optional<ChestCavityEntity> of() {
        return ChestCavityEntity.of(Minecraft.getInstance().player);
    }

    ChestCavityInstance getChestCavityInstance();

    void setChestCavityInstance(ChestCavityInstance var1);

    InventoryTypeData getInventoryTypeData();

    void setInventoryTypeData(ResourceLocation id);
}
