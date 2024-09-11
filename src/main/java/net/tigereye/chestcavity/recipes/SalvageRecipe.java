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
        return NonNullList.m_122780_(this.required, this.input);
    }

    public boolean matches(CraftingContainer inv, Level world) {
        int count = 0;

        for(int i = 0; i < inv.m_6643_(); ++i) {
            ItemStack target = inv.m_8020_(i);
            if (target != null && target != ItemStack.f_41583_ && target.m_41720_() != Items.f_41852_) {
                if (!this.input.test(inv.m_8020_(i))) {
                    return false;
                }

                ++count;
            }
        }

        return count > 0 && count % this.required == 0;
    }

    public ItemStack assemble(CraftingContainer inv, RegistryAccess registryAccess) {
        int count = 0;

        for(int i = 0; i < inv.m_6643_(); ++i) {
            ItemStack target = inv.m_8020_(i);
            if (target != null && target != ItemStack.f_41583_ && target.m_41720_() != Items.f_41852_) {
                if (!this.input.test(inv.m_8020_(i))) {
                    return ItemStack.f_41583_;
                }

                ++count;
            }
        }

        if (count != 0 && count % this.required == 0) {
            count = count / this.required * this.outputStack.m_41613_();
            if (count > this.outputStack.m_41741_()) {
                return ItemStack.f_41583_;
            } else {
                ItemStack out = this.m_8043_(registryAccess);
                out.m_41764_(count);
                return out;
            }
        } else {
            return ItemStack.f_41583_;
        }
    }

    public boolean m_8004_(int width, int height) {
        return true;
    }

    public ItemStack m_8043_(RegistryAccess registryAccess) {
        return this.outputStack.m_41777_();
    }

    public ResourceLocation m_6423_() {
        return this.id;
    }

    public RecipeSerializer<?> m_7707_() {
        return (RecipeSerializer)CCRecipes.SALVAGE_RECIPE_SERIALIZER.get();
    }

    public CraftingBookCategory m_245232_() {
        return this.category;
    }
}
