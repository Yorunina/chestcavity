package net.tigereye.chestcavity.ui;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.ChestCavitySlotDefinition;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.SlotDefinition;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.network.ChestCavityNetwork;
import net.tigereye.chestcavity.network.packet.TargetEntityInventoryTypePacket;
import net.tigereye.chestcavity.service.ChestCavitySurgeryService;
import net.tigereye.chestcavity.registration.CCRegistries;
import net.tigereye.chestcavity.util.TargetEntityInventoryTypeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChestCavityScreenHandler extends AbstractContainerMenu {
    private final ChestCavityInventory inventory;
    private ItemStack chestCavityItem;

    // 为MenuType注册保留的双参数构造函数
    public ChestCavityScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, null);
    }

    public ChestCavityScreenHandler(int syncId, Inventory playerInventory, ChestCavityEntity targetEntity) {
        super(CCRegistries.CHEST_CAVITY_SCREEN_HANDLER.get(), syncId);
        Player player = playerInventory.player;
        Level level = player.level();
        InventoryTypeData inventoryTypeData;
        chestCavityItem = player.getMainHandItem();
        if (level.isClientSide() && targetEntity == null) {
            ResourceLocation inventoryType = TargetEntityInventoryTypeManager.getTargetEntityInventoryType();
            inventoryTypeData = InventoryTypeManager.getInventoryTypeData(inventoryType);
        } else {
            inventoryTypeData = targetEntity.getInventoryTypeData();
        }

        List<ChestCavitySlotDefinition> slotDefinitionList = inventoryTypeData.getSlotDefinitions();
        if (level.isClientSide()) {
            this.inventory = new ChestCavityInventory(inventoryTypeData.getSlotSize());
        } else {
            this.inventory = ChestCavitySurgeryService.openChestCavity(targetEntity.getChestCavityInstance());
        }

        // 在服务器端，向客户端发送目标实体的inventoryType信息
        if (player instanceof ServerPlayer serverPlayer) {
            ChestCavityNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new TargetEntityInventoryTypePacket(inventoryTypeData.getId()));
        }

        SlotDefinition playerInventoryPosition = inventoryTypeData.getPlayerInventoryPosition();
        int n;
        int m;

        for (int j = 0; j < this.inventory.getContainerSize(); j++) {
            this.addSlot(new ChestCavitySlot(this.inventory, j, slotDefinitionList.get(j).getX(), slotDefinitionList.get(j).getY()));
        }

        for (n = 0; n < 3; n++) {
            for (m = 0; m < 9; m++) {
                this.addSlot(new ChestCavityInventorySlot(playerInventory, m + n * 9 + 9, 8 + m * 18 + playerInventoryPosition.getX(), 84 + n * 18 + playerInventoryPosition.getY(), chestCavityItem));
            }
        }
        // 组装玩家快捷栏
        for (n = 0; n < 9; n++) {
            this.addSlot(new ChestCavityInventorySlot(playerInventory, n, 8 + n * 18 + playerInventoryPosition.getX(), 142 + playerInventoryPosition.getY(), chestCavityItem));
        }
    }

    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            if (!this.inventory.stillValid(player)) {
                return ItemStack.EMPTY;
            }
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    public boolean stillValid(@NotNull Player player) {
        boolean valid = this.inventory.stillValid(player);
        if (!valid && !player.level().isClientSide()) {
            player.closeContainer();
        }
        return valid;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
//        if (player.level().isClientSide()) {
//            TargetEntityInventoryTypeManager.removeTargetEntityInventoryType();
//        }
    }
}
