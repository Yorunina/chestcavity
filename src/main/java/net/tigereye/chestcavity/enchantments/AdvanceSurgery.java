package net.tigereye.chestcavity.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import static net.tigereye.chestcavity.registration.CCTags.CHEST_OPENER;

public class AdvanceSurgery extends Enchantment {
    public AdvanceSurgery() {
        super(Rarity.VERY_RARE, CCEnchantmentCategories.CHEST_OPENER_CATEGORY, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }
    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.is(CHEST_OPENER);
    }

    @Override
    public boolean isCurse() {
        return super.isCurse();
    }

    @Override
    public boolean isTradeable() {
        return true;
    }

    @Override
    public boolean isDiscoverable() {
        return true;
    }
}