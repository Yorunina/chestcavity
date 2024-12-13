package net.tigereye.chestcavity.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;

import java.util.Optional;

public class ChestCavityScreen extends AbstractContainerScreen<AbstractContainerMenu> {
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/shulker_box.png");

    public ChestCavityScreen(AbstractContainerMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        ResourceLocation backgroundTexture = DEFAULT_TEXTURE;
        if (this.minecraft != null) {
            Optional<ChestCavityEntity> optional = ChestCavityEntity.of(this.minecraft.player);
            if (optional.isPresent()) {
                ChestCavityEntity chestCavityEntity = optional.get();
                backgroundTexture = chestCavityEntity.getInventoryType();
            }
            context.blit(backgroundTexture, x, y, 0, 0, this.imageWidth, this.imageHeight);
        }
    }


    public void renderBg(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    protected void renderBg() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }
}
