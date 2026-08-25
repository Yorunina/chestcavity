package net.tigereye.chestcavity;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tigereye.chestcavity.bootstrap.ChestCavityConfigBootstrap;
import net.tigereye.chestcavity.bootstrap.ChestCavityIntegrationBootstrap;
import net.tigereye.chestcavity.bootstrap.ChestCavityNetworkBootstrap;
import net.tigereye.chestcavity.bootstrap.ChestCavityRegistryBootstrap;
import net.tigereye.chestcavity.config.CCConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(ChestCavity.MODID)
public class ChestCavity {
    public static final String MODID = "chestcavity";

    public static final Logger LOGGER = LogManager.getLogger();
    public static CCConfig config;

    public static final String COMPATIBILITY_TAG = MODID + ":organ_compatibility";
    public static boolean KUBEJS_LOADED = false;

    public ChestCavity() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ChestCavityConfigBootstrap.initialize();
        ChestCavityRegistryBootstrap.register(eventBus);
        ChestCavityNetworkBootstrap.initialize();
        ChestCavityIntegrationBootstrap.initialize();
    }
}
