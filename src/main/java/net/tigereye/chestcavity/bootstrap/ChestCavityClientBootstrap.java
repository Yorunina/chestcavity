package net.tigereye.chestcavity.bootstrap;

import net.minecraft.client.gui.screens.MenuScreens;
import net.tigereye.chestcavity.registration.CCRegistries;
import net.tigereye.chestcavity.ui.ChestCavityItemScreen;
import net.tigereye.chestcavity.ui.ChestCavityScreen;

public final class ChestCavityClientBootstrap {
    private ChestCavityClientBootstrap() {
    }

    public static void registerScreens() {
        MenuScreens.register(CCRegistries.CHEST_CAVITY_SCREEN_HANDLER.get(), ChestCavityScreen::new);
        MenuScreens.register(CCRegistries.CHEST_CAVITY_ITEM_SCREEN_HANDLER.get(), ChestCavityItemScreen::new);
    }
}
