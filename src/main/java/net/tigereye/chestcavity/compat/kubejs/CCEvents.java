package net.tigereye.chestcavity.compat.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface CCEvents {
    EventGroup CCGROUP = EventGroup.of("ChestCavityEvents");
    EventHandler EVAL_CC = CCGROUP
            .server("evaluateChestCavity", () -> EvaluateChestCavityJS.class);
    EventHandler UPDATE_CC_SCORE = CCGROUP
            .server("updateOrganScore", () -> UpdateOrganScoreJS.class);
}
