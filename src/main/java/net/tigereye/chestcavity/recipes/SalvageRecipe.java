package net.tigereye.chestcavity.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.registration.CCRecipes;

public class SalvageRecipe implements CraftingRecipe {
    private final Ingredient input;
    private int required;
    private final CraftingBookCategory category;
    public final ItemStack outputStack;
    private final ResourceLocation id;

    public SalvageRecipe(Ingredient input, int required, CraftingBookCategory category, ItemStack outputStack, ResourceLocation id) {
        this.input = input;
        this.required = required;
        this.category = category;
        this.outputStack = outputStack;
        this.id = id;
    }

    public Ingredient getInput() {
        return this.input;
    }

    public int getRequired() {
        return this.required;
    }

    public NonNullList<Ingredient> m_7527_() {
        return NonNullList.withSize(this.required, this.input);
    }

    public boolean matches(CraftingContainer inv, Level world) {
        int count = 0;

        for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack target = inv.getItem(i);
            if (target != null && target != ItemStack.EMPTY && target.getItem() != Items.AIR) {
                if (!this.input.test(inv.getItem(i))) {
                    return false;
                }

                ++count;
            }
        }

        return count > 0 && count % this.required == 0;
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        int count = 0;

        for(int i = 0; i < inv.getContainerSize(); ++i) {
            ItemStack target = inv.getItem(i);
                if (target != null && target != ItemStack.EMPTY && target.getItem() != Items.AIR) {
                if (!this.input.test(inv.getItem(i))) {
                    return ItemStack.EMPTY;
                }

                ++count;
            }
        }

        if (count != 0 && count % this.required == 0) {
            count = count / this.required * this.outputStack.getCount();
            if (count > this.outputStack.getMaxStackSize()) {
                return ItemStack.EMPTY;
            } else {
                ItemStack out = this.getResultItem(registryAccess);
                out.setCount(count);
                return out;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }


    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.outputStack.copy();
    }

    public ResourceLocation getId() {
        return this.id;
    }
    public RecipeSerializer<?> getSerializer() {
        return (RecipeSerializer)CCRecipes.SALVAGE_RECIPE_SERIALIZER.get();
    }

    public CraftingBookCategory category() {
        return this.category;
    }

}
