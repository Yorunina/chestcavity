package net.tigereye.chestcavity.listeners;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.registration.CCOrganScores;

public class OrganOnHitListeners {
    public OrganOnHitListeners() {
    }

    public static void call(LivingEntity attacker, LivingEntity target, ChestCavityInstance cc) {
        TickLaunching(attacker, target, cc);
    }

    private static void TickLaunching(LivingEntity attacker, LivingEntity target, ChestCavityInstance cc) {
        float launchingDiff = cc.getOrganScore(CCOrganScores.LAUNCHING) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.LAUNCHING);
        if (launchingDiff != 0.0F && attacker.closerThan(target, 4.0)) {
            double KBRes = target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            target.absMoveTo(0.0, Math.max(0.0, (double)(ChestCavity.config.LAUNCHING_POWER * launchingDiff) * (1.0 - KBRes)), 0.0);
        }

    }
}
