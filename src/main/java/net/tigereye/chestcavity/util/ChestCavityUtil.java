package net.tigereye.chestcavity.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;
import net.tigereye.chestcavity.compat.kubejs.CCEvents;
import net.tigereye.chestcavity.compat.tinker.OrganToolStats;
import net.tigereye.chestcavity.compat.tinker.TinkerOrganItem;
import net.tigereye.chestcavity.interfaces.CCOrganItem;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.listeners.OrganAddStatusEffectListeners;
import net.tigereye.chestcavity.listeners.OrganUpdateListeners;
import net.tigereye.chestcavity.registration.CCItems;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCStatusEffects;
import net.tigereye.chestcavity.registration.CCTagOrgans;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ChestCavityUtil {
    public ChestCavityUtil() {
    }

    public static void addOrganScore(ResourceLocation id, float value, Map<ResourceLocation, Float> organScores) {
        organScores.put(id, organScores.getOrDefault(id, 0.0F) + value);
    }

    public static int applyBreathInWater(ChestCavityInstance cc, int oldAir, int newAir) {
        //if your chest cavity is untouched or normal, we do nothing
        if (!cc.opened || (cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.BREATH_CAPACITY) == cc.getOrganScore(CCOrganScores.BREATH_CAPACITY) &&
                cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.WATERBREATH) == cc.getOrganScore(CCOrganScores.WATERBREATH))) {
            return newAir;
        }

        float airLoss = 1;
        //if you have waterbreath, you can breath underwater. Yay! This will overwrite any incoming air loss.
        float waterBreath = cc.getOrganScore(CCOrganScores.WATERBREATH);
        if (cc.owner.isSprinting()) {
            waterBreath /= 4;
        }
        if (waterBreath > 0) {
            airLoss += (-2 * waterBreath);
        }

        //if you don't (or you are still breath negative),
        //we check how well your lungs can hold oxygen
        if (airLoss > 0) {
            if (oldAir == newAir) {
                //this would indicate that resperation was a success
                airLoss = 0;
            } else {
                float capacity = cc.getOrganScore(CCOrganScores.BREATH_CAPACITY);
                airLoss *= (oldAir - newAir); //if you are downing at bonus speed, ok
                if (airLoss > 0) {
                    float lungRatio = 5f;
                    if (capacity > 0.2f) {
                        lungRatio = Math.min(1 / capacity, 5f);
                    }
                    airLoss = (airLoss * lungRatio) + cc.lungRemainder;
                }
            }
        }

        cc.lungRemainder = airLoss % 1;
        int airResult = Math.min(oldAir - ((int) airLoss), cc.owner.getMaxAirSupply());
        //I don't trust vanilla to do this job right, so I will choke you myself
        if (airResult <= -20) {
            airResult = 0;
            cc.lungRemainder = 0;
            cc.owner.hurt(cc.owner.damageSources().drown(), 2.0F);
        }
        return airResult;
    }

    public static int applyBreathOnLand(ChestCavityInstance cc, int oldAir, int airGain) {
        if (!cc.opened || (cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.BREATH_RECOVERY) == cc.getOrganScore(CCOrganScores.BREATH_RECOVERY) &&
                cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.BREATH_CAPACITY) == cc.getOrganScore(CCOrganScores.BREATH_CAPACITY) &&
                cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.WATERBREATH) == cc.getOrganScore(CCOrganScores.WATERBREATH))) {
            return oldAir;
        }

        float airLoss;
        if (cc.owner.hasEffect(MobEffects.WATER_BREATHING) || cc.owner.hasEffect(MobEffects.CONDUIT_POWER)) {
            airLoss = 0;
        } else {
            airLoss = 1;
        }


        //if you have breath, you can breath on land. Yay!
        //if in contact with water or rain apply on quarter your water breath as well
        //(so 2 gills can survive in humid conditions)
        float breath = cc.getOrganScore(CCOrganScores.BREATH_RECOVERY);
        if (cc.owner.isSprinting()) {
            breath /= 4;
        }
        if (cc.owner.isInWaterOrRain()) {
            breath += cc.getOrganScore(CCOrganScores.WATERBREATH) / 4;
        }
        if (breath > 0) {
            airLoss += (-airGain * breath / 2);// + cc.lungRemainder;
        }

        //if you don't then unless you have the water breathing status effect you must hold your watery breath.
        //it's also possible to not have enough breath to keep up with airLoss
        if (airLoss > 0) {
            //first, check if resperation cancels the sequence.
            int resperation = EnchantmentHelper.getRespiration(cc.owner);
            if (cc.owner.getRandom().nextInt(resperation + 1) != 0) {
                airLoss = 0;
            } else {
                //then, we apply our breath capacity
                float capacity = cc.getOrganScore(CCOrganScores.BREATH_CAPACITY);
                float breathRatio = 5f;
                if (capacity > 0.2f) {
                    breathRatio = Math.min(1 / capacity, 5f);
                }
                airLoss = (airLoss * breathRatio) + cc.lungRemainder;
            }
        } else if (oldAir == cc.owner.getMaxAirSupply()) {
            return oldAir;
        }

        cc.lungRemainder = airLoss % 1;
        //we finally undo the air gained in vanilla while calculating final results
        int airResult = Math.min(oldAir - ((int) airLoss) - airGain, cc.owner.getMaxAirSupply());
        //I don't trust vanilla to do this job right, so I will choke you myself
        if (airResult <= -20) {
            airResult = 0;
            cc.lungRemainder = 0;
            cc.owner.hurt(cc.owner.damageSources().drown(), 2.0F);
        }
        return airResult;
    }

    public static float applyDefenses(ChestCavityInstance cc, DamageSource source, float damage) {
        if (!cc.opened) {
            return damage;
        } else if (attemptArrowDodging(cc, source)) {
            return 0.0F;
        } else {

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
            return 0;
        } else if (digestion < 1.0F) {
            return digestion * hunger > 1.0F ? 1 : 0;
        } else {
            return Math.max((int) ((float) hunger * digestion), 1);
        }
    }

    public static float applyFireResistant(ChestCavityInstance cc, float damage) {
        float fireproof = cc.getOrganScore(CCOrganScores.FIRE_RESISTANT);
        return fireproof > 0.0F ? (float) ((double) damage * Math.pow(1.0F - ChestCavity.config.FIREPROOF_DEFENSE, fireproof / 4.0F)) : damage;
    }

    public static float applyImpactResistant(ChestCavityInstance cc, float damage) {
        float impactResistant = cc.getOrganScore(CCOrganScores.IMPACT_RESISTANT);
        return impactResistant > 0.0F ? (float) ((double) damage * Math.pow(1.0F - ChestCavity.config.IMPACT_DEFENSE, impactResistant / 4.0F)) : damage;
    }

    public static Float applyLeaping(ChestCavityInstance cc, float velocity) {
        float leaping = cc.getOrganScore(CCOrganScores.LEAPING);
        float defaultLeaping = cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.LEAPING);
        return velocity * Math.max(0.0F, 1.0F + (leaping - defaultLeaping) * ChestCavity.config.LEAPING_POWER);
    }

    public static float applyLeapingToFallDamage(ChestCavityInstance cc, float damage) {
        float leapingDiff = cc.getOrganScore(CCOrganScores.LEAPING) - cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.LEAPING);
        return leapingDiff > 0.0F ? Math.max(0.0F, damage - leapingDiff * leapingDiff / 4.0F) : damage;
    }

    public static double getBuoyancyLift(LivingEntity entity, ChestCavityInstance chestCavity) {
        float buoyancy = chestCavity.getOrganScore(CCOrganScores.BUOYANT) - chestCavity.getChestCavityType().getDefaultOrganScore(CCOrganScores.BUOYANT);
        float breathRatio = (float) entity.getAirSupply() / entity.getMaxAirSupply();
        return buoyancy * breathRatio * ChestCavity.config.BUOYANCY_LIFT;
    }


    public static float applyNutrition(ChestCavityInstance cc, float nutrition, float saturation) {
        if (nutrition == 4.0F) {
            return saturation;
        } else if (nutrition < 0.0F) {
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
            if (metabolismDiff != 0.0F) {
                if (metabolismDiff > 0.0F) {
                    cc.metabolismRemainder += metabolismDiff;
                    foodStarvationTimer += (int) cc.metabolismRemainder;
                } else {
                    cc.metabolismRemainder += 1.0F - 1.0F / (-metabolismDiff + 1.0F);
                    foodStarvationTimer -= (int) cc.metabolismRemainder;
                }

                cc.metabolismRemainder %= 1.0F;
            }
            return foodStarvationTimer;
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
        } else if (cc.owner.hasEffect(CCStatusEffects.ARROW_DODGE_COOLDOWN.get())) {
            return false;
        } else if (!source.is(DamageTypeTags.IS_PROJECTILE)) {
            return false;
        } else if (!OrganUtil.teleportRandomly(cc.owner, (float) ChestCavity.config.ARROW_DODGE_DISTANCE / dodge)) {
            return false;
        } else {
            cc.owner.addEffect(new MobEffectInstance(CCStatusEffects.ARROW_DODGE_COOLDOWN.get(), (int) ((float) ChestCavity.config.ARROW_DODGE_COOLDOWN / dodge), 0, false, false, true));
            return true;
        }
    }

    public static void destroyOrgansWithKey(ChestCavityInstance cc, ResourceLocation organ) {
        for (int i = 0; i < cc.inventory.getContainerSize(); ++i) {
            ItemStack slot = cc.inventory.getItem(i);
            if (slot != ItemStack.EMPTY) {
                OrganData organData = lookupOrgan(slot, cc.getChestCavityType());
                if (organData != null && organData.organScores.containsKey(organ)) {
                    cc.inventory.removeItemNoUpdate(i);
                }
            }
        }

        cc.inventory.setChanged();
    }


    public static void evaluateChestCavity(ChestCavityInstance cc) {
        if (cc.owner == null || cc.owner.level().isClientSide()) return;
        Map<ResourceLocation, Float> organScores = cc.getOrganScores();
        if (!cc.opened) {
            organScores.clear();
            if (cc.getChestCavityType().getDefaultOrganScores() != null) {
                organScores.putAll(cc.getChestCavityType().getDefaultOrganScores());
            }
        } else {
            cc.getChestCavityType().loadBaseOrganScores(organScores);
            InventoryTypeData inventoryTypeData = cc.getInventoryTypeData();
            for (int i = 0; i < cc.inventory.getContainerSize(); i++) {
                String slotType = inventoryTypeData.getSlotType(i);
                ItemStack itemStack = cc.inventory.getItem(i);
                // 容器槽不进行分数结算
                if (Objects.equals(slotType, "container_slot")) continue;
                if (itemStack != ItemStack.EMPTY) {
                    OrganData data = lookupOrgan(itemStack, cc.getChestCavityType());
                    if (data != null) {
                        data.organScores.forEach((key, value) -> {
                            addOrganScore(key, value * Math.min((float) itemStack.getCount() / (float) itemStack.getMaxStackSize(), 1.0F), organScores);
                        });

                        if (!data.pseudoOrgan) {
                            boolean isCompat = getCompatibility(cc, itemStack);
                            if (!isCompat) {
                                addOrganScore(CCOrganScores.INCOMPATIBILITY, 1.0F, organScores);
                            }
                        }
                    }
                }
            }
        }
        // kubejs接入点：胸腔属性计算节点，取代激活属性计算
        CCEvents.postEvaluateChestCavity(cc);
        organUpdate(cc);
    }

    public static void forcefullyAddStack(ChestCavityInstance cc, ItemStack stack, int slot) {
        if (!cc.inventory.canAddItem(stack)) {
            if (cc.owner.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) && cc.owner instanceof Player) {
                if (!((Player) cc.owner).getInventory().add(stack)) {
                    cc.owner.spawnAtLocation(cc.inventory.removeItemNoUpdate(slot));
                }
            } else {
                cc.owner.spawnAtLocation(cc.inventory.removeItemNoUpdate(slot));
            }
        }

        cc.inventory.addItem(stack);
    }

    public static void generateChestCavityIfOpened(ChestCavityInstance cc) {
        ListTag tagList = cc.getChestCavityType().getDefaultChestCavity().createTag();
        cc.inventory.removeListener(cc);
        cc.inventory = new ChestCavityInventory(cc);
        cc.inventory.fromTag(tagList);
        cc.inventory.addListener(cc);
        cc.getChestCavityType().setOrganCompatibility(cc);
        cc.opened = true;
        ChestCavityUtil.evaluateChestCavity(cc);
    }

    public static boolean getCompatibility(ChestCavityInstance cc, ItemStack itemStack) {
        if (itemStack == null || itemStack == ItemStack.EMPTY) {
            return true;
        }

        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains(ChestCavity.COMPATIBILITY_TAG.toString())) {
            return true;
        }

        boolean isCompat = false;
        tag = tag.getCompound(ChestCavity.COMPATIBILITY_TAG.toString());
        if (tag.getUUID("owner").equals(cc.compatibility_id)) {
            isCompat = true;
        }
        return isCompat;
    }

    public static boolean isOriginalOrgan(ChestCavityInstance cc, ItemStack itemStack) {
        if (itemStack == null || itemStack == ItemStack.EMPTY) {
            return true;
        }
        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains(ChestCavity.COMPATIBILITY_TAG.toString())) {
            return false;
        }
        tag = tag.getCompound(ChestCavity.COMPATIBILITY_TAG.toString());
        return tag.getUUID("owner").equals(cc.compatibility_id);
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
            }
            Item item = itemStack.getItem();
            if (item instanceof CCOrganItem oItem) {
                return oItem.getOrganData(itemStack);
            }
            if (OrganManager.hasEntry(itemStack.getItem())) {
                return OrganManager.getEntry(itemStack.getItem());
            }
            if (item instanceof TinkerOrganItem) {
                return OrganToolStats.getOrganDataFromTinkerOrgan(itemStack);
            }
            for (TagKey<Item> itemTagKey : CCTagOrgans.tagMap.keySet()) {
                if (itemStack.is(itemTagKey)) {
                    organData = new OrganData();
                    organData.pseudoOrgan = true;
                    organData.organScores = CCTagOrgans.tagMap.get(itemTagKey);
                    return organData;
                }
            }
            return null;
        }
    }


    public static MobEffectInstance onAddStatusEffect(ChestCavityInstance cc, MobEffectInstance effect) {
        if (cc.opened) {
            effect = OrganAddStatusEffectListeners.call(cc.owner, cc, effect);
            try {
                if (cc.owner != null && !cc.owner.level().isClientSide()) {
                    return CCEvents.postOpenedEntityAddStatus(cc, effect);
                }
            } catch (Exception err) {
                return effect;
            }
        }
        return effect;
    }

    public static void onDeath(ChestCavityEntity entity) {
        ChestCavityInstance ccInstance = entity.getChestCavityInstance();
        ccInstance.getChestCavityType().onDeath(ccInstance);
        if (entity instanceof Player) {
            insertWelfareOrgans(ccInstance);
        } else {
            for (int i = 0; i < ccInstance.inventory.getContainerSize(); ++i) {
                ItemStack curItem = ccInstance.inventory.getItem(i);
                if (!isOriginalOrgan(ccInstance, curItem)) {
                    ccInstance.inventory.removeItemNoUpdate(i);
                    ccInstance.owner.spawnAtLocation(curItem);
                }
            }
        }
    }

    public static ChestCavityInventory openChestCavity(ChestCavityInstance cc) {
        if (!cc.opened) {
            generateChestCavityIfOpened(cc);
        }
        return cc.inventory;
    }

    public static void organUpdate(ChestCavityInstance cc) {
        if (cc.owner == null || cc.owner.level().isClientSide()) return;
        Map<ResourceLocation, Float> organScores = cc.getOrganScores();
        if (!cc.oldOrganScores.equals(organScores)) {
            OrganUpdateListeners.call(cc.owner, cc);
            CCEvents.postUpdateCCScore(cc);
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
            String scoreName = key.getPath();
            output.accept(scoreName + ": " + value + " ");
        });
    }

    public static void setOrganCompatibility(ChestCavityInstance instance, ItemStack itemStack) {
        if (itemStack != ItemStack.EMPTY) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("owner", instance.compatibility_id);
            tag.putString("name", instance.owner.getDisplayName().getString());
            itemStack.addTagElement(ChestCavity.COMPATIBILITY_TAG.toString(), tag);
        }
    }

    public static void removeOrganCompatibility(ItemStack itemStack) {
        if (itemStack != ItemStack.EMPTY) {
            itemStack.removeTagKey(ChestCavity.COMPATIBILITY_TAG.toString());
        }
    }
}