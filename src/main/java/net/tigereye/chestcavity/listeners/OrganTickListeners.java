package net.tigereye.chestcavity.listeners;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.compat.kubejs.CCEvents;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCAttributes;
import net.tigereye.chestcavity.registration.CCDamageSources;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCStatusEffects;
import net.tigereye.chestcavity.util.NetworkUtil;

@Mod.EventBusSubscriber(modid = ChestCavity.MODID)
public class OrganTickListeners {
    public OrganTickListeners() {
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        TickClimbingAttribute(entity);
        if (entity.level().isClientSide) return;

        if (!(entity instanceof ChestCavityEntity ccEntity)) return;
        ChestCavityInstance cc = ccEntity.getChestCavityInstance();
        if (cc.updatePacket) NetworkUtil.SendS2CChestCavityUpdatePacket(cc, true);


        if (!cc.opened) return;
        if (cc.owner != null) CCEvents.postOpenedEntityTick(entity, cc);

        if (!entity.hasEffect(CCStatusEffects.ORGAN_PROTECTION.get())) {
            TickFiltration(entity, cc);
            TickHealth(entity, cc);
            TickIncompatibility(entity, cc);
        }
    }

    public static void TickClimbingAttribute(LivingEntity entity) {
        AttributeInstance climbSpeed = entity.getAttribute(CCAttributes.CLIMB_SPEED.get());
        if (climbSpeed == null) return;
        if (entity.horizontalCollision) {
            if (entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D) {
                entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x(), climbSpeed.getValue(), entity.getDeltaMovement().z()));
            }
        }
    }

    public static void TickHealth(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getOrganScore(CCOrganScores.HEALTH) <= 0F && cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.HEALTH) > 0F) {
            if (entity.level().getGameTime() % 20 == 0L) {
                entity.hurt(CCDamageSources.of(entity.level(), CCDamageSources.HEARTBLEED), Math.max(1.0F, entity.getMaxHealth() * 0.1F));
            }
        }
    }


    public static void TickFiltration(LivingEntity entity, ChestCavityInstance cc) {
        if (!entity.level().isClientSide() && !(cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.FILTRATION) <= 0.0F)) {
            float KidneyRatio = cc.getOrganScore(CCOrganScores.FILTRATION) / cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.FILTRATION);
            if (KidneyRatio < 1.0F) {
                ++cc.bloodPoisonTimer;
                if (cc.bloodPoisonTimer >= ChestCavity.config.KIDNEY_RATE) {
                    entity.addEffect(new MobEffectInstance(MobEffects.POISON, (int) Math.max(1.0F, 48.0F * (1.0F - KidneyRatio))));
                    cc.bloodPoisonTimer = 0;
                }
            }
        }

    }


    public static void TickIncompatibility(LivingEntity entity, ChestCavityInstance chestCavity) {
        if (!entity.level().isClientSide() && !ChestCavity.config.DISABLE_ORGAN_REJECTION) {
            float incompatibility = chestCavity.getOrganScore(CCOrganScores.INCOMPATIBILITY);
            if (incompatibility > 0.0F && !entity.hasEffect(CCStatusEffects.ORGAN_REJECTION.get())) {
                entity.addEffect(new MobEffectInstance(CCStatusEffects.ORGAN_REJECTION.get(), (int) ((float) ChestCavity.config.ORGAN_REJECTION_RATE / incompatibility), 0, false, true, true));
                entity.hurt(CCDamageSources.of(entity.level(), CCDamageSources.ORGAN_REJECTION), (float) ChestCavity.config.ORGAN_REJECTION_DAMAGE);
            }
        }
    }
}
