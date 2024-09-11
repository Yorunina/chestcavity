package net.tigereye.chestcavity;

import net.tigereye.chestcavity.listeners.KeybindingClientListeners;
import net.tigereye.chestcavity.registration.CCKeybindings;
import net.tigereye.chestcavity.registration.CCNetworkingPackets;

public class ChestCavityClient {
    public ChestCavityClient() {
    }

    public static void onInitializeClient() {
        CCNetworkingPackets.registerClient();
        CCKeybindings.init();
        KeybindingClientListeners.register();
    }
}
