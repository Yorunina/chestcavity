package net.tigereye.chestcavity.chestcavities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;

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

    public ChestCavityInventory(ChestCavityInstance instance) {
        super(InventoryTypeManager.getInventoryTypeData(instance.getInventoryType()).getSlotSize());
        this.instance = instance;
    }

    public void readTags(ListTag tags) {
        this.clearContent();

        for (int j = 0; j < tags.size(); ++j) {
            CompoundTag NbtCompound = tags.getCompound(j);
            int k = NbtCompound.getInt("Slot");
            if (k < this.getContainerSize()) {
                this.setItem(k, ItemStack.of(NbtCompound));
            }
        }

    }

    public ListTag getTags() {
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

    public boolean stillValid(Player player) {
        if (this.instance == null) {
            return false;
        } else if (this.instance.owner.isDeadOrDying()) {
            return false;
        } else {
            return player.distanceTo(this.instance.owner) < 32.0F;
        }
    }

    public ChestCavityInventory clone() {
        ChestCavityInventory inventory = new ChestCavityInventory(this.instance);
        for (int i = 0; i < this.getContainerSize(); ++i) {
            inventory.setItem(i, this.getItem(i).copy());
        }
        return inventory;
    }
}
