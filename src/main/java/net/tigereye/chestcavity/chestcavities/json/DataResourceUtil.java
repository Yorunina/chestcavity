package net.tigereye.chestcavity.chestcavities.json;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.ChestCavity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Shared resource loading and ID normalization for Chest Cavity JSON data.
 */
public final class DataResourceUtil {
    public static final int MAX_JSON_LENGTH = 32767;

    private DataResourceUtil() {
    }

    public static ResourceLocation normalizeId(ResourceLocation id) {
        if (id == null) {
            return null;
        }
        String path = id.getPath();
        if (path.endsWith(".json")) {
            path = path.substring(0, path.length() - 5);
        }
        return new ResourceLocation(id.getNamespace(), path);
    }

    public static ResourceLocation normalizeId(String id) {
        return normalizeId(new ResourceLocation(id));
    }

    public static Map<ResourceLocation, String> readResources(ResourceManager manager, String root) {
        Map<ResourceLocation, Resource> resources = manager.listResources(
                root,
                path -> path.getPath().endsWith(".json")
        );
        List<ResourceLocation> ids = new ArrayList<>(resources.keySet());
        ids.sort((left, right) -> left.toString().compareTo(right.toString()));

        Map<ResourceLocation, String> result = new LinkedHashMap<>();
        for (ResourceLocation fileId : ids) {
            ResourceLocation id = normalizeId(fileId);
            Resource resource = resources.get(fileId);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.open(), StandardCharsets.UTF_8)
            )) {
                String data = reader.lines().collect(Collectors.joining("\n"));
                if (data.length() > MAX_JSON_LENGTH) {
                    throw new IllegalArgumentException("JSON exceeds " + MAX_JSON_LENGTH + " characters");
                }
                result.put(id, data);
            } catch (Exception error) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + fileId, error);
            }
        }
        return result;
    }
}
