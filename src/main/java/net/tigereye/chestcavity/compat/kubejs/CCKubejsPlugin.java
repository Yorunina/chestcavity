package net.tigereye.chestcavity.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public class CCKubejsPlugin extends KubeJSPlugin {

    public static EventGroup CCGROUP = EventGroup.of("ChestCavityEvents");

    public static EventHandler EVAL_CC = CCGROUP
            .server("evaluateChestCavity", () -> EvaluateChestCavityJS.class);
    public static EventHandler UPDATE_CC_SCORE = CCGROUP
            .server("updateOrganScore", () -> UpdateOrganScoreJS.class);

    public static EventHandler OPENED_ENTITY_TICK = CCGROUP
            .server("openedEntityTick", () -> OpenedEntityTickJS.class);
    @Override
    public void registerEvents() {
        CCGROUP.register();
    }
}
