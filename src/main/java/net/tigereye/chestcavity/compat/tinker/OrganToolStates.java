package net.tigereye.chestcavity.compat.tinker;

import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.registration.CCOrganScores;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class OrganToolStates {
//    public static final FloatToolStat ORGAN_HEALTH = ToolStats.register(new FloatToolStat(new ToolStatId(ChestCavity.MODID, "organ_health"), 0xbd41a2, 0, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, TINKER_ORGAN));
//    public static final FloatToolStat ORGAN_STRENGTH = ToolStats.register(new FloatToolStat(new ToolStatId(ChestCavity.MODID, "organ_strength"), 0xbd41a2, 0, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, TINKER_ORGAN));

//    public static HashMap<FloatToolStat, ResourceLocation> ToolStatToOrganScoreMap = new HashMap<>();
//    public static void register() {
//        ToolStatToOrganScoreMap.put(ORGAN_HEALTH, CCOrganScores.HEALTH);
//        ToolStatToOrganScoreMap.put(ORGAN_STRENGTH, CCOrganScores.STRENGTH);
//    }

    public static OrganData getOrganDataFromTinkerOrgan(ItemStack itemStack) {
        ToolStack tool = ToolStack.from(itemStack);
        OrganData organData = new OrganData();
        if (tool.isBroken()) {
            return organData;
        }
        StatsNBT toolState = tool.getStats();
//        ToolStatToOrganScoreMap.forEach((stat, organScore) -> {
//            float score = toolState.get(stat).floatValue();
//            if (score != 0) {
//                organData.organScores.put(organScore, score);
//            }
//        });

        if (toolState.hasStat(ToolStats.DURABILITY)) {
            organData.organScores.put(CCOrganScores.HEALTH, toolState.get(ToolStats.DURABILITY) / 100);
        }
        if (toolState.hasStat(ToolStats.ATTACK_DAMAGE)) {
            organData.organScores.put(CCOrganScores.STRENGTH, toolState.get(ToolStats.ATTACK_DAMAGE));
        }

        float defense = 0;
        if (toolState.hasStat(ToolStats.ARMOR)) {
            defense += (float) (toolState.get(ToolStats.ARMOR) * 0.5);
        }
        if (toolState.hasStat(ToolStats.ARMOR_TOUGHNESS)) {
            defense += toolState.get(ToolStats.ARMOR_TOUGHNESS);
        }
        if (defense != 0) {
            organData.organScores.put(CCOrganScores.DEFENSE, defense);
        }


        float nerves = 0;
        if (toolState.hasStat(ToolStats.ATTACK_SPEED)) {
            nerves += (float) (toolState.get(ToolStats.ATTACK_SPEED) * 0.5);
        }
        if (toolState.hasStat(ToolStats.MINING_SPEED)) {
            nerves += (float) (toolState.get(ToolStats.MINING_SPEED) * 0.25);
        }
        if (nerves != 0) {
            organData.organScores.put(CCOrganScores.NERVES, nerves);
        }

        if (toolState.hasStat(ToolStats.KNOCKBACK_RESISTANCE)) {
            organData.organScores.put(CCOrganScores.KNOCKBACK_RESISTANT, toolState.get(ToolStats.KNOCKBACK_RESISTANCE));
        }
        return organData;
    }
}
