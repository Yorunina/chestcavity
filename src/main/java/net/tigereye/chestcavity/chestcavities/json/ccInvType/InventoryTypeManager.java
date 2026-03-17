package net.tigereye.chestcavity.chestcavities.json.ccInvType;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryTypeManager {
    private static final InventoryTypeSerializer SERIALIZER = new InventoryTypeSerializer();
    public static Map<ResourceLocation, InventoryTypeData> InventoryTypeData = new HashMap<>();
    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("chestcavity", "textures/gui/default.png");
    public static final List<ChestCavitySlotDefinition> DEFAULT_SLOT_DEFINITION = getDefaultInventoryTypeSlotDefinition();
    public static final String DEFAULT_INVENTORY_TYPE_STRING = "chestcavity:cc_inventory_types/default";

    public InventoryTypeManager() {
    }

    public static InventoryTypeData getDefaultInventoryTypeData() {
        return new InventoryTypeData(new ResourceLocation(DEFAULT_INVENTORY_TYPE_STRING), DEFAULT_TEXTURE, DEFAULT_SLOT_DEFINITION, new SlotDefinition(0, 0), new TitleSlotDefinition(0, 0), new TitleSlotDefinition(0, 0), new SlotDefinition(176, 166));
    }

    public static List<ChestCavitySlotDefinition> getDefaultInventoryTypeSlotDefinition() {
        return new ArrayList<>();
    }

    public static void reloadInventoryType(ResourceManager manager) {
        InventoryTypeData.clear();
        ChestCavity.LOGGER.info("Loading screenType.");
        manager.listResources("cc_inventory_types", (path) -> path.getPath().endsWith(".json")).forEach((jsonId, resource) -> {
            try {
                InputStream stream = resource.open();
                try {
                    ResourceLocation id = new ResourceLocation(jsonId.getNamespace(), jsonId.getPath().substring(0, jsonId.getPath().length() - 5));
                    Reader reader = new InputStreamReader(stream);
                    InventoryTypeData inventoryTypeData = SERIALIZER.read(jsonId, (new Gson()).fromJson(reader, InventoryTypeJsonFormat.class));
                    InventoryTypeData.put(id, inventoryTypeData);
                    ChestCavity.LOGGER.info("Loaded inventory " + jsonId);
                } catch (Throwable readError) {
                    try {
                        stream.close();
                    } catch (Throwable closeError) {
                        readError.addSuppressed(closeError);
                    }
                    throw readError;
                }
                stream.close();
            } catch (Exception openError) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + jsonId.toString(), openError);
            }
        });

    }

    public static InventoryTypeData getInventoryTypeData(ResourceLocation id) {
        return InventoryTypeData.getOrDefault(id, getDefaultInventoryTypeData());
    }
}
