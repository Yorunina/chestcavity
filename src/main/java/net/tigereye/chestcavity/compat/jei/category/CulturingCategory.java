package net.tigereye.chestcavity.compat.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.blocks.CultureVatBlockEntity;
import net.tigereye.chestcavity.recipes.CulturingRecipe;
import net.tigereye.chestcavity.registration.CCBlocks;

public class CulturingCategory implements IRecipeCategory<CulturingRecipe> {
    public static final RecipeType<CulturingRecipe> TYPE =
            RecipeType.create(ChestCavity.MODID, "culturing", CulturingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final IDrawable slotBackground;

    public CulturingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(100, 60);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(CCBlocks.CULTURE_VAT_ITEM.get()));
        this.title = Component.translatable("jei.chestcavity.category.culturing");
        this.slotBackground = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<CulturingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CulturingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 2)
                .setFluidRenderer(CultureVatBlockEntity.CAPACITY, true, 16, 52)
                .addFluidStack(recipe.getFluid().getFluid(), recipe.getFluid().getAmount())
                .setBackground(this.slotBackground, -1, -1);
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 22)
                .addIngredients(recipe.getIngredient())
                .setBackground(this.slotBackground, -1, -1);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 22)
                .addItemStack(recipe.getOutput())
                .setBackground(this.slotBackground, -1, -1);
    }

    @Override
    public void draw(CulturingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.drawString(Minecraft.getInstance().font, "->", 52, 26, 0x404040, false);
        int seconds = Math.max(1, recipe.getTicks() / 20);
        guiGraphics.drawString(Minecraft.getInstance().font, seconds + "s", 48, 44, 0x808080, false);
    }
}
