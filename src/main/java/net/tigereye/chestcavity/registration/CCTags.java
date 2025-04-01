package net.tigereye.chestcavity.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CCTags {
    public static final TagKey<Item> ROTTEN_FOOD;
    public static final TagKey<Item> CARNIVORE_FOOD;
    public static final TagKey<Item> IRON_REPAIR_MATERIAL;
    public static final TagKey<Item> CHEST_OPENER;

    public CCTags() {
    }

    static {
        ROTTEN_FOOD = TagKey.create(Registries.ITEM, new ResourceLocation("chestcavity", "rotten_food"));
        CARNIVORE_FOOD = TagKey.create(Registries.ITEM, new ResourceLocation("chestcavity", "carnivore_food"));
        IRON_REPAIR_MATERIAL = TagKey.create(Registries.ITEM, new ResourceLocation("chestcavity", "iron_repair_material"));
        CHEST_OPENER = TagKey.create(Registries.ITEM, new ResourceLocation("chestcavity", "chest_opener"));
    }
}
