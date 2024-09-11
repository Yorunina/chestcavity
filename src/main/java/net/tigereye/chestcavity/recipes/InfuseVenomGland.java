package net.tigereye.chestcavity.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.registration.CCItems;
import net.tigereye.chestcavity.registration.CCRecipes;
import net.tigereye.chestcavity.util.OrganUtil;

public class InfuseVenomGland extends CustomRecipe {
    public InfuseVenomGland(ResourceLocation id, CraftingBookCategory craftingRecipeCategory) {
        super(id, craftingRecipeCategory);
    }

    public boolean matches(CraftingContainer craftingInventory, Level world) {
        boolean foundVenomGland = false;
        boolean foundPotion = false;

        for(int i = 0; i < craftingInventory.m_39347_(); ++i) {
            for(int j = 0; j < craftingInventory.m_39346_(); ++j) {
                ItemStack itemStack = craftingInventory.m_8020_(i + j * craftingInventory.m_39347_());
                if (itemStack.m_41720_() == CCItems.VENOM_GLAND.get()) {
                    if (foundVenomGland) {
                        return false;
                    }

                    foundVenomGland = true;
                } else if (itemStack.m_41720_() == Items.f_42589_ || itemStack.m_41720_() == Items.f_42736_ || itemStack.m_41720_() == Items.f_42739_) {
                    if (foundPotion) {
                        return false;
                    }

                    foundPotion = true;
                }
            }
        }

        return foundVenomGland && foundPotion;
    }

    public ItemStack assemble(CraftingContainer craftingInventory, RegistryAccess registryAccess) {
        ItemStack venomGland = null;
        ItemStack potion = null;
        ItemStack output = null;

        for(int i = 0; i < craftingInventory.m_39347_(); ++i) {
            for(int j = 0; j < craftingInventory.m_39346_(); ++j) {
                ItemStack itemStack = craftingInventory.m_8020_(i + j * craftingInventory.m_39347_());
                if (itemStack.m_41720_() == CCItems.VENOM_GLAND.get()) {
                    if (venomGland != null) {
                        return ItemStack.f_41583_;
                    }

                    venomGland = itemStack;
                } else if (itemStack.m_41720_() == Items.f_42589_ || itemStack.m_41720_() == Items.f_42736_ || itemStack.m_41720_() == Items.f_42739_) {
                    if (potion != null) {
                        return ItemStack.f_41583_;
                    }

                    potion = itemStack;
                }
            }
        }

        if (venomGland != null && potion != null) {
            output = venomGland.m_41777_();
            OrganUtil.setStatusEffects(output, potion);
            return output;
        } else {
            return ItemStack.f_41583_;
        }
    }

    public boolean m_8004_(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<?> m_7707_() {
        return (RecipeSerializer)CCRecipes.INFUSE_VENOM_GLAND.get();
    }
}
