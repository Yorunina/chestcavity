package net.tigereye.chestcavity.compat.requiem;

import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.compat.CrossModContent;

public class CCRequiem {
    public static String MODID = "requiem";
    public static String NAME = "Requiem";
    public static boolean REQUIEM_ACTIVE = false;
    public static ResourceLocation PLAYER_SHELL_ID;

    public CCRequiem() {
    }

    public static void register() {
        if (CrossModContent.checkIntegration(MODID, NAME, ChestCavity.config.REQUIEM_INTEGRATION)) {
            REQUIEM_ACTIVE = true;
        }

    }

    static {
        PLAYER_SHELL_ID = new ResourceLocation(MODID, "player_shell");
    }
}
