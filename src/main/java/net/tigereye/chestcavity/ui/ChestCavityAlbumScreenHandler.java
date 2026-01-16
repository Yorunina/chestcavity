package net.tigereye.chestcavity.ui;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.ChestCavity;

import java.util.List;

public class ChestCavityAlbumScreenHandler extends AbstractContainerMenu {
    private final SimpleContainer container;

    public ChestCavityAlbumScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, 0);
    }

    public ChestCavityAlbumScreenHandler(int syncId, Inventory playerInventory, int pageIndex) {
        super(ChestCavity.CHEST_CAVITY_ALBUM_SCREEN_HANDLER.get(), syncId);
        Player player = playerInventory.player;
        ItemStack mainHandItem = player.getMainHandItem();

        CompoundTag tag = mainHandItem.getOrCreateTag();
        CompoundTag organsNbt = new CompoundTag();



        List<String> itemIds = ChestCavityAlbumScreen.getPageIds(pageIndex);
        this.container = new SimpleContainer(itemIds.size());
        for (int i = 0; i < itemIds.size(); i++) {
            String itemId = itemIds.get(i);

            this.addSlot(new AlbumOrganSlot(
                    container,
                    i,
                    i < 15 ? 31 + (i % 3) * 41 : 170 + ((i - 15) % 3) * 41,
                    21 + ((i % 15) / 3) * 29,
                    itemId));
        }


        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                int slotId = x + (y * 9) + 9;
                this.addSlot(new Slot(playerInventory, slotId, 73 + x * 18, 176 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x, 73 + x * 18, 234));
        }

    }


    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }


    public static class AlbumOrganSlot extends Slot {
        private final String itemId;

        public AlbumOrganSlot(Container container, int slotIndex, int slotX, int slotY, String itemId) {
            super(container, slotIndex, slotX, slotY);
            this.itemId = itemId;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString().equals(itemId);
        }

        @Override
        public void setChanged() {
            super.setChanged();
        }
    }
}
