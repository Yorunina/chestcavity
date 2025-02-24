package net.tigereye.chestcavity.ui;

import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.tigereye.chestcavity.chestcavities.json.ccInvType.ChestCavitySlotDefinition.DEFAULT_SLOT_TYPE;
import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.DEFAULT_INVENTORY_TYPE_STRING;
import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.DEFAULT_TEXTURE;

public class ChestCavityItemScreen extends AbstractContainerScreen<AbstractContainerMenu> {

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
        InventoryTypeData inventoryTypeData = getInventoryTypeData();
        ResourceLocation backgroundTexture  = inventoryTypeData.getBackgroundTexture();
        int imageWidth = inventoryTypeData.getBackgroundSize().getX();
        int imageHeight = inventoryTypeData.getBackgroundSize().getY();
        int x = (this.width - imageWidth) / 2;
        int y = (this.height - imageHeight) / 2;

        context.blit(backgroundTexture, x, y, 0, 0, imageWidth, imageHeight);
    }


    @Override
    protected boolean hasClickedOutside(double pMouseX, double pMouseY, int pGuiLeft, int pGuiTop, int pMouseButton) {
        InventoryTypeData inventoryTypeData = getInventoryTypeData();
        int imageWidth = inventoryTypeData.getBackgroundSize().getX();
        int imageHeight = inventoryTypeData.getBackgroundSize().getY();
        return pMouseX < (double)pGuiLeft || pMouseY < (double)pGuiTop || pMouseX >= (double)(pGuiLeft + imageWidth) || pMouseY >= (double)(pGuiTop + imageHeight);
    }
    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        InventoryTypeData inventoryTypeData = getInventoryTypeData();
        int titleX = this.titleLabelX + inventoryTypeData.getTitlePosition().getX();
        int titleY = this.titleLabelY + inventoryTypeData.getTitlePosition().getY();
        int inventoryLabelX = this.inventoryLabelX + inventoryTypeData.getInventoryLabelPosition().getX();
        int inventoryLabelY = this.inventoryLabelY + inventoryTypeData.getInventoryLabelPosition().getY();
        pGuiGraphics.drawString(this.font, this.title, titleX, titleY, 4210752, false);
        pGuiGraphics.drawString(this.font, this.playerInventoryTitle, inventoryLabelX, inventoryLabelY, 4210752, false);
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
            InventoryTypeData inventoryTypeData = getInventoryTypeData();
            String slotType = inventoryTypeData.getSlotType(this.hoveredSlot.getSlotIndex());
            if (Objects.equals(slotType, DEFAULT_SLOT_TYPE)) return;
            List<Component> slotTypeTooltips = new ArrayList<>();
            slotTypeTooltips.add(Component.translatable(String.format("chestcavity.slot_type.%s.name", slotType)));
            slotTypeTooltips.add(Component.translatable(String.format("chestcavity.slot_type.%s.desc", slotType)));
            pGuiGraphics.renderTooltip(this.font, slotTypeTooltips, Optional.empty(), ItemStack.EMPTY, pX, pY);
        }
    }

    @Override
    protected void init() {
        InventoryTypeData inventoryTypeData = getInventoryTypeData();
        int imageWidth = inventoryTypeData.getBackgroundSize().getX();
        int imageHeight = inventoryTypeData.getBackgroundSize().getY();
        this.leftPos = (this.width - imageWidth) / 2;
        this.topPos = (this.height - imageHeight) / 2;
        this.titleLabelX = (imageWidth - this.font.width(this.title)) / 2;
    }
}
