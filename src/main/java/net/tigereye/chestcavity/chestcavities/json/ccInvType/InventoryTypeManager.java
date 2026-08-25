package net.tigereye.chestcavity.chestcavities.json.ccInvType;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.ChestCavityDataRepository;
import net.tigereye.chestcavity.util.ResourceDataUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryTypeManager {
    private static final InventoryTypeSerializer SERIALIZER = new InventoryTypeSerializer();
    private static final Gson GSON = new Gson();
    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("chestcavity", "textures/gui/default.png");
    public static final List<ChestCavitySlotDefinition> DEFAULT_SLOT_DEFINITION = getDefaultInventoryTypeSlotDefinition();
    public static final String DEFAULT_INVENTORY_TYPE_STRING = "chestcavity:cc_inventory_types/default";
    @Deprecated
    public static Map<ResourceLocation, InventoryTypeData> InventoryTypeData = Map.of();
    @Deprecated
    public static Map<ResourceLocation, String> RawInventoryTypeData = Map.of();

    public InventoryTypeManager() {
    }

    public static InventoryTypeData getDefaultInventoryTypeData() {
        return new InventoryTypeData(new ResourceLocation(DEFAULT_INVENTORY_TYPE_STRING), DEFAULT_TEXTURE, DEFAULT_SLOT_DEFINITION, new SlotDefinition(0, 0), new TitleSlotDefinition(0, 0), new TitleSlotDefinition(0, 0), new SlotDefinition(176, 166));
    }

    public static List<ChestCavitySlotDefinition> getDefaultInventoryTypeSlotDefinition() {
        return new ArrayList<>();
    }

    public static Map<ResourceLocation, String> loadRawData(ResourceManager manager) {
        Map<ResourceLocation, String> rawData = new HashMap<>();
        manager.listResources("cc_inventory_types", (path) -> path.getPath().endsWith(".json")).forEach((jsonId, resource) -> {
            try {
                ResourceLocation id = new ResourceLocation(jsonId.getNamespace(), jsonId.getPath().substring(0, jsonId.getPath().length() - 5));
                rawData.put(id, ResourceDataUtil.readUtf8(resource));
            } catch (Exception openError) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + jsonId.toString(), openError);
            }
        });
        return rawData;
    }

    public static InventoryTypeData getInventoryTypeData(ResourceLocation id) {
        InventoryTypeData data = ChestCavityDataRepository.getCurrent().getInventoryType(id);
        return data != null ? data : getDefaultInventoryTypeData();
    }

    public static Map<ResourceLocation, InventoryTypeData> parseRawData(Map<ResourceLocation, String> rawData) {
        Map<ResourceLocation, InventoryTypeData> parsedData = new HashMap<>();
        rawData.forEach((id, data) -> {
            InventoryTypeData inventoryTypeData = SERIALIZER.read(id, GSON.fromJson(data, InventoryTypeJsonFormat.class));
            parsedData.put(id, inventoryTypeData);
        });
        return parsedData;
    }

    public static void publish(Map<ResourceLocation, InventoryTypeData> parsedData, Map<ResourceLocation, String> rawData) {
        InventoryTypeData = Collections.unmodifiableMap(new HashMap<>(parsedData));
        RawInventoryTypeData = Collections.unmodifiableMap(new HashMap<>(rawData));
    }
}
