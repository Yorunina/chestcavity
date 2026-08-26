package net.tigereye.chestcavity.chestcavities.json;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentManager;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.chestcavities.json.ccType.ChestCavityTypeManager;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;

import java.util.Map;

/**
 * Single source of truth for all data-driven Chest Cavity resources.
 */
public final class ChestCavityDataRepository {
    private static volatile ChestCavityDataSnapshot current = ChestCavityDataSnapshot.empty();

    private ChestCavityDataRepository() {
    }

    public static ChestCavityDataSnapshot getCurrent() {
        return current;
    }

    public static synchronized void reload(ResourceManager resourceManager) {
        try {
            Map<ResourceLocation, String> rawOrgans = OrganManager.loadRawData(resourceManager);
            Map<ResourceLocation, String> rawInventoryTypes = InventoryTypeManager.loadRawData(resourceManager);
            Map<ResourceLocation, String> rawChestCavityTypes = ChestCavityTypeManager.loadRawData(resourceManager);
            Map<ResourceLocation, String> rawAssignments = ChestCavityAssignmentManager.loadRawData(resourceManager);

            publish(buildSnapshot(
                    rawOrgans,
                    rawInventoryTypes,
                    rawChestCavityTypes,
                    rawAssignments
            ));
        } catch (Exception error) {
            ChestCavity.LOGGER.error("Chest Cavity resource reload failed; retaining the previous snapshot", error);
        }
    }

    public static synchronized void install(
            Map<ResourceLocation, String> rawOrgans,
            Map<ResourceLocation, String> rawInventoryTypes,
            Map<ResourceLocation, String> rawChestCavityTypes,
            Map<ResourceLocation, String> rawAssignments
    ) {
        try {
            publish(buildSnapshot(rawOrgans, rawInventoryTypes, rawChestCavityTypes, rawAssignments));
        } catch (Exception error) {
            ChestCavity.LOGGER.error("Chest Cavity client data installation failed; retaining the previous snapshot", error);
        }
    }

    private static ChestCavityDataSnapshot buildSnapshot(
            Map<ResourceLocation, String> rawOrgans,
            Map<ResourceLocation, String> rawInventoryTypes,
            Map<ResourceLocation, String> rawChestCavityTypes,
            Map<ResourceLocation, String> rawAssignments
    ) {
        Map<ResourceLocation, OrganData> organs = OrganManager.parseRawData(rawOrgans);
        Map<ResourceLocation, InventoryTypeData> inventoryTypes = InventoryTypeManager.parseRawData(rawInventoryTypes);
        Map<ResourceLocation, ChestCavityType> chestCavityTypes =
                ChestCavityTypeManager.parseRawData(rawChestCavityTypes, inventoryTypes);
        Map<ResourceLocation, ResourceLocation> assignments = ChestCavityAssignmentManager.parseRawData(rawAssignments);

        return new ChestCavityDataSnapshot(
                organs,
                inventoryTypes,
                chestCavityTypes,
                assignments,
                rawOrgans,
                rawInventoryTypes,
                rawChestCavityTypes,
                rawAssignments
        );
    }

    private static void publish(ChestCavityDataSnapshot snapshot) {
        current = snapshot;
    }
}
