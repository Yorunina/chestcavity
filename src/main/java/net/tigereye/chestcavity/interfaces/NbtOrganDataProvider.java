package net.tigereye.chestcavity.interfaces;

import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;

public class NbtOrganDataProvider implements OrganDataProvider {
    @Override
    public OrganData getOrganData(ItemStack itemStack) {
        return OrganManager.readNBTOrganData(itemStack);
    }
}
