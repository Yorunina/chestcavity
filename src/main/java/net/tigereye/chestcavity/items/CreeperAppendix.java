package net.tigereye.chestcavity.items;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class CreeperAppendix extends Item {
    public CreeperAppendix() {
        super((new Item.Properties()).stacksTo(1));
    }

    public void appendHoverText(ItemStack itemStack, Level world, List<Component> tooltip, TooltipFlag tooltipContext) {
        super.appendHoverText(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Component.translatable("This appears to be a fuse.").withStyle(ChatFormatting.ITALIC));
        tooltip.add(Component.translatable("It won't do much by itself.").withStyle(ChatFormatting.ITALIC));
    }
}
