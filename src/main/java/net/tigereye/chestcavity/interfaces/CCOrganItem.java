package net.tigereye.chestcavity.interfaces;

import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.organs.OrganData;

public interface CCOrganItem {
    OrganData getOrganData(ItemStack var1);
}
