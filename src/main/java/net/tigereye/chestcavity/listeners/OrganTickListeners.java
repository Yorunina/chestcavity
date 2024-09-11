package net.tigereye.chestcavity.listeners;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.registration.CCDamageSources;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCStatusEffects;
import net.tigereye.chestcavity.util.OrganUtil;

public class OrganTickListeners {
    public OrganTickListeners() {
    }

    public static void call(LivingEntity entity, ChestCavityInstance cc) {
        TickIncompatibility(entity, cc);
        TickProjectileQueue(entity, cc);
        TickHealth(entity, cc);
        TickFiltration(entity, cc);
        TickBuoyant(entity, cc);
        TickCrystalsynthesis(entity, cc);
        TickPhotosynthesis(entity, cc);
        TickHydroallergenic(entity, cc);
        TickHydrophobia(entity, cc);
        TickGlowing(entity, cc);
    }

    public static void TickBuoyant(LivingEntity entity, ChestCavityInstance chestCavity) {
        float buoyancy = chestCavity.getOrganScore(CCOrganScores.BUOYANT) - chestCavity.getChestCavityType().getDefaultOrganScore(CCOrganScores.BUOYANT);
        if (entity instanceof Player ent) {
            if (ent.m_7500_() && ent.m_150110_().f_35935_) {
                return;
            }
        }

        if (!entity.m_20096_() && !entity.m_20068_() && buoyancy != 0.0F) {
            entity.m_5997_(0.0, (double)buoyancy * 0.02, 0.0);
        }

    }

    public static void TickCrystalsynthesis(LivingEntity entity, ChestCavityInstance cc) {
        float crystalsynthesis = cc.getOrganScore(CCOrganScores.CRYSTALSYNTHESIS);
        if (cc.connectedCrystal != null) {
            if (cc.connectedCrystal.m_213877_()) {
                entity.m_6469_(entity.m_269291_().m_269064_(), crystalsynthesis * 2.0F);
                cc.connectedCrystal = null;
            } else if (crystalsynthesis != 0.0F) {
                cc.connectedCrystal.m_31052_(entity.m_20183_().m_6625_(2));
            } else {
                cc.connectedCrystal.m_31052_((BlockPos)null);
                cc.connectedCrystal = null;
            }
        }

        if (crystalsynthesis != 0.0F && entity.m_9236_().m_46467_() % (long)ChestCavity.config.CRYSTALSYNTHESIS_FREQUENCY == 0L && !(entity instanceof EnderDragon)) {
            EndCrystal oldcrystal = cc.connectedCrystal;
            List<EndCrystal> list = entity.m_9236_().m_45976_(EndCrystal.class, entity.m_20191_().m_82400_((double)ChestCavity.config.CRYSTALSYNTHESIS_RANGE));
            EndCrystal endCrystalEntity = null;
            double d = Double.MAX_VALUE;
            Iterator<EndCrystal> var8 = list.iterator();

            while(var8.hasNext()) {
                EndCrystal endCrystalEntity2 = (EndCrystal)var8.next();
                double e = endCrystalEntity2.m_20280_(entity);
                if (e < d) {
                    d = e;
                    endCrystalEntity = endCrystalEntity2;
                }
            }

            cc.connectedCrystal = endCrystalEntity;
            if (oldcrystal != null && oldcrystal != cc.connectedCrystal) {
                oldcrystal.m_31052_((BlockPos)null);
            }

            if (cc.connectedCrystal != null) {
                if (entity instanceof Player) {
                    Player playerEntity = (Player)entity;
                    FoodData hungerManager = playerEntity.m_36324_();
                    if (hungerManager.m_38721_()) {
                        if (crystalsynthesis >= 5.0F || (float)(entity.m_9236_().m_46467_() % ((long)ChestCavity.config.CRYSTALSYNTHESIS_FREQUENCY * 5L)) < (float)ChestCavity.config.CRYSTALSYNTHESIS_FREQUENCY * crystalsynthesis) {
                            hungerManager.m_38707_(1, 0.0F);
                        }
                    } else if (hungerManager.m_38722_() < (float)hungerManager.m_38702_()) {
                        hungerManager.m_38707_(1, crystalsynthesis / 10.0F);
                    } else {
                        playerEntity.m_5634_(crystalsynthesis / 5.0F);
                    }
                } else {
                    entity.m_5634_(crystalsynthesis / 5.0F);
                }
            }
        }

    }

    public static void TickPhotosynthesis(LivingEntity entity, ChestCavityInstance cc) {
        if (!entity.m_9236_().m_5776_()) {
            float photosynthesis = cc.getOrganScore(CCOrganScores.PHOTOSYNTHESIS) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.PHOTOSYNTHESIS);
            if (photosynthesis > 0.0F) {
                cc.photosynthesisProgress = (int)((float)cc.photosynthesisProgress + photosynthesis * (float)entity.m_9236_().m_46803_(entity.m_20183_()));
                if (cc.photosynthesisProgress > ChestCavity.config.PHOTOSYNTHESIS_FREQUENCY * 8 * 15) {
                    cc.photosynthesisProgress = 0;
                    if (entity instanceof Player) {
                        Player playerEntity = (Player)entity;
                        FoodData hungerManager = playerEntity.m_36324_();
                        if (hungerManager.m_38721_()) {
                            hungerManager.m_38707_(1, 0.0F);
                        } else if (hungerManager.m_38722_() < (float)hungerManager.m_38702_()) {
                            hungerManager.m_38707_(1, 0.5F);
                        } else {
                            playerEntity.m_5634_(1.0F);
                        }
                    } else {
                        entity.m_5634_(1.0F);
                    }
                }
            }
        }

    }

    public static void TickHealth(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getOrganScore(CCOrganScores.HEALTH) <= 0.0F && cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.HEALTH) != 0.0F) {
            if (entity.m_9236_().m_46467_() % (long)ChestCavity.config.HEARTBLEED_RATE == 0L) {
                ++cc.heartBleedTimer;
                entity.m_6469_(CCDamageSources.of(entity.m_9236_(), CCDamageSources.HEARTBLEED), Math.min((float)cc.heartBleedTimer, cc.getChestCavityType().getHeartBleedCap()));
            }
        } else {
            cc.heartBleedTimer = 0;
        }

    }

    public static void TickFiltration(LivingEntity entity, ChestCavityInstance cc) {
        if (!entity.m_20193_().m_5776_() && !(cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.FILTRATION) <= 0.0F)) {
            float KidneyRatio = cc.getOrganScore(CCOrganScores.FILTRATION) / cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.FILTRATION);
            if (KidneyRatio < 1.0F) {
                ++cc.bloodPoisonTimer;
                if (cc.bloodPoisonTimer >= ChestCavity.config.KIDNEY_RATE) {
                    entity.m_7292_(new MobEffectInstance(MobEffects.f_19614_, (int)Math.max(1.0F, 48.0F * (1.0F - KidneyRatio))));
                    cc.bloodPoisonTimer = 0;
                }
            }
        }

    }

    private static void TickProjectileQueue(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.projectileCooldown > 0) {
            --cc.projectileCooldown;
        } else if (!cc.projectileQueue.isEmpty()) {
            cc.projectileCooldown = 5;
            ((Consumer)cc.projectileQueue.pop()).accept(entity);
        }

    }

    private static void TickHydroallergenic(LivingEntity entity, ChestCavityInstance cc) {
        if (!entity.m_20193_().m_5776_()) {
            float Hydroallergy = cc.getOrganScore(CCOrganScores.HYDROALLERGENIC);
            if (!(Hydroallergy <= 0.0F)) {
                if (entity.m_5842_()) {
                    if (!entity.m_21023_((MobEffect)CCStatusEffects.WATER_VULNERABILITY.get())) {
                        entity.m_6469_(entity.m_269291_().m_269425_(), 10.0F);
                        entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.WATER_VULNERABILITY.get(), (int)(260.0F / Hydroallergy), 0, false, false, true));
                    }
                } else if (entity.m_20070_() && !entity.m_21023_((MobEffect)CCStatusEffects.WATER_VULNERABILITY.get())) {
                    entity.m_6469_(entity.m_269291_().m_269425_(), 1.0F);
                    entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.WATER_VULNERABILITY.get(), (int)(260.0F / Hydroallergy), 0, false, false, true));
                }
            }
        }

    }

    public static void TickHydrophobia(LivingEntity entity, ChestCavityInstance cc) {
        float hydrophobia = cc.getOrganScore(CCOrganScores.HYDROPHOBIA);
        if (!(hydrophobia <= 0.0F) && cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.HYDROPHOBIA) == 0.0F && entity.m_20070_()) {
            OrganUtil.teleportRandomly(entity, hydrophobia * 32.0F);
        }

    }

    public static void TickIncompatibility(LivingEntity entity, ChestCavityInstance chestCavity) {
        if (!entity.m_20193_().m_5776_() && !ChestCavity.config.DISABLE_ORGAN_REJECTION) {
            float incompatibility = chestCavity.getOrganScore(CCOrganScores.INCOMPATIBILITY);
            if (incompatibility > 0.0F && !entity.m_21023_((MobEffect)CCStatusEffects.ORGAN_REJECTION.get())) {
                entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.ORGAN_REJECTION.get(), (int)((float)ChestCavity.config.ORGAN_REJECTION_RATE / incompatibility), 0, false, true, true));
            }
        }

    }

    public static void TickGlowing(LivingEntity entity, ChestCavityInstance chestCavity) {
        if (!entity.m_20193_().m_5776_()) {
            float glowing = chestCavity.getOrganScore(CCOrganScores.GLOWING);
            if (glowing > 0.0F && !entity.m_21023_(MobEffects.f_19619_)) {
                entity.m_7292_(new MobEffectInstance(MobEffects.f_19619_, 200, 0, false, true, true));
            }
        }

    }
}
