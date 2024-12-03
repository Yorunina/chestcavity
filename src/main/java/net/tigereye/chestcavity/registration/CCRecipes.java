package net.tigereye.chestcavity.registration;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.recipes.InfuseVenomGland;
import net.tigereye.chestcavity.recipes.SalvageRecipe;
import net.tigereye.chestcavity.recipes.json.SalvageRecipeSerializer;

public class CCRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES;
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS;
    public static final DeferredRegister<RecipeSerializer<?>> MCRECIPE_SERIALIZERS;
    public static final RegistryObject<SimpleCraftingRecipeSerializer<InfuseVenomGland>> INFUSE_VENOM_GLAND;
    public static final ResourceLocation SALVAGE_RECIPE_ID;
    public static final RegistryObject<RecipeType<SalvageRecipe>> SALVAGE_RECIPE_TYPE;
    public static RegistryObject<SalvageRecipeSerializer> SALVAGE_RECIPE_SERIALIZER;

    public CCRecipes() {
    }

    static {
        RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, "chestcavity");
        RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "chestcavity");
        MCRECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "minecraft");
        INFUSE_VENOM_GLAND = MCRECIPE_SERIALIZERS.register("crafting_special_infuse_venom_gland", () -> {
            return new SimpleCraftingRecipeSerializer(InfuseVenomGland::new);
        });
        SALVAGE_RECIPE_ID = new ResourceLocation("chestcavity", "crafting_salvage");
        SALVAGE_RECIPE_TYPE = RECIPE_TYPES.register(SALVAGE_RECIPE_ID.getPath(), () -> {
            return new RecipeType<SalvageRecipe>() {
                public String toString() {
                    return CCRecipes.SALVAGE_RECIPE_ID.toString();
                }
            };
        });
        SALVAGE_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(SALVAGE_RECIPE_ID.getPath(), SalvageRecipeSerializer::new);
    }
}
