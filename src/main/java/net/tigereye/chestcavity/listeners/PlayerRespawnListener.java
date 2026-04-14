package net.tigereye.chestcavity.listeners;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCStatusEffects;

import java.util.Optional;

@Mod.EventBusSubscriber
public class PlayerRespawnListener {
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        Optional<ChestCavityEntity> ccEntityOpt = ChestCavityEntity.of(player);
        if (ccEntityOpt.isEmpty()) return;
        ChestCavityInstance ccInstance = ccEntityOpt.get().getChestCavityInstance();
        if (ccInstance.getOrganScore(CCOrganScores.HEALTH) <= 0 || ccInstance.getOrganScore(CCOrganScores.BREATH_RECOVERY) <= 0 || ccInstance.getOrganScore(CCOrganScores.INCOMPATIBILITY) > 0) {
            player.addEffect(new MobEffectInstance(CCStatusEffects.ORGAN_PROTECTION.get(), 20 * 60, 0, false, false));
        }
    }
}
