package net.tigereye.chestcavity.chestcavities.json.ccType;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.types.GeneratedChestCavityType;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class ChestCavityTypeManager {
    private static final ChestCavityTypeSerializer SERIALIZER = new ChestCavityTypeSerializer();
    public static Map<ResourceLocation, GeneratedChestCavityType> GeneratedChestCavityTypes = new HashMap<>();

    public ChestCavityTypeManager() {
    }

    public static void reloadChestCavityType(ResourceManager manager) {
        GeneratedChestCavityTypes.clear();
        ChestCavity.LOGGER.info("Loading chest cavity types.");
        manager.listResources("types", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try {
                InputStream stream = resource.open();
                try {
                    Reader reader = new InputStreamReader(stream);
                    GeneratedChestCavityTypes.put(id, SERIALIZER.read(id, new Gson().fromJson(reader, ChestCavityTypeJsonFormat.class)));
                } catch (Throwable readError) {
                    try {
                        stream.close();
                    } catch (Throwable closeError) {
                        readError.addSuppressed(closeError);
                    }
                    throw readError;
                }
                stream.close();
            } catch (Exception var8) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), var8);
            }
        });
        ChestCavity.LOGGER.info("Loaded " + GeneratedChestCavityTypes.size() + " chest cavity types.");
    }

}