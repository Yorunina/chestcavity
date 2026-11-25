package net.tigereye.chestcavity.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.recipes.CulturingRecipe;

public class CCRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, ChestCavity.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ChestCavity.MODID);

    public static final RegistryObject<RecipeType<CulturingRecipe>> CULTURING = RECIPE_TYPES.register("culturing",
            () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return ChestCavity.MODID + ":culturing";
                }
            });

    public static final RegistryObject<RecipeSerializer<CulturingRecipe>> CULTURING_SERIALIZER =
            RECIPE_SERIALIZERS.register("culturing", CulturingRecipe.Serializer::new);
}
