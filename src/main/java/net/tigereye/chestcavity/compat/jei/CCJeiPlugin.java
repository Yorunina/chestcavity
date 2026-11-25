package net.tigereye.chestcavity.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.compat.jei.category.CulturingCategory;
import net.tigereye.chestcavity.compat.jei.category.EntityOrgansCategory;
import net.tigereye.chestcavity.compat.jei.category.OrganSourceCategory;
import net.tigereye.chestcavity.compat.jei.recipe.EntityOrgansRecipe;
import net.tigereye.chestcavity.compat.jei.recipe.OrganSourceRecipe;
import net.tigereye.chestcavity.recipes.CulturingRecipe;
import net.tigereye.chestcavity.registration.CCRecipeTypes;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class CCJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(ChestCavity.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new OrganSourceCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new EntityOrgansCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CulturingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<OrganSourceRecipe> organSourceRecipes = buildOrganSourceRecipes();
        List<EntityOrgansRecipe> entityOrgansRecipes = buildEntityOrgansRecipes();

        registration.addRecipes(OrganSourceCategory.TYPE, organSourceRecipes);
        registration.addRecipes(EntityOrgansCategory.TYPE, entityOrgansRecipes);
        registration.addRecipes(CulturingCategory.TYPE, buildCulturingRecipes());
    }

    private List<CulturingRecipe> buildCulturingRecipes() {
        if (Minecraft.getInstance().level == null) {
            return List.of();
        }
        return new ArrayList<>(Minecraft.getInstance().level.getRecipeManager()
                .getAllRecipesFor(CCRecipeTypes.CULTURING.get()));
    }

    private List<OrganSourceRecipe> buildOrganSourceRecipes() {
        return JeiRecipeBuilder.buildOrganSourceRecipes();
    }

    private List<EntityOrgansRecipe> buildEntityOrgansRecipes() {
        return JeiRecipeBuilder.buildEntityOrgansRecipes();
    }
}