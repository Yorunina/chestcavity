package net.tigereye.chestcavity.compat.kubejs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.client.OrganHoloTagFilter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Client event used to configure the item tags shown in the organ catalogue.
 *
 * <pre>
 * ChestCavityEvents.organHoloFilterTags(event => {
 *     event.addTag('forge:ores', 'Ores')
 * })
 * </pre>
 */
public class OrganHoloFilterTagsJS extends EventJS {
    private final Map<ResourceLocation, OrganHoloTagFilter> tags = new LinkedHashMap<>();

    public OrganHoloFilterTagsJS addTag(String id) {
        return addTag(id, null);
    }

    public OrganHoloFilterTagsJS addTag(String id, String label) {
        String normalized = id == null ? "" : id.trim();
        if (normalized.startsWith("#")) {
            normalized = normalized.substring(1);
        }
        ResourceLocation resourceLocation = ResourceLocation.tryParse(normalized);
        if (resourceLocation == null) {
            throw new IllegalArgumentException("Invalid item tag id: " + id);
        }
        String displayName = label == null || label.isBlank() ? resourceLocation.toString() : label;
        tags.put(resourceLocation, new OrganHoloTagFilter(resourceLocation, displayName));
        return this;
    }

    public OrganHoloFilterTagsJS removeTag(String id) {
        ResourceLocation resourceLocation = parse(id);
        if (resourceLocation != null) {
            tags.remove(resourceLocation);
        }
        return this;
    }

    public OrganHoloFilterTagsJS clear() {
        tags.clear();
        return this;
    }

    public List<OrganHoloTagFilter> getTags() {
        return List.copyOf(new ArrayList<>(tags.values()));
    }

    private static ResourceLocation parse(String id) {
        if (id == null) {
            return null;
        }
        String normalized = id.trim();
        if (normalized.startsWith("#")) {
            normalized = normalized.substring(1);
        }
        return ResourceLocation.tryParse(normalized);
    }
}
