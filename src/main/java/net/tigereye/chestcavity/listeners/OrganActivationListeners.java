package net.tigereye.chestcavity.listeners;

import it.unimi.dsi.fastutil.ints.IntComparators;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeHooks;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCStatusEffects;
import net.tigereye.chestcavity.registration.CCTags;
import net.tigereye.chestcavity.util.ChestCavityUtil;
import net.tigereye.chestcavity.util.OrganUtil;

public class OrganActivationListeners {
    private static final Map<ResourceLocation, BiConsumer<LivingEntity, ChestCavityInstance>> abilityIDMap = new HashMap();

    public OrganActivationListeners() {
    }

    public static void register() {
        register(CCOrganScores.CREEPY, OrganActivationListeners::ActivateCreepy);
        register(CCOrganScores.DRAGON_BREATH, OrganActivationListeners::ActivateDragonBreath);
        register(CCOrganScores.DRAGON_BOMBS, OrganActivationListeners::ActivateDragonBombs);
        register(CCOrganScores.FORCEFUL_SPIT, OrganActivationListeners::ActivateForcefulSpit);
        register(CCOrganScores.FURNACE_POWERED, OrganActivationListeners::ActivateFurnacePowered);
        register(CCOrganScores.IRON_REPAIR, OrganActivationListeners::ActivateIronRepair);
        register(CCOrganScores.PYROMANCY, OrganActivationListeners::ActivatePyromancy);
        register(CCOrganScores.GHASTLY, OrganActivationListeners::ActivateGhastly);
        register(CCOrganScores.GRAZING, OrganActivationListeners::ActivateGrazing);
        register(CCOrganScores.SHULKER_BULLETS, OrganActivationListeners::ActivateShulkerBullets);
        register(CCOrganScores.SILK, OrganActivationListeners::ActivateSilk);
    }

    public static void register(ResourceLocation id, BiConsumer<LivingEntity, ChestCavityInstance> ability) {
        abilityIDMap.put(id, ability);
    }

    public static boolean activate(ResourceLocation id, ChestCavityInstance cc) {
        if (abilityIDMap.containsKey(id)) {
            ((BiConsumer)abilityIDMap.get(id)).accept(cc.owner, cc);
            return true;
        } else {
            return false;
        }
    }

    public static void ActivateCreepy(LivingEntity entity, ChestCavityInstance cc) {
        if (!(cc.getOrganScore(CCOrganScores.CREEPY) < 1.0F) && !entity.m_21023_((MobEffect)CCStatusEffects.EXPLOSION_COOLDOWN.get())) {
            float explosion_yield = cc.getOrganScore(CCOrganScores.EXPLOSIVE);
            ChestCavityUtil.destroyOrgansWithKey(cc, CCOrganScores.EXPLOSIVE);
            OrganUtil.explode(entity, explosion_yield);
            if (entity.m_6084_()) {
                entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.EXPLOSION_COOLDOWN.get(), ChestCavity.config.EXPLOSION_COOLDOWN, 0, false, false, true));
            }
        }

    }

    public static void ActivateDragonBreath(LivingEntity entity, ChestCavityInstance cc) {
        float breath = cc.getOrganScore(CCOrganScores.DRAGON_BREATH);
        if (entity instanceof Player) {
            ((Player)entity).m_36399_(breath * 0.6F);
        }

        if (!(breath <= 0.0F) && !entity.m_21023_((MobEffect)CCStatusEffects.DRAGON_BREATH_COOLDOWN.get())) {
            entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.DRAGON_BREATH_COOLDOWN.get(), ChestCavity.config.DRAGON_BREATH_COOLDOWN, 0, false, false, true));
            cc.projectileQueue.add(OrganUtil::spawnDragonBreath);
        }

    }

    public static void ActivateDragonBombs(LivingEntity entity, ChestCavityInstance cc) {
        float projectiles = cc.getOrganScore(CCOrganScores.DRAGON_BOMBS);
        if (!(projectiles < 1.0F) && !entity.m_21023_((MobEffect)CCStatusEffects.DRAGON_BOMB_COOLDOWN.get())) {
            OrganUtil.queueDragonBombs(entity, cc, (int)projectiles);
        }

    }

    public static void ActivateForcefulSpit(LivingEntity entity, ChestCavityInstance cc) {
        float projectiles = cc.getOrganScore(CCOrganScores.FORCEFUL_SPIT);
        if (!(projectiles < 1.0F) && !entity.m_21023_((MobEffect)CCStatusEffects.FORCEFUL_SPIT_COOLDOWN.get())) {
            OrganUtil.queueForcefulSpit(entity, cc, (int)projectiles);
        }

    }

    public static void ActivateFurnacePowered(LivingEntity entity, ChestCavityInstance cc) {
        int furnacePowered = Math.round(cc.getOrganScore(CCOrganScores.FURNACE_POWERED));
        if (furnacePowered >= 1) {
            int fuelValue = 0;
            ItemStack itemStack = cc.owner.m_6844_(EquipmentSlot.MAINHAND);
            if (itemStack != null && itemStack != ItemStack.f_41583_) {
                try {
                    fuelValue = ForgeHooks.getBurnTime(itemStack, (RecipeType)null);
                } catch (Exception var13) {
                }
            }

            if (fuelValue == 0) {
                itemStack = cc.owner.m_6844_(EquipmentSlot.OFFHAND);
                if (itemStack != null && itemStack != ItemStack.f_41583_) {
                    try {
                        fuelValue = ForgeHooks.getBurnTime(itemStack, (RecipeType)null);
                    } catch (Exception var12) {
                    }
                }
            }

            if (fuelValue != 0) {
                MobEffectInstance newSEI = null;
                if (!cc.owner.m_21023_((MobEffect)CCStatusEffects.FURNACE_POWER.get())) {
                    newSEI = new MobEffectInstance((MobEffect)CCStatusEffects.FURNACE_POWER.get(), fuelValue, 0, false, false, true);
                } else {
                    MobEffectInstance oldPower = cc.owner.m_21124_((MobEffect)CCStatusEffects.FURNACE_POWER.get());
                    if (oldPower.m_19564_() >= furnacePowered - 1) {
                        return;
                    }

                    CompoundTag oldTag = new CompoundTag();
                    List<Integer> durations = new ArrayList();
                    durations.add(fuelValue);
                    oldPower.m_19555_(oldTag);

                    while(true) {
                        durations.add(oldTag.m_128451_("Duration"));
                        if (!oldTag.m_128441_("HiddenEffect")) {
                            durations.sort(IntComparators.OPPOSITE_COMPARATOR);
                            int amplifier = 0;

                            for(Iterator<Integer> var10 = durations.iterator(); var10.hasNext(); ++amplifier) {
                                Integer duration = (Integer)var10.next();
                                newSEI = new MobEffectInstance((MobEffect)CCStatusEffects.FURNACE_POWER.get(), duration, amplifier, false, false, true, newSEI, Optional.empty());
                            }
                            break;
                        }

                        oldTag = oldTag.m_128469_("HiddenEffect");
                    }
                }

                entity.m_21195_((MobEffect)CCStatusEffects.FURNACE_POWER.get());
                entity.m_7292_(newSEI);
                itemStack.m_41774_(1);
            }
        }

    }

    public static void ActivateIronRepair(LivingEntity entity, ChestCavityInstance cc) {
        float ironRepair = cc.getOrganScore(CCOrganScores.IRON_REPAIR) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.IRON_REPAIR);
        if (!(ironRepair <= 0.0F) && !cc.owner.m_21023_((MobEffect)CCStatusEffects.IRON_REPAIR_COOLDOWN.get()) && !(cc.owner.m_21223_() >= cc.owner.m_21233_())) {
            ItemStack itemStack = cc.owner.m_6844_(EquipmentSlot.MAINHAND);
            if (itemStack == null || !itemStack.m_204117_(CCTags.IRON_REPAIR_MATERIAL)) {
                itemStack = cc.owner.m_6844_(EquipmentSlot.OFFHAND);
                if (itemStack == null || !itemStack.m_204117_(CCTags.IRON_REPAIR_MATERIAL)) {
                    return;
                }
            }

            cc.owner.m_5634_(cc.owner.m_21233_() * ChestCavity.config.IRON_REPAIR_PERCENT);
            entity.m_5496_(SoundEvents.f_12009_, 0.75F, 1.0F);
            cc.owner.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.IRON_REPAIR_COOLDOWN.get(), (int)((float)ChestCavity.config.IRON_REPAIR_COOLDOWN / ironRepair), 0, false, false, true));
            itemStack.m_41774_(1);
        }

    }

    public static void ActivateGhastly(LivingEntity entity, ChestCavityInstance cc) {
        float ghastly = cc.getOrganScore(CCOrganScores.GHASTLY);
        if (!(ghastly < 1.0F) && !entity.m_21023_((MobEffect)CCStatusEffects.GHASTLY_COOLDOWN.get())) {
            OrganUtil.queueGhastlyFireballs(entity, cc, (int)ghastly);
        }

    }

    private static void ActivateGrazing(LivingEntity entity, ChestCavityInstance cc) {
        float grazing = cc.getOrganScore(CCOrganScores.GRAZING);
        if (!(grazing <= 0.0F)) {
            BlockPos blockPos = entity.m_20183_().m_7495_();
            boolean ateGrass = false;
            if (!entity.m_9236_().m_8055_(blockPos).m_60713_(Blocks.f_50440_) && !entity.m_9236_().m_8055_(blockPos).m_60713_(Blocks.f_50195_)) {
                if (entity.m_9236_().m_8055_(blockPos).m_60713_(Blocks.f_50699_) || entity.m_9236_().m_8055_(blockPos).m_60713_(Blocks.f_50690_)) {
                    entity.m_9236_().m_7731_(blockPos, Blocks.f_50134_.m_49966_(), 2);
                    ateGrass = true;
                }
            } else {
                entity.m_9236_().m_7731_(blockPos, Blocks.f_50493_.m_49966_(), 2);
                ateGrass = true;
            }

            if (ateGrass) {
                int duration;
                if (entity.m_21023_((MobEffect)CCStatusEffects.RUMINATING.get())) {
                    MobEffectInstance ruminating = entity.m_21124_((MobEffect)CCStatusEffects.RUMINATING.get());
                    duration = (int)Math.min((float)(ChestCavity.config.RUMINATION_TIME * ChestCavity.config.RUMINATION_GRASS_PER_SQUARE * ChestCavity.config.RUMINATION_SQUARES_PER_STOMACH) * grazing, (float)(ruminating.m_19557_() + ChestCavity.config.RUMINATION_TIME * ChestCavity.config.RUMINATION_GRASS_PER_SQUARE));
                } else {
                    duration = ChestCavity.config.RUMINATION_TIME * ChestCavity.config.RUMINATION_GRASS_PER_SQUARE;
                }

                entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.RUMINATING.get(), duration, 0, false, false, true));
            }
        }

    }

    public static void ActivatePyromancy(LivingEntity entity, ChestCavityInstance cc) {
        float pyromancy = cc.getOrganScore(CCOrganScores.PYROMANCY);
        if (!(pyromancy < 1.0F) && !entity.m_21023_((MobEffect)CCStatusEffects.PYROMANCY_COOLDOWN.get())) {
            OrganUtil.queuePyromancyFireballs(entity, cc, (int)pyromancy);
        }

    }

    public static void ActivateShulkerBullets(LivingEntity entity, ChestCavityInstance cc) {
        float projectiles = cc.getOrganScore(CCOrganScores.SHULKER_BULLETS);
        if (!(projectiles < 1.0F) && !entity.m_21023_((MobEffect)CCStatusEffects.SHULKER_BULLET_COOLDOWN.get())) {
            OrganUtil.queueShulkerBullets(entity, cc, (int)projectiles);
        }

    }

    public static void ActivateSilk(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getOrganScore(CCOrganScores.SILK) != 0.0F && !entity.m_21023_((MobEffect)CCStatusEffects.SILK_COOLDOWN.get()) && OrganUtil.spinWeb(entity, cc, cc.getOrganScore(CCOrganScores.SILK))) {
            entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.SILK_COOLDOWN.get(), ChestCavity.config.SILK_COOLDOWN, 0, false, false, true));
        }

    }
}
