package net.tigereye.chestcavity.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;

import java.util.List;

public final class OrganUtil {
    private OrganUtil() {
    }

    @OnlyIn(Dist.CLIENT)
    public static void displayCompatibility(ItemStack itemStack, Level world, List<Component> tooltip, TooltipFlag tooltipContext) {
        CompoundTag tag = itemStack.getOrCreateTag();
        boolean isCompat = false;
        MinecraftServer server = null;
        if (world != null) server = world.getServer();


        if (server == null) server = Minecraft.getInstance().getSingleplayerServer();


        if (server != null) {
            Player serverPlayer = server.getPlayerList().getPlayer(Minecraft.getInstance().player.getUUID());
            if (serverPlayer instanceof ChestCavityEntity ccPlayer) {
                isCompat = ChestCavityUtil.getCompatibility(ccPlayer.getChestCavityInstance(), itemStack);
            }
        }
        OrganData organData = ChestCavityUtil.lookupOrgan(itemStack, null);
        if (organData.pseudoOrgan) {
            return;
        }

        MutableComponent compatibleTooltip;
        if (tag.contains(ChestCavity.COMPATIBILITY_TAG.toString())) {
            tag = tag.getCompound(ChestCavity.COMPATIBILITY_TAG.toString());
            String name = tag.getString("name");
            compatibleTooltip = Component.translatable("tooltips.organ.only_compatible_with", name);
        } else {
            compatibleTooltip = Component.translatable("tooltips.organ.safe_to_use");
        }

        if (isCompat) {
            compatibleTooltip.withStyle(ChatFormatting.GREEN);
        } else {
            compatibleTooltip.withStyle(ChatFormatting.RED);
        }
        tooltip.add(compatibleTooltip);
    }
}