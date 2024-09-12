package net.tigereye.chestcavity.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;

public class CCKubejsPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        CCEvents.CCGROUP.register();
    }
}
