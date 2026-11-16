package net.tigereye.chestcavity.interfaces;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;

public class ManagedOrganDataProvider implements OrganDataProvider {
    @Override
    public OrganData getOrganData(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return OrganManager.hasEntry(item) ? OrganManager.getEntry(item) : null;
    }
}
