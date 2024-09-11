package net.tigereye.chestcavity.chestcavities.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.organs.OrganData;
import net.tigereye.chestcavity.chestcavities.organs.OrganManager;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.util.ChestCavityUtil;

public class GeneratedChestCavityType implements ChestCavityType {
    private Map<ResourceLocation, Float> defaultOrganScores = null;
    private ChestCavityInventory defaultChestCavity = null;
    private Map<ResourceLocation, Float> baseOrganScores = null;
    private Map<Ingredient, Map<ResourceLocation, Float>> exceptionalOrganList = null;
    private List<ItemStack> droppableOrgans = null;
    private List<Integer> forbiddenSlots = new ArrayList();
    private float dropRateMultiplier = 1.0F;
    private boolean bossChestCavity = false;
    private boolean playerChestCavity = false;

    public GeneratedChestCavityType() {
    }

    public Map<ResourceLocation, Float> getDefaultOrganScores() {
        if (this.defaultOrganScores == null) {
            this.defaultOrganScores = new HashMap();
            if (!ChestCavityUtil.determineDefaultOrganScores(this)) {
                this.defaultOrganScores = null;
            }
        }

        return this.defaultOrganScores;
    }

    public float getDefaultOrganScore(ResourceLocation id) {
        return (Float)this.getDefaultOrganScores().getOrDefault(id, 0.0F);
    }

    public ChestCavityInventory getDefaultChestCavity() {
        return this.defaultChestCavity;
    }

    public void setDefaultChestCavity(ChestCavityInventory inv) {
        this.defaultChestCavity = inv;
    }

    public Map<ResourceLocation, Float> getBaseOrganScores() {
        return this.baseOrganScores;
    }

    public float getBaseOrganScore(ResourceLocation id) {
        return (Float)this.getBaseOrganScores().getOrDefault(id, 0.0F);
    }

    public void setBaseOrganScores(Map<ResourceLocation, Float> organScores) {
        this.baseOrganScores = organScores;
    }

    public void setBaseOrganScore(ResourceLocation id, float score) {
        this.baseOrganScores.put(id, score);
    }

    public Map<Ingredient, Map<ResourceLocation, Float>> getExceptionalOrganList() {
        return this.exceptionalOrganList;
    }

    public Map<ResourceLocation, Float> getExceptionalOrganScore(ItemStack itemStack) {
        Iterator<Ingredient> var2 = this.getExceptionalOrganList().keySet().iterator();

        while(var2.hasNext()) {
            Ingredient ingredient = (Ingredient)var2.next();
            if (ingredient.test(itemStack)) {
                return (Map)this.getExceptionalOrganList().get(ingredient);
            }
        }

        return null;
    }

    public void setExceptionalOrganList(Map<Ingredient, Map<ResourceLocation, Float>> list) {
        this.exceptionalOrganList = list;
    }

    public void setExceptionalOrgan(Ingredient ingredient, Map<ResourceLocation, Float> scores) {
        this.exceptionalOrganList.put(ingredient, scores);
    }

    public List<ItemStack> getDroppableOrgans() {
        if (this.droppableOrgans == null) {
            this.deriveDroppableOrgans();
        }

        return this.droppableOrgans;
    }

    public void setDroppableOrgans(List<ItemStack> list) {
        this.droppableOrgans = list;
    }

    private void deriveDroppableOrgans() {
        this.droppableOrgans = new LinkedList();

        for(int i = 0; i < this.defaultChestCavity.m_6643_(); ++i) {
            ItemStack stack = this.defaultChestCavity.m_8020_(i);
            if (OrganManager.isTrueOrgan(stack.m_41720_())) {
                this.droppableOrgans.add(stack);
            }
        }

    }

    public List<Integer> getForbiddenSlots() {
        return this.forbiddenSlots;
    }

    public void setForbiddenSlots(List<Integer> list) {
        this.forbiddenSlots = list;
    }

    public void forbidSlot(int slot) {
        this.forbiddenSlots.add(slot);
    }

    public void allowSlot(int slot) {
        int index = this.forbiddenSlots.indexOf(slot);
        if (index != -1) {
            this.forbiddenSlots.remove(index);
        }

    }

    public boolean isSlotForbidden(int index) {
        return this.forbiddenSlots.contains(index);
    }

    public boolean isBossChestCavity() {
        return this.bossChestCavity;
    }

    public void setBossChestCavity(boolean bool) {
        this.bossChestCavity = bool;
    }

    public boolean isPlayerChestCavity() {
        return this.playerChestCavity;
    }

    public void setPlayerChestCavity(boolean bool) {
        this.playerChestCavity = bool;
    }

    public void fillChestCavityInventory(ChestCavityInventory chestCavity) {
        chestCavity.m_6211_();

        for(int i = 0; i < chestCavity.m_6643_(); ++i) {
            chestCavity.m_6836_(i, this.defaultChestCavity.m_8020_(i));
        }

    }

    public void loadBaseOrganScores(Map<ResourceLocation, Float> organScores) {
        organScores.clear();
    }

    public OrganData catchExceptionalOrgan(ItemStack slot) {
        Map<ResourceLocation, Float> organMap = this.getExceptionalOrganScore(slot);
        if (organMap != null) {
            OrganData organData = new OrganData();
            organData.organScores = organMap;
            organData.pseudoOrgan = true;
            return organData;
        } else {
            return null;
        }
    }

    public float getDropRateMultiplier() {
        return this.dropRateMultiplier;
    }

    public void setDropRateMultiplier(float multiplier) {
        this.dropRateMultiplier = multiplier;
    }

    public List<ItemStack> generateLootDrops(RandomSource random, int looting) {
        List<ItemStack> loot = new ArrayList();
        if (this.playerChestCavity) {
            return loot;
        } else if (this.bossChestCavity) {
            this.generateGuaranteedOrganDrops(random, looting, loot);
            return loot;
        } else {
            if (random.m_188501_() < (ChestCavity.config.UNIVERSAL_DONOR_RATE + ChestCavity.config.ORGAN_BUNDLE_LOOTING_BOOST * (float)looting) * this.getDropRateMultiplier()) {
                this.generateRareOrganDrops(random, looting, loot);
            }

            return loot;
        }
    }

    public void generateRareOrganDrops(RandomSource random, int looting, List<ItemStack> loot) {
        LinkedList<ItemStack> organPile = new LinkedList(this.getDroppableOrgans());
        int rolls = 1 + random.m_188503_(3) + random.m_188503_(3);
        ChestCavityUtil.drawOrgansFromPile(organPile, rolls, random, loot);
    }

    public void generateGuaranteedOrganDrops(RandomSource random, int looting, List<ItemStack> loot) {
        LinkedList<ItemStack> organPile = new LinkedList(this.getDroppableOrgans());
        int rolls = 3 + random.m_188503_(2 + looting) + random.m_188503_(2 + looting);
        ChestCavityUtil.drawOrgansFromPile(organPile, rolls, random, loot);
    }

    public void setOrganCompatibility(ChestCavityInstance instance) {
        ChestCavityInventory chestCavity = instance.inventory;

        int universalOrgans;
        for(universalOrgans = 0; universalOrgans < chestCavity.m_6643_(); ++universalOrgans) {
            ItemStack itemStack = chestCavity.m_8020_(universalOrgans);
            if (itemStack != null && itemStack != ItemStack.f_41583_) {
                CompoundTag tag = new CompoundTag();
                tag.m_128362_("owner", instance.compatibility_id);
                tag.m_128359_("name", instance.owner.m_5446_().getString());
                itemStack.m_41700_(ChestCavity.COMPATIBILITY_TAG.toString(), tag);
            }
        }

        if (!this.playerChestCavity) {
            universalOrgans = 0;
            RandomSource random = instance.owner.m_217043_();
            if (this.bossChestCavity) {
                universalOrgans = 3 + random.m_188503_(2) + random.m_188503_(2);
            } else if (random.m_188501_() < ChestCavity.config.UNIVERSAL_DONOR_RATE) {
                universalOrgans = 1 + random.m_188503_(3) + random.m_188503_(3);
            }

            for(; universalOrgans > 0; --universalOrgans) {
                int i = random.m_188503_(chestCavity.m_6643_());
                ItemStack itemStack = chestCavity.m_8020_(i);
                if (itemStack != null && itemStack != ItemStack.f_41583_ && OrganManager.isTrueOrgan(itemStack.m_41720_())) {
                    itemStack.m_41749_(ChestCavity.COMPATIBILITY_TAG.toString());
                }
            }
        }

    }

    public float getHeartBleedCap() {
        return this.bossChestCavity ? 5.0F : Float.MAX_VALUE;
    }

    public boolean isOpenable(ChestCavityInstance instance) {
        boolean weakEnough = instance.owner.m_21223_() <= (float)ChestCavity.config.CHEST_OPENER_ABSOLUTE_HEALTH_THRESHOLD || instance.owner.m_21223_() <= instance.owner.m_21233_() * ChestCavity.config.CHEST_OPENER_FRACTIONAL_HEALTH_THRESHOLD;
        boolean chestVulnerable = instance.owner.m_6844_(EquipmentSlot.CHEST).m_41619_();
        boolean easeOfAccess = instance.getOrganScore(CCOrganScores.EASE_OF_ACCESS) > 0.0F;
        return chestVulnerable && (easeOfAccess || weakEnough);
    }

    public void onDeath(ChestCavityInstance cc) {
        cc.projectileQueue.clear();
        if (cc.connectedCrystal != null) {
            cc.connectedCrystal.m_31052_((BlockPos)null);
            cc.connectedCrystal = null;
        }

        if (cc.opened && (!this.playerChestCavity || !ChestCavity.config.KEEP_CHEST_CAVITY)) {
            ChestCavityUtil.dropUnboundOrgans(cc);
        }

    }
}
