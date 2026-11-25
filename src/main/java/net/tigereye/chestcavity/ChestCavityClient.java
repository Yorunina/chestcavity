package net.tigereye.chestcavity;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.tigereye.chestcavity.client.CultureVatRenderer;
import net.tigereye.chestcavity.registration.CCBlockEntities;
import net.tigereye.chestcavity.registration.CCBlocks;
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
        ItemBlockRenderTypes.setRenderLayer(CCBlocks.CULTURE_VAT.get(), RenderType.translucent());
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(CCBlockEntities.CULTURE_VAT.get(), CultureVatRenderer::new);
    }
}