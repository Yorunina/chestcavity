package net.tigereye.chestcavity.ui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;

public class ChestCavityScreenHandler extends AbstractContainerMenu {
    private final ChestCavityInventory inventory;
    private final int size;
    private final int rows;

    private static ChestCavityInventory getOrCreateChestCavityInventory(Inventory playerInventory) {
        return new ChestCavityInventory();
    }

    public ChestCavityScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, getOrCreateChestCavityInventory(playerInventory));
    }

    public ChestCavityScreenHandler(int syncId, Inventory playerInventory, ChestCavityInventory inventory) {
        super((MenuType)ChestCavity.CHEST_CAVITY_SCREEN_HANDLER.get(), syncId);
        this.size = inventory.getContainerSize();
        this.inventory = inventory;
        this.rows = (this.size - 1) / 9 + 1;
        inventory.startOpen(playerInventory.player);
        int i = (this.rows - 4) * 18;

        int n;
        int m;
        for(n = 0; n < this.rows; ++n) {
            for(m = 0; m < 9 && n * 9 + m < this.size; ++m) {
                this.addSlot(new Slot(inventory, m + n * 9, 8 + m * 18, 18 + n * 18));
            }
        }

        for(n = 0; n < 3; ++n) {
            for(m = 0; m < 9; ++m) {
                this.addSlot(new Slot(playerInventory, m + n * 9 + 9, 8 + m * 18, 102 + n * 18 + i));
            }
        }

        for(n = 0; n < 9; ++n) {
            this.addSlot(new Slot(playerInventory, n, 8 + n * 18, 160 + i));
        }

    }

    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(invSlot);
        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    public boolean stillValid(Player player) {
        return this.inventory.m_6542_(player);
    }
}
