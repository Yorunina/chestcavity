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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChestCavityItemScreen extends AbstractContainerScreen<AbstractContainerMenu> {
    private ResourceLocation backgroundTexture;
    private InventoryTypeData inventoryTypeData;
    public ChestCavityItemScreen(AbstractContainerMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    public InventoryTypeData getInventoryTypeData() {
        InventoryTypeData inventoryTypeData = InventoryTypeManager.getDefaultInventoryTypeData();
        if (this.minecraft != null && this.minecraft.player != null) {
            Player player = this.minecraft.player;
            ItemStack chestCavityItem = player.getMainHandItem();
            if (chestCavityItem.isEmpty()) {
                chestCavityItem = player.getOffhandItem();
            }
            if (!chestCavityItem.isEmpty()) {
                CompoundTag itemNbt = chestCavityItem.getOrCreateTag();
                if (itemNbt.contains("InventoryType")) {
                    inventoryTypeData = InventoryTypeManager.getInventoryTypeData(new ResourceLocation(itemNbt.getString("InventoryType")));
                }
            }
        }
        return inventoryTypeData;
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        context.blit(this.backgroundTexture, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
            int slotIndex = this.hoveredSlot.index;
            if (slotIndex < 0 || slotIndex >= inventoryTypeData.getSlotSize()) return;
            String slotType = this.inventoryTypeData.getSlotType(slotIndex);
            List<Component> slotTypeTooltips = new ArrayList<>();
            slotTypeTooltips.add(Component.translatable(String.format("slot_type.chestcavity.%s.name", slotType), slotIndex + 1));
            slotTypeTooltips.add(Component.translatable(String.format("slot_type.chestcavity.%s.desc", slotType)));
            pGuiGraphics.renderTooltip(this.font, slotTypeTooltips, Optional.empty(), ItemStack.EMPTY, pX, pY);
        }
    }

    @Override
    protected void init() {
        InventoryTypeData inventoryTypeData = getInventoryTypeData();
        this.inventoryTypeData = inventoryTypeData;
        this.imageWidth = inventoryTypeData.getBackgroundSize().getX();
        this.imageHeight = inventoryTypeData.getBackgroundSize().getY();
        this.leftPos = (this.width - imageWidth) / 2;
        this.topPos = (this.height - imageHeight) / 2;
        this.titleLabelX = (imageWidth - this.font.width(this.title)) / 2 + inventoryTypeData.getTitlePosition().getX();
        this.titleLabelY = 6 + inventoryTypeData.getTitlePosition().getY();
        this.inventoryLabelX = 8 + inventoryTypeData.getInventoryLabelPosition().getX();
        this.inventoryLabelY = this.imageHeight - 94 + inventoryTypeData.getInventoryLabelPosition().getY();
        this.backgroundTexture = inventoryTypeData.getBackgroundTexture();
    }
}
