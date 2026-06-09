package net.tigereye.chestcavity.util;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;

import java.util.List;

public final class OrganUtil {
    private OrganUtil() {
    }


    @OnlyIn(Dist.CLIENT)
    public static void displayCompatibility(ItemStack itemStack, Level world, List<Component> tooltip, TooltipFlag tooltipContext) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (itemStack.isEmpty()) return;
        OrganData organData = ChestCavityUtil.lookupOrgan(itemStack, null);
        if (organData.pseudoOrgan) return;

        MutableComponent compatibleTooltip;
        if (tag.contains(ChestCavity.COMPATIBILITY_TAG)) {
            CompoundTag compactTag = tag.getCompound(ChestCavity.COMPATIBILITY_TAG);
            MutableComponent name = Component.translatable(compactTag.getString("name"));
            compatibleTooltip = Component.translatable("tooltips.organ.only_compatible_with", name);
        } else {
            compatibleTooltip = Component.translatable("tooltips.organ.safe_to_use");
        }
        compatibleTooltip.withStyle(ChatFormatting.GRAY);
        tooltip.add(compatibleTooltip);
    }
}