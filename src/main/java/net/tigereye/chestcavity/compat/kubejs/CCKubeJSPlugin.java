package net.tigereye.chestcavity.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.tigereye.chestcavity.compat.kubejs.events.*;
import net.tigereye.chestcavity.chestcavities.instance.OrganScoreModifier;
import net.tigereye.chestcavity.util.ChestCavityUtil;

public class CCKubeJSPlugin extends KubeJSPlugin {

    public static EventGroup CCGROUP = EventGroup.of("ChestCavityEvents");

    public static EventHandler EVAL_CC = CCGROUP
            .server("evaluateChestCavity", () -> EvaluateChestCavityJS.class);
    public static EventHandler INIT_CC = CCGROUP
            .server("initChestCavity", () -> InitChestCavityJS.class);
    public static EventHandler UPDATE_CC_SCORE = CCGROUP
            .server("updateOrganScore", () -> UpdateOrganScoreJS.class);

    public static EventHandler OPENED_ENTITY_TICK = CCGROUP
            .server("openedEntityTick", () -> OpenedEntityTickJS.class);

    public static EventHandler ORGAN_ADD_STATUS_EFFECT = CCGROUP
            .server("organAddStatusEffect", () -> OrganAddStatusEffectJS.class);

    public static EventHandler ORGAN_HOLO_FILTER_TAGS = CCGROUP
            .client("organHoloFilterTags", () -> OrganHoloFilterTagsJS.class);

    @Override
    public void registerEvents() {
        CCGROUP.register();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("ChestCavityUtils", ChestCavityUtil.class);
        event.add("OrganScoreModifier", OrganScoreModifier.class);
    }
}
