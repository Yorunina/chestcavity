//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.tigereye.chestcavity.chestcavities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;

import java.util.Map;

public interface IChestCavityType {
    Map<ResourceLocation, Float> getDefaultOrganScores();

    float getDefaultOrganScore(ResourceLocation var1);

    ChestCavityInventory getDefaultChestCavity();

    void fillChestCavityInventory(ChestCavityInventory var1);

    void loadBaseOrganScores(Map<ResourceLocation, Float> var1);

    OrganData catchExceptionalOrgan(ItemStack var1);

    void setOrganCompatibility(ChestCavityInstance var1);

    boolean isOpenable(ChestCavityInstance var1, Map<Enchantment, Integer> allEnchantments);

    void setInventoryType(ResourceLocation id);

    ResourceLocation getInventoryType();

}
