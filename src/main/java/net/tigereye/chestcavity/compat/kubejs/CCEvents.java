package net.tigereye.chestcavity.compat.kubejs;

import net.minecraft.world.effect.MobEffectInstance;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;

import static net.tigereye.chestcavity.ChestCavity.KUBEJS_LOADED;
import static net.tigereye.chestcavity.compat.kubejs.CCKubeJSPlugin.*;

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
            OPENED_ENTITY_TICK.post(new OpenedEntityTickJS(cc, cc.owner, cc.owner.level()));
        }
    }

    public static MobEffectInstance postOpenedEntityAddStatus(ChestCavityInstance cc, MobEffectInstance effect) {
        if (KUBEJS_LOADED) {
            OrganAddStatusEffectJS event = new OrganAddStatusEffectJS(cc, cc.owner, cc.owner.level(), effect);
            ORGAN_ADD_STATUS_EFFECT.post(event);
            return event.getEffect();
        }
        return effect;
    }
}
