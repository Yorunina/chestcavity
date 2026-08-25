package net.tigereye.chestcavity.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

/**
 * Shared NBT serialization for the slot-based containers used by the mod.
 */
public final class ContainerNbtUtil {
    private ContainerNbtUtil() {
    }

    public static void load(Container container, ListTag tags) {
        container.clearContent();
        for (int index = 0; index < tags.size(); index++) {
            CompoundTag itemTag = tags.getCompound(index);
            int slot = itemTag.getInt("Slot");
            if (slot >= 0 && slot < container.getContainerSize()) {
                container.setItem(slot, ItemStack.of(itemTag));
            }
        }
    }

    public static ListTag save(Container container) {
        ListTag tags = new ListTag();
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack itemStack = container.getItem(slot);
            if (itemStack.isEmpty()) {
                continue;
            }

            CompoundTag itemTag = new CompoundTag();
            itemTag.putInt("Slot", slot);
            itemStack.save(itemTag);
            tags.add(itemTag);
        }
        return tags;
    }
}
