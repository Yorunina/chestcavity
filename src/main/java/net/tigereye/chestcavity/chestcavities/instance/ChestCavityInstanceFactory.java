package net.tigereye.chestcavity.chestcavities.instance;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.types.DefaultChestCavityType;
import net.tigereye.chestcavity.chestcavities.types.json.GeneratedChestCavityAssignmentManager;
import net.tigereye.chestcavity.chestcavities.types.json.GeneratedChestCavityTypeManager;

public class ChestCavityInstanceFactory {
    private static final Map<ResourceLocation, ChestCavityType> entityIdentifierMap = new HashMap<>();
    private static final ChestCavityType DEFAULT_CHEST_CAVITY_TYPE = new DefaultChestCavityType();

    public ChestCavityInstanceFactory() {
    }

    public static ChestCavityInstance newChestCavityInstance(EntityType<? extends LivingEntity> entityType, LivingEntity owner) {
        ResourceLocation entityID = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (GeneratedChestCavityAssignmentManager.GeneratedChestCavityAssignments.containsKey(entityID)) {
            ResourceLocation chestCavityTypeID = GeneratedChestCavityAssignmentManager.GeneratedChestCavityAssignments.get(entityID);
            if (GeneratedChestCavityTypeManager.GeneratedChestCavityTypes.containsKey(chestCavityTypeID)) {
                return new ChestCavityInstance(GeneratedChestCavityTypeManager.GeneratedChestCavityTypes.get(chestCavityTypeID), owner);
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
