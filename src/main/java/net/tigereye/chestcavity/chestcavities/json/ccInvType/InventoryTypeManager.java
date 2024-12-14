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
    public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/shulker_box.png");
    public static final List<SlotPosition> DEFAULT_SLOT_POSITIONS = getDefaultInventoryTypeSlotPositions();
    public static final String DEFAULT_INVENTORY_TYPE_STRING = "chestcavity:default";
    public InventoryTypeManager() {
    }

    public static InventoryTypeData getDefaultInventoryTypeData() {
        return new InventoryTypeData(DEFAULT_TEXTURE, DEFAULT_SLOT_POSITIONS, new SlotPosition(0, 0));
    }
    public static List<SlotPosition> getDefaultInventoryTypeSlotPositions() {
        int n, m;
        List<SlotPosition> slotPositions = new ArrayList<>();
        for(n = 0; n < 3; ++n) {
            for(m = 0; m < 9; ++m) {
                slotPositions.add(new SlotPosition(8 + m * 18, 18 + n * 18));
            }
        }
        return slotPositions;
    }
    public static void reloadInventoryType(ResourceManager manager) {
        GeneratedInventoryTypeData.clear();
        ChestCavity.LOGGER.info("Loading screenType.");
        manager.listResources("inventory", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
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
