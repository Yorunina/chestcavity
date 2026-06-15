package net.tigereye.chestcavity.compat.jei.category;

import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.compat.jei.RenderHelper;
import net.tigereye.chestcavity.compat.jei.recipe.EntityOrgansRecipe;

import java.util.List;

public class EntityOrgansCategory implements IRecipeCategory<EntityOrgansRecipe> {
    public static final RecipeType<EntityOrgansRecipe> TYPE = RecipeType.create(ChestCavity.MODID, "entity_organs", EntityOrgansRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final IDrawable slotBackground;

    public EntityOrgansCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(160, 100);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(Items.SPAWNER));
        this.title = Component.translatable("jei.chestcavity.category.entity_organs");
        this.slotBackground = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<EntityOrgansRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, EntityOrgansRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> organItems = recipe.getOrganItems();
        int xOffset = 60;
        int yOffset = 20;
        int maxPerRow = 5;
        int slotWidth = 18;
        int slotHeight = 18;

        for (int i = 0; i < Math.min(organItems.size(), 15); i++) {
            int row = i / maxPerRow;
            int col = i % maxPerRow;
            int x = xOffset + col * slotWidth;
            int y = yOffset + row * slotHeight;

            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                    .addItemStack(organItems.get(i))
                    .setBackground(slotBackground, -1, -1);
        }
    }

    @Override
    public void draw(EntityOrgansRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        RenderSystem.enableBlend();

        Minecraft minecraft = Minecraft.getInstance();

        guiGraphics.drawString(minecraft.font, recipe.getEntityType().getDescription(), 20, 25, 0x404040, false);
        guiGraphics.drawString(minecraft.font, Component.translatable("jei.chestcavity.entity_organs.organs"), 60, 5, 0x404040, false);

        guiGraphics.fillGradient(45, 20, 47, 95, 0xFFC6C6C6, 0xFFC6C6C6);

        LivingEntity entity = RenderHelper.INSTANCE.getOrCreateEntity(recipe.getEntityType());
        if (entity != null) {
            RenderHelper.INSTANCE.renderEntity(guiGraphics, 29, 52, 10, 0, 0, entity);
        }

        RenderSystem.disableBlend();
    }

    @Override
    public List<Component> getTooltipStrings(EntityOrgansRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 20 && mouseX < 38 && mouseY >= 40 && mouseY < 58) {
            return List.of(recipe.getEntityType().getDescription());
        }
        return List.of();
    }
}