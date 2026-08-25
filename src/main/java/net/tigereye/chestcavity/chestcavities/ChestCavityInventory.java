package net.tigereye.chestcavity.chestcavities;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.util.ContainerNbtUtil;
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
        ContainerNbtUtil.load(this, tags);
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
        return ContainerNbtUtil.save(this);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.instance == null) {
            return false;
        } else if (this.instance.owner.isDeadOrDying()) {
            return false;
        } else if (this.instance.owner.isRemoved()) {
            return false;
        } else if (this.instance.owner.level() == null) {
            return false;
        } else {
            return player.distanceTo(this.instance.owner) < 32.0F;
        }
    }


    @Override
    public ChestCavityInventory clone() {
        return copyFor(this.instance);
    }

    /**
     * Creates an item-copy snapshot associated with the supplied chest-cavity
     * instance. This is used when transferring state between entities so the
     * snapshot cannot retain a listener target from the source instance.
     */
    public ChestCavityInventory copyFor(ChestCavityInstance targetInstance) {
        ChestCavityInventory inventory = new ChestCavityInventory(this.getContainerSize());
        inventory.setInstance(targetInstance);
        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack pItem = this.getItem(i);
            if (pItem.isEmpty()) continue;
            inventory.setItem(i, pItem.copy());
        }
        return inventory;
    }
}
