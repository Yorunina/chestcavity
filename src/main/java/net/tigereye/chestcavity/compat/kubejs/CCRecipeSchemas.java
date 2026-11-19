package net.tigereye.chestcavity.compat.kubejs;

import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.ChestCavity;

public class CCRecipeSchemas {
    private static final RecipeKey<OutputItem> OUTPUT = ItemComponents.OUTPUT.key("output");
    private static final RecipeKey<InputFluid> FLUID = FluidComponents.INPUT.key("fluid");
    private static final RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    private static final RecipeKey<Integer> TICKS = NumberComponent.ANY_INT.key("ticks").optional(200);

    public static void register(RegisterRecipeSchemasEvent event) {
        event.register(new ResourceLocation(ChestCavity.MODID, "culturing"),
                new RecipeSchema(OUTPUT, FLUID, INGREDIENT, TICKS)
                        .constructor(OUTPUT, FLUID, INGREDIENT, TICKS));
    }
}
