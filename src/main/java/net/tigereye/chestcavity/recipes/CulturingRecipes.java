package net.tigereye.chestcavity.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.fluids.FluidStack;
import net.tigereye.chestcavity.registration.CCRecipeTypes;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CulturingRecipes {
    private static final Map<String, Optional<CulturingRecipe>> CACHE = new HashMap<>();

    private CulturingRecipes() {
    }

    public static @Nullable CulturingRecipe find(Level level, FluidStack fluid, ItemStack organ) {
        if (level == null || fluid.isEmpty() || organ.isEmpty()) {
            return null;
        }
        String key = organ.getItem() + "|" + net.minecraftforge.registries.ForgeRegistries.FLUIDS.getKey(fluid.getFluid());
        return CACHE.computeIfAbsent(key, ignored -> lookup(level, fluid, organ)).orElse(null);
    }

    private static Optional<CulturingRecipe> lookup(Level level, FluidStack fluid, ItemStack organ) {
        for (CulturingRecipe recipe : level.getRecipeManager().getAllRecipesFor(CCRecipeTypes.CULTURING.get())) {
            if (recipe.matches(fluid, organ)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    public static void onDatapackSync(OnDatapackSyncEvent event) {
        CACHE.clear();
    }
}
