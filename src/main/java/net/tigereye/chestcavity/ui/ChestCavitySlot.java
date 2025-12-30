package net.tigereye.chestcavity.ui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.tigereye.chestcavity.registration.CCTags;

public class ChestCavitySlot extends Slot {
    private final int index;

    public ChestCavitySlot(Container inv, int index, int xPosition, int yPosition) {
        super(inv, index, xPosition, yPosition);
        this.index = index;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !this.container.getItem(index).is(CCTags.CANNOT_REMOVE);
    }
}
