package net.tigereye.chestcavity.bootstrap;

import net.minecraftforge.fml.ModList;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.compat.ftb.ChestCavityQuestEventHandler;

public final class ChestCavityIntegrationBootstrap {
    private ChestCavityIntegrationBootstrap() {
    }

    public static void initialize() {
        ChestCavity.KUBEJS_LOADED = ModList.get().isLoaded("kubejs");
        if (ModList.get().isLoaded("ftbquests")) {
            ChestCavityQuestEventHandler.getInstance().init();
        }
    }
}
