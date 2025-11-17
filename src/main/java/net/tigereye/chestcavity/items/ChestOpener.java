package net.tigereye.chestcavity.items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCItems;
import net.tigereye.chestcavity.registration.CCOrganScores;
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
        if (!target.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            player.displayClientMessage(Component.translatable("status_msg.chestcavity.chestopener.fail.obstructed"), true);
            player.playNotifySound(SoundEvents.BONE_BLOCK_HIT, SoundSource.PLAYERS, 0.75F, 1.0F);
        } else {
            player.displayClientMessage(Component.translatable("status_msg.chestcavity.chestopener.fail.healthy"), true);
            player.playNotifySound(SoundEvents.BONE_BLOCK_HIT, SoundSource.PLAYERS, 0.75F, 1.0F);
        }
    }

    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack chestOpener = player.getItemInHand(hand);
        if (chestOpener.getAllEnchantments().containsKey(SAFE_SURGERY.get())) {
            return InteractionResultHolder.fail(chestOpener);
        }
        return this.openChestCavity(player, player, chestOpener, false) ? InteractionResultHolder.sidedSuccess(chestOpener, false) : InteractionResultHolder.fail(chestOpener);
    }

    public boolean openChestCavity(Player player, LivingEntity target, ItemStack chestOpener) {
        return this.openChestCavity(player, target, chestOpener, true);
    }

    public boolean openChestCavity(Player player, LivingEntity target, ItemStack chestOpener, boolean shouldKnockback) {
        Optional<ChestCavityEntity> optional = ChestCavityEntity.of(target);
        if (optional.isEmpty()) return false;
        ChestCavityEntity chestCavityEntity = optional.get();
        ChestCavityInstance cc = chestCavityEntity.getChestCavityInstance();
        cc.inventory.setInstance(cc);
        Map<Enchantment, Integer> allEnchantments = chestOpener.getAllEnchantments();
        if (target != player && !cc.getChestCavityType().isOpenable(cc, allEnchantments) && !allEnchantments.containsKey(CREATIVE_SURGERY.get())) {
            if (player.level().isClientSide()) {
                canNotOpenChestCavity(player, target);
            }
            return false;
        } else {
            if (cc.getOrganScore(CCOrganScores.EASE_OF_ACCESS) > 0.0F || allEnchantments.containsKey(CREATIVE_SURGERY.get()) || allEnchantments.containsKey(PAINLESS_SURGERY.get())) {
//                if (player.level().isClientSide()) {
//                    player.playNotifySound(SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 0.75F, 1.0F);
//                }
            }  else if (!shouldKnockback) {
                target.hurt(player.damageSources().generic(), 4.0F);
            } else {
                target.hurt(player.damageSources().playerAttack(player), 4.0F);
            }

            if (target.isAlive()) {
                ((ChestCavityEntity) player).getChestCavityInstance().ccBeingOpened = cc;
                // 界面渲染
                player.openMenu(new SimpleMenuProvider((i, playerInventory, playerEntity) -> new ChestCavityScreenHandler(i, playerInventory, chestCavityEntity), Component.translatable("gui.chestcavity.chestopener.title", target.getDisplayName())));
                if (player instanceof ServerPlayer serverPlayer) {
                    ChestCavity.FTB_EVENT_HANDLER.onChestCavityOpened(serverPlayer, target);
                }
            }
            return true;
        }

    }
}
