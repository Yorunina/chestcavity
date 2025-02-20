package net.tigereye.chestcavity.compat.kubejs;

import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;

import static net.tigereye.chestcavity.ChestCavity.KUBEJS_LOADED;
import static net.tigereye.chestcavity.compat.kubejs.CCKubejsPlugin.*;

public class CCEvents {
    public static void postUpdateCCScore(ChestCavityInstance cc) {
        if (KUBEJS_LOADED) {
            UPDATE_CC_SCORE.post(new UpdateOrganScoreJS(cc, cc.owner, cc.owner.level()));
        }
    }

    public static void postEvaluateChestCavity(ChestCavityInstance cc) {
        if (KUBEJS_LOADED) {
            EVAL_CC.post(new EvaluateChestCavityJS(cc, cc.owner, cc.owner.level()));
        }
    }

    public static void postOpenedEntityTick(ChestCavityInstance cc) {
        if (KUBEJS_LOADED) {
            OPENED_ENTITY_TICK.post(new EvaluateChestCavityJS(cc, cc.owner, cc.owner.level()));
        }
    }
}
