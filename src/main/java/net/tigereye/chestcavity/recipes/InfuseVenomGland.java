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

        for(int i = 0; i < craftingInventory.getWidth(); ++i) {
            for(int j = 0; j < craftingInventory.getHeight(); ++j) {
                ItemStack itemStack = craftingInventory.getItem(i + j * craftingInventory.getWidth());
                if (itemStack.getItem() == CCItems.VENOM_GLAND.get()) {
                    if (foundVenomGland) {
                        return false;
                    }

                    foundVenomGland = true;
                } else if (itemStack.getItem() == Items.POTION || itemStack.getItem() == Items.SPLASH_POTION || itemStack.getItem() == Items.LINGERING_POTION) {
                    if (foundPotion) {
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

        for(int i = 0; i < craftingInventory.getWidth(); ++i) {
            for(int j = 0; j < craftingInventory.getHeight(); ++j) {
                ItemStack itemStack = craftingInventory.getItem(i + j * craftingInventory.getWidth());
                if (itemStack.getItem() == CCItems.VENOM_GLAND.get()) {
                    if (venomGland != null) {
                        return ItemStack.EMPTY;
                    }

                    venomGland = itemStack;
                } else if (itemStack.getItem() == Items.POTION || itemStack.getItem() == Items.SPLASH_POTION || itemStack.getItem() == Items.LINGERING_POTION) {
                    if (potion != null) {
                        return ItemStack.EMPTY;
                    }

                    potion = itemStack;
                }
            }
        }

        if (venomGland != null && potion != null) {
            output = venomGland.copy();
            OrganUtil.setStatusEffects(output, potion);
            return output;
        } else {
            return ItemStack.EMPTY;
        }
    }


    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer)CCRecipes.INFUSE_VENOM_GLAND.get();
    }
}
