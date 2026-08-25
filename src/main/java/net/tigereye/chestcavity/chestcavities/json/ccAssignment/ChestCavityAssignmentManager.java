package net.tigereye.chestcavity.chestcavities.json.ccAssignment;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.util.ResourceDataUtil;

import java.util.HashMap;
import java.util.Map;

public class ChestCavityAssignmentManager {
    private static final ChestCavityAssignmentSerializer SERIALIZER = new ChestCavityAssignmentSerializer();
    private static final Gson GSON = new Gson();
    public static Map<ResourceLocation, ResourceLocation> ChestCavityAssignments = new HashMap<>();
    public static Map<ResourceLocation, String> RawChestCavityAssignments = new HashMap<>();

    public static void reloadChestCavityAssignment(ResourceManager manager) {
        RawChestCavityAssignments.clear();
        manager.listResources("cc_entity_assignments", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try {
                RawChestCavityAssignments.put(id, ResourceDataUtil.readUtf8(resource));
            } catch (Exception var8) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), var8);
            }
        });
        parseData(RawChestCavityAssignments);
    }


    public static void parseData(Map<ResourceLocation, String> rawData) {
        ChestCavityAssignments.clear();
        rawData.forEach((id, data) -> {
            ChestCavityAssignmentResult assignmentResult = SERIALIZER.read(id, GSON.fromJson(data, ChestCavityAssignmentJsonFormat.class));
            ChestCavityAssignments.putAll(assignmentResult.getChestcavityMap());
        });
    }
}
