package net.tigereye.chestcavity.ui;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;

public class ChestCavityItemScreen extends ChestCavityScreen {

    public ChestCavityItemScreen(AbstractContainerMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
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
}
