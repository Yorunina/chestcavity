package net.tigereye.chestcavity.chestcavities.types;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.items.ItemStackHandler;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.util.ChestCavityUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.DEFAULT_INVENTORY_TYPE_STRING;
import static net.tigereye.chestcavity.registration.CCEnchantments.ADVANCE_SURGERY;

public class GeneratedChestCavityType implements ChestCavityType {
    private Map<ResourceLocation, Float> defaultOrganScores = null;
    private ChestCavityInventory defaultChestCavity = new ChestCavityInventory(0);
    private Map<ResourceLocation, Float> baseOrganScores = null;
    private Map<Ingredient, Map<ResourceLocation, Float>> exceptionalOrganList = null;
    private ResourceLocation inventoryType = new ResourceLocation(DEFAULT_INVENTORY_TYPE_STRING);

    public GeneratedChestCavityType() {
    }

    public Map<ResourceLocation, Float> getDefaultOrganScores() {
        if (this.defaultOrganScores == null) {
            this.defaultOrganScores = new HashMap<>();
            this.loadBaseOrganScores(this.defaultOrganScores);
            for (int i = 0; i < this.getDefaultChestCavity().getContainerSize(); ++i) {
                ItemStack itemStack = this.getDefaultChestCavity().getItem(i);
                if (itemStack.isEmpty()) continue;
                OrganData data = ChestCavityUtil.lookupOrgan(itemStack, this);
                if (data.isEmpty()) continue;
                data.organScores.forEach((key, value) -> ChestCavityUtil.addOrganScore(key, value * Math.min((float) itemStack.getCount() / (float) itemStack.getMaxStackSize(), 1.0F), this.defaultOrganScores));
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
        organScores.putAll(this.getBaseOrganScores());
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
            ChestCavityUtil.setOrganCompatibility(instance, itemStack);
        }
    }


    public float getHeartBleedCap() {
        return 5.0F;
    }

    public boolean isOpenable(ChestCavityInstance instance, Map<Enchantment, Integer> allEnchantments) {
        int enchantLevel = allEnchantments.getOrDefault(ADVANCE_SURGERY.get(), 0);
        boolean weakEnough = instance.owner.getHealth() <= (float) ChestCavity.config.CHEST_OPENER_ABSOLUTE_HEALTH_THRESHOLD + 10 * enchantLevel ||
                instance.owner.getHealth() <= instance.owner.getMaxHealth() * (ChestCavity.config.CHEST_OPENER_FRACTIONAL_HEALTH_THRESHOLD + 0.2F * enchantLevel);
        boolean chestVulnerable = instance.owner.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
        double easeOfAccess = instance.getOrganScore(CCOrganScores.EASE_OF_ACCESS);
        return chestVulnerable && (easeOfAccess > 0 || weakEnough) && easeOfAccess >= 0;
    }

    public void onDeath(ChestCavityInstance cc) {
        cc.projectileQueue.clear();
        if (cc.connectedCrystal != null) {
            cc.connectedCrystal.setBeamTarget(null);
            cc.connectedCrystal = null;
        }
    }


    public static List<ItemStack> setInventoryTypeData(ItemStack stack, ResourceLocation inventoryType) {
        List<ItemStack> resList = new ArrayList<>();
        InventoryTypeData inventoryTypeData = InventoryTypeManager.getInventoryTypeData(inventoryType);
        CompoundTag itemNbt = stack.getOrCreateTag();
        if (!itemNbt.contains("Inventory")) {
            itemNbt.put("Inventory", new ItemStackHandler(inventoryTypeData.getSlotSize()).serializeNBT());
        }
        ItemStackHandler itemInventory = new ItemStackHandler();
        itemInventory.deserializeNBT(itemNbt.getCompound("Inventory"));
        ItemStackHandler newItemInventory = new ItemStackHandler(inventoryTypeData.getSlotSize());
        for (int i = 0; i < itemInventory.getSlots(); i++) {
            if (i >= newItemInventory.getSlots()) {
                resList.add(itemInventory.getStackInSlot(i));
                continue;
            }
            newItemInventory.setStackInSlot(i, itemInventory.getStackInSlot(i));
        }
        itemNbt.put("Inventory", newItemInventory.serializeNBT());
        itemNbt.putString("InventoryType", inventoryTypeData.getId().toString());
        return resList;
    }
}
