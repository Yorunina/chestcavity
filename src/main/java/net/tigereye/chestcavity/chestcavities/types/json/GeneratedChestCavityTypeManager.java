package net.tigereye.chestcavity.chestcavities.types.json;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.types.GeneratedChestCavityType;
import net.tigereye.chestcavity.forge.port.SimpleSynchronousResourceReloadListener;

public class GeneratedChestCavityTypeManager implements SimpleSynchronousResourceReloadListener {
    private static final String RESOURCE_LOCATION = "types";
    private final ChestCavityTypeSerializer SERIALIZER = new ChestCavityTypeSerializer();
    public static Map<ResourceLocation, GeneratedChestCavityType> GeneratedChestCavityTypes = new HashMap();

    public GeneratedChestCavityTypeManager() {
    }

    public ResourceLocation getFabricId() {
        return new ResourceLocation("chestcavity", "types");
    }

    public void m_6213_(ResourceManager manager) {
        GeneratedChestCavityTypes.clear();
        ChestCavity.LOGGER.info("Loading chest cavity types.");
        manager.m_214159_("types", (path) -> {
            return path.m_135815_().endsWith(".json");
        }).forEach((id, resource) -> {
            try {
                InputStream stream = resource.m_215507_();

                try {
                    Reader reader = new InputStreamReader(stream);
                    GeneratedChestCavityTypes.put(id, this.SERIALIZER.read(id, (ChestCavityTypeJsonFormat)(new Gson()).fromJson(reader, ChestCavityTypeJsonFormat.class)));
                } catch (Throwable var7) {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (Throwable var6) {
                            var7.addSuppressed(var6);
                        }
                    }

                    throw var7;
                }

                if (stream != null) {
                    stream.close();
                }
            } catch (Exception var8) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), var8);
            }

        });
        ChestCavity.LOGGER.info("Loaded " + GeneratedChestCavityTypes.size() + " chest cavity types.");
    }
}
