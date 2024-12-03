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
import net.tigereye.chestcavity.forge.port.SimpleSynchronousResourceReloadListener;

public class GeneratedChestCavityAssignmentManager implements SimpleSynchronousResourceReloadListener {
    private static final String RESOURCE_LOCATION = "entity_assignment";
    private final ChestCavityAssignmentSerializer SERIALIZER = new ChestCavityAssignmentSerializer();
    public static Map<ResourceLocation, ResourceLocation> GeneratedChestCavityAssignments = new HashMap<>();

    public GeneratedChestCavityAssignmentManager() {
    }

    public ResourceLocation getFabricId() {
        return new ResourceLocation("chestcavity", RESOURCE_LOCATION);
    }

    public void onResourceManagerReload(ResourceManager manager) {
        GeneratedChestCavityAssignments.clear();
        ChestCavity.LOGGER.info("Loading chest cavity assignments.");
        manager.listResources("entity_assignment", (path) -> {
            return path.getPath().endsWith(".json");
        }).forEach((id, resource) -> {
            try {
                InputStream stream = resource.open();

                try {
                    Reader reader = new InputStreamReader(stream);
                    GeneratedChestCavityAssignments.putAll(this.SERIALIZER.read(id, (ChestCavityAssignmentJsonFormat)(new Gson()).fromJson(reader, ChestCavityAssignmentJsonFormat.class)));
                } catch (Throwable var7) {
                    try {
                        stream.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }

                    throw var7;
                }

                stream.close();
            } catch (Exception var8) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), var8);
            }

        });
        ChestCavity.LOGGER.info("Loaded " + GeneratedChestCavityAssignments.size() + " chest cavity assignments.");
    }
}
