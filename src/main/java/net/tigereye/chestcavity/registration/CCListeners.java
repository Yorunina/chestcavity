package net.tigereye.chestcavity.registration;

import net.tigereye.chestcavity.listeners.LootRegister;
import net.tigereye.chestcavity.listeners.OrganActivationListeners;

public class CCListeners {
    public CCListeners() {
    }

    public static void register() {
        LootRegister.register();
        OrganActivationListeners.register();
    }
}
