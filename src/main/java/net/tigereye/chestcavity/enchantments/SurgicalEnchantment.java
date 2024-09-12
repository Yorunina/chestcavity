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

    public int getMinCost(int level) {
        return 15 + (level - 1) * 9;
    }

    public int getMaxCost(int level) {
        return super.getMinCost(level) + 50;
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean checkCompatibility(Enchantment other) {
        return super.isCompatibleWith(other) && other.isCompatibleWith(Enchantments.MOB_LOOTING) && other != CCEnchantments.TOMOPHOBIA.get();
    }
}
