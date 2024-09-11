//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.tigereye.chestcavity.chestcavities;

import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.organs.OrganData;

public interface ChestCavityType {
    Map<ResourceLocation, Float> getDefaultOrganScores();

    float getDefaultOrganScore(ResourceLocation var1);

    ChestCavityInventory getDefaultChestCavity();

    boolean isSlotForbidden(int var1);

    void fillChestCavityInventory(ChestCavityInventory var1);

    void loadBaseOrganScores(Map<ResourceLocation, Float> var1);

    OrganData catchExceptionalOrgan(ItemStack var1);

    List<ItemStack> generateLootDrops(RandomSource var1, int var2);

    void setOrganCompatibility(ChestCavityInstance var1);

    float getHeartBleedCap();

    boolean isOpenable(ChestCavityInstance var1);

    void onDeath(ChestCavityInstance var1);
}
