package net.tigereye.chestcavity.compat.tinker;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.helper.TooltipBuilder;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nullable;
import java.util.List;

public class TinkerOrganItem extends ModifiableItem {

    public TinkerOrganItem(Properties properties, ToolDefinition toolDefinition) {
        super(properties, toolDefinition);
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        return;
    }

    @Override
    public List<Component> getStatInformation(IToolStackView tool, @Nullable Player player, List<Component> tooltips, TooltipKey key, TooltipFlag tooltipFlag) {
        List<Component> list = super.getStatInformation(tool, player, tooltips, key, tooltipFlag);
        TooltipBuilder builder = new TooltipBuilder(tool, list);
        builder.add(ToolStats.ATTACK_DAMAGE);
        builder.add(ToolStats.ATTACK_SPEED);
        builder.add(ToolStats.ARMOR);
        builder.add(ToolStats.ARMOR_TOUGHNESS);
        builder.add(ToolStats.MINING_SPEED);
        builder.add(ToolStats.KNOCKBACK_RESISTANCE);
        List<Component> result = builder.getTooltips();
        result.add(0, Component.translatable("tooltip.chestcavity.tinker_info.tinker_organ"));
        return result;
    }
}