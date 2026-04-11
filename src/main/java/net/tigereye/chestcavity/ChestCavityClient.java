package net.tigereye.chestcavity;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.tigereye.chestcavity.ui.ChestCavityItemScreen;
import net.tigereye.chestcavity.ui.ChestCavityScreen;

import static net.tigereye.chestcavity.ChestCavity.*;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChestCavityClient {
    public ChestCavityClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(CHEST_CAVITY_SCREEN_HANDLER.get(), ChestCavityScreen::new);
        MenuScreens.register(CHEST_CAVITY_ITEM_SCREEN_HANDLER.get(), ChestCavityItemScreen::new);
    }
}