package net.tigereye.chestcavity.bootstrap;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.config.CCConfig;

public final class ChestCavityConfigBootstrap {
    private ChestCavityConfigBootstrap() {
    }

    public static void initialize() {
        AutoConfig.register(CCConfig.class, GsonConfigSerializer::new);
        ChestCavity.config = AutoConfig.getConfigHolder(CCConfig.class).getConfig();
    }
}
