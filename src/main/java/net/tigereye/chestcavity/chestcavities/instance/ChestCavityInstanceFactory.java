package net.tigereye.chestcavity.chestcavities.instance;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentManager;
import net.tigereye.chestcavity.chestcavities.json.ccType.ChestCavityTypeManager;
import net.tigereye.chestcavity.chestcavities.types.DefaultChestCavityType;

import java.util.HashMap;
import java.util.Map;

public class ChestCavityInstanceFactory {
    private static final Map<ResourceLocation, ChestCavityType> entityIdentifierMap = new HashMap<>();
    private static final ChestCavityType DEFAULT_CHEST_CAVITY_TYPE = new DefaultChestCavityType();

    public ChestCavityInstanceFactory() {
    }

    public static ChestCavityInstance newChestCavityInstance(EntityType<? extends LivingEntity> entityType, LivingEntity owner) {
        ResourceLocation entityID = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (ChestCavityAssignmentManager.GeneratedChestCavityAssignments.containsKey(entityID)) {
            ResourceLocation chestCavityTypeID = ChestCavityAssignmentManager.GeneratedChestCavityAssignments.get(entityID);
            if (ChestCavityTypeManager.GeneratedChestCavityTypes.containsKey(chestCavityTypeID)) {
                return new ChestCavityInstance(ChestCavityTypeManager.GeneratedChestCavityTypes.get(chestCavityTypeID), owner);
            }
        }

        return entityIdentifierMap.containsKey(entityID) ? new ChestCavityInstance(entityIdentifierMap.get(ForgeRegistries.ENTITY_TYPES.getKey(entityType)), owner) : new ChestCavityInstance(DEFAULT_CHEST_CAVITY_TYPE, owner);
    }

    public static void register(EntityType<? extends LivingEntity> entityType, ChestCavityType chestCavityType) {
        entityIdentifierMap.put(ForgeRegistries.ENTITY_TYPES.getKey(entityType), chestCavityType);
    }

    public static void register(ResourceLocation entityIdentifier, ChestCavityType chestCavityType) {
        entityIdentifierMap.put(entityIdentifier, chestCavityType);
    }
}
