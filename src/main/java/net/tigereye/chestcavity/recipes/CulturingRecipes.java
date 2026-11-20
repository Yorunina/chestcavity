package net.tigereye.chestcavity.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.fluids.FluidStack;
import net.tigereye.chestcavity.registration.CCRecipeTypes;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class CulturingRecipes {
    private static final Map<String, CulturingRecipe> CACHE = new HashMap<>();

    private CulturingRecipes() {
    }

    public static @Nullable CulturingRecipe find(Level level, FluidStack fluid, ItemStack organ) {
        if (level == null || fluid.isEmpty() || organ.isEmpty()) {
            return null;
        }
        String key = organ.getItem() + "|" + (organ.hasTag() ? organ.getTag().hashCode() : 0) + "|"
                + net.minecraftforge.registries.ForgeRegistries.FLUIDS.getKey(fluid.getFluid());
        CulturingRecipe recipe = CACHE.computeIfAbsent(key, ignored -> lookup(level, fluid, organ));
        return recipe != null && recipe.matches(fluid, organ) ? recipe : null;
    }

    private static @Nullable CulturingRecipe lookup(Level level, FluidStack fluid, ItemStack organ) {
        for (CulturingRecipe recipe : level.getRecipeManager().getAllRecipesFor(CCRecipeTypes.CULTURING.get())) {
            if (recipe.getIngredient().test(organ) && recipe.getFluid().getFluid().isSame(fluid.getFluid())) {
                return recipe;
            }
        }
        return null;
    }

    public static void onDatapackSync(OnDatapackSyncEvent event) {
        CACHE.clear();
    }
}
