package net.tigereye.chestcavity.chestcavities.json.ccInvType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.DataResourceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryTypeManager {
    private static final InventoryTypeSerializer SERIALIZER = new InventoryTypeSerializer();
    private static final com.google.gson.Gson GSON = new com.google.gson.Gson();
    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("chestcavity", "textures/gui/default.png");
    public static final List<ChestCavitySlotDefinition> DEFAULT_SLOT_DEFINITION = getDefaultInventoryTypeSlotDefinition();
    public static final String DEFAULT_INVENTORY_TYPE_STRING = "chestcavity:cc_inventory_types/default";
    public static final ResourceLocation DEFAULT_INVENTORY_TYPE =
            new ResourceLocation(DEFAULT_INVENTORY_TYPE_STRING);
    public static volatile Map<ResourceLocation, InventoryTypeData> InventoryTypeData = Map.of();
    public static volatile Map<ResourceLocation, String> RawInventoryTypeData = Map.of();

    public InventoryTypeManager() {
    }

    public static InventoryTypeData getDefaultInventoryTypeData() {
        return new InventoryTypeData(new ResourceLocation(DEFAULT_INVENTORY_TYPE_STRING), DEFAULT_TEXTURE, DEFAULT_SLOT_DEFINITION, new SlotDefinition(0, 0), new TitleSlotDefinition(0, 0), new TitleSlotDefinition(0, 0), new SlotDefinition(176, 166));
    }

    public static List<ChestCavitySlotDefinition> getDefaultInventoryTypeSlotDefinition() {
        return new ArrayList<>();
    }

    public static void reloadInventoryType(ResourceManager manager) {
        Map<ResourceLocation, String> rawData = loadRawData(manager);
        applySnapshot(rawData, parseDataSnapshot(rawData));
    }

    public static Map<ResourceLocation, String> loadRawData(ResourceManager manager) {
        return DataResourceUtil.readResources(manager, "cc_inventory_types");
    }

    public static InventoryTypeData getInventoryTypeData(ResourceLocation id) {
        ResourceLocation normalized = DataResourceUtil.normalizeId(id);
        return InventoryTypeData.getOrDefault(normalized, getDefaultInventoryTypeData());
    }

    public static Map<ResourceLocation, InventoryTypeData> parseDataSnapshot(Map<ResourceLocation, String> rawData) {
        Map<ResourceLocation, InventoryTypeData> result = new HashMap<>();
        rawData.forEach((id, data) -> {
            try {
                InventoryTypeData inventoryTypeData = SERIALIZER.read(
                        id,
                        GSON.fromJson(data, InventoryTypeJsonFormat.class)
                );
                result.put(DataResourceUtil.normalizeId(id), inventoryTypeData);
            } catch (Exception error) {
                ChestCavity.LOGGER.error("Error parsing inventory type resource " + id, error);
            }
        });
        return result;
    }

    public static void applySnapshot(
            Map<ResourceLocation, String> rawData,
            Map<ResourceLocation, InventoryTypeData> parsedData
    ) {
        RawInventoryTypeData = Map.copyOf(rawData);
        InventoryTypeData = Map.copyOf(parsedData);
    }

    public static void parseData(Map<ResourceLocation, String> rawData) {
        applySnapshot(rawData, parseDataSnapshot(rawData));
    }
}
