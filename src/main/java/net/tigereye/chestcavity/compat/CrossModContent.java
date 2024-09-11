package net.tigereye.chestcavity.compat;

import net.minecraftforge.fml.ModList;
import net.tigereye.chestcavity.compat.requiem.CCRequiem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrossModContent {
    public static final Logger LOGGER = LogManager.getLogger();

    public CrossModContent() {
    }

    public static void register() {
        CCRequiem.register();
    }

    public static boolean checkIntegration(String modid, String name, boolean config) {
        if (ModList.get().isLoaded(modid)) {
            LOGGER.info("[Chest Cavity] " + name + " Detected!");
            if (config) {
                LOGGER.info("[Chest Cavity] Integrating with " + name);
                return true;
            }

            LOGGER.info("[Chest Cavity] " + name + " integration has been disabled in the config.");
        }

        return false;
    }
}
