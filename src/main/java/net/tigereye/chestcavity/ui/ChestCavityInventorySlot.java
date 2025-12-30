package net.tigereye.chestcavity.ui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ChestCavityInventorySlot extends Slot {
    private final int index;
    private final ItemStack chestOpener;

    public ChestCavityInventorySlot(Container inv, int index, int xPosition, int yPosition, ItemStack chestOpener) {
        super(inv, index, xPosition, yPosition);
        this.chestOpener = chestOpener;
        this.index = index;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.equals(chestOpener);
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !this.container.getItem(index).equals(chestOpener);
    }
}
