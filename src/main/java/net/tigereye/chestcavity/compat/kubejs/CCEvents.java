package net.tigereye.chestcavity.compat.kubejs;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.compat.kubejs.events.EvaluateChestCavityJS;
import net.tigereye.chestcavity.compat.kubejs.events.InitChestCavityJS;
import net.tigereye.chestcavity.compat.kubejs.events.OpenedEntityTickJS;
import net.tigereye.chestcavity.compat.kubejs.events.OrganAddStatusEffectJS;
import net.tigereye.chestcavity.compat.kubejs.events.OrganHoloFilterTagsJS;
import net.tigereye.chestcavity.compat.kubejs.events.UpdateOrganScoreJS;
import net.tigereye.chestcavity.client.OrganHoloTagFilter;

import java.util.List;

import static net.tigereye.chestcavity.ChestCavity.KUBEJS_LOADED;
import static net.tigereye.chestcavity.compat.kubejs.CCKubeJSPlugin.*;

public class CCEvents {
    public static ChestCavityInstance postInitChestCavity(ChestCavityInstance cc) {
        if (KUBEJS_LOADED) {
            InitChestCavityJS event = new InitChestCavityJS(cc, cc.owner, cc.owner.level());
            INIT_CC.post(event);
            return event.getChestCavity();
        }
        return cc;
    }

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

    public static void postOpenedEntityTick(LivingEntity entity, ChestCavityInstance cc) {
        if (KUBEJS_LOADED) {
            OPENED_ENTITY_TICK.post(new OpenedEntityTickJS(cc, entity, entity.level()));
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

    public static List<OrganHoloTagFilter> getOrganHoloFilterTags() {
        if (KUBEJS_LOADED) {
            OrganHoloFilterTagsJS event = new OrganHoloFilterTagsJS();
            ORGAN_HOLO_FILTER_TAGS.post(event);
            return event.getTags();
        }
        return List.of();
    }
}
