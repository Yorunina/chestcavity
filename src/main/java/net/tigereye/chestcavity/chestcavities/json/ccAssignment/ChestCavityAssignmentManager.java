package net.tigereye.chestcavity.chestcavities.json.ccAssignment;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.util.ResourceDataUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class ChestCavityAssignmentManager {
    private static final ChestCavityAssignmentSerializer SERIALIZER = new ChestCavityAssignmentSerializer();
    private static final Gson GSON = new Gson();
    @Deprecated
    public static Map<ResourceLocation, ResourceLocation> ChestCavityAssignments = Map.of();
    @Deprecated
    public static Map<ResourceLocation, String> RawChestCavityAssignments = Map.of();

    public static Map<ResourceLocation, String> loadRawData(ResourceManager manager) {
        Map<ResourceLocation, String> rawData = new HashMap<>();
        manager.listResources("cc_entity_assignments", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try {
                rawData.put(id, ResourceDataUtil.readUtf8(resource));
            } catch (Exception var8) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), var8);
            }
        });
        return rawData;
    }


    public static Map<ResourceLocation, ResourceLocation> parseRawData(Map<ResourceLocation, String> rawData) {
        Map<ResourceLocation, ResourceLocation> parsedData = new HashMap<>();
        rawData.forEach((id, data) -> {
            ChestCavityAssignmentResult assignmentResult = SERIALIZER.read(id, GSON.fromJson(data, ChestCavityAssignmentJsonFormat.class));
            parsedData.putAll(assignmentResult.getChestcavityMap());
        });
        return parsedData;
    }

    public static void publish(Map<ResourceLocation, ResourceLocation> parsedData, Map<ResourceLocation, String> rawData) {
        ChestCavityAssignments = Collections.unmodifiableMap(new HashMap<>(parsedData));
        RawChestCavityAssignments = Collections.unmodifiableMap(new HashMap<>(rawData));
    }
}
