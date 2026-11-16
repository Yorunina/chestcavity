package net.tigereye.chestcavity.organproviders;

import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;
import net.tigereye.chestcavity.interfaces.OrganDataProvider;

public class NbtOrganDataProvider implements OrganDataProvider {
    @Override
    public OrganData getOrganData(ItemStack itemStack) {
        return OrganManager.readNBTOrganData(itemStack);
    }
}
