package net.tigereye.chestcavity.chestcavities.instance;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.LivingEntity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.IChestCavityType;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.ui.ChestCavityScreenHandler;
import net.tigereye.chestcavity.util.ChestCavityUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ChestCavityInstance implements ContainerListener {
    public static final Logger LOGGER = LogManager.getLogger();
    protected IChestCavityType type;
    public LivingEntity owner;
    public UUID compatibilityId;
    public boolean opened = false;
    public ChestCavityInventory inventory;
    public ChestCavityInventory oldInventory;
    public Map<ResourceLocation, Float> oldOrganScores = new HashMap<>();
    protected Map<ResourceLocation, Float> organScores = new HashMap<>();
    public int bloodPoisonTimer = 0;
    public int liverTimer = 0;
    public float metabolismRemainder = 0.0F;
    public float lungRemainder = 0.0F;
    public boolean updatePacket = true;
    public ResourceLocation inventoryType;
    public ResourceLocation oldInventoryType;
    public Map<String, Map<Integer, String>> slotListenerMap = new HashMap<>();
    public Map<String, Object> customDataMap = new HashMap<>();

    public ChestCavityInstance(IChestCavityType type, LivingEntity owner) {
        this.type = type;
        this.owner = owner;
        this.compatibilityId = owner.getUUID();
        this.inventoryType = type.getInventoryType();
        if (owner instanceof ChestCavityEntity ccEntity) {
            ccEntity.setInventoryTypeData(this.getInventoryType());
        }
        this.inventory = new ChestCavityInventory(this);
        this.oldInventoryType = type.getInventoryType();
        this.oldInventory = this.inventory.clone();
    }

    public IChestCavityType getChestCavityType() {
        return this.type;
    }

    public Map<ResourceLocation, Float> getOrganScores() {
        return this.organScores;
    }

    public void setOrganScore(ResourceLocation id, float score) {
        this.organScores.put(id, score);
    }

    public void setOrganScores(Map<ResourceLocation, Float> organScores) {
        this.organScores = organScores;
    }

    public float getOrganScore(ResourceLocation id) {
        return this.organScores.getOrDefault(id, 0.0F);
    }

    public float getOrganScoreOrDefault(ResourceLocation id, float defaultValue) {
        return this.organScores.getOrDefault(id, defaultValue);
    }

    public float getOldOrganScore(ResourceLocation id) {
        return this.oldOrganScores.getOrDefault(id, 0.0F);
    }

    public ResourceLocation getInventoryType() {
        return this.inventoryType;
    }

    public InventoryTypeData getInventoryTypeData() {
        return InventoryTypeManager.getInventoryTypeData(this.inventoryType);
    }

    public InventoryTypeData getOldInventoryTypeData() {
        return InventoryTypeManager.getInventoryTypeData(this.oldInventoryType);
    }

    public void clearListenerMap() {
        this.slotListenerMap.clear();
    }

    public void addListener(String eventName, int slotIndex) {
        if (!this.slotListenerMap.containsKey(eventName)) {
            this.slotListenerMap.put(eventName, new HashMap<>());
        }
        this.slotListenerMap.get(eventName).put(slotIndex, this.getInventoryTypeData().getSlotType(slotIndex));
    }

    public Map<Integer, String> getListenerMap(String eventName) {
        return this.slotListenerMap.getOrDefault(eventName, new HashMap<>());
    }

    public void removeListener(String eventName, int slotIndex) {
        if (this.slotListenerMap.containsKey(eventName)) {
            this.slotListenerMap.get(eventName).remove(slotIndex);
        }
    }

    @Override
    public void containerChanged(@NotNull Container sender) {
        if (isSameAsOldInventory()) return;
        ChestCavityUtil.evaluateChestCavity(this);
        this.oldInventory = this.inventory.clone();
        if (this.oldInventoryType != this.inventoryType) {
            this.oldInventoryType = this.inventoryType;
            if (this.owner instanceof ChestCavityEntity ccEntity) {
                ccEntity.setInventoryTypeData(this.inventoryType);
            }
        }
    }

    public boolean isSameAsOldInventory() {
        if (this.oldInventoryType != this.inventoryType) {
            return false;
        }
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            if (!this.oldInventory.getItem(i).equals(this.inventory.getItem(i), true)) {
                return false;
            }
        }
        return true;
    }

    public void setInventoryType(ResourceLocation inventoryType) {
        this.oldInventoryType = this.inventoryType;
        this.inventoryType = inventoryType;
        this.inventory.removeListener(this);
        int newInventorySize = InventoryTypeManager.getInventoryTypeData(inventoryType).getSlotSize();
        ChestCavityInventory newInventory = new ChestCavityInventory(this);
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            if (newInventorySize <= i) {
                this.owner.spawnAtLocation(this.inventory.getItem(i));
                continue;
            }
            newInventory.setItem(i, this.inventory.getItem(i));
        }
        this.inventory = newInventory;
        this.inventory.addListener(this);
        if (this.owner instanceof ChestCavityEntity ccEntity) {
            ccEntity.setInventoryTypeData(this.inventoryType);
        }
        if (this.owner instanceof ServerPlayer player && player.containerMenu instanceof ChestCavityScreenHandler) {
            player.closeContainer();
        }
        this.containerChanged(this.inventory);
    }

    public void fromTag(CompoundTag tag, LivingEntity owner) {
        this.owner = owner;
        CompoundTag ccTag;
        if (tag.contains("ChestCavity")) {
            ccTag = tag.getCompound("ChestCavity");
            this.opened = ccTag.getBoolean("opened");
            this.bloodPoisonTimer = ccTag.getInt("KidneyTimer");
            this.liverTimer = ccTag.getInt("LiverTimer");
            this.metabolismRemainder = ccTag.getFloat("MetabolismRemainder");
            this.lungRemainder = ccTag.getFloat("LungRemainder");
            this.inventoryType = new ResourceLocation(ccTag.getString("InventoryType"));
            this.oldInventoryType = this.inventoryType;
            if (this.owner instanceof ChestCavityEntity ccEntity) {
                ccEntity.setInventoryTypeData(this.inventoryType);
            }
            if (ccTag.contains("CompatibilityId")) {
                this.compatibilityId = ccTag.getUUID("CompatibilityId");
            } else {
                this.compatibilityId = owner.getUUID();
            }
            this.inventory.removeListener(this);

            int newInventorySize = InventoryTypeManager.getInventoryTypeData(this.inventoryType).getSlotSize();
            if (newInventorySize < this.inventory.getContainerSize()) {
                for (int i = newInventorySize; i < this.inventory.getContainerSize(); i++) {
                    this.owner.spawnAtLocation(this.inventory.getItem(i));
                }
            }
            this.inventory = new ChestCavityInventory(this);
            if (ccTag.contains("Inventory")) {
                ListTag nbtList = ccTag.getList("Inventory", 10);
                this.inventory.fromTag(nbtList);
            }
            this.inventory.addListener(this);
            ChestCavityUtil.evaluateChestCavity(this);
            this.oldInventory = this.inventory.clone();
        }
    }

    public void toTag(CompoundTag tag, LivingEntity owner) {
        CompoundTag ccTag = new CompoundTag();
        ccTag.putBoolean("opened", this.opened);
        ccTag.putString("InventoryType", this.inventoryType.toString());
        ccTag.putUUID("CompatibilityId", this.compatibilityId);
        ccTag.putInt("KidneyTimer", this.bloodPoisonTimer);
        ccTag.putInt("LiverTimer", this.liverTimer);
        ccTag.putFloat("MetabolismRemainder", this.metabolismRemainder);
        ccTag.putFloat("LungRemainder", this.lungRemainder);
        ccTag.put("Inventory", this.inventory.createTag());
        tag.put("ChestCavity", ccTag);
    }

    public void clone(ChestCavityInstance other) {
        this.opened = other.opened;
        this.type = other.type;
        this.compatibilityId = other.compatibilityId;
        this.oldInventoryType = other.oldInventoryType;
        this.inventoryType = other.inventoryType;
        this.oldInventory = other.oldInventory;
        if (this.owner instanceof ChestCavityEntity ccEntity) {
            ccEntity.setInventoryTypeData(this.inventoryType);
        }
        this.inventory.removeListener(this);
        for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
            this.inventory.setItem(i, other.inventory.getItem(i));
        }
        this.inventory = other.inventory.clone();
        this.inventory.addListener(this);
        this.liverTimer = other.liverTimer;
        this.bloodPoisonTimer = other.bloodPoisonTimer;
        this.metabolismRemainder = other.metabolismRemainder;
        this.lungRemainder = other.lungRemainder;
        this.customDataMap =  other.customDataMap;
        this.slotListenerMap =  other.slotListenerMap;
        ChestCavityUtil.evaluateChestCavity(this);
    }
}