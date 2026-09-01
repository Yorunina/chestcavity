package net.tigereye.chestcavity.chestcavities.json.ccType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.json.DataResourceUtil;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;

import java.util.HashMap;
import java.util.Map;

public class ChestCavityTypeManager {
    private static final ChestCavityTypeSerializer SERIALIZER = new ChestCavityTypeSerializer();
    private static final com.google.gson.Gson GSON = new com.google.gson.Gson();
    public static volatile Map<ResourceLocation, ChestCavityType> ChestCavityTypes = Map.of();
    public static volatile Map<ResourceLocation, String> RawChestCavityTypes = Map.of();

    public ChestCavityTypeManager() {
    }

    public static void reloadChestCavityType(ResourceManager manager) {
        Map<ResourceLocation, String> rawData = loadRawData(manager);
        applySnapshot(rawData, parseDataSnapshot(rawData, InventoryTypeManager.InventoryTypeData));
    }

    public static Map<ResourceLocation, String> loadRawData(ResourceManager manager) {
        return DataResourceUtil.readResources(manager, "cc_types");
    }

    public static Map<ResourceLocation, ChestCavityType> parseDataSnapshot(
            Map<ResourceLocation, String> rawData,
            Map<ResourceLocation, InventoryTypeData> inventoryTypeData
    ) {
        Map<ResourceLocation, ChestCavityType> result = new HashMap<>();
        rawData.forEach((id, data) -> {
            try {
                ChestCavityType chestCavityType = SERIALIZER.read(
                        id,
                        GSON.fromJson(data, ChestCavityTypeJsonFormat.class),
                        inventoryTypeData
                );
                result.put(DataResourceUtil.normalizeId(id), chestCavityType);
            } catch (Exception error) {
                ChestCavity.LOGGER.error("Error parsing chest cavity type resource " + id, error);
            }
        });
        return result;
    }

    public static void applySnapshot(
            Map<ResourceLocation, String> rawData,
            Map<ResourceLocation, ChestCavityType> parsedData
    ) {
        RawChestCavityTypes = Map.copyOf(rawData);
        ChestCavityTypes = Map.copyOf(parsedData);
    }

    public static void parseData(Map<ResourceLocation, String> rawData) {
        applySnapshot(
                rawData,
                parseDataSnapshot(rawData, InventoryTypeManager.InventoryTypeData)
        );
    }

}
