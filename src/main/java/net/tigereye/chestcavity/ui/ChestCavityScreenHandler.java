package net.tigereye.chestcavity.ui;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.ChestCavitySlotDefinition;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.SlotDefinition;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.util.ChestCavityUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChestCavityScreenHandler extends AbstractContainerMenu {
    private ChestCavityInventory inventory;
    private ChestCavityEntity targetEntity;

    // 为MenuType注册保留的双参数构造函数
    public ChestCavityScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, null);
    }

    public ChestCavityScreenHandler(int syncId, Inventory playerInventory, ChestCavityEntity targetEntity) {
        super(ChestCavity.CHEST_CAVITY_SCREEN_HANDLER.get(), syncId);
        this.targetEntity = targetEntity;
        Player player = playerInventory.player;
        Level level = player.level();

        // 如果targetEntity为null（MenuType注册时），使用玩家作为默认值
        ChestCavityEntity actualTargetEntity = targetEntity != null ? targetEntity : ChestCavityEntity.of(player).get();

        InventoryTypeData inventoryTypeData = actualTargetEntity.getInventoryTypeData();
        List<ChestCavitySlotDefinition> slotDefinitionList = inventoryTypeData.getSlotDefinitions();
        if (level.isClientSide()) {
            this.inventory = new ChestCavityInventory(inventoryTypeData.getSlotSize());
        } else {
            this.inventory = ChestCavityUtil.openChestCavity(actualTargetEntity.getChestCavityInstance());
        }
        SlotDefinition playerInventoryPosition = inventoryTypeData.getPlayerInventoryPosition();
        int n;
        int m;

        for (int j = 0; j < this.inventory.getContainerSize(); j++) {
            this.addSlot(new Slot(this.inventory, j, slotDefinitionList.get(j).getX(), slotDefinitionList.get(j).getY()));
        }

        for (n = 0; n < 3; n++) {
            for (m = 0; m < 9; m++) {
                this.addSlot(new Slot(playerInventory, m + n * 9 + 9, 8 + m * 18 + playerInventoryPosition.getX(), 84 + n * 18 + playerInventoryPosition.getY()));
            }
        }
        // 组装玩家快捷栏
        for (n = 0; n < 9; n++) {
            this.addSlot(new Slot(playerInventory, n, 8 + n * 18 + playerInventoryPosition.getX(), 142 + playerInventoryPosition.getY()));
        }
    }

    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
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

    public boolean stillValid(@NotNull Player player) {
        return this.inventory.stillValid(player);
    }

    // 获取目标实体的方法，供ChestCavityScreen使用
    public ChestCavityEntity getTargetEntity() {
        return this.targetEntity;
    }
}