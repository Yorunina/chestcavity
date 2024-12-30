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
            (abilityIDMap.get(id)).accept(cc.owner, cc);
            return true;
        } else {
            return false;
        }
    }

    public static void ActivateCreepy(LivingEntity entity, ChestCavityInstance cc) {
        if (!(cc.getOrganScore(CCOrganScores.CREEPY) < 1.0F) && !entity.hasEffect((MobEffect)CCStatusEffects.EXPLOSION_COOLDOWN.get())) {
            float explosion_yield = cc.getOrganScore(CCOrganScores.EXPLOSIVE);
            ChestCavityUtil.destroyOrgansWithKey(cc, CCOrganScores.EXPLOSIVE);
            OrganUtil.explode(entity, explosion_yield);
            if (entity.isAlive()) {
                entity.addEffect(new MobEffectInstance((MobEffect)CCStatusEffects.EXPLOSION_COOLDOWN.get(), ChestCavity.config.EXPLOSION_COOLDOWN, 0, false, false, true));
            }
        }

    }

    public static void ActivateDragonBreath(LivingEntity entity, ChestCavityInstance cc) {
        float breath = cc.getOrganScore(CCOrganScores.DRAGON_BREATH);
        if (entity instanceof Player) {
            ((Player)entity).causeFoodExhaustion(breath * 0.6F);
        }

        if (!(breath <= 0.0F) && !entity.hasEffect((MobEffect)CCStatusEffects.DRAGON_BREATH_COOLDOWN.get())) {
            entity.addEffect(new MobEffectInstance((MobEffect)CCStatusEffects.DRAGON_BREATH_COOLDOWN.get(), ChestCavity.config.DRAGON_BREATH_COOLDOWN, 0, false, false, true));
            cc.projectileQueue.add(OrganUtil::spawnDragonBreath);
        }

    }

    public static void ActivateDragonBombs(LivingEntity entity, ChestCavityInstance cc) {
        float projectiles = cc.getOrganScore(CCOrganScores.DRAGON_BOMBS);
        if (!(projectiles < 1.0F) && !entity.hasEffect((MobEffect)CCStatusEffects.DRAGON_BOMB_COOLDOWN.get())) {
            OrganUtil.queueDragonBombs(entity, cc, (int)projectiles);
        }

    }

    public static void ActivateForcefulSpit(LivingEntity entity, ChestCavityInstance cc) {
        float projectiles = cc.getOrganScore(CCOrganScores.FORCEFUL_SPIT);
        if (!(projectiles < 1.0F) && !entity.hasEffect((MobEffect)CCStatusEffects.FORCEFUL_SPIT_COOLDOWN.get())) {
            OrganUtil.queueForcefulSpit(entity, cc, (int)projectiles);
        }

    }

    public static void ActivateFurnacePowered(LivingEntity entity, ChestCavityInstance cc) {
        int furnacePowered = Math.round(cc.getOrganScore(CCOrganScores.FURNACE_POWERED));
        if (furnacePowered >= 1) {
            int fuelValue = 0;
            ItemStack itemStack = cc.owner.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemStack != null && itemStack != ItemStack.EMPTY) {
                try {
                    fuelValue = ForgeHooks.getBurnTime(itemStack, (RecipeType)null);
                } catch (Exception var13) {
                }
            }

            if (fuelValue == 0) {
                itemStack = cc.owner.getItemBySlot(EquipmentSlot.OFFHAND);
                if (itemStack != null && itemStack != ItemStack.EMPTY) {
                    try {
                        fuelValue = ForgeHooks.getBurnTime(itemStack, (RecipeType)null);
                    } catch (Exception var12) {
                    }
                }
            }

            if (fuelValue != 0) {
                MobEffectInstance newSEI = null;
                if (!cc.owner.hasEffect((MobEffect)CCStatusEffects.FURNACE_POWER.get())) {
                    newSEI = new MobEffectInstance((MobEffect)CCStatusEffects.FURNACE_POWER.get(), fuelValue, 0, false, false, true);
                } else {
                    MobEffectInstance oldPower = cc.owner.getEffect((MobEffect)CCStatusEffects.FURNACE_POWER.get());
                    if (oldPower.getAmplifier() >= furnacePowered - 1) {
                        return;
                    }

                    CompoundTag oldTag = new CompoundTag();
                    List<Integer> durations = new ArrayList();
                    durations.add(fuelValue);
                    oldPower.save(oldTag);

                    while(true) {
                        durations.add(oldTag.getInt("Duration"));
                        if (!oldTag.contains("HiddenEffect")) {
                            durations.sort(IntComparators.OPPOSITE_COMPARATOR);
                            int amplifier = 0;

                            for(Iterator<Integer> var10 = durations.iterator(); var10.hasNext(); ++amplifier) {
                                Integer duration = (Integer)var10.next();
                                newSEI = new MobEffectInstance((MobEffect)CCStatusEffects.FURNACE_POWER.get(), duration, amplifier, false, false, true, newSEI, Optional.empty());
                            }
                            break;
                        }

                        oldTag = oldTag.getCompound("HiddenEffect");
                    }
                }

                entity.removeEffect((MobEffect)CCStatusEffects.FURNACE_POWER.get());
                entity.addEffect(newSEI);
                itemStack.shrink(1);
            }
        }

    }

    public static void ActivateIronRepair(LivingEntity entity, ChestCavityInstance cc) {
        float ironRepair = cc.getOrganScore(CCOrganScores.IRON_REPAIR) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.IRON_REPAIR);
        if (!(ironRepair <= 0.0F) && !cc.owner.hasEffect((MobEffect)CCStatusEffects.IRON_REPAIR_COOLDOWN.get()) && !(cc.owner.getHealth() >= cc.owner.getMaxHealth())) {
            ItemStack itemStack = cc.owner.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemStack == null || !itemStack.is(CCTags.IRON_REPAIR_MATERIAL)) {
                itemStack = cc.owner.getItemBySlot(EquipmentSlot.OFFHAND);
                if (itemStack == null || !itemStack.is(CCTags.IRON_REPAIR_MATERIAL)) {
                    return;
                }
            }

            cc.owner.heal(cc.owner.getMaxHealth() * ChestCavity.config.IRON_REPAIR_PERCENT);
            entity.playSound(SoundEvents.IRON_GOLEM_REPAIR, 0.75F, 1.0F);
            cc.owner.addEffect(new MobEffectInstance((MobEffect)CCStatusEffects.IRON_REPAIR_COOLDOWN.get(), (int)((float)ChestCavity.config.IRON_REPAIR_COOLDOWN / ironRepair), 0, false, false, true));
            itemStack.shrink(1);
        }

    }

    public static void ActivateGhastly(LivingEntity entity, ChestCavityInstance cc) {
        float ghastly = cc.getOrganScore(CCOrganScores.GHASTLY);
        if (!(ghastly < 1.0F) && !entity.hasEffect((MobEffect)CCStatusEffects.GHASTLY_COOLDOWN.get())) {
            OrganUtil.queueGhastlyFireballs(entity, cc, (int)ghastly);
        }

    }

    private static void ActivateGrazing(LivingEntity entity, ChestCavityInstance cc) {
        float grazing = cc.getOrganScore(CCOrganScores.GRAZING);
        if (!(grazing <= 0.0F)) {
            BlockPos blockPos = entity.blockPosition().below();
            boolean ateGrass = false;
            if (!entity.level().getBlockState(blockPos).is(Blocks.GRASS_BLOCK) && !entity.level().getBlockState(blockPos).is(Blocks.MYCELIUM)) {
                if (entity.level().getBlockState(blockPos).is(Blocks.CRIMSON_NYLIUM) || entity.level().getBlockState(blockPos).is(Blocks.WARPED_NYLIUM)) {
                    entity.level().setBlock(blockPos, Blocks.NETHERRACK.defaultBlockState(), 2);
                    ateGrass = true;
                }
            } else {
                entity.level().setBlock(blockPos, Blocks.DIRT.defaultBlockState(), 2);
                ateGrass = true;
            }

            if (ateGrass) {
                int duration;
                if (entity.hasEffect((MobEffect)CCStatusEffects.RUMINATING.get())) {
                    MobEffectInstance ruminating = entity.getEffect((MobEffect)CCStatusEffects.RUMINATING.get());
                    duration = (int)Math.min((float)(ChestCavity.config.RUMINATION_TIME * ChestCavity.config.RUMINATION_GRASS_PER_SQUARE * ChestCavity.config.RUMINATION_SQUARES_PER_STOMACH) * grazing, (float)(ruminating.getDuration() + ChestCavity.config.RUMINATION_TIME * ChestCavity.config.RUMINATION_GRASS_PER_SQUARE));
                } else {
                    duration = ChestCavity.config.RUMINATION_TIME * ChestCavity.config.RUMINATION_GRASS_PER_SQUARE;
                }

                entity.addEffect(new MobEffectInstance((MobEffect)CCStatusEffects.RUMINATING.get(), duration, 0, false, false, true));
            }
        }

    }

    public static void ActivatePyromancy(LivingEntity entity, ChestCavityInstance cc) {
        float pyromancy = cc.getOrganScore(CCOrganScores.PYROMANCY);
        if (!(pyromancy < 1.0F) && !entity.hasEffect((MobEffect)CCStatusEffects.PYROMANCY_COOLDOWN.get())) {
            OrganUtil.queuePyromancyFireballs(entity, cc, (int)pyromancy);
        }

    }

    public static void ActivateShulkerBullets(LivingEntity entity, ChestCavityInstance cc) {
        float projectiles = cc.getOrganScore(CCOrganScores.SHULKER_BULLETS);
        if (!(projectiles < 1.0F) && !entity.hasEffect((MobEffect)CCStatusEffects.SHULKER_BULLET_COOLDOWN.get())) {
            OrganUtil.queueShulkerBullets(entity, cc, (int)projectiles);
        }

    }

    public static void ActivateSilk(LivingEntity entity, ChestCavityInstance cc) {
        if (cc.getOrganScore(CCOrganScores.SILK) != 0.0F && !entity.hasEffect((MobEffect)CCStatusEffects.SILK_COOLDOWN.get()) && OrganUtil.spinWeb(entity, cc, cc.getOrganScore(CCOrganScores.SILK))) {
            entity.addEffect(new MobEffectInstance((MobEffect)CCStatusEffects.SILK_COOLDOWN.get(), ChestCavity.config.SILK_COOLDOWN, 0, false, false, true));
        }

    }
}
