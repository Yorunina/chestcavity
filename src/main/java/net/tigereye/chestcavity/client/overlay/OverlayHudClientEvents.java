package net.tigereye.chestcavity.client.overlay;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tigereye.chestcavity.ChestCavity;

@Mod.EventBusSubscriber(modid = ChestCavity.MODID, value = Dist.CLIENT)
public final class OverlayHudClientEvents {
    private OverlayHudClientEvents() {
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        OverlayHudClient.resetSession();
    }
}
