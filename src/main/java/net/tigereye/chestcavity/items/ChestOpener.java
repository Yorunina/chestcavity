package net.tigereye.chestcavity.items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.compat.ftb.ChestCavityQuestEventHandler;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCItems;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCStatusEffects;
import net.tigereye.chestcavity.ui.ChestCavityScreenHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

import static net.tigereye.chestcavity.registration.CCEnchantments.*;

public class ChestOpener extends Item {
    public ChestOpener() {
        super(CCItems.CHEST_OPENER_SETTINGS);
    }

    public static void canNotOpenChestCavity(Player player, LivingEntity target) {
        player.level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.BONE_BLOCK_HIT, SoundSource.PLAYERS, 0.75F, 1.0F);
        if (!target.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            player.sendSystemMessage(Component.translatable("status_msg.chestcavity.chestopener.fail.obstructed"));
        } else {
            player.sendSystemMessage(Component.translatable("status_msg.chestcavity.chestopener.fail.healthy"));
        }
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        if (target instanceof Player) {
            return this.openLivingEntity(stack, player, target, hand);
        }
        return InteractionResult.PASS;
    }

    public @NotNull InteractionResult openLivingEntity(@NotNull ItemStack stack, Player player, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        if (player.level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (!hand.equals(InteractionHand.MAIN_HAND)) return InteractionResult.FAIL;
        boolean success = this.openChestCavity(player, target, stack);
        if (success) {
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack chestOpener = player.getItemInHand(hand);
        if (world.isClientSide()) {
            return InteractionResultHolder.pass(chestOpener);
        }
        if (!hand.equals(InteractionHand.MAIN_HAND)) return InteractionResultHolder.fail(chestOpener);

        if (chestOpener.getAllEnchantments().containsKey(SAFE_SURGERY.get()) && !player.isCrouching()) {
            return InteractionResultHolder.fail(chestOpener);
        }
        return this.openChestCavity(player, player, chestOpener) ? InteractionResultHolder.success(chestOpener) : InteractionResultHolder.fail(chestOpener);
    }

    public boolean openChestCavity(Player player, LivingEntity target, ItemStack chestOpener) {
        Optional<ChestCavityEntity> optional = ChestCavityEntity.of(target);

        if (optional.isEmpty()) return false;
        ChestCavityEntity chestCavityEntity = optional.get();
        ChestCavityInstance cc = chestCavityEntity.getChestCavityInstance();
        cc.inventory.setInstance(cc);

        Map<Enchantment, Integer> allEnchantments = chestOpener.getAllEnchantments();

        double easeAccess = cc.opened ? cc.getOrganScore(CCOrganScores.EASE_OF_ACCESS) : cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.EASE_OF_ACCESS);

        if (cc.owner.hasEffect(CCStatusEffects.SURGICAL_ANESTHESIA.get())) easeAccess = Math.max(easeAccess, 1.0D);

        boolean selfOpen = target == player;
        boolean canEaseAccess = easeAccess > 0 || allEnchantments.containsKey(CREATIVE_SURGERY.get()) || allEnchantments.containsKey(PAINLESS_SURGERY.get());

        if (target instanceof Player && !canEaseAccess && !selfOpen) return false;

        if (!selfOpen && !cc.getChestCavityType().isOpenable(cc, allEnchantments, easeAccess)) {
            canNotOpenChestCavity(player, target);
            return false;
        } else {
            if (!canEaseAccess)
                target.hurt(player.damageSources().magic(), 4.0F);
            if (target.isAlive()) {
                player.openMenu(new SimpleMenuProvider((i, playerInventory, playerEntity) -> new ChestCavityScreenHandler(i, playerInventory, chestCavityEntity), Component.translatable("gui.chestcavity.chestopener.title", target.getDisplayName())));
                if (player instanceof ServerPlayer serverPlayer)
                    ChestCavityQuestEventHandler.getInstance().onChestCavityOpened(serverPlayer, target);
            }
            return true;
        }
    }
}