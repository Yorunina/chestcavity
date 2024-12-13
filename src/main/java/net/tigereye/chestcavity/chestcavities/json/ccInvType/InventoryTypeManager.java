package net.tigereye.chestcavity.chestcavities.json.ccInvType;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.forge.port.SimpleSynchronousResourceReloadListener;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryTypeManager implements SimpleSynchronousResourceReloadListener {
    private final InventoryTypeSerializer SERIALIZER = new InventoryTypeSerializer();
    public static Map<ResourceLocation, InventoryTypeData> GeneratedInventoryTypeData = new HashMap<>();

    public InventoryTypeManager() {
    }

    public ResourceLocation getFabricId() {
        return new ResourceLocation("chestcavity", "organs");
    }

    public void onResourceManagerReload(ResourceManager manager) {
        GeneratedInventoryTypeData.clear();
        ChestCavity.LOGGER.info("Loading screenType.");
        manager.listResources("organs", (path) -> {
            return path.getPath().endsWith(".json");
        }).forEach((id, resource) -> {
            try {
                InputStream stream = resource.open();
                try {
                    Reader reader = new InputStreamReader(stream);
                    InventoryTypeData inventoryTypeData = this.SERIALIZER.read(id, (InventoryTypeJsonFormat)(new Gson()).fromJson(reader, InventoryTypeJsonFormat.class));
                    GeneratedInventoryTypeData.put(id, inventoryTypeData);
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
        ChestCavity.LOGGER.info("Loaded " + GeneratedInventoryTypeData.size() + " organs.");
    }
}
