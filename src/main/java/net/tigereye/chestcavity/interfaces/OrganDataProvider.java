package net.tigereye.chestcavity.interfaces;

import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;

@FunctionalInterface
public interface OrganDataProvider {
    OrganData getOrganData(ItemStack itemStack);
}
