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
    public static Map<ResourceLocation, InventoryTypeData> GeneratedInventoryTypeData = new HashMap<>();
    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("chestcavity", "textures/gui/chest_cavity.png");
    public static final List<ChestCavitySlotDefinition> DEFAULT_SLOT_DEFINITION = getDefaultInventoryTypeSlotDefinition();
    public static final String DEFAULT_INVENTORY_TYPE_STRING = "chestcavity:cc_inventory_types/default.json";

    public InventoryTypeManager() {
    }

    public static InventoryTypeData getDefaultInventoryTypeData() {
        return new InventoryTypeData(new ResourceLocation(DEFAULT_INVENTORY_TYPE_STRING), DEFAULT_TEXTURE, DEFAULT_SLOT_DEFINITION, new SlotDefinition(0, 0), new SlotDefinition(0, 0), new SlotDefinition(0, 0), new SlotDefinition(176, 1616));
    }

    public static List<ChestCavitySlotDefinition> getDefaultInventoryTypeSlotDefinition() {
        int n, m;
        List<ChestCavitySlotDefinition> slotDefinitions = new ArrayList<>();
        for (n = 0; n < 3; ++n) {
            for (m = 0; m < 9; ++m) {
                slotDefinitions.add(new ChestCavitySlotDefinition(8 + m * 18, 18 + n * 18));
            }
        }
        return slotDefinitions;
    }

    public static void reloadInventoryType(ResourceManager manager) {
        GeneratedInventoryTypeData.clear();
        ChestCavity.LOGGER.info("Loading screenType.");
        manager.listResources("cc_inventory_types", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try {
                InputStream stream = resource.open();
                try {
                    Reader reader = new InputStreamReader(stream);
                    InventoryTypeData inventoryTypeData = SERIALIZER.read(id, (new Gson()).fromJson(reader, InventoryTypeJsonFormat.class));
                    GeneratedInventoryTypeData.put(id, inventoryTypeData);
                    ChestCavity.LOGGER.info("Loaded inventory " + id.toString());
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
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), openError);
            }
        });

        ChestCavity.LOGGER.info("Loaded " + GeneratedInventoryTypeData.size() + " inventory.");
    }

    public static InventoryTypeData getInventoryTypeData(ResourceLocation id) {
        return GeneratedInventoryTypeData.getOrDefault(id, getDefaultInventoryTypeData());
    }
}
