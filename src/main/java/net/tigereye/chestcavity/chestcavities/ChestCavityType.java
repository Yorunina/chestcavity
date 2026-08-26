package net.tigereye.chestcavity.chestcavities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.items.ItemStackHandler;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.service.OrganLookupService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.DEFAULT_INVENTORY_TYPE_STRING;
import static net.tigereye.chestcavity.registration.CCEnchantments.ADVANCE_SURGERY;
import static net.tigereye.chestcavity.registration.CCEnchantments.CREATIVE_SURGERY;

public class ChestCavityType implements IChestCavityType {
    private Map<ResourceLocation, Float> defaultOrganScores = null;
    private ChestCavityInventory defaultChestCavity = new ChestCavityInventory(0);
    private Map<ResourceLocation, Float> baseOrganScores = null;
    private Map<Ingredient, Map<ResourceLocation, Float>> exceptionalOrganList = null;
    private ResourceLocation inventoryType = new ResourceLocation(DEFAULT_INVENTORY_TYPE_STRING);

    public ChestCavityType() {
    }

    public Map<ResourceLocation, Float> getDefaultOrganScores() {
        if (this.defaultOrganScores == null) {
            this.defaultOrganScores = new HashMap<>();
            this.loadBaseOrganScores(this.defaultOrganScores);
            for (int i = 0; i < this.getDefaultChestCavity().getContainerSize(); ++i) {
                ItemStack itemStack = this.getDefaultChestCavity().getItem(i);
                if (itemStack.isEmpty()) continue;
                OrganData data = OrganLookupService.lookupOrgan(itemStack, this);
                if (data.isEmpty()) continue;
                data.organScores.forEach((key, value) -> OrganLookupService.addOrganScore(
                        key,
                        value * Math.min((float) itemStack.getCount() / (float) itemStack.getMaxStackSize(), 1.0F),
                        this.defaultOrganScores
                ));
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

    public void setInventoryType(ResourceLocation id) {
        this.inventoryType = id;
    }

    public ResourceLocation getInventoryType() {
        return this.inventoryType;
    }

    public void setBaseOrganScores(Map<ResourceLocation, Float> organScores) {
        this.baseOrganScores = organScores;
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
            if (itemStack.isEmpty()) continue;
            OrganData organData = OrganLookupService.lookupOrgan(itemStack, instance.getChestCavityType());
            if (organData.isEmpty()) continue;
            OrganLookupService.setOrganCompatibility(instance, itemStack);
        }
    }


    public boolean isOpenable(ChestCavityInstance instance, Map<Enchantment, Integer> allEnchantments, double easeAccess) {
        if (allEnchantments.containsKey(CREATIVE_SURGERY.get())) return true;

        int enchantLevel = allEnchantments.getOrDefault(ADVANCE_SURGERY.get(), 0);

        boolean weakEnough = instance.owner.getHealth() <= (float) ChestCavity.config.CHEST_OPENER_ABSOLUTE_HEALTH_THRESHOLD + 10 * enchantLevel ||
                instance.owner.getHealth() <= instance.owner.getMaxHealth() * (ChestCavity.config.CHEST_OPENER_FRACTIONAL_HEALTH_THRESHOLD + 0.1F * enchantLevel);

        boolean chestVulnerable = instance.owner.getItemBySlot(EquipmentSlot.CHEST).isEmpty();

        return chestVulnerable && (easeAccess > 0 || weakEnough) && easeAccess >= 0;
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
