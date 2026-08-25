package net.tigereye.chestcavity.chestcavities.json;

import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentResult;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Atomically published resource data used by the mod.
 *
 * <p>The maps are defensive copies and cannot be changed by consumers. A
 * reload creates a completely new snapshot before it is published, so readers
 * never observe a partially reloaded combination of resource types.</p>
 */
public final class ChestCavityDataSnapshot {
    private final long version;
    private final Map<ResourceLocation, OrganData> organs;
    private final Map<ResourceLocation, InventoryTypeData> inventoryTypes;
    private final Map<ResourceLocation, ChestCavityType> chestCavityTypes;
    private final Map<ResourceLocation, ResourceLocation> assignments;
    private final Map<ResourceLocation, String> rawOrgans;
    private final Map<ResourceLocation, String> rawInventoryTypes;
    private final Map<ResourceLocation, String> rawChestCavityTypes;
    private final Map<ResourceLocation, String> rawAssignments;

    public ChestCavityDataSnapshot(
            long version,
            Map<ResourceLocation, OrganData> organs,
            Map<ResourceLocation, InventoryTypeData> inventoryTypes,
            Map<ResourceLocation, ChestCavityType> chestCavityTypes,
            Map<ResourceLocation, ResourceLocation> assignments,
            Map<ResourceLocation, String> rawOrgans,
            Map<ResourceLocation, String> rawInventoryTypes,
            Map<ResourceLocation, String> rawChestCavityTypes,
            Map<ResourceLocation, String> rawAssignments
    ) {
        this.version = version;
        this.organs = immutableCopy(organs);
        this.inventoryTypes = immutableCopy(inventoryTypes);
        this.chestCavityTypes = immutableCopy(chestCavityTypes);
        this.assignments = immutableCopy(assignments);
        this.rawOrgans = immutableCopy(rawOrgans);
        this.rawInventoryTypes = immutableCopy(rawInventoryTypes);
        this.rawChestCavityTypes = immutableCopy(rawChestCavityTypes);
        this.rawAssignments = immutableCopy(rawAssignments);
    }

    public static ChestCavityDataSnapshot empty() {
        return new ChestCavityDataSnapshot(
                0L,
                Map.of(),
                Map.of(),
                Map.of(),
                Map.of(),
                Map.of(),
                Map.of(),
                Map.of(),
                Map.of()
        );
    }

    private static <K, V> Map<K, V> immutableCopy(Map<K, V> source) {
        return Collections.unmodifiableMap(new LinkedHashMap<>(source));
    }

    public long getVersion() {
        return this.version;
    }

    public Map<ResourceLocation, OrganData> getOrgans() {
        return this.organs;
    }

    public Map<ResourceLocation, InventoryTypeData> getInventoryTypes() {
        return this.inventoryTypes;
    }

    public Map<ResourceLocation, ChestCavityType> getChestCavityTypes() {
        return this.chestCavityTypes;
    }

    public Map<ResourceLocation, ResourceLocation> getAssignments() {
        return this.assignments;
    }

    public Map<ResourceLocation, String> getRawOrgans() {
        return this.rawOrgans;
    }

    public Map<ResourceLocation, String> getRawInventoryTypes() {
        return this.rawInventoryTypes;
    }

    public Map<ResourceLocation, String> getRawChestCavityTypes() {
        return this.rawChestCavityTypes;
    }

    public Map<ResourceLocation, String> getRawAssignments() {
        return this.rawAssignments;
    }

    public OrganData getOrgan(ResourceLocation id) {
        return this.organs.get(id);
    }

    public InventoryTypeData getInventoryType(ResourceLocation id) {
        return this.inventoryTypes.get(id);
    }

    public ChestCavityType getChestCavityType(ResourceLocation id) {
        return this.chestCavityTypes.get(id);
    }

    public ResourceLocation getAssignment(ResourceLocation entityType) {
        return this.assignments.get(entityType);
    }
}
