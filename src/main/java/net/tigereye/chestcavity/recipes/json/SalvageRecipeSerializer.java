package net.tigereye.chestcavity.recipes.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.recipes.SalvageRecipe;

public class SalvageRecipeSerializer implements RecipeSerializer<SalvageRecipe> {
    public SalvageRecipeSerializer() {
    }

    public SalvageRecipe fromJson(ResourceLocation id, JsonObject json) {
        SalvageRecipeJsonFormat recipeJson = (SalvageRecipeJsonFormat)(new Gson()).fromJson(json, SalvageRecipeJsonFormat.class);
        if (recipeJson.ingredient != null && recipeJson.result != null) {
            if (recipeJson.required == 0) {
                recipeJson.required = 1;
            }

            if (recipeJson.count == 0) {
                recipeJson.count = 1;
            }

            Ingredient input = Ingredient.fromJson(recipeJson.ingredient);
            Item outputItem = (Item)Optional.ofNullable((Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation(recipeJson.result))).orElseThrow(() -> {
                return new JsonSyntaxException("No such item " + recipeJson.result);
            });
            ItemStack output = new ItemStack(outputItem, recipeJson.count);
            return new SalvageRecipe(input, recipeJson.required, CraftingBookCategory.MISC, output, id);
        } else {
            throw new JsonSyntaxException("A required attribute is missing!");
        }
    }

    public SalvageRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        Ingredient input = Ingredient.fromNetwork(buf);
        int required = buf.readInt();
        ItemStack output = buf.readItem();
        return new SalvageRecipe(input, required, CraftingBookCategory.MISC, output, id);
    }

    public void toNetwork(FriendlyByteBuf buf, SalvageRecipe recipe) {
        recipe.getInput().toNetwork(buf);
        buf.writeInt(recipe.getRequired());
        buf.writeItem(recipe.outputStack.copy());
    }
}
