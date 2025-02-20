package net.tigereye.chestcavity.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;

import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.DEFAULT_TEXTURE;

public class ChestCavityItemScreen extends AbstractContainerScreen<AbstractContainerMenu> {

    public ChestCavityItemScreen(AbstractContainerMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        ResourceLocation backgroundTexture = DEFAULT_TEXTURE;
        if (this.minecraft != null && this.minecraft.player != null) {
            Player player = this.minecraft.player;
            ItemStack chestCavityItem = player.getMainHandItem();
            if (chestCavityItem.isEmpty()) {
                chestCavityItem = player.getOffhandItem();
            }
            if (!chestCavityItem.isEmpty()) {
                CompoundTag itemNbt = chestCavityItem.getOrCreateTag();
                if (itemNbt.contains("InventoryType")) {
                    InventoryTypeData inventoryTypeData = InventoryTypeManager.getInventoryTypeData(new ResourceLocation(itemNbt.getString("InventoryType")));
                    backgroundTexture = inventoryTypeData.getBackgroundTexture();
                }
            }
        }
        context.blit(backgroundTexture, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }
}
