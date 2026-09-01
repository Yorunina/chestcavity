package net.tigereye.chestcavity.chestcavities.json.ccAssignment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.DataResourceUtil;

import java.util.HashMap;
import java.util.Map;

public class ChestCavityAssignmentManager {
    private static final ChestCavityAssignmentSerializer SERIALIZER = new ChestCavityAssignmentSerializer();
    private static final com.google.gson.Gson GSON = new com.google.gson.Gson();
    public static volatile Map<ResourceLocation, ResourceLocation> ChestCavityAssignments = Map.of();
    public static volatile Map<ResourceLocation, String> RawChestCavityAssignments = Map.of();

    public static void reloadChestCavityAssignment(ResourceManager manager) {
        Map<ResourceLocation, String> rawData = loadRawData(manager);
        applySnapshot(rawData, parseDataSnapshot(rawData));
    }

    public static Map<ResourceLocation, String> loadRawData(ResourceManager manager) {
        return DataResourceUtil.readResources(manager, "cc_entity_assignments");
    }

    public static Map<ResourceLocation, ResourceLocation> parseDataSnapshot(Map<ResourceLocation, String> rawData) {
        Map<ResourceLocation, ResourceLocation> result = new HashMap<>();
        rawData.forEach((id, data) -> {
            try {
                ChestCavityAssignmentResult assignmentResult = SERIALIZER.read(
                        id,
                        GSON.fromJson(data, ChestCavityAssignmentJsonFormat.class)
                );
                if (assignmentResult.getChestcavityMap() != null) {
                    result.putAll(assignmentResult.getChestcavityMap());
                }
            } catch (Exception error) {
                ChestCavity.LOGGER.error("Error parsing chest cavity assignment resource " + id, error);
            }
        });
        return result;
    }

    public static void applySnapshot(
            Map<ResourceLocation, String> rawData,
            Map<ResourceLocation, ResourceLocation> parsedData
    ) {
        RawChestCavityAssignments = Map.copyOf(rawData);
        ChestCavityAssignments = Map.copyOf(parsedData);
    }

    public static void parseData(Map<ResourceLocation, String> rawData) {
        applySnapshot(rawData, parseDataSnapshot(rawData));
    }
}
