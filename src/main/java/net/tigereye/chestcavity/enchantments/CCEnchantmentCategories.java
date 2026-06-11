package net.tigereye.chestcavity.enchantments;

import net.minecraft.world.item.enchantment.EnchantmentCategory;

import static net.tigereye.chestcavity.registration.CCTags.CHEST_OPENER;

public class CCEnchantmentCategories {
    public static final EnchantmentCategory CHEST_OPENER_CATEGORY = EnchantmentCategory.create("chest_opener", item -> item.builtInRegistryHolder().is(CHEST_OPENER));

    private CCEnchantmentCategories() {}
}