package net.tigereye.chestcavity.chestcavities.json.ccType;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ChestCavityTypeManager {
    private static final ChestCavityTypeSerializer SERIALIZER = new ChestCavityTypeSerializer();
    public static Map<ResourceLocation, ChestCavityType> ChestCavityTypes = new HashMap<>();
    public static Map<ResourceLocation, String> RawChestCavityTypes = new HashMap<>();

    public ChestCavityTypeManager() {
    }

    public static void reloadChestCavityType(ResourceManager manager) {
        ChestCavityTypes.clear();
        manager.listResources("cc_types", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try {
                InputStream stream = resource.open();
                String result = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining(System.lineSeparator()));
                RawChestCavityTypes.put(id, result);
                stream.close();
            } catch (Exception var8) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), var8);
            }
        });
        parseData(RawChestCavityTypes);
    }

    public static void parseData(Map<ResourceLocation, String> rawData) {
        ChestCavityTypes.clear();
        rawData.forEach((id, data) -> {
            ChestCavityType chestCavityType = SERIALIZER.read(id, new Gson().fromJson(data, ChestCavityTypeJsonFormat.class));
            ChestCavityTypes.put(id, chestCavityType);
        });
    }

}