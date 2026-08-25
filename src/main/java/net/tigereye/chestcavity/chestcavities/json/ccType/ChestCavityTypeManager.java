package net.tigereye.chestcavity.chestcavities.json.ccType;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.util.ResourceDataUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChestCavityTypeManager {
    private static final ChestCavityTypeSerializer SERIALIZER = new ChestCavityTypeSerializer();
    private static final Gson GSON = new Gson();
    @Deprecated
    public static Map<ResourceLocation, ChestCavityType> ChestCavityTypes = Map.of();
    @Deprecated
    public static Map<ResourceLocation, String> RawChestCavityTypes = Map.of();

    public ChestCavityTypeManager() {
    }

    public static Map<ResourceLocation, String> loadRawData(ResourceManager manager) {
        Map<ResourceLocation, String> rawData = new HashMap<>();
        manager.listResources("cc_types", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try {
                rawData.put(id, ResourceDataUtil.readUtf8(resource));
            } catch (Exception var8) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), var8);
            }
        });
        return rawData;
    }

    public static Map<ResourceLocation, ChestCavityType> parseRawData(
            Map<ResourceLocation, String> rawData,
            Map<ResourceLocation, InventoryTypeData> inventoryTypes
    ) {
        Map<ResourceLocation, ChestCavityType> parsedData = new HashMap<>();
        rawData.forEach((id, data) -> {
            ChestCavityType chestCavityType = SERIALIZER.read(id, GSON.fromJson(data, ChestCavityTypeJsonFormat.class), inventoryTypes);
            parsedData.put(id, chestCavityType);
        });
        return parsedData;
    }

    public static void publish(Map<ResourceLocation, ChestCavityType> parsedData, Map<ResourceLocation, String> rawData) {
        ChestCavityTypes = Collections.unmodifiableMap(new HashMap<>(parsedData));
        RawChestCavityTypes = Collections.unmodifiableMap(new HashMap<>(rawData));
    }

}
