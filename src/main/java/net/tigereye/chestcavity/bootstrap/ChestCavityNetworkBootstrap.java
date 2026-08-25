package net.tigereye.chestcavity.bootstrap;

import net.tigereye.chestcavity.network.ChestCavityNetwork;

public final class ChestCavityNetworkBootstrap {
    private ChestCavityNetworkBootstrap() {
    }

    public static void initialize() {
        ChestCavityNetwork.register();
    }
}
