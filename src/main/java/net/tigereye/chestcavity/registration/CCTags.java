package net.tigereye.chestcavity.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CCTags {
    public static final TagKey<Item> CHEST_OPENER;
    public static final TagKey<Item> CANNOT_REMOVE;

    public CCTags() {
    }

    static {
        CHEST_OPENER = TagKey.create(Registries.ITEM, new ResourceLocation("chestcavity", "chest_opener"));
        CANNOT_REMOVE = TagKey.create(Registries.ITEM, new ResourceLocation("chestcavity", "cannot_remove"));
    }
}
