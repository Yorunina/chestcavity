package net.tigereye.chestcavity.compat.tinker;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.ChestCavity;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IRepairableMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

import static net.tigereye.chestcavity.compat.tinker.OrganToolStats.ToolStatToOrganScoreMap;

public record CrystalMaterialStats(int durability, float adaptability) implements IRepairableMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(new ResourceLocation(ChestCavity.MODID, "crystal"));

    public static final MaterialStatType<CrystalMaterialStats> TYPE = new MaterialStatType<>(ID, new CrystalMaterialStats(100, 1), RecordLoadable.create(
            IRepairableMaterialStats.DURABILITY_FIELD,
            FloatLoadable.FROM_ZERO.defaultField("adaptability", 1f, true, CrystalMaterialStats::adaptability),
            CrystalMaterialStats::new));

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
        info.add(ToolStats.DURABILITY.formatValue(this.durability));
        info.add(Component.translatable("material_stat.chestcavity.adaptability", this.adaptability));
        return info;
    }

    @Override
    public List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }

    @Override
    public void apply(ModifierStatsBuilder builder, float scale) {
        ToolStats.DURABILITY.update(builder, durability * scale);
        ToolStatToOrganScoreMap.keySet().forEach(toolStat -> {
            toolStat.multiply(builder, adaptability * scale);
        });
    }

}
