package net.tigereye.chestcavity.compat.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;

public class CCEvents {
   public static EventGroup CCGROUP = EventGroup.of("ChestCavityEvents");

    public static EventHandler EVAL_CC = CCGROUP
            .server("evaluateChestCavity", () -> EvaluateChestCavityJS.class);
    public static EventHandler UPDATE_CC_SCORE = CCGROUP
            .server("updateOrganScore", () -> UpdateOrganScoreJS.class);

    public static void postUpdateCCScore(ChestCavityInstance cc) {
            UPDATE_CC_SCORE.post(new UpdateOrganScoreJS(cc, cc.owner, cc.owner.level()));
    }

    public static void postEvaluateChestCavity(ChestCavityInstance cc) {
        EVAL_CC.post(new EvaluateChestCavityJS(cc, cc.owner, cc.owner.level()));
    }
}
