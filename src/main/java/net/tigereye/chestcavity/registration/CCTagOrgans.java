package net.tigereye.chestcavity.registration;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class CCTagOrgans {
    public static Map<TagKey<Item>, Map<ResourceLocation, Float>> tagMap = new HashMap();

    public CCTagOrgans() {
    }

    public static void init() {
        Map<ResourceLocation, Float> ease_of_access = new HashMap();
        ease_of_access.put(CCOrganScores.EASE_OF_ACCESS, (float)Items.f_42342_.m_41459_());
        tagMap.put(ItemTags.f_13179_, ease_of_access);
        tagMap.put(ItemTags.f_13144_, ease_of_access);
    }
}
