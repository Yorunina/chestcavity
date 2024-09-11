package net.tigereye.chestcavity.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.tigereye.chestcavity.registration.CCEnchantments;

public class TomophobiaEnchantment extends Enchantment {
    public TomophobiaEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    public int m_6183_(int level) {
        return 25;
    }

    public int m_6175_(int level) {
        return 50;
    }

    public int m_6586_() {
        return 1;
    }

    public boolean m_6591_() {
        return true;
    }
    public boolean m_5975_(Enchantment other) {
        return super.isCompatibleWith(other) && other != CCEnchantments.SURGICAL.get() && other != CCEnchantments.MALPRACTICE.get();
    }
}
