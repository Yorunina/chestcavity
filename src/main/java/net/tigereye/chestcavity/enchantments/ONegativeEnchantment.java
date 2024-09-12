package net.tigereye.chestcavity.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;

public class ONegativeEnchantment extends Enchantment {
    public ONegativeEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.VANISHABLE, new EquipmentSlot[0]);
    }

    public int getMinCost(int level) {
        return 50 * level;
    }

    public int getMaxCost(int level) {
        return 100 * level;
    }

    public int getMaxLevel() {
        return 2;
    }

    public boolean canEnchant(ItemStack stack) {
        return true;
    }

    public boolean isAllowedOnBooks() {
        return true;
    }

    public boolean isCurse() {
        return false;
    }
}
