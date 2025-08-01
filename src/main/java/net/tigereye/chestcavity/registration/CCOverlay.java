package net.tigereye.chestcavity.registration;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tigereye.chestcavity.ui.SkillWheelOverlay;

@Mod.EventBusSubscriber(Dist.CLIENT)

public class CCOverlay {
    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.PLAYER_LIST.id(), "skill_wheel", SkillWheelOverlay.instance);
    }
}