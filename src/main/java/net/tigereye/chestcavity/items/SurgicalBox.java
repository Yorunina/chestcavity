package net.tigereye.chestcavity.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.ui.ChestCavityItemScreenHandler;
import net.tigereye.chestcavity.util.ChestCavityUtil;

import java.util.Map;
import java.util.Optional;

public class SurgicalBox extends Item implements MenuProvider {

    public SurgicalBox() {
        super(new Item.Properties().stacksTo(1));
    }

    public static InventoryTypeData getInventoryTypeData(ItemStack stack) {
        CompoundTag itemNbt = stack.getOrCreateTag();
        InventoryTypeData inventoryTypeData = InventoryTypeManager.getDefaultInventoryTypeData();
        if (itemNbt.contains("InventoryType")) {
            inventoryTypeData = InventoryTypeManager.getInventoryTypeData(new ResourceLocation(itemNbt.getString("InventoryType")));
        }
        return inventoryTypeData;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            if (player.isShiftKeyDown()) {
                NetworkHooks.openScreen((ServerPlayer) player, this);
            } else {
                player.startUsingItem(hand);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (pLevel.isClientSide) return pStack;
        if (pLivingEntity instanceof Player player) {
            Optional<ChestCavityEntity> optionalChestCavityEntity = ChestCavityEntity.of(player);
            if (!optionalChestCavityEntity.isPresent()) return pStack;
            ChestCavityEntity chestCavityEntity = optionalChestCavityEntity.get();
            replaceChestCavity(pStack, chestCavityEntity);
        }
        return pStack;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (pPlayer.level().isClientSide) {
            return InteractionResult.PASS;
        }
        Optional<ChestCavityEntity> optionalChestCavityEntity = ChestCavityEntity.of(pInteractionTarget);
        if (!optionalChestCavityEntity.isPresent()) {
            return InteractionResult.FAIL;
        }
        ChestCavityEntity chestCavityEntity = optionalChestCavityEntity.get();
        ChestCavityInstance entityInstance = chestCavityEntity.getChestCavityInstance();
        entityInstance.inventory.setInstance(entityInstance);
        Map<Enchantment, Integer> allEnchantments = pStack.getAllEnchantments();
        if (!entityInstance.getChestCavityType().isOpenable(entityInstance, allEnchantments)) {
            ChestOpener.canNotOpenChestCavity(pPlayer, pInteractionTarget);
            return InteractionResult.FAIL;
        }
        ((ChestCavityEntity) pPlayer).getChestCavityInstance().ccBeingOpened = entityInstance;

        replaceChestCavity(pStack, chestCavityEntity);

        pPlayer.setItemInHand(pUsedHand, pStack);
        return InteractionResult.SUCCESS;
    }

    public static void replaceChestCavity(ItemStack pStack, ChestCavityEntity chestCavityEntity) {
        ChestCavityInstance entityInstance = chestCavityEntity.getChestCavityInstance();
        InventoryTypeData itemInventoryTypeData = getInventoryTypeData(pStack);
        InventoryTypeData inventoryTypeData = chestCavityEntity.getInventoryTypeData();
        if (!entityInstance.opened) {
            ChestCavityUtil.generateChestCavityIfOpened(entityInstance);
        }

        CompoundTag itemNbt = pStack.getOrCreateTag();
        if (!itemNbt.contains("Inventory")) {
            itemNbt.put("Inventory", new ItemStackHandler(itemInventoryTypeData.getSlotSize()).serializeNBT());
        }
        // 替换胸腔类
        entityInstance.inventory.removeListener(entityInstance);
        chestCavityEntity.setInventoryTypeData(itemInventoryTypeData.getId());
        entityInstance.inventoryType = itemInventoryTypeData.getId();

        itemNbt.putString("InventoryType", inventoryTypeData.getId().toString());
        // 替换胸腔物品栏数量，保存物品信息
        entityInstance.oldInventory = entityInstance.inventory.clone();
        ListTag entityItemListNbt = entityInstance.inventory.getTags();
        entityInstance.inventory = new ChestCavityInventory(itemInventoryTypeData.getSlotSize(), entityInstance);
        // 替换物品
        ItemStackHandler itemInventory = new ItemStackHandler(itemInventoryTypeData.getSlotSize());
        itemInventory.deserializeNBT(itemNbt.getCompound("Inventory"));
        for (int i = 0; i < itemInventoryTypeData.getSlotSize(); i++) {
            entityInstance.inventory.setItem(i, itemInventory.getStackInSlot(i));
        }
        entityInstance.inventory.addListener(entityInstance);
        itemNbt.put("Inventory", new ItemStackHandler(inventoryTypeData.getSlotSize()).serializeNBT());
        itemNbt.getCompound("Inventory").put("Items", entityItemListNbt);
        ChestCavityUtil.evaluateChestCavity(entityInstance);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 20;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ChestCavityItemScreenHandler(id, inv);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.chestcavity.surgical_box.title");
    }
}
