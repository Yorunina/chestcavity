package net.tigereye.chestcavity.compat.tinker;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.registration.CCOrganScores;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.HashMap;

public class OrganToolStats {
    public static final FloatToolStat ORGAN_HEALTH = ToolStats.register(new FloatToolStat(new ToolStatId(ChestCavity.MODID, "organ_health"), 0xbd41a2, 0, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY));
    public static final FloatToolStat ORGAN_STRENGTH = ToolStats.register(new FloatToolStat(new ToolStatId(ChestCavity.MODID, "organ_strength"), 0xbd41a2, 0, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY));

    public static HashMap<FloatToolStat, ResourceLocation> ToolStatToOrganScoreMap = new HashMap<>();

    public static void register() {
        ToolStatToOrganScoreMap.put(ORGAN_HEALTH, CCOrganScores.HEALTH);
        ToolStatToOrganScoreMap.put(ORGAN_STRENGTH, CCOrganScores.STRENGTH);
    }

    public static OrganData getOrganDataFromTinkerOrgan(ItemStack itemStack) {
        ToolStack tool = ToolStack.from(itemStack);
        OrganData organData = new OrganData();
        if (tool.isBroken()) {
            return organData;
        }
        StatsNBT toolState = tool.getStats();
        ToolStatToOrganScoreMap.forEach((stat, organScore) -> {
            float score = toolState.get(stat);
            if (score != 0) {
                organData.organScores.put(organScore, score);
            }
        });
        return organData;
    }
}
