package net.tigereye.chestcavity.compat.jei.category;

import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.compat.jei.RenderHelper;
import net.tigereye.chestcavity.compat.jei.recipe.OrganSourceRecipe;

import java.util.ArrayList;
import java.util.List;

public class OrganSourceCategory implements IRecipeCategory<OrganSourceRecipe> {
    public static final RecipeType<OrganSourceRecipe> TYPE = RecipeType.create(ChestCavity.MODID, "organ_source", OrganSourceRecipe.class);

    private final IDrawable icon;
    private final Component title;
    private final IDrawable slotBackground;

    public OrganSourceCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(Items.HEART_OF_THE_SEA));
        this.title = Component.translatable("jei.chestcavity.category.organ_source");
        this.slotBackground = guiHelper.getSlotDrawable();
    }

    @Override
    public RecipeType<OrganSourceRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public int getWidth() {
        return 160;
    }

    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, OrganSourceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 20, 40)
                .addItemStack(recipe.getOrganItem())
                .setBackground(slotBackground, -1, -1);
    }

    @Override
    public void draw(OrganSourceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        RenderSystem.enableBlend();

        Minecraft minecraft = Minecraft.getInstance();

        guiGraphics.drawString(minecraft.font, Component.translatable("jei.chestcavity.organ_source.organ"), 20, 25, 0x404040, false);
        guiGraphics.drawString(minecraft.font, Component.translatable("jei.chestcavity.organ_source.sources"), 60, 5, 0x404040, false);

        guiGraphics.fillGradient(45, 20, 47, 95, 0xFFC6C6C6, 0xFFC6C6C6);

        List<EntityType<?>> entities = recipe.getSourceEntities();
        int xOffset = 60;
        int yOffset = 20;
        int maxPerRow = 5;
        int slotWidth = 18;
        int slotHeight = 18;

        for (int i = 0; i < Math.min(entities.size(), 15); i++) {
            int row = i / maxPerRow;
            int col = i % maxPerRow;
            int x = xOffset + col * slotWidth;
            int y = yOffset + row * slotHeight;

            EntityType<?> entityType = entities.get(i);
            LivingEntity entity = RenderHelper.INSTANCE.getOrCreateEntity(entityType);
            if (entity != null) {
                RenderHelper.INSTANCE.renderEntity(guiGraphics, x + 9, y + 12, 10, 0, 0, entity);
            }
        }

        RenderSystem.disableBlend();
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, OrganSourceRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> tooltips = new ArrayList<>();

        List<EntityType<?>> entities = recipe.getSourceEntities();
        int xOffset = 60;
        int yOffset = 20;
        int maxPerRow = 5;
        int slotWidth = 18;
        int slotHeight = 18;

        for (int i = 0; i < Math.min(entities.size(), 15); i++) {
            int row = i / maxPerRow;
            int col = i % maxPerRow;
            int x = xOffset + col * slotWidth;
            int y = yOffset + row * slotHeight;

            double relMouseX = mouseX - x;
            double relMouseY = mouseY - y;

            if (relMouseX >= 0 && relMouseX < slotWidth && relMouseY >= 0 && relMouseY < slotHeight) {
                EntityType<?> entityType = entities.get(i);
                tooltips.add(entityType.getDescription());
            }
        }
        tooltip.addAll(tooltips);
    }
}
