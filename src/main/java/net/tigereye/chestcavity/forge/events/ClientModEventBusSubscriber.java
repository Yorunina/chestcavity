package net.tigereye.chestcavity.forge.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.tigereye.chestcavity.registration.CCKeybindings;

@EventBusSubscriber(
        modid = "chestcavity",
        value = {Dist.CLIENT},
        bus = Bus.MOD
)
public class ClientModEventBusSubscriber {
    public ClientModEventBusSubscriber() {
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(CCKeybindings.UTILITY_ABILITIES);
        event.register(CCKeybindings.SKILL_WHEEL);
        event.register(CCKeybindings.ATTACK_ABILITIES);
        event.register(CCKeybindings.CREEPY);
        event.register(CCKeybindings.DRAGON_BREATH);
        event.register(CCKeybindings.DRAGON_BOMBS);
        event.register(CCKeybindings.FORCEFUL_SPIT);
        event.register(CCKeybindings.FURNACE_POWERED);
        event.register(CCKeybindings.IRON_REPAIR);
        event.register(CCKeybindings.PYROMANCY);
        event.register(CCKeybindings.GHASTLY);
        event.register(CCKeybindings.GRAZING);
        event.register(CCKeybindings.SHULKER_BULLETS);
    }
}
