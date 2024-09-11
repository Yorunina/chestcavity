package net.tigereye.chestcavity.chestcavities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;

public class ChestCavityInventory extends SimpleContainer {
    ChestCavityInstance instance;
    boolean test;

    public ChestCavityInstance getInstance() {
        return this.instance;
    }

    public void setInstance(ChestCavityInstance instance) {
        this.instance = instance;
    }

    public ChestCavityInventory() {
        super(27);
    }

    public ChestCavityInventory(int size, ChestCavityInstance instance) {
        super(size);
        this.instance = instance;
    }

    public void readTags(ListTag tags) {
        this.clearContent();

        for(int j = 0; j < tags.size(); ++j) {
            CompoundTag NbtCompound = tags.getCompound(j);
            int k = NbtCompound.getByte("Slot") & 255;
            boolean f = NbtCompound.getBoolean("Forbidden");
            if (k >= 0 && k < this.getContainerSize()) {
                this.setItem(k, ItemStack.of(NbtCompound));
            }
        }

    }

    public ListTag getTags() {
        ListTag list = new ListTag();

        for(int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemStack = this.getItem(i);
            if (!itemStack.isEmpty()) {
                CompoundTag NbtCompound = new CompoundTag();
                NbtCompound.putByte("Slot", (byte)i);
                itemStack.setTag(NbtCompound);
                list.add(NbtCompound);
            }
        }

        return list;
    }

    public boolean m_6542_(Player player) {
        if (this.instance == null) {
            return true;
        } else if (this.instance.owner.isDeadOrDying()) {
            return false;
        } else {
            return player.distanceTo(this.instance.owner) < 8.0F;
        }
    }
}
