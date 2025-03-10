package net.tigereye.chestcavity.chestcavities.types;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.util.ChestCavityUtil;

import java.util.HashMap;
import java.util.Map;

import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.DEFAULT_INVENTORY_TYPE_STRING;

public class GeneratedChestCavityType implements ChestCavityType {
    private Map<ResourceLocation, Float> defaultOrganScores = null;
    private ChestCavityInventory defaultChestCavity = null;
    private Map<ResourceLocation, Float> baseOrganScores = null;
    private Map<Ingredient, Map<ResourceLocation, Float>> exceptionalOrganList = null;
    private ResourceLocation inventoryType = new ResourceLocation(DEFAULT_INVENTORY_TYPE_STRING);

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
        return this.getDefaultOrganScores().getOrDefault(id, 0.0F);
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
        return this.getBaseOrganScores().getOrDefault(id, 0.0F);
    }

    public void setInventoryType(ResourceLocation id) {
        this.inventoryType = id;
    }

    public ResourceLocation getInventoryType() {
        return this.inventoryType;
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
                return this.getExceptionalOrganList().get(ingredient);
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

    public void fillChestCavityInventory(ChestCavityInventory chestCavity) {
        chestCavity.clearContent();

        for (int i = 0; i < chestCavity.getContainerSize(); ++i) {
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


    public void setOrganCompatibility(ChestCavityInstance instance) {
        ChestCavityInventory chestCavity = instance.inventory;

        int universalOrgans;
        for (universalOrgans = 0; universalOrgans < chestCavity.getContainerSize(); ++universalOrgans) {
            ItemStack itemStack = chestCavity.getItem(universalOrgans);
            if (itemStack != ItemStack.EMPTY) {
                CompoundTag tag = new CompoundTag();
                tag.putUUID("owner", instance.compatibility_id);
                tag.putString("name", instance.owner.getDisplayName().getString());
                itemStack.addTagElement(ChestCavity.COMPATIBILITY_TAG.toString(), tag);
            }
        }

    }

    public float getHeartBleedCap() {
        return 5.0F;
    }

    public boolean isOpenable(ChestCavityInstance instance) {
        boolean weakEnough = instance.owner.getHealth() <= (float) ChestCavity.config.CHEST_OPENER_ABSOLUTE_HEALTH_THRESHOLD || instance.owner.getHealth() <= instance.owner.getMaxHealth() * ChestCavity.config.CHEST_OPENER_FRACTIONAL_HEALTH_THRESHOLD;
        boolean chestVulnerable = instance.owner.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
        boolean easeOfAccess = instance.getOrganScore(CCOrganScores.EASE_OF_ACCESS) > 0.0F;
        return chestVulnerable && (easeOfAccess || weakEnough);
    }

    public void onDeath(ChestCavityInstance cc) {
        cc.projectileQueue.clear();
        if (cc.connectedCrystal != null) {
            cc.connectedCrystal.setBeamTarget(null);
            cc.connectedCrystal = null;
        }

    }
}
