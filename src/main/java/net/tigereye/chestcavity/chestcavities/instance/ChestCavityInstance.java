package net.tigereye.chestcavity.chestcavities.instance;

import java.util.*;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.listeners.OrganOnHitContext;
import net.tigereye.chestcavity.util.ChestCavityUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import static net.tigereye.chestcavity.registration.CCAttributes.ADDITIONAL_SLOT;


public class ChestCavityInstance implements ContainerListener {
    public static final Logger LOGGER = LogManager.getLogger();
    protected ChestCavityType type;
    public LivingEntity owner;
    public UUID compatibility_id;
    public boolean opened = false;
    public ChestCavityInventory inventory;
    public Map<ResourceLocation, Float> oldOrganScores = new HashMap<>();
    protected Map<ResourceLocation, Float> organScores = new HashMap<>();
    public List<OrganOnHitContext> onHitListeners = new ArrayList<>();
    public LinkedList<Consumer<LivingEntity>> projectileQueue = new LinkedList<>();
    public int heartBleedTimer = 0;
    public int bloodPoisonTimer = 0;
    public int liverTimer = 0;
    public float metabolismRemainder = 0.0F;
    public float lungRemainder = 0.0F;
    public int projectileCooldown = 0;
    public int furnaceProgress = 0;
    public int photosynthesisProgress = 0;
    public EndCrystal connectedCrystal = null;
    public boolean updatePacket = false;
    public ChestCavityInstance ccBeingOpened = null;

    public ChestCavityInstance(ChestCavityType type, LivingEntity owner) {
        this.type = type;
        this.owner = owner;
        this.compatibility_id = owner.getUUID();
        int slotSize = type.getInventorySize();
        this.inventory = new ChestCavityInventory(slotSize);
        ChestCavityUtil.evaluateChestCavity(this);
    }

    public ChestCavityType getChestCavityType() {
        return this.type;
    }

    public Map<ResourceLocation, Float> getOrganScores() {
        return this.organScores;
    }

    public void setOrganScores(Map<ResourceLocation, Float> organScores) {
        this.organScores = organScores;
    }

    public float getOrganScore(ResourceLocation id) {
        return (Float)this.organScores.getOrDefault(id, 0.0F);
    }

    public float getOldOrganScore(ResourceLocation id) {
        return (Float)this.oldOrganScores.getOrDefault(id, 0.0F);
    }

    public void containerChanged(@NotNull Container sender) {
        ChestCavityUtil.clearForbiddenSlots(this);
        ChestCavityUtil.evaluateChestCavity(this);
    }

    public void fromTag(CompoundTag tag, LivingEntity owner) {
        if (ChestCavity.config.DEBUG_MODE) {
            LOGGER.debug("[Chest Cavity] Reading ChestCavityManager fromTag");
        }

        this.owner = owner;
        CompoundTag ccTag;
        ListTag NbtList;
        if (tag.contains("ChestCavity")) {
            ccTag = tag.getCompound("ChestCavity");
            this.opened = ccTag.getBoolean("opened");
            this.heartBleedTimer = ccTag.getInt("HeartTimer");
            this.bloodPoisonTimer = ccTag.getInt("KidneyTimer");
            this.liverTimer = ccTag.getInt("LiverTimer");
            this.metabolismRemainder = ccTag.getFloat("MetabolismRemainder");
            this.lungRemainder = ccTag.getFloat("LungRemainder");
            this.furnaceProgress = ccTag.getInt("FurnaceProgress");
            this.photosynthesisProgress = ccTag.getInt("PhotosynthesisProgress");
            if (ccTag.contains("compatibility_id")) {
                this.compatibility_id = ccTag.getUUID("compatibility_id");
            } else {
                this.compatibility_id = owner.getUUID();
            }
            int slotSize = type.getInventorySize();
            if (Objects.nonNull(owner.getAttribute(ADDITIONAL_SLOT.get()))) {
                slotSize = slotSize + (int) owner.getAttributeValue(ADDITIONAL_SLOT.get());
            }
            ChestCavity.LOGGER.error("[Chest Cavity] Created ChestCavityManager with " + slotSize + " slots");
            this.inventory = new ChestCavityInventory(slotSize);
            try {
                this.inventory.removeListener(this);
            } catch (NullPointerException ignored) {
            }

            if (ccTag.contains("Inventory")) {
                NbtList = ccTag.getList("Inventory", 10);
                this.inventory.readTags(NbtList);
            } else if (this.opened) {
                LOGGER.warn("[Chest Cavity] " + owner.getName().getString() + "'s Chest Cavity is mangled. It will be replaced");
                ChestCavityUtil.generateChestCavityIfOpened(this);
            }

            this.inventory.addListener(this);
        } else if (tag.contains("cardinal_components")) {
            ccTag = tag.getCompound("cardinal_components");
            if (ccTag.contains("chestcavity:inventorycomponent")) {
                ccTag = tag.getCompound("chestcavity:inventorycomponent");
                if (ccTag.contains("chestcavity")) {
                    LOGGER.info("[Chest Cavity] Found " + owner.getName().getString() + "'s old [Cardinal Components] Chest Cavity.");
                    this.opened = true;
                    NbtList = ccTag.getList("Inventory", 10);

                    try {
                        this.inventory.removeListener(this);
                    } catch (NullPointerException ignored) {
                    }

                    this.inventory.readTags(NbtList);
                    this.inventory.addListener(this);
                }
            }
        }

        ChestCavityUtil.evaluateChestCavity(this);
    }

    public void toTag(CompoundTag tag) {
        CompoundTag ccTag = new CompoundTag();
        ccTag.putBoolean("opened", this.opened);
        ccTag.putUUID("compatibility_id", this.compatibility_id);
        ccTag.putInt("HeartTimer", this.heartBleedTimer);
        ccTag.putInt("KidneyTimer", this.bloodPoisonTimer);
        ccTag.putInt("LiverTimer", this.liverTimer);
        ccTag.putFloat("MetabolismRemainder", this.metabolismRemainder);
        ccTag.putFloat("LungRemainder", this.lungRemainder);
        ccTag.putInt("FurnaceProgress", this.furnaceProgress);
        ccTag.putInt("PhotosynthesisProgress", this.photosynthesisProgress);
        ccTag.put("Inventory", this.inventory.getTags());
        tag.put("ChestCavity",ccTag);
    }

    public void clone(ChestCavityInstance other) {
        this.opened = other.opened;
        this.type = other.type;
        this.compatibility_id = other.compatibility_id;
        try {
            this.inventory.removeListener(this);
        } catch (NullPointerException ignored) {
        }

        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            this.inventory.setItem(i, other.inventory.getItem(i));
        }

        this.inventory.readTags(other.inventory.getTags());
        this.inventory.addListener(this);
        this.heartBleedTimer = other.heartBleedTimer;
        this.liverTimer = other.liverTimer;
        this.bloodPoisonTimer = other.bloodPoisonTimer;
        this.metabolismRemainder = other.metabolismRemainder;
        this.lungRemainder = other.lungRemainder;
        this.furnaceProgress = other.furnaceProgress;
        this.connectedCrystal = other.connectedCrystal;
        ChestCavityUtil.evaluateChestCavity(this);
    }

}
