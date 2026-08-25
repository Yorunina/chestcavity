package net.tigereye.chestcavity.chestcavities.json.ccType;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.util.ResourceDataUtil;

import java.util.HashMap;
import java.util.Map;

public class ChestCavityTypeManager {
    private static final ChestCavityTypeSerializer SERIALIZER = new ChestCavityTypeSerializer();
    private static final Gson GSON = new Gson();
    public static Map<ResourceLocation, ChestCavityType> ChestCavityTypes = new HashMap<>();
    public static Map<ResourceLocation, String> RawChestCavityTypes = new HashMap<>();

    public ChestCavityTypeManager() {
    }

    public static void reloadChestCavityType(ResourceManager manager) {
        RawChestCavityTypes.clear();
        manager.listResources("cc_types", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try {
                RawChestCavityTypes.put(id, ResourceDataUtil.readUtf8(resource));
            } catch (Exception var8) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), var8);
            }
        });
        parseData(RawChestCavityTypes);
    }

    public static void parseData(Map<ResourceLocation, String> rawData) {
        ChestCavityTypes.clear();
        rawData.forEach((id, data) -> {
            ChestCavityType chestCavityType = SERIALIZER.read(id, GSON.fromJson(data, ChestCavityTypeJsonFormat.class));
            ChestCavityTypes.put(id, chestCavityType);
        });
    }

}
