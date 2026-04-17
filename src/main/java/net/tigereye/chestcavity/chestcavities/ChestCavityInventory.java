package net.tigereye.chestcavity.chestcavities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import org.jetbrains.annotations.NotNull;

public class ChestCavityInventory extends SimpleContainer {
    ChestCavityInstance instance;

    public ChestCavityInstance getInstance() {
        return this.instance;
    }
    public void setInstance(ChestCavityInstance instance) {
        this.instance = instance;
    }

    public ChestCavityInventory(int size) {
        super(size);
    }

    public ChestCavityInventory(ChestCavityInstance ccInstance) {
        super(InventoryTypeManager.getInventoryTypeData(ccInstance.getInventoryType()).getSlotSize());
        this.instance = ccInstance;
    }

    @Override
    public void fromTag(ListTag tags) {
        this.clearContent();
        for (int j = 0; j < tags.size(); ++j) {
            CompoundTag NbtCompound = tags.getCompound(j);
            int k = NbtCompound.getInt("Slot");
            if (k < this.getContainerSize()) {
                this.setItem(k, ItemStack.of(NbtCompound));
            }
        }
    }

    public int countEmpty() {
        int empty = 0;
        for (int i = 0; i < this.getContainerSize(); ++i) {
            if (this.getItem(i).isEmpty()) {
                empty++;
            }
        }
        return empty;
    }

    @Override
    public @NotNull ListTag createTag() {
        ListTag list = new ListTag();
        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemStack = this.getItem(i);
            if (!itemStack.isEmpty()) {
                CompoundTag NbtCompound = new CompoundTag();
                NbtCompound.putInt("Slot", i);
                itemStack.save(NbtCompound);
                list.add(NbtCompound);
            }
        }
        return list;
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.instance == null) {
            return false;
        } else if (this.instance.owner.isDeadOrDying()) {
            return false;
        } else {
            return player.distanceTo(this.instance.owner) < 32.0F;
        }
    }


    @Override
    public ChestCavityInventory clone() {
        ChestCavityInventory inventory = new ChestCavityInventory(this.instance);
        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack pItem = this.getItem(i);
            if (pItem.isEmpty()) continue;
            inventory.setItem(i, pItem.copy());
        }
        return inventory;
    }
}
