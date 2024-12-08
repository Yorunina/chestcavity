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
    private List<Integer> forbiddenSlots = new ArrayList<>();
    private float dropRateMultiplier = 1.0F;
    private boolean bossChestCavity = false;
    private boolean playerChestCavity = false;
    private int inventorySize = 27;

    public GeneratedChestCavityType() {
    }

    public Map<ResourceLocation, Float> getDefaultOrganScores() {
        if (this.defaultOrganScores == null) {
            this.defaultOrganScores = new HashMap<>();
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

        for (Ingredient ingredient : this.getExceptionalOrganList().keySet()) {
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
        this.droppableOrgans = new LinkedList<>();

        for(int i = 0; i < this.defaultChestCavity.getContainerSize(); ++i) {
            ItemStack stack = this.defaultChestCavity.getItem(i);
            if (OrganManager.isTrueOrgan(stack.getItem())) {
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
        chestCavity.clearContent();

        for(int i = 0; i < chestCavity.getContainerSize(); ++i) {
            chestCavity.setItem(i, this.defaultChestCavity.getItem(i));
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
        List<ItemStack> loot = new ArrayList<>();
        if (this.playerChestCavity) {
            return loot;
        } else if (this.bossChestCavity) {
            this.generateGuaranteedOrganDrops(random, looting, loot);
            return loot;
        } else {
            if (random.nextFloat() < (ChestCavity.config.UNIVERSAL_DONOR_RATE + ChestCavity.config.ORGAN_BUNDLE_LOOTING_BOOST * (float)looting) * this.getDropRateMultiplier()) {
                this.generateRareOrganDrops(random, looting, loot);
            }

            return loot;
        }
    }

    public void generateRareOrganDrops(RandomSource random, int looting, List<ItemStack> loot) {
        LinkedList<ItemStack> organPile = new LinkedList<>(this.getDroppableOrgans());
        int rolls = 1 + random.nextInt(3) + random.nextInt(3);
        ChestCavityUtil.drawOrgansFromPile(organPile, rolls, random, loot);
    }

    public void generateGuaranteedOrganDrops(RandomSource random, int looting, List<ItemStack> loot) {
        LinkedList<ItemStack> organPile = new LinkedList<>(this.getDroppableOrgans());
        int rolls = 3 + random.nextInt(2 + looting) + random.nextInt(2 + looting);
        ChestCavityUtil.drawOrgansFromPile(organPile, rolls, random, loot);
    }

    public void setOrganCompatibility(ChestCavityInstance instance) {
        ChestCavityInventory chestCavity = instance.inventory;

        int universalOrgans;
        for(universalOrgans = 0; universalOrgans < chestCavity.getContainerSize(); ++universalOrgans) {
            ItemStack itemStack = chestCavity.getItem(universalOrgans);
            if (itemStack != ItemStack.EMPTY) {
                CompoundTag tag = new CompoundTag();
                tag.putUUID("owner", instance.compatibility_id);
                tag.putString("name", instance.owner.getDisplayName().getString());
                itemStack.addTagElement(ChestCavity.COMPATIBILITY_TAG.toString(), tag);
            }
        }

        if (!this.playerChestCavity) {
            universalOrgans = 0;
            RandomSource random = instance.owner.getRandom();
            if (this.bossChestCavity) {
                universalOrgans = 3 + random.nextInt(2) + random.nextInt(2);
            } else if (random.nextFloat() < ChestCavity.config.UNIVERSAL_DONOR_RATE) {
                universalOrgans = 1 + random.nextInt(3) + random.nextInt(3);
            }

            for(; universalOrgans > 0; --universalOrgans) {
                int i = random.nextInt(chestCavity.getContainerSize());
                ItemStack itemStack = chestCavity.getItem(i);
                if (itemStack != ItemStack.EMPTY && OrganManager.isTrueOrgan(itemStack.getItem())) {
                    itemStack.removeTagKey(ChestCavity.COMPATIBILITY_TAG.toString());
                }
            }
        }

    }
    public int getInventorySize() {
        return this.inventorySize;
    }
    public void setInventorySize(int size) {
        this.inventorySize = size;
    }
    public float getHeartBleedCap() {
        return this.bossChestCavity ? 5.0F : Float.MAX_VALUE;
    }

    public boolean isOpenable(ChestCavityInstance instance) {
        boolean weakEnough = instance.owner.getHealth() <= (float)ChestCavity.config.CHEST_OPENER_ABSOLUTE_HEALTH_THRESHOLD || instance.owner.getHealth() <= instance.owner.getMaxHealth() * ChestCavity.config.CHEST_OPENER_FRACTIONAL_HEALTH_THRESHOLD;
        boolean chestVulnerable = instance.owner.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
        boolean easeOfAccess = instance.getOrganScore(CCOrganScores.EASE_OF_ACCESS) > 0.0F;
        return chestVulnerable && (easeOfAccess || weakEnough);
    }

    public void onDeath(ChestCavityInstance cc) {
        cc.projectileQueue.clear();
        if (cc.connectedCrystal != null) {
            cc.connectedCrystal.setBeamTarget((BlockPos)null);
            cc.connectedCrystal = null;
        }

        if (cc.opened && (!this.playerChestCavity || !ChestCavity.config.KEEP_CHEST_CAVITY)) {
            ChestCavityUtil.dropUnboundOrgans(cc);
        }

    }
}
