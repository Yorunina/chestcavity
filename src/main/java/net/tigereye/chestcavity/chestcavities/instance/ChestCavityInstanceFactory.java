package net.tigereye.chestcavity.chestcavities.instance;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.ChestCavityDataRepository;
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentManager;
import net.tigereye.chestcavity.chestcavities.json.ccType.ChestCavityTypeManager;

public class ChestCavityInstanceFactory {
    private static final ResourceLocation DEFAULT_CHEST_CAVITY_TYPE =
            new ResourceLocation(ChestCavity.MODID, "cc_types/default");

    public static ChestCavityInstance newChestCavityInstance(EntityType<? extends LivingEntity> entityType, LivingEntity owner) {
        ResourceLocation entityID = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        ResourceLocation chestCavityTypeID =
                ChestCavityDataRepository.getCurrent().getAssignment(entityID);
        if (chestCavityTypeID == null) {
            chestCavityTypeID = DEFAULT_CHEST_CAVITY_TYPE;
        }

        var chestCavityType = ChestCavityDataRepository.getCurrent().getChestCavityType(chestCavityTypeID);
        if (chestCavityType == null) {
            chestCavityType = ChestCavityDataRepository.getCurrent().getChestCavityType(DEFAULT_CHEST_CAVITY_TYPE);
        }
        if (chestCavityType == null) {
            throw new IllegalStateException("Missing default chest cavity type: " + DEFAULT_CHEST_CAVITY_TYPE);
        }
        return new ChestCavityInstance(chestCavityType, owner);
    }
}
