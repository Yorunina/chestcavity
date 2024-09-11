package net.tigereye.chestcavity.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CCTags {
    public static final TagKey<Item> BUTCHERING_TOOL;
    public static final TagKey<Item> ROTTEN_FOOD;
    public static final TagKey<Item> CARNIVORE_FOOD;
    public static final TagKey<Item> SALVAGEABLE;
    public static final TagKey<Item> IRON_REPAIR_MATERIAL;

    public CCTags() {
    }

    static {
        BUTCHERING_TOOL = TagKey.m_203882_(Registries.f_256913_, new ResourceLocation("chestcavity", "butchering_tool"));
        ROTTEN_FOOD = TagKey.m_203882_(Registries.f_256913_, new ResourceLocation("chestcavity", "rotten_food"));
        CARNIVORE_FOOD = TagKey.m_203882_(Registries.f_256913_, new ResourceLocation("chestcavity", "carnivore_food"));
        SALVAGEABLE = TagKey.m_203882_(Registries.f_256913_, new ResourceLocation("chestcavity", "salvageable"));
        IRON_REPAIR_MATERIAL = TagKey.m_203882_(Registries.f_256913_, new ResourceLocation("chestcavity", "iron_repair_material"));
    }
}
