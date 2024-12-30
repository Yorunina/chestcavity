package net.tigereye.chestcavity.items;

import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCItems;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.ui.ChestCavityScreenHandler;
import net.tigereye.chestcavity.util.ChestCavityUtil;

public class ChestOpener extends Item {
	public ChestOpener() {
		super(CCItems.CHEST_OPENER_SETTINGS);
	}

	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		LivingEntity target = player;
//		if (!player.isShiftKeyDown()) {
//			return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), false);
//		}

		return this.openChestCavity(player, target, false) ? InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), false) : InteractionResultHolder.fail(player.getItemInHand(hand));
	}

	public boolean openChestCavity(Player player, LivingEntity target) {
		return this.openChestCavity(player, target, true);
	}

	public boolean openChestCavity(Player player, LivingEntity target, boolean shouldKnockback) {
		Optional<ChestCavityEntity> optional = ChestCavityEntity.of(target);
		if (optional.isPresent()) {
			ChestCavityEntity chestCavityEntity = optional.get();
			ChestCavityInstance cc = chestCavityEntity.getChestCavityInstance();
			if (target != player && !cc.getChestCavityType().isOpenable(cc)) {
				if (player.level().isClientSide()) {
					if (!target.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
						// todo 本地化
						player.displayClientMessage(Component.translatable("Target's chest is obstructed"), true);
						player.playNotifySound(SoundEvents.BONE_BLOCK_HIT, SoundSource.PLAYERS, 0.75F, 1.0F);
					} else {
						player.displayClientMessage(Component.translatable("Target is too healthy to open"), true);
						player.playNotifySound(SoundEvents.BONE_BLOCK_HIT, SoundSource.PLAYERS, 0.75F, 1.0F);
					}
				}

				return false;
			} else {
				if (cc.getOrganScore(CCOrganScores.EASE_OF_ACCESS) > 0.0F) {
					if (player.level().isClientSide()) {
						player.playNotifySound(SoundEvents.CHEST_OPEN, SoundSource.PLAYERS, 0.75F, 1.0F);
					}
				} else if (!shouldKnockback) {
					target.hurt(player.damageSources().generic(), 4.0F);
				} else {
					target.hurt(player.damageSources().playerAttack(player), 4.0F);
				}

				if (target.isAlive()) {
					String name;
					try {
						name = target.getDisplayName().getString();
						// todo 本地化提取
						name = name.concat("'s ");
					} catch (Exception var9) {
						name = "";
					}
					((ChestCavityEntity)player).getChestCavityInstance().ccBeingOpened = cc;
					// 界面渲染
					player.openMenu(new SimpleMenuProvider((i, playerInventory, playerEntity) -> new ChestCavityScreenHandler(i, playerInventory, chestCavityEntity), Component.translatable(name + "Chest Cavity")));
				}
				return true;
			}
		} else {
			return false;
		}
	}
}
