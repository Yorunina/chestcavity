package net.tigereye.chestcavity.chestcavities.json.ccAssignment;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ChestCavityAssignmentManager {
    private static final ChestCavityAssignmentSerializer SERIALIZER = new ChestCavityAssignmentSerializer();
    public static Map<ResourceLocation, ResourceLocation> ChestCavityAssignments = new HashMap<>();
    public static Map<ResourceLocation, String> RawChestCavityAssignments = new HashMap<>();

    public static void reloadChestCavityAssignment(ResourceManager manager) {
        ChestCavityAssignments.clear();
        manager.listResources("cc_entity_assignments", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try {
                InputStream stream = resource.open();
                String result = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining(System.lineSeparator()));
                RawChestCavityAssignments.put(id, result);
                stream.close();
            } catch (Exception var8) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), var8);
            }
        });
        parseData(RawChestCavityAssignments);
    }


    public static void parseData(Map<ResourceLocation, String> rawData) {
        ChestCavityAssignments.clear();
        rawData.forEach((id, data) -> {
            ChestCavityAssignmentResult assignmentResult = SERIALIZER.read(id, new Gson().fromJson(data, ChestCavityAssignmentJsonFormat.class));
            ChestCavityAssignments.putAll(assignmentResult.getChestcavityMap());
        });
    }
}
