package net.tigereye.chestcavity.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LootBonusEnchantment;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.tigereye.chestcavity.registration.CCEnchantments;

public class SurgicalEnchantment extends LootBonusEnchantment {
    public SurgicalEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public int m_6183_(int level) {
        return 15 + (level - 1) * 9;
    }

    public int m_6175_(int level) {
        return super.getMinCost(level) + 50;
    }

    public int m_6586_() {
        return 3;
    }

    public boolean m_5975_(Enchantment other) {
        return super.isCompatibleWith(other) && other.isCompatibleWith(Enchantments.MOB_LOOTING) && other != CCEnchantments.TOMOPHOBIA.get();
    }
}
