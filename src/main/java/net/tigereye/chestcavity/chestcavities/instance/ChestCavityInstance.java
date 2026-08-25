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
import net.tigereye.chestcavity.network.ChestCavitySyncService;
import net.tigereye.chestcavity.ui.ChestCavityScreenHandler;
import net.tigereye.chestcavity.service.ChestCavityEvaluationService;
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
    // CustomDataMap暴露给Kubejs层使用
    public Map<String, Object> customDataMap = new HashMap<>();
    private ChestCavitySnapshot lastSnapshot;

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
        this.commitSnapshot();
    }

    public IChestCavityType getChestCavityType() {
        return this.type;
    }

    public Map<ResourceLocation, Float> getOrganScores() {
        return this.organScores;
    }

    public void setOrganScore(ResourceLocation id, float score) {
        this.organScores.put(id, score);
        this.markDirty();
    }

    public void setOrganScores(Map<ResourceLocation, Float> organScores) {
        this.organScores = new HashMap<>(organScores);
        this.markDirty();
    }

    public float getOrganScore(ResourceLocation id) {
        return this.organScores.getOrDefault(id, 0.0F);
    }

    public float getOrganScoreOrDefault(ResourceLocation id, float defaultValue) {
        return this.organScores.getOrDefault(id, defaultValue);
    }

    public float getOldOrganScore(ResourceLocation id) {
        if (this.lastSnapshot != null) {
            return this.lastSnapshot.getOrganScore(id);
        }
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

    public ChestCavitySnapshot createSnapshot() {
        return ChestCavitySnapshot.capture(this);
    }

    public boolean hasInventoryChangesSinceSnapshot() {
        return this.lastSnapshot == null || this.lastSnapshot.hasInventoryChanges(this);
    }

    public boolean hasOrganScoreChangesSinceSnapshot() {
        return this.lastSnapshot == null || this.lastSnapshot.hasOrganScoreChanges(this);
    }

    public void markDirty() {
        this.updatePacket = true;
        ChestCavitySyncService.enqueue(this);
    }

    public void setOpened(boolean opened) {
        if (this.opened != opened) {
            this.opened = opened;
            this.markDirty();
        }
    }

    public void applyRemoteState(boolean opened, Map<ResourceLocation, Float> organScores) {
        this.opened = opened;
        this.organScores = new HashMap<>(organScores);
        this.commitSnapshot();
    }

    public void markSyncPending() {
        this.updatePacket = true;
    }

    public boolean isSyncPending() {
        return this.updatePacket;
    }

    public void acknowledgeSync() {
        this.updatePacket = false;
    }

    /**
     * Commits the current state as the previous-state baseline used by
     * change detection. The legacy public fields are kept in sync for
     * compatibility with existing integrations.
     */
    public void commitSnapshot() {
        if (this.inventory == null) {
            return;
        }
        this.lastSnapshot = ChestCavitySnapshot.capture(this);
        this.oldInventory = this.inventory.clone();
        this.oldInventoryType = this.inventoryType;
        this.oldOrganScores.clear();
        this.oldOrganScores.putAll(this.organScores);
    }

    public void clearListenerMap() {
        this.slotListenerMap.clear();
    }

    public void addListener(String eventName, int slotIndex) {
        this.slotListenerMap
                .computeIfAbsent(eventName, ignored -> new HashMap<>())
                .put(slotIndex, this.getInventoryTypeData().getSlotType(slotIndex));
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
        this.markDirty();
        ChestCavityEvaluationService.evaluate(this);
        this.syncInventoryTypeData();
    }

    public boolean isSameAsOldInventory() {
        if (this.lastSnapshot != null) {
            return !this.lastSnapshot.hasInventoryChanges(this);
        }
        if (!this.oldInventoryType.equals(this.inventoryType)) {
            return false;
        }
        if (this.oldInventory.getContainerSize() != this.inventory.getContainerSize()) {
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
        int newInventorySize = InventoryTypeManager.getInventoryTypeData(inventoryType).getSlotSize();
        ChestCavityInventory newInventory = new ChestCavityInventory(newInventorySize);
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            if (newInventorySize <= i) {
                this.owner.spawnAtLocation(this.inventory.getItem(i));
                continue;
            }
            newInventory.setItem(i, this.inventory.getItem(i));
        }
        this.replaceInventory(newInventory, inventoryType);
    }

    public void replaceInventory(ChestCavityInventory replacement, ResourceLocation newInventoryType) {
        this.replaceInventory(replacement, newInventoryType, true);
    }

    public void replaceInventory(ChestCavityInventory replacement, ResourceLocation newInventoryType, boolean evaluate) {
        if (replacement == null) {
            throw new IllegalArgumentException("replacement inventory cannot be null");
        }
        if (this.inventory != null) {
            this.oldInventory = this.inventory.clone();
            this.oldInventoryType = this.inventoryType;
            this.inventory.removeListener(this);
        }
        this.inventoryType = newInventoryType;
        replacement.setInstance(this);
        this.inventory = replacement;
        this.inventory.addListener(this);
        this.syncInventoryTypeData();
        if (this.owner instanceof ServerPlayer player && player.containerMenu instanceof ChestCavityScreenHandler) {
            player.closeContainer();
        }
        this.markDirty();
        if (evaluate) {
            this.containerChanged(this.inventory);
        }
    }

    private void syncInventoryTypeData() {
        if (this.owner instanceof ChestCavityEntity ccEntity) {
            ccEntity.setInventoryTypeData(this.inventoryType);
        }
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
            ResourceLocation loadedInventoryType = new ResourceLocation(ccTag.getString("InventoryType"));
            if (ccTag.contains("CompatibilityId")) {
                this.compatibilityId = ccTag.getUUID("CompatibilityId");
            } else {
                this.compatibilityId = owner.getUUID();
            }

            int newInventorySize = InventoryTypeManager.getInventoryTypeData(loadedInventoryType).getSlotSize();
            if (newInventorySize < this.inventory.getContainerSize()) {
                for (int i = newInventorySize; i < this.inventory.getContainerSize(); i++) {
                    this.owner.spawnAtLocation(this.inventory.getItem(i));
                }
            }

            ChestCavityInventory loadedInventory = new ChestCavityInventory(newInventorySize);
            if (ccTag.contains("Inventory")) {
                ListTag nbtList = ccTag.getList("Inventory", 10);
                loadedInventory.fromTag(nbtList);
            }
            this.replaceInventory(loadedInventory, loadedInventoryType, false);
            this.containerChanged(this.inventory);
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

    public void copyFrom(ChestCavityInstance other) {
        this.opened = other.opened;
        this.type = other.type;
        this.compatibilityId = other.compatibilityId;
        this.liverTimer = other.liverTimer;
        this.bloodPoisonTimer = other.bloodPoisonTimer;
        this.metabolismRemainder = other.metabolismRemainder;
        this.lungRemainder = other.lungRemainder;
        this.customDataMap = new HashMap<>(other.customDataMap);
        this.slotListenerMap = new HashMap<>();
        other.slotListenerMap.forEach((eventName, listeners) ->
                this.slotListenerMap.put(eventName, new HashMap<>(listeners)));
        this.replaceInventory(other.inventory.copyFor(this), other.inventoryType, false);
        ChestCavityEvaluationService.evaluate(this);
    }

    /**
     * Compatibility alias retained for existing respawn integrations.
     */
    public void clone(ChestCavityInstance other) {
        this.copyFrom(other);
    }
}
