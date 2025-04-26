package net.tigereye.chestcavity.ui;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.ChestCavitySlotDefinition;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.SlotDefinition;

import java.util.List;

import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.DEFAULT_INVENTORY_TYPE_STRING;

public class ChestCavityItemScreenHandler extends AbstractContainerMenu {

    private ItemStackHandler inventory;
    private InteractionHand itemHand;
    private ItemStack chestCavityItem;

    public ChestCavityItemScreenHandler(int syncId, Inventory playerInventory) {
        super(ChestCavity.CHEST_CAVITY_ITEM_SCREEN_HANDLER.get(), syncId);
        Player player = playerInventory.player;
        itemHand = InteractionHand.MAIN_HAND;
        chestCavityItem = player.getMainHandItem();
        String inventoryType = DEFAULT_INVENTORY_TYPE_STRING;

        if (chestCavityItem.isEmpty()) {
            chestCavityItem = player.getOffhandItem();
            itemHand = InteractionHand.OFF_HAND;
        }
        CompoundTag nbt = new CompoundTag();
        if (!chestCavityItem.hasTag()) {
            nbt.putString("InventoryType", inventoryType);
            nbt.put("Inventory", new ListTag());
            chestCavityItem.setTag(nbt);
        } else {
            nbt = chestCavityItem.getTag();
        }

        inventoryType = nbt.getString("InventoryType");
        InventoryTypeData inventoryTypeData = InventoryTypeManager.getInventoryTypeData(new ResourceLocation(inventoryType));

        int slotSize = inventoryTypeData.getSlotSize();
        this.inventory = new ItemStackHandler(slotSize);
        nbt.putInt("Size", slotSize);
        this.inventory.deserializeNBT(nbt.getCompound("Inventory"));

        List<ChestCavitySlotDefinition> slotDefinitionList = inventoryTypeData.getSlotDefinitions();

        SlotDefinition playerInventoryPosition = inventoryTypeData.getPlayerInventoryPosition();
        int n;
        int m;
        // 组装自定义胸腔界面
        for (int j = 0; j < slotSize; j++) {
            this.addSlot(new SlotItemHandler(this.inventory, j, slotDefinitionList.get(j).getX(), slotDefinitionList.get(j).getY()) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return !stack.is(chestCavityItem.getItem());
                }
            });
        }
        // 组装玩家背包
        for (n = 0; n < 3; n++) {
            for (m = 0; m < 9; m++) {
                this.addSlot(new Slot(playerInventory, m + n * 9 + 9, 8 + m * 18 + playerInventoryPosition.getX(), 84 + n * 18 + playerInventoryPosition.getY()));
            }
        }
        // 组装玩家快捷栏
        for (n = 0; n < 9; n++) {
            if (playerInventory.selected == n) {
                this.addSlot(new SlotLocked(playerInventory, n, 8 + n * 18 + playerInventoryPosition.getX(), 142 + playerInventoryPosition.getY()));
            } else {
                this.addSlot(new Slot(playerInventory, n, 8 + n * 18 + playerInventoryPosition.getX(), 142 + playerInventoryPosition.getY()));
            }
        }

    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getSlots()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getSlots(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getSlots(), false)) {
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

    @Override
    public void removed(Player player) {
        chestCavityItem.getOrCreateTag().put("Inventory", inventory.serializeNBT());
        super.removed(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return !chestCavityItem.isEmpty() && player.getItemInHand(itemHand).equals(chestCavityItem);
    }

}
