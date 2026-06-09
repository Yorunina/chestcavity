package net.tigereye.chestcavity.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.items.ChestOpener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MixinMob {
    @Inject(method = "checkAndHandleImportantInteractions", at = @At("HEAD"), cancellable = true)
    private void checkAndHandleImportantInteractions(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getItem() instanceof ChestOpener chestOpener) {
            InteractionResult action = chestOpener.openLivingEntity(itemstack, player, (Mob) (Object) this, hand);
            if (action.consumesAction()) {
                cir.setReturnValue(action);
            }
        }
    }
}
