package net.tigereye.chestcavity;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.tigereye.chestcavity.registration.CCKeybindings;
import net.tigereye.chestcavity.ui.ChestCavityItemScreen;
import net.tigereye.chestcavity.ui.ChestCavityScreen;

import static net.tigereye.chestcavity.ChestCavity.*;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChestCavityClient {
    public ChestCavityClient() {
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(CCKeybindings.UTILITY_ABILITIES);
        event.register(CCKeybindings.ATTACK_ABILITIES);
        event.register(CCKeybindings.CREEPY);
        event.register(CCKeybindings.DRAGON_BREATH);
        event.register(CCKeybindings.DRAGON_BOMBS);
        event.register(CCKeybindings.FORCEFUL_SPIT);
        event.register(CCKeybindings.FURNACE_POWERED);
        event.register(CCKeybindings.PYROMANCY);
        event.register(CCKeybindings.GHASTLY);
        event.register(CCKeybindings.GRAZING);
        event.register(CCKeybindings.SHULKER_BULLETS);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(CHEST_CAVITY_SCREEN_HANDLER.get(), ChestCavityScreen::new);
        MenuScreens.register(CHEST_CAVITY_ITEM_SCREEN_HANDLER.get(), ChestCavityItemScreen::new);
    }
}