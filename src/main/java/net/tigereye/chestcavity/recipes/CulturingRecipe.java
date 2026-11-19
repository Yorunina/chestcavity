package net.tigereye.chestcavity.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.registration.CCRecipeTypes;
import org.jetbrains.annotations.Nullable;

public class CulturingRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final FluidStack fluid;
    private final Ingredient ingredient;
    private final int ticks;
    private final ItemStack output;

    public CulturingRecipe(ResourceLocation id, FluidStack fluid, Ingredient ingredient, int ticks, ItemStack output) {
        this.id = id;
        this.fluid = fluid;
        this.ingredient = ingredient;
        this.ticks = ticks;
        this.output = output;
    }

    public FluidStack getFluid() {
        return this.fluid;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public int getTicks() {
        return this.ticks;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public boolean matches(FluidStack tankFluid, ItemStack organ) {
        if (organ.isEmpty() || tankFluid.isEmpty()) {
            return false;
        }
        if (!this.ingredient.test(organ)) {
            return false;
        }
        return tankFluid.getFluid().isSame(this.fluid.getFluid()) && tankFluid.getAmount() >= this.fluid.getAmount();
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.ingredient);
        return ingredients;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CCRecipeTypes.CULTURING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CCRecipeTypes.CULTURING.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<CulturingRecipe> {
        @Override
        public CulturingRecipe fromJson(ResourceLocation id, JsonObject json) {
            FluidStack fluid = readFluid(GsonHelper.getAsJsonObject(json, "fluid"));
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            int ticks = GsonHelper.getAsInt(json, "ticks", 200);
            ItemStack output = net.minecraftforge.common.crafting.CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "output"), true);
            return new CulturingRecipe(id, fluid, ingredient, ticks, output);
        }

        @Override
        public @Nullable CulturingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ResourceLocation fluidId = buf.readResourceLocation();
            int amount = buf.readVarInt();
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            int ticks = buf.readVarInt();
            ItemStack output = buf.readItem();
            return new CulturingRecipe(id, new FluidStack(ForgeRegistries.FLUIDS.getValue(fluidId), amount), ingredient, ticks, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CulturingRecipe recipe) {
            buf.writeResourceLocation(ForgeRegistries.FLUIDS.getKey(recipe.fluid.getFluid()));
            buf.writeVarInt(recipe.fluid.getAmount());
            recipe.ingredient.toNetwork(buf);
            buf.writeVarInt(recipe.ticks);
            buf.writeItem(recipe.output);
        }

        private static FluidStack readFluid(JsonObject json) {
            ResourceLocation fluidId = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
            int amount = GsonHelper.getAsInt(json, "amount", 1000);
            return new FluidStack(ForgeRegistries.FLUIDS.getValue(fluidId), amount);
        }
    }
}
