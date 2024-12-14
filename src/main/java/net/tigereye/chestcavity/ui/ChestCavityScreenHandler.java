package net.tigereye.chestcavity.ui;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.SlotPosition;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.util.ChestCavityUtil;

import java.util.List;
import java.util.Optional;

import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.DEFAULT_INVENTORY_TYPE_DATA;
import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.GeneratedInventoryTypeData;

public class ChestCavityScreenHandler extends AbstractContainerMenu {
    private final ChestCavityInventory inventory;
    private final int size;

    private static ChestCavityEntity getChestCavityEntity(Inventory playerInventory) {
        ChestCavityEntity playerCCEntity = (ChestCavityEntity) playerInventory.player;
        ChestCavityInstance playerCC = playerCCEntity.getChestCavityInstance();
        ChestCavityInstance targetCCI = playerCC.ccBeingOpened;

        if (targetCCI != null) {
            Optional<ChestCavityEntity> optional = ChestCavityEntity.of(targetCCI.owner);
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return playerCCEntity;
    }

    public ChestCavityScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, getChestCavityEntity(playerInventory));
    }

    public ChestCavityScreenHandler(int syncId, Inventory playerInventory, ChestCavityEntity chestCavityEntity) {
        super(ChestCavity.CHEST_CAVITY_SCREEN_HANDLER.get(), syncId);

        InventoryTypeData inventoryTypeData = chestCavityEntity.getInventoryTypeData();
        int slotSize = inventoryTypeData.getSlotSize();
        List<SlotPosition> slotPositionList = inventoryTypeData.getSlotPosition();

        ChestCavityInventory inventory = ChestCavityUtil.openChestCavity(chestCavityEntity.getChestCavityInstance());
        this.size = slotSize;
        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);
        SlotPosition playerInventoryPosition = inventoryTypeData.getPlayerInventoryPosition();
        int n;
        int m;
        // 组装自定义胸腔界面
        for (int j = 0; j < this.size; ++j) {
            this.addSlot(new Slot(inventory, j, slotPositionList.get(j).getX(), slotPositionList.get(j).getY()));
        }
        // 组装玩家背包
        for (n = 0; n < 3; ++n) {
            for (m = 0; m < 9; ++m) {
                this.addSlot(new Slot(playerInventory, m + n * 9 + 9, 8 + m * 18 + playerInventoryPosition.getX(), 84 + n * 18 + playerInventoryPosition.getY()));
            }
        }
        // 组装玩家快捷栏
        for (n = 0; n < 9; ++n) {
            this.addSlot(new Slot(playerInventory, n, 8 + n * 18 + playerInventoryPosition.getX(), 142 + playerInventoryPosition.getY()));
        }

    }

    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
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
        return this.inventory.stillValid(player);
    }
}
