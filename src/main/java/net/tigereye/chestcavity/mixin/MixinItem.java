package net.tigereye.chestcavity.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.util.ChestCavityUtil;
import net.tigereye.chestcavity.util.OrganUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin({Item.class})
public class MixinItem {
    public MixinItem() {
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"appendHoverText"}
    )
    public void chestCavityItemAppendTooltip(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context, CallbackInfo info) {
        if (world == null) return;
        OrganData data = ChestCavityUtil.lookupOrgan(stack, null);
        if (!data.isEmpty() && world.isClientSide) {
            OrganUtil.displayCompatibility(stack, world, tooltip, context);
        }
    }
}
