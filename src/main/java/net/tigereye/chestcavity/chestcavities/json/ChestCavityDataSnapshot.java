package net.tigereye.chestcavity.chestcavities.json;

import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;

import java.util.Map;

/**
 * A fully constructed data definition snapshot. Raw maps are retained so the
 * same validated data can be sent to clients without rereading resources.
 */
public final class ChestCavityDataSnapshot {
    private final long revision;
    private final Map<ResourceLocation, String> rawOrganData;
    private final Map<ResourceLocation, String> rawInventoryTypeData;
    private final Map<ResourceLocation, String> rawChestCavityTypes;
    private final Map<ResourceLocation, String> rawChestCavityAssignments;
    private final Map<ResourceLocation, OrganData> organData;
    private final Map<ResourceLocation, InventoryTypeData> inventoryTypeData;
    private final Map<ResourceLocation, ChestCavityType> chestCavityTypes;
    private final Map<ResourceLocation, ResourceLocation> chestCavityAssignments;
    private final boolean usable;

    public ChestCavityDataSnapshot(
            long revision,
            Map<ResourceLocation, String> rawOrganData,
            Map<ResourceLocation, String> rawInventoryTypeData,
            Map<ResourceLocation, String> rawChestCavityTypes,
            Map<ResourceLocation, String> rawChestCavityAssignments,
            Map<ResourceLocation, OrganData> organData,
            Map<ResourceLocation, InventoryTypeData> inventoryTypeData,
            Map<ResourceLocation, ChestCavityType> chestCavityTypes,
            Map<ResourceLocation, ResourceLocation> chestCavityAssignments,
            boolean usable
    ) {
        this.revision = revision;
        this.rawOrganData = Map.copyOf(rawOrganData);
        this.rawInventoryTypeData = Map.copyOf(rawInventoryTypeData);
        this.rawChestCavityTypes = Map.copyOf(rawChestCavityTypes);
        this.rawChestCavityAssignments = Map.copyOf(rawChestCavityAssignments);
        this.organData = Map.copyOf(organData);
        this.inventoryTypeData = Map.copyOf(inventoryTypeData);
        this.chestCavityTypes = Map.copyOf(chestCavityTypes);
        this.chestCavityAssignments = Map.copyOf(chestCavityAssignments);
        this.usable = usable;
    }

    public long getRevision() {
        return revision;
    }

    public Map<ResourceLocation, String> getRawOrganData() {
        return rawOrganData;
    }

    public Map<ResourceLocation, String> getRawInventoryTypeData() {
        return rawInventoryTypeData;
    }

    public Map<ResourceLocation, String> getRawChestCavityTypes() {
        return rawChestCavityTypes;
    }

    public Map<ResourceLocation, String> getRawChestCavityAssignments() {
        return rawChestCavityAssignments;
    }

    public Map<ResourceLocation, OrganData> getOrganData() {
        return organData;
    }

    public Map<ResourceLocation, InventoryTypeData> getInventoryTypeData() {
        return inventoryTypeData;
    }

    public Map<ResourceLocation, ChestCavityType> getChestCavityTypes() {
        return chestCavityTypes;
    }

    public Map<ResourceLocation, ResourceLocation> getChestCavityAssignments() {
        return chestCavityAssignments;
    }

    public boolean isUsable() {
        return usable;
    }
}
