package net.tigereye.chestcavity.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.organs.OrganData;
import net.tigereye.chestcavity.chestcavities.organs.OrganManager;
import net.tigereye.chestcavity.compat.kubejs.CCEvents;
import net.tigereye.chestcavity.compat.kubejs.EvaluateChestCavityJS;
import net.tigereye.chestcavity.compat.requiem.CCRequiem;
import net.tigereye.chestcavity.interfaces.CCOrganItem;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.listeners.OrganAddStatusEffectListeners;
import net.tigereye.chestcavity.listeners.OrganOnHitContext;
import net.tigereye.chestcavity.listeners.OrganOnHitListener;
import net.tigereye.chestcavity.listeners.OrganTickListeners;
import net.tigereye.chestcavity.listeners.OrganUpdateListeners;
import net.tigereye.chestcavity.registration.CCEnchantments;
import net.tigereye.chestcavity.registration.CCItems;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCStatusEffects;
import net.tigereye.chestcavity.registration.CCTagOrgans;

public class ChestCavityUtil {
    public ChestCavityUtil() {
    }

    public static void addOrganScore(ResourceLocation id, float value, Map<ResourceLocation, Float> organScores) {
        organScores.put(id, (Float)organScores.getOrDefault(id, 0.0F) + value);
    }

    public static float applyBoneDefense(ChestCavityInstance cc, float damage) {
        float boneDiff = (cc.getOrganScore(CCOrganScores.DEFENSE) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.DEFENSE)) / 4.0F;
        return (float)((double)damage * Math.pow((double)(1.0F - ChestCavity.config.BONE_DEFENSE), (double)boneDiff));
    }

    public static int applyBreathInWater(ChestCavityInstance cc, int oldAir, int newAir) {
        if (!cc.opened || cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.BREATH_CAPACITY) == cc.getOrganScore(CCOrganScores.BREATH_CAPACITY) && cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.WATERBREATH) == cc.getOrganScore(CCOrganScores.WATERBREATH)) {
            return newAir;
        } else {
            float airLoss = 1.0F;
            float waterBreath = cc.getOrganScore(CCOrganScores.WATERBREATH);
            if (cc.owner.isSprinting()) {
                waterBreath /= 4.0F;
            }

            if (waterBreath > 0.0F) {
                airLoss += -2.0F * waterBreath;
            }

            if (airLoss > 0.0F) {
                if (oldAir == newAir) {
                    airLoss = 0.0F;
                } else {
                    float capacity = cc.getOrganScore(CCOrganScores.BREATH_CAPACITY);
                    airLoss *= (float)(oldAir - newAir);
                    if (airLoss > 0.0F) {
                        float lungRatio = 20.0F;
                        if (capacity != 0.0F) {
                            lungRatio = Math.min(2.0F / capacity, 20.0F);
                        }

                        airLoss = airLoss * lungRatio + cc.lungRemainder;
                    }
                }
            }

            cc.lungRemainder = airLoss % 1.0F;
            int airResult = Math.min(oldAir - (int)airLoss, cc.owner.getMaxAirSupply());
            if (airResult <= -20) {
                airResult = 0;
                cc.lungRemainder = 0.0F;
                cc.owner.hurt(cc.owner.damageSources().drown(), 2.0F);
            }

            return airResult;
        }
    }

    public static int applyBreathOnLand(ChestCavityInstance cc, int oldAir, int airGain) {
        if (!cc.opened || cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.BREATH_RECOVERY) == cc.getOrganScore(CCOrganScores.BREATH_RECOVERY) && cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.BREATH_CAPACITY) == cc.getOrganScore(CCOrganScores.BREATH_CAPACITY) && cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.WATERBREATH) == cc.getOrganScore(CCOrganScores.WATERBREATH)) {
            return oldAir;
        } else {
            float airLoss;
            if (!cc.owner.hasEffect(MobEffects.WATER_BREATHING) && !cc.owner.hasEffect(MobEffects.CONDUIT_POWER)) {
                airLoss = 1.0F;
            } else {
                airLoss = 0.0F;
            }

            float breath = cc.getOrganScore(CCOrganScores.BREATH_RECOVERY);
            if (cc.owner.isSprinting()) {
                breath /= 4.0F;
            }

            if (cc.owner.isInWaterOrRain()) {
                breath += cc.getOrganScore(CCOrganScores.WATERBREATH) / 4.0F;
            }

            if (breath > 0.0F) {
                airLoss += (float)(-airGain) * breath / 2.0F;
            }

            int airResult;
            if (airLoss > 0.0F) {
                airResult = EnchantmentHelper.getRespiration(cc.owner);
                if (cc.owner.getRandom().nextInt(airResult + 1) != 0) {
                    airLoss = 0.0F;
                } else {
                    float capacity = cc.getOrganScore(CCOrganScores.BREATH_CAPACITY);
                    float breathRatio = 20.0F;
                    if (capacity != 0.0F) {
                        breathRatio = Math.min(2.0F / capacity, 20.0F);
                    }

                    airLoss = airLoss * breathRatio + cc.lungRemainder;
                }
            } else if (oldAir == cc.owner.getMaxAirSupply()) {
                return oldAir;
            }

            cc.lungRemainder = airLoss % 1.0F;
            airResult = Math.min(oldAir - (int)airLoss - airGain, cc.owner.getMaxAirSupply());
            if (airResult <= -20) {
                airResult = 0;
                cc.lungRemainder = 0.0F;
                cc.owner.hurt(cc.owner.damageSources().drown(), 2.0F);
            }

            return airResult;
        }
    }

    public static float applyDefenses(ChestCavityInstance cc, DamageSource source, float damage) {
        if (!cc.opened) {
            return damage;
        } else if (attemptArrowDodging(cc, source)) {
            return 0.0F;
        } else {
            if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                damage = applyBoneDefense(cc, damage);
            }

            if (source.is(DamageTypeTags.IS_FALL)) {
                damage = applyLeapingToFallDamage(cc, damage);
            }

            if (source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL)) {
                damage = applyImpactResistant(cc, damage);
            }

            if (source.is(DamageTypeTags.IS_FIRE)) {
                damage = applyFireResistant(cc, damage);
            }

            return damage;
        }
    }

    public static int applyDigestion(ChestCavityInstance cc, float digestion, int hunger) {
        if (digestion == 1.0F) {
            return hunger;
        } else if (digestion < 0.0F) {
            cc.owner.addEffect(new MobEffectInstance(MobEffects.CONFUSION, (int)((float)(-hunger) * digestion * 400.0F)));
            return 0;
        } else {
            return Math.max((int)((float)hunger * digestion), 1);
        }
    }

    public static float applyFireResistant(ChestCavityInstance cc, float damage) {
        float fireproof = cc.getOrganScore(CCOrganScores.FIRE_RESISTANT);
        return fireproof > 0.0F ? (float)((double)damage * Math.pow((double)(1.0F - ChestCavity.config.FIREPROOF_DEFENSE), (double)(fireproof / 4.0F))) : damage;
    }

    public static float applyImpactResistant(ChestCavityInstance cc, float damage) {
        float impactResistant = cc.getOrganScore(CCOrganScores.IMPACT_RESISTANT);
        return impactResistant > 0.0F ? (float)((double)damage * Math.pow((double)(1.0F - ChestCavity.config.IMPACT_DEFENSE), (double)(impactResistant / 4.0F))) : damage;
    }

    public static Float applyLeaping(ChestCavityInstance cc, float velocity) {
        float leaping = cc.getOrganScore(CCOrganScores.LEAPING);
        float defaultLeaping = cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.LEAPING);
        return velocity * Math.max(0.0F, 1.0F + (leaping - defaultLeaping) * 0.25F);
    }

    public static float applyLeapingToFallDamage(ChestCavityInstance cc, float damage) {
        float leapingDiff = cc.getOrganScore(CCOrganScores.LEAPING) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.LEAPING);
        return leapingDiff > 0.0F ? Math.max(0.0F, damage - leapingDiff * leapingDiff / 4.0F) : damage;
    }

    public static float applyNutrition(ChestCavityInstance cc, float nutrition, float saturation) {
        if (nutrition == 4.0F) {
            return saturation;
        } else if (nutrition < 0.0F) {
            cc.owner.addEffect(new MobEffectInstance(MobEffects.HUNGER, (int)(saturation * nutrition * 800.0F)));
            return 0.0F;
        } else {
            return saturation * nutrition / 4.0F;
        }
    }

    public static float applyNervesToMining(ChestCavityInstance cc, float miningProgress) {
        float defaultNerves = cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.NERVES);
        if (defaultNerves == 0.0F) {
            return miningProgress;
        } else {
            float NervesDiff = cc.getOrganScore(CCOrganScores.NERVES) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.NERVES);
            return miningProgress * (1.0F + ChestCavity.config.NERVES_HASTE * NervesDiff);
        }
    }

    public static int applySpleenMetabolism(ChestCavityInstance cc, int foodStarvationTimer) {
        if (!cc.opened) {
            return foodStarvationTimer;
        } else {
            float metabolismDiff = cc.getOrganScore(CCOrganScores.METABOLISM) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.METABOLISM);
            if (metabolismDiff == 0.0F) {
                return foodStarvationTimer;
            } else {
                if (metabolismDiff > 0.0F) {
                    cc.metabolismRemainder += metabolismDiff;
                    foodStarvationTimer += (int)cc.metabolismRemainder;
                } else {
                    cc.metabolismRemainder += 1.0F - 1.0F / (-metabolismDiff + 1.0F);
                    foodStarvationTimer -= (int)cc.metabolismRemainder;
                }

                cc.metabolismRemainder %= 1.0F;
                return foodStarvationTimer;
            }
        }
    }

    public static float applySwimSpeedInWater(ChestCavityInstance cc) {
        if (cc.opened && cc.owner.isInWaterOrRain()) {
            float speedDiff = cc.getOrganScore(CCOrganScores.SWIM_SPEED) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.SWIM_SPEED);
            return speedDiff == 0.0F ? 1.0F : Math.max(0.0F, 1.0F + speedDiff * ChestCavity.config.SWIMSPEED_FACTOR / 8.0F);
        } else {
            return 1.0F;
        }
    }

    public static boolean attemptArrowDodging(ChestCavityInstance cc, DamageSource source) {
        float dodge = cc.getOrganScore(CCOrganScores.ARROW_DODGING);
        if (dodge == 0.0F) {
            return false;
        } else if (cc.owner.hasEffect((MobEffect)CCStatusEffects.ARROW_DODGE_COOLDOWN.get())) {
            return false;
        } else if (!source.is(DamageTypeTags.IS_PROJECTILE)) {
            return false;
        } else if (!OrganUtil.teleportRandomly(cc.owner, (float)ChestCavity.config.ARROW_DODGE_DISTANCE / dodge)) {
            return false;
        } else {
            cc.owner.addEffect(new MobEffectInstance((MobEffect)CCStatusEffects.ARROW_DODGE_COOLDOWN.get(), (int)((float)ChestCavity.config.ARROW_DODGE_COOLDOWN / dodge), 0, false, false, true));
            return true;
        }
    }

    public static void clearForbiddenSlots(ChestCavityInstance cc) {
        try {
            cc.inventory.removeListener(cc);
        } catch (NullPointerException var2) {
        }

        for(int i = 0; i < cc.inventory.getContainerSize(); ++i) {
            if (cc.getChestCavityType().isSlotForbidden(i)) {
                cc.owner.spawnAtLocation(cc.inventory.removeItemNoUpdate(i));
            }
        }

        cc.inventory.addListener(cc);
    }

    public static void destroyOrgansWithKey(ChestCavityInstance cc, ResourceLocation organ) {
        for(int i = 0; i < cc.inventory.getContainerSize(); ++i) {
            ItemStack slot = cc.inventory.getItem(i);
            if (slot != null && slot != ItemStack.EMPTY) {
                OrganData organData = lookupOrgan(slot, cc.getChestCavityType());
                if (organData != null && organData.organScores.containsKey(organ)) {
                    cc.inventory.removeItemNoUpdate(i);
                }
            }
        }

        cc.inventory.setChanged();
    }

    public static boolean determineDefaultOrganScores(ChestCavityType chestCavityType) {
        Map<ResourceLocation, Float> organScores = chestCavityType.getDefaultOrganScores();
        chestCavityType.loadBaseOrganScores(organScores);

        try {
            for(int i = 0; i < chestCavityType.getDefaultChestCavity().getContainerSize(); ++i) {
                ItemStack itemStack = chestCavityType.getDefaultChestCavity().getItem(i);
                if (itemStack != null && itemStack != ItemStack.EMPTY) {
                    Item slotitem = itemStack.getItem();
                    OrganData data = lookupOrgan(itemStack, chestCavityType);
                    if (data != null) {
                        data.organScores.forEach((key, value) -> {
                            addOrganScore(key, value * Math.min((float)itemStack.getCount() / (float)itemStack.getMaxStackSize(), 1.0F), organScores);
                        });
                    }
                }
            }

            return true;
        } catch (IllegalStateException var6) {
            ChestCavity.LOGGER.warn(var6.getMessage() + ". Chest Cavity will attempt to calculate this default organ score later.");
            return false;
        }
    }

    public static void drawOrgansFromPile(List<ItemStack> organPile, int rolls, RandomSource random, List<ItemStack> loot) {
        for(int i = 0; i < rolls && !organPile.isEmpty(); ++i) {
            int roll = random.nextInt(organPile.size());
            int count = 1;
            ItemStack rolledItem = ((ItemStack)organPile.remove(roll)).copy();
            if (rolledItem.getCount() > 1) {
                count += random.nextInt(rolledItem.getMaxStackSize());
            }

            rolledItem.setCount(count);
            loot.add(rolledItem);
        }

    }

    public static void dropUnboundOrgans(ChestCavityInstance cc) {
        if (!ChestCavity.config.REQUIEM_INTEGRATION || ForgeRegistries.ENTITY_TYPES.getKey(cc.owner.getType()).compareTo(CCRequiem.PLAYER_SHELL_ID) != 0) {
            try {
                cc.inventory.removeListener(cc);
            } catch (NullPointerException var4) {
            }

            for(int i = 0; i < cc.inventory.getContainerSize(); ++i) {
                ItemStack itemStack = cc.inventory.getItem(i);
                if (itemStack != null && itemStack != ItemStack.EMPTY) {
                    int compatibility = getCompatibilityLevel(cc, itemStack);
                    if (compatibility < 2) {
                        cc.owner.spawnAtLocation(cc.inventory.removeItemNoUpdate(i));
                    }
                }
            }

            cc.inventory.addListener(cc);
            evaluateChestCavity(cc);
        }

    }

    public static void evaluateChestCavity(ChestCavityInstance cc) {
        Map<ResourceLocation, Float> organScores = cc.getOrganScores();

        var e = new EvaluateChestCavityJS(cc, cc.owner, cc.owner.level());
        CCEvents.EVAL_CC.post(e);

        if (!cc.opened) {
            organScores.clear();
            if (cc.getChestCavityType().getDefaultOrganScores() != null) {
                organScores.putAll(cc.getChestCavityType().getDefaultOrganScores());
            }
        } else {
            cc.onHitListeners.clear();
            cc.getChestCavityType().loadBaseOrganScores(organScores);

            for(int i = 0; i < cc.inventory.getContainerSize(); ++i) {
                ItemStack itemStack = cc.inventory.getItem(i);
                if (itemStack != null && itemStack != ItemStack.EMPTY) {
                    Item slotitem = itemStack.getItem();
                    OrganData data = lookupOrgan(itemStack, cc.getChestCavityType());
                    if (data != null) {
                        data.organScores.forEach((key, value) -> {
                            addOrganScore(key, value * Math.min((float)itemStack.getCount() / (float)itemStack.getMaxStackSize(), 1.0F), organScores);
                        });
                        if (slotitem instanceof OrganOnHitListener) {
                            cc.onHitListeners.add(new OrganOnHitContext(itemStack, (OrganOnHitListener)slotitem));
                        }

                        if (!data.pseudoOrgan) {
                            int compatibility = getCompatibilityLevel(cc, itemStack);
                            if (compatibility < 1) {
                                addOrganScore(CCOrganScores.INCOMPATIBILITY, 1.0F, organScores);
                            }
                        }
                    }
                }
            }
        }

        organUpdate(cc);
    }

    public static void forcefullyAddStack(ChestCavityInstance cc, ItemStack stack, int slot) {
        if (!cc.inventory.canAddItem(stack)) {
            if (cc.owner.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) && cc.owner instanceof Player) {
                if (!((Player)cc.owner).getInventory().add(stack)) {
                    cc.owner.spawnAtLocation(cc.inventory.removeItemNoUpdate(slot));
                }
            } else {
                cc.owner.spawnAtLocation(cc.inventory.removeItemNoUpdate(slot));
            }
        }

        cc.inventory.addItem(stack);
    }

    public static void generateChestCavityIfOpened(ChestCavityInstance cc) {
        if (cc.opened) {
            cc.inventory.readTags(cc.getChestCavityType().getDefaultChestCavity().getTags());
            cc.getChestCavityType().setOrganCompatibility(cc);
        }

    }

    public static int getCompatibilityLevel(ChestCavityInstance cc, ItemStack itemStack) {
        if (itemStack != null && itemStack != ItemStack.EMPTY) {
            if (EnchantmentHelper.getTagEnchantmentLevel((Enchantment)CCEnchantments.MALPRACTICE.get(), itemStack) > 0) {
                return 0;
            } else {
                int oNegative = EnchantmentHelper.getTagEnchantmentLevel((Enchantment)CCEnchantments.O_NEGATIVE.get(), itemStack);
                int ownership = 0;
                CompoundTag tag = itemStack.getTag();
                if (tag != null && tag.contains(ChestCavity.COMPATIBILITY_TAG.toString())) {
                    tag = tag.getCompound(ChestCavity.COMPATIBILITY_TAG.toString());
                    if (tag.getUUID("owner").equals(cc.compatibility_id)) {
                        ownership = 2;
                    }
                } else {
                    ownership = 1;
                }

                return Math.max(oNegative, ownership);
            }
        } else {
            return 1;
        }
    }

    public static void insertWelfareOrgans(ChestCavityInstance cc) {
        if (cc.getOrganScore(CCOrganScores.HEALTH) <= 0.0F) {
            forcefullyAddStack(cc, new ItemStack(CCItems.ROTTEN_HEART.get()), 4);
        }

        if (cc.getOrganScore(CCOrganScores.BREATH_RECOVERY) <= 0.0F) {
            forcefullyAddStack(cc, new ItemStack(CCItems.ROTTEN_LUNG.get()), 3);
        }

        if (cc.getOrganScore(CCOrganScores.NERVES) <= 0.0F) {
            forcefullyAddStack(cc, new ItemStack(CCItems.ROTTEN_SPINE.get()), 13);
        }

        if (cc.getOrganScore(CCOrganScores.STRENGTH) <= 0.0F) {
            forcefullyAddStack(cc, new ItemStack(Items.ROTTEN_FLESH, 16), 0);
        }

    }

    public static boolean isHydroPhobicOrAllergic(LivingEntity entity) {
        Optional<ChestCavityEntity> optional = ChestCavityEntity.of(entity);
        if (optional.isEmpty()) {
            return false;
        } else {
            ChestCavityInstance cc = optional.get().getChestCavityInstance();
            return cc.getOrganScore(CCOrganScores.HYDROALLERGENIC) > 0.0F || cc.getOrganScore(CCOrganScores.HYDROPHOBIA) > 0.0F;
        }
    }

    public static OrganData lookupOrgan(ItemStack itemStack, ChestCavityType cct) {
        OrganData organData = null;
        if (cct != null) {
            organData = cct.catchExceptionalOrgan(itemStack);
        }

        if (organData != null) {
            return organData;
        } else {
            organData = OrganManager.readNBTOrganData(itemStack);
            if (organData != null) {
                return organData;
            } else {
                Item var4 = itemStack.getItem();
                if (var4 instanceof CCOrganItem) {
                    CCOrganItem oItem = (CCOrganItem)var4;
                    return oItem.getOrganData(itemStack);
                } else if (OrganManager.hasEntry(itemStack.getItem())) {
                    return OrganManager.getEntry(itemStack.getItem());
                } else {

                    for (TagKey<Item> itemTagKey : CCTagOrgans.tagMap.keySet()) {
                        TagKey<Item> itemTag = (TagKey) itemTagKey;
                        if (itemStack.is(itemTag)) {
                            organData = new OrganData();
                            organData.pseudoOrgan = true;
                            organData.organScores = (Map) CCTagOrgans.tagMap.get(itemTag);
                            return organData;
                        }
                    }

                    return null;
                }
            }
        }
    }

    public static MobEffectInstance onAddStatusEffect(ChestCavityInstance cc, MobEffectInstance effect) {
        return OrganAddStatusEffectListeners.call(cc.owner, cc, effect);
    }

    public static void onDeath(ChestCavityEntity entity) {
        ChestCavityInstance ccinstance = entity.getChestCavityInstance();
        ccinstance.getChestCavityType().onDeath(ccinstance);
        if (entity instanceof Player playerEntity) {
            if (!ChestCavity.config.KEEP_CHEST_CAVITY) {
                Map<Integer, ItemStack> organsToKeep = new HashMap();

                for(int i = 0; i < ccinstance.inventory.getContainerSize(); ++i) {
                    ItemStack organ = ccinstance.inventory.getItem(i);
                    if (EnchantmentHelper.getTagEnchantmentLevel((Enchantment)CCEnchantments.O_NEGATIVE.get(), organ) >= 2) {
                        organsToKeep.put(i, organ.copy());
                    }
                }

                ccinstance.compatibility_id = UUID.randomUUID();
                generateChestCavityIfOpened(ccinstance);

                for (Map.Entry<Integer, ItemStack> integerItemStackEntry : organsToKeep.entrySet()) {
                    Map.Entry<Integer, ItemStack> entry = (Map.Entry) integerItemStackEntry;
                    ccinstance.inventory.setItem((Integer) entry.getKey(), (ItemStack) entry.getValue());
                }
            }

            insertWelfareOrgans(ccinstance);
        }

    }

    public static float onHit(ChestCavityInstance cc, DamageSource source, LivingEntity target, float damage) {
        if (cc.opened) {
            OrganOnHitContext e;
            for(Iterator<OrganOnHitContext> var4 = cc.onHitListeners.iterator(); var4.hasNext(); damage = e.listener.onHit(source, cc.owner, target, cc, e.organ, damage)) {
                e = (OrganOnHitContext)var4.next();
            }

            organUpdate(cc);
        }

        return damage;
    }

    public static void onTick(ChestCavityInstance cc) {
        if (cc.updatePacket) {
            NetworkUtil.SendS2CChestCavityUpdatePacket(cc, cc.updatePacket);
        }

        if (cc.opened) {
            OrganTickListeners.call(cc.owner, cc);
            organUpdate(cc);
        }

    }

    public static ChestCavityInventory openChestCavity(ChestCavityInstance cc) {
        if (!cc.opened) {
            try {
                cc.inventory.removeListener(cc);
            } catch (NullPointerException var2) {
            }

            cc.opened = true;
            generateChestCavityIfOpened(cc);
            cc.inventory.addListener(cc);
        }

        return cc.inventory;
    }

    public static void organUpdate(ChestCavityInstance cc) {
        Map<ResourceLocation, Float> organScores = cc.getOrganScores();
        if (!cc.oldOrganScores.equals(organScores)) {
            OrganUpdateListeners.call(cc.owner, cc);
            cc.oldOrganScores.clear();
            cc.oldOrganScores.putAll(organScores);
            NetworkUtil.SendS2CChestCavityUpdatePacket(cc);
        }

    }

    public static void outputOrganScoresString(Consumer<String> output, ChestCavityInstance cc) {
        try {
            Component name = cc.owner.getDisplayName();
            output.accept("[Chest Cavity] Displaying " + name.getString() + "'s organ scores:");
        } catch (Exception var3) {
            output.accept("[Chest Cavity] Displaying organ scores:");
        }

        cc.getOrganScores().forEach((key, value) -> {
            String var10001 = key.getPath();
            output.accept(var10001 + ": " + value + " ");
        });
    }

    public static void splashHydrophobicWithWater(ThrownPotion splash) {
        AABB box = splash.getBoundingBox().expandTowards(4.0, 2.0, 4.0);
        List<LivingEntity> list = splash.level().getEntitiesOfClass(LivingEntity.class, box, ChestCavityUtil::isHydroPhobicOrAllergic);
        if (!list.isEmpty()) {
            Iterator var3 = list.iterator();

            while(var3.hasNext()) {
                LivingEntity livingEntity = (LivingEntity)var3.next();
                double d = splash.distanceToSqr(livingEntity);
                if (d < 16.0) {
                    Optional<ChestCavityEntity> optional = ChestCavityEntity.of(livingEntity);
                    if (optional.isPresent()) {
                        ChestCavityInstance cc = ((ChestCavityEntity)optional.get()).getChestCavityInstance();
                        float allergy = cc.getOrganScore(CCOrganScores.HYDROALLERGENIC);
                        float phobia = cc.getOrganScore(CCOrganScores.HYDROPHOBIA);
                        if (allergy > 0.0F) {
                            livingEntity.hurt(livingEntity.damageSources().indirectMagic(splash, splash.getOwner()), allergy / 26.0F);
                        }

                        if (phobia > 0.0F) {
                            OrganUtil.teleportRandomly(livingEntity, phobia * 32.0F);
                        }
                    }
                }
            }
        }

    }
}
