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
        if (ChestCavityAssignmentManager.ChestCavityAssignments.containsKey(entityID)) {
            ResourceLocation chestCavityTypeID = ChestCavityAssignmentManager.ChestCavityAssignments.get(entityID);
            if (ChestCavityTypeManager.ChestCavityTypes.containsKey(chestCavityTypeID)) {
                return new ChestCavityInstance(ChestCavityTypeManager.ChestCavityTypes.get(chestCavityTypeID), owner);
            }
        }
        return new ChestCavityInstance(ChestCavityTypeManager.ChestCavityTypes.get(DEFAULT_CHEST_CAVITY_TYPE), owner);
    }
}