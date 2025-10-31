package net.tigereye.chestcavity.compat.tinker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.ChestCavity;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

import static net.tigereye.chestcavity.compat.tinker.OrganToolStats.ToolStatToOrganScoreMap;

public record OrganShellMaterialStats(float adaptability, float efficiency) implements IMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(new ResourceLocation(ChestCavity.MODID, "organ_shell"));

    public static final MaterialStatType<OrganShellMaterialStats> TYPE = new MaterialStatType<>(ID, new OrganShellMaterialStats(1, 1), RecordLoadable.create(
            FloatLoadable.FROM_ZERO.defaultField("efficiency", 1f, true, OrganShellMaterialStats::efficiency),
            FloatLoadable.FROM_ZERO.defaultField("adaptability", 1f, true, OrganShellMaterialStats::adaptability),
            OrganShellMaterialStats::new));

    private static final List<Component> DESCRIPTION = ImmutableList.of();

    @Override
    public MaterialStatType<?> getType() {
        return TYPE;
    }

    @Override
    public MaterialStatsId getIdentifier() {
        return ID;
    }

    @Override
    public List<Component> getLocalizedInfo() {
        List<Component> info = Lists.newArrayList();
        info.add(Component.translatable("material_stat.chestcavity.efficiency", this.efficiency));
        info.add(Component.translatable("material_stat.chestcavity.adaptability", this.adaptability));
        return info;
    }

    @Override
    public List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void apply(ModifierStatsBuilder builder, float scale) {
        ToolStats.DURABILITY.multiply(builder, scale);
        ToolStatToOrganScoreMap.keySet().forEach(toolStat -> {
            toolStat.multiply(builder, adaptability * scale);
        });
    }

}
