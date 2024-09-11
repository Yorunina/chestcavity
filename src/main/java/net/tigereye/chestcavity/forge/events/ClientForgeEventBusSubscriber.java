package net.tigereye.chestcavity.forge.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.tigereye.chestcavity.forge.EventHelper;

@EventBusSubscriber(
        modid = "chestcavity",
        value = {Dist.CLIENT},
        bus = Bus.FORGE
)
public class ClientForgeEventBusSubscriber {
    public ClientForgeEventBusSubscriber() {
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        EventHelper.executeEventList(EventHelper.CLIENT_TICK, event);
    }
}
