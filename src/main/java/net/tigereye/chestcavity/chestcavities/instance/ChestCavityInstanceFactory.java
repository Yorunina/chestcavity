package net.tigereye.chestcavity.chestcavities.instance;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentManager;
import net.tigereye.chestcavity.chestcavities.json.ccType.ChestCavityTypeManager;

public class ChestCavityInstanceFactory {
    private static final ResourceLocation DEFAULT_CHEST_CAVITY_TYPE = new ResourceLocation("chestcavity:cc_types/default.json");

    public static ChestCavityInstance newChestCavityInstance(EntityType<? extends LivingEntity> entityType, LivingEntity owner) {
        ResourceLocation entityID = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (ChestCavityAssignmentManager.GeneratedChestCavityAssignments.containsKey(entityID)) {
            ResourceLocation chestCavityTypeID = ChestCavityAssignmentManager.GeneratedChestCavityAssignments.get(entityID);
            if (ChestCavityTypeManager.GeneratedChestCavityTypes.containsKey(chestCavityTypeID)) {
                return new ChestCavityInstance(ChestCavityTypeManager.GeneratedChestCavityTypes.get(chestCavityTypeID), owner);
            }
        }
        return new ChestCavityInstance(ChestCavityTypeManager.GeneratedChestCavityTypes.get(DEFAULT_CHEST_CAVITY_TYPE), owner);
    }
}