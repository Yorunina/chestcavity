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

    public int m_6183_(int level) {
        return 50 * level;
    }

    public int m_6175_(int level) {
        return 100 * level;
    }

    public int m_6586_() {
        return 2;
    }

    public boolean m_6081_(ItemStack stack) {
        return true;
    }

    public boolean m_6591_() {
        return true;
    }

    public boolean m_6594_() {
        return false;
    }
}
