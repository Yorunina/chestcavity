package net.tigereye.chestcavity.ui;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ChestCavityContainer extends SimpleContainer {

    public ChestCavityContainer(int size) {
        super(size);
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


}
