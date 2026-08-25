package net.tigereye.chestcavity.ui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.registration.CCTags;

import java.util.Optional;

public class ChestCavitySlot extends Slot {
    private final int index;

    public ChestCavitySlot(Container inv, int index, int xPosition, int yPosition) {
        super(inv, index, xPosition, yPosition);
        this.index = index;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        if (!this.container.stillValid(playerIn)) {
            return false;
        }
        if (playerIn.isCreative()) return true;
        ItemStack itemstack = this.container.getItem(index);
        if (itemstack.is(CCTags.CANNOT_REMOVE)) return false;
        if (itemstack.hasTag() && itemstack.getTag().getBoolean("cannotRemove")) return false;
        return true;
    }

    @Override
    public Optional<ItemStack> tryRemove(int pCount, int pDecrement, Player pPlayer) {
        if (!this.mayPickup(pPlayer)) {
            return Optional.empty();
        } else if (!this.allowModification(pPlayer) && pDecrement < this.getItem().getCount()) {
            return Optional.empty();
        } else {
            pCount = Math.min(pCount, pDecrement);
            ItemStack itemstack = this.remove(pCount);
            if (itemstack.isEmpty()) {
                return Optional.empty();
            } else {
                if (this.getItem().isEmpty()) {
                    this.setByPlayer(ItemStack.EMPTY);
                }
                return Optional.of(itemstack);
            }
        }
    }
}