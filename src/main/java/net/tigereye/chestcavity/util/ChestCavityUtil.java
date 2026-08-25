package net.tigereye.chestcavity.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.IChestCavityType;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;
import net.tigereye.chestcavity.compat.kubejs.CCEvents;
import net.tigereye.chestcavity.listeners.OrganAddStatusEffectListeners;
import net.tigereye.chestcavity.listeners.OrganUpdateListeners;
import net.tigereye.chestcavity.registration.CCOrganScores;

import java.util.Map;
import java.util.Objects;

public class ChestCavityUtil {
    public ChestCavityUtil() {
    }

    public static void addOrganScore(ResourceLocation id, float value, Map<ResourceLocation, Float> organScores) {
        organScores.put(id, organScores.getOrDefault(id, 0.0F) + value);
    }

    public static int applyBreathInWater(ChestCavityInstance cc, int oldAir, int newAir) {
        //if your chest cavity is untouched or normal, we do nothing
        if (!cc.opened) {
            return newAir;
        }
        IChestCavityType ccType = cc.getChestCavityType();
        if (ccType.getDefaultOrganScore(CCOrganScores.BREATH_CAPACITY) <= 0 && ccType.getDefaultOrganScore(CCOrganScores.WATERBREATH) <= 0) {
            return newAir;
        }
        if (ccType.getDefaultOrganScore(CCOrganScores.BREATH_CAPACITY) == cc.getOrganScore(CCOrganScores.BREATH_CAPACITY) && ccType.getDefaultOrganScore(CCOrganScores.WATERBREATH) == cc.getOrganScore(CCOrganScores.WATERBREATH)) {
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
        if (!cc.opened) {
            return oldAir;
        }
        IChestCavityType ccType = cc.getChestCavityType();
        if (ccType.getDefaultOrganScore(CCOrganScores.BREATH_CAPACITY) <= 0 && ccType.getDefaultOrganScore(CCOrganScores.WATERBREATH) <= 0 && ccType.getDefaultOrganScore(CCOrganScores.BREATH_RECOVERY) <= 0) {
            return oldAir;
        }
        if (ccType.getDefaultOrganScore(CCOrganScores.BREATH_CAPACITY) == cc.getOrganScore(CCOrganScores.BREATH_CAPACITY) && ccType.getDefaultOrganScore(CCOrganScores.WATERBREATH) == cc.getOrganScore(CCOrganScores.WATERBREATH) && ccType.getDefaultOrganScore(CCOrganScores.BREATH_RECOVERY) == cc.getOrganScore(CCOrganScores.BREATH_RECOVERY)) {
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
        } else {

            if (source.is(DamageTypeTags.IS_FALL) || source.is(DamageTypes.FLY_INTO_WALL)) {
                damage = applyImpactResistant(cc, damage);
            }

            if (source.is(DamageTypeTags.IS_FIRE)) {
                damage = applyFireResistant(cc, damage);
            }

            return damage;
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


    public static int applyDigestion(ChestCavityInstance cc, int hunger, float saturation) {
        float defaultDigestion = cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.DIGESTION);
        float digestion = cc.getOrganScore(CCOrganScores.DIGESTION);

        float digestionDiff = digestion - defaultDigestion;
        if (digestionDiff == 0) {
            return hunger;
        } else if (digestionDiff < 0) {
            return Math.abs(digestionDiff) < hunger ? 1 : 0;
        } else {
            return Math.max((int) (hunger * (1 + digestionDiff / 4)), 1);
        }
    }

    public static float applyNutrition(ChestCavityInstance cc, int hunger, float saturation) {
        float defaultNutrition = cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.NUTRITION);
        float nutrition = cc.getOrganScore(CCOrganScores.NUTRITION);
        float nutritionDiff = nutrition - defaultNutrition;
        if (nutritionDiff == 0) {
            return saturation;
        } else if (nutritionDiff < 0) {
            return saturation * Math.max(1 + nutritionDiff / 2, 0.1F);
        } else {
            return saturation * (1 + nutritionDiff / 4);
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
            float defaultMetabolism = cc.getChestCavityType().getDefaultOrganScore(CCOrganScores.METABOLISM);
            float metabolismDiff = cc.getOrganScoreOrDefault(CCOrganScores.METABOLISM, defaultMetabolism) - defaultMetabolism;
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
                if (itemStack.isEmpty()) continue;
                OrganData data = lookupOrgan(itemStack, cc.getChestCavityType());
                if (data.isEmpty()) continue;
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
        ChestCavityInventory newInventory = new ChestCavityInventory(cc);
        newInventory.fromTag(tagList);
        cc.replaceInventory(newInventory, cc.getInventoryType(), false);
        cc.getChestCavityType().setOrganCompatibility(cc);
        cc.setOpened(true);
        ChestCavityUtil.evaluateChestCavity(cc);
    }

    public static boolean getCompatibility(ChestCavityInstance cc, ItemStack itemStack) {
        if (itemStack == null || itemStack == ItemStack.EMPTY) {
            return true;
        }

        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains(ChestCavity.COMPATIBILITY_TAG)) {
            return true;
        }

        boolean isCompat = false;
        tag = tag.getCompound(ChestCavity.COMPATIBILITY_TAG);
        if (tag.getUUID("owner").equals(cc.compatibilityId)) {
            isCompat = true;
        }
        return isCompat;
    }

    public static boolean isOriginalOrgan(ChestCavityInstance cc, int slot, ItemStack itemStack) {
        if (itemStack == null || itemStack == ItemStack.EMPTY) {
            return true;
        }

        CompoundTag tag = itemStack.getTag();
        if (tag != null && tag.contains(ChestCavity.COMPATIBILITY_TAG)) {
            CompoundTag compatibilityTag = tag.getCompound(ChestCavity.COMPATIBILITY_TAG);
            return compatibilityTag.getUUID("owner").equals(cc.compatibilityId);
        }

        ChestCavityInventory defaultInv = cc.getChestCavityType().getDefaultChestCavity();
        ItemStack defaultItem = defaultInv.getItem(slot);
        return ItemStack.isSameItemSameTags(itemStack, defaultItem) && itemStack.getCount() == defaultItem.getCount();
    }


    public static OrganData lookupOrgan(ItemStack itemStack, IChestCavityType cct) {
        OrganData organData = new OrganData();

        if (cct != null) {
            OrganData exceptionalOrganData = cct.catchExceptionalOrgan(itemStack);
            organData.mergeOrganScores(exceptionalOrganData);
        }

        OrganData nbtOrganData = OrganManager.readNBTOrganData(itemStack);
        organData.mergeOrganScores(nbtOrganData);

        Item item = itemStack.getItem();
        if (OrganManager.hasEntry(item)) {
            OrganData managedOrganData = OrganManager.getEntry(item);
            organData.mergeOrganScores(managedOrganData);
        }

        return organData;
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


    public static ChestCavityInventory openChestCavity(ChestCavityInstance cc) {
        if (!cc.opened) {
            generateChestCavityIfOpened(cc);
        }
        return cc.inventory;
    }

    public static void organUpdate(ChestCavityInstance cc) {
        if (cc.owner == null || cc.owner.level().isClientSide()) return;
        if (cc.hasOrganScoreChangesSinceSnapshot()) {
            cc.markDirty();
            OrganUpdateListeners.call(cc.owner, cc);
            CCEvents.postUpdateCCScore(cc);

            NetworkUtil.SendS2CChestCavityUpdatePacket(cc);
        }
        cc.commitSnapshot();
    }


    public static void setOrganCompatibility(ChestCavityInstance instance, ItemStack itemStack) {
        if (itemStack != ItemStack.EMPTY) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("owner", instance.compatibilityId);
            tag.putString("name", instance.owner instanceof Player ? instance.owner.getName().getString() : instance.owner.getType().getDescriptionId());
            itemStack.addTagElement(ChestCavity.COMPATIBILITY_TAG, tag);
        }
    }
}
