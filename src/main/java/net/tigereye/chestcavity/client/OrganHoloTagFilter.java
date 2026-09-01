package net.tigereye.chestcavity.client;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * A tag that can be selected from the organ catalogue's client-side filter.
 */
public final class OrganHoloTagFilter {
    private final ResourceLocation id;
    private final TagKey<Item> tag;
    private final String label;

    public OrganHoloTagFilter(ResourceLocation id, String label) {
        this.id = id;
        this.tag = TagKey.create(Registries.ITEM, id);
        this.label = label;
    }

    public ResourceLocation getId() {
        return id;
    }

    public TagKey<Item> getTag() {
        return tag;
    }

    public String getLabel() {
        return label;
    }

    public boolean matches(ItemStack stack) {
        return stack.is(tag);
    }
}
