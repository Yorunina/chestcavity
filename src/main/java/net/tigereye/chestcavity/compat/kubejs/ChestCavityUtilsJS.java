package net.tigereye.chestcavity.compat.kubejs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;

import java.util.ArrayList;
import java.util.List;

public class ChestCavityUtilsJS {
    public static List<ItemStack> setInventoryTypeData(ItemStack stack, ResourceLocation inventoryType) {
        List<ItemStack> resList = new ArrayList<>();
        InventoryTypeData inventoryTypeData = InventoryTypeManager.getInventoryTypeData(inventoryType);
        CompoundTag itemNbt = stack.getOrCreateTag();
        if (!itemNbt.contains("Inventory")) {
            itemNbt.put("Inventory", new ItemStackHandler(inventoryTypeData.getSlotSize()).serializeNBT());
        }
        ItemStackHandler itemInventory = new ItemStackHandler();
        itemInventory.deserializeNBT(itemNbt.getCompound("Inventory"));
        ItemStackHandler newItemInventory = new ItemStackHandler(inventoryTypeData.getSlotSize());
        for (int i = 0; i < itemInventory.getSlots(); i++) {
            if (i >= newItemInventory.getSlots()) {
                resList.add(itemInventory.getStackInSlot(i));
                continue;
            }
            newItemInventory.setStackInSlot(i, itemInventory.getStackInSlot(i));
        }
        itemNbt.put("Inventory", newItemInventory.serializeNBT());
        itemNbt.putString("InventoryType", inventoryTypeData.getId().toString());
        return resList;
    }
}
