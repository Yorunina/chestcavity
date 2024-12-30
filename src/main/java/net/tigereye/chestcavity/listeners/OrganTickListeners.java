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
            if (ent.isCreative() && ent.getAbilities().flying) {
                return;
            }
        }

        if (!entity.onGround() && !entity.isNoGravity() && buoyancy != 0.0F) {
            entity.absMoveTo(0.0, (double)buoyancy * 0.02, 0.0);
        }

    }

    public static void TickCrystalsynthesis(LivingEntity entity, ChestCavityInstance cc) {
        float crystalsynthesis = cc.getOrganScore(CCOrganScores.CRYSTALSYNTHESIS);
        if (cc.connectedCrystal != null) {
            if (cc.connectedCrystal.isRemoved()) {
                entity.hurt(entity.damageSources().starve(), crystalsynthesis * 2.0F);
                cc.connectedCrystal = null;
            } else if (crystalsynthesis != 0.0F) {
                cc.connectedCrystal.setBeamTarget(entity.blockPosition().below(2));
            } else {
                cc.connectedCrystal.setBeamTarget(null);
                cc.connectedCrystal = null;
            }
        }

        if (crystalsynthesis != 0.0F && entity.level().getGameTime() % (long)ChestCavity.config.CRYSTALSYNTHESIS_FREQUENCY == 0L && !(entity instanceof EnderDragon)) {
            EndCrystal oldcrystal = cc.connectedCrystal;
            List<EndCrystal> list = entity.level().getEntitiesOfClass(EndCrystal.class, entity.getBoundingBox().inflate((double)ChestCavity.config.CRYSTALSYNTHESIS_RANGE));
            EndCrystal endCrystalEntity = null;
            double d = Double.MAX_VALUE;

            for (EndCrystal endCrystalEntity2 : list) {
                double e = endCrystalEntity2.distanceToSqr(entity);
                if (e < d) {
                    d = e;
                    endCrystalEntity = endCrystalEntity2;
                }
            }

            cc.connectedCrystal = endCrystalEntity;
            if (oldcrystal != null && oldcrystal != cc.connectedCrystal) {
                oldcrystal.setBeamTarget(null);
            }

            if (cc.connectedCrystal != null) {
                if (entity instanceof Player playerEntity) {
                    FoodData hungerManager = playerEntity.getFoodData();
                    if (hungerManager.needsFood()) {
                        if (crystalsynthesis >= 5.0F || (float)(entity.level().getGameTime() % ((long)ChestCavity.config.CRYSTALSYNTHESIS_FREQUENCY * 5L)) < (float)ChestCavity.config.CRYSTALSYNTHESIS_FREQUENCY * crystalsynthesis) {
                            hungerManager.eat(1, 0.0F);
                        }
                    } else if (hungerManager.getSaturationLevel() < (float)hungerManager.getFoodLevel()) {
                        hungerManager.eat(1, crystalsynthesis / 10.0F);
                    } else {
                        playerEntity.heal(crystalsynthesis / 5.0F);
                    }
                } else {
                    entity.heal(crystalsynthesis / 5.0F);
                }
            }
        }

    }

    public static void TickPhotosynthesis(LivingEntity entity, ChestCavityInstance cc) {
        if (!entity.level().isClientSide()) {
            float photosynthesis = cc.getOrganScore(CCOrganScores.PHOTOSYNTHESIS) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.PHOTOSYNTHESIS);
            if (photosynthesis > 0.0F) {
                cc.photosynthesisProgress = (int)((float)cc.photosynthesisProgress + photosynthesis * (float)entity.level().getLightEmission(entity.blockPosition()));
                if (cc.photosynthesisProgress > ChestCavity.config.PHOTOSYNTHESIS_FREQUENCY * 8 * 15) {
                    cc.photosynthesisProgress = 0;
                    if (entity instanceof Player) {
                        Player playerEntity = (Player)entity;
                        FoodData hungerManager = playerEntity.getFoodData();
                        if (hungerManager.needsFood()) {
                            hungerManager.eat(1, 0.0F);
                        } else if (hungerManager.getSaturationLevel() < (float)hungerManager.getFoodLevel()) {
                            hungerManager.eat(1, 0.5F);
                        } else {
                            playerEntity.heal(1.0F);
                        }
                    } else {
                        entity.heal(1.0F);
                    }
                }
            }
        }

    }

    public static void TickHealth(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getOrganScore(CCOrganScores.HEALTH) <= 0.0F && cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.HEALTH) != 0.0F) {
            if (entity.level().getGameTime() % (long)ChestCavity.config.HEARTBLEED_RATE == 0L) {
                ++cc.heartBleedTimer;
                entity.hurt(CCDamageSources.of(entity.level(), CCDamageSources.HEARTBLEED), Math.min((float)cc.heartBleedTimer, cc.getChestCavityType().getHeartBleedCap()));
            }
        } else {
            cc.heartBleedTimer = 0;
        }

    }

    public static void TickFiltration(LivingEntity entity, ChestCavityInstance cc) {
        if (!entity.level().isClientSide() && !(cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.FILTRATION) <= 0.0F)) {
            float KidneyRatio = cc.getOrganScore(CCOrganScores.FILTRATION) / cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.FILTRATION);
            if (KidneyRatio < 1.0F) {
                ++cc.bloodPoisonTimer;
                if (cc.bloodPoisonTimer >= ChestCavity.config.KIDNEY_RATE) {
                    entity.addEffect(new MobEffectInstance(MobEffects.POISON, (int)Math.max(1.0F, 48.0F * (1.0F - KidneyRatio))));
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
        if (!entity.level().isClientSide()) {
            float Hydroallergy = cc.getOrganScore(CCOrganScores.HYDROALLERGENIC);
            if (!(Hydroallergy <= 0.0F)) {
                if (entity.isInWater()) {
                    if (!entity.hasEffect((MobEffect)CCStatusEffects.WATER_VULNERABILITY.get())) {
                        entity.hurt(entity.damageSources().magic(), 10.0F);
                        entity.addEffect(new MobEffectInstance((MobEffect)CCStatusEffects.WATER_VULNERABILITY.get(), (int)(260.0F / Hydroallergy), 0, false, false, true));
                    }
                } else if (entity.isInWaterOrRain() && !entity.hasEffect((MobEffect)CCStatusEffects.WATER_VULNERABILITY.get())) {
                    entity.hurt(entity.damageSources().magic(), 1.0F);
                    entity.addEffect(new MobEffectInstance((MobEffect)CCStatusEffects.WATER_VULNERABILITY.get(), (int)(260.0F / Hydroallergy), 0, false, false, true));
                }
            }
        }

    }

    public static void TickHydrophobia(LivingEntity entity, ChestCavityInstance cc) {
        float hydrophobia = cc.getOrganScore(CCOrganScores.HYDROPHOBIA);
        if (!(hydrophobia <= 0.0F) && cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.HYDROPHOBIA) == 0.0F && entity.isInWaterOrRain()) {
            OrganUtil.teleportRandomly(entity, hydrophobia * 32.0F);
        }

    }

    public static void TickIncompatibility(LivingEntity entity, ChestCavityInstance chestCavity) {
        if (!entity.level().isClientSide() && !ChestCavity.config.DISABLE_ORGAN_REJECTION) {
            float incompatibility = chestCavity.getOrganScore(CCOrganScores.INCOMPATIBILITY);
            if (incompatibility > 0.0F && !entity.hasEffect((MobEffect)CCStatusEffects.ORGAN_REJECTION.get())) {
                entity.addEffect(new MobEffectInstance((MobEffect)CCStatusEffects.ORGAN_REJECTION.get(), (int)((float)ChestCavity.config.ORGAN_REJECTION_RATE / incompatibility), 0, false, true, true));
            }
        }

    }

    public static void TickGlowing(LivingEntity entity, ChestCavityInstance chestCavity) {
        if (!entity.level().isClientSide()) {
            float glowing = chestCavity.getOrganScore(CCOrganScores.GLOWING);
            if (glowing > 0.0F && !entity.hasEffect(MobEffects.GLOWING)) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, false, true, true));
            }
        }

    }
}
