package net.tigereye.chestcavity.ui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * 用于锁定某个槽位，避免逻辑异常
 */
public class SlotLocked extends Slot {

    public SlotLocked(Container inv, int index, int x, int y) {
        super(inv, index, x, y);
    }

    @Override
    public boolean mayPickup(Player player) {
        return false;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }
}