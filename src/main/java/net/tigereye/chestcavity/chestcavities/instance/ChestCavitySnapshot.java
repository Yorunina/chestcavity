package net.tigereye.chestcavity.chestcavities.instance;

import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.util.ContainerNbtUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable view of the state that is relevant for change detection and
 * client synchronization.
 *
 * <p>The existing public fields on {@link ChestCavityInstance} remain
 * available for compatibility. New code should use this snapshot through the
 * instance methods instead of maintaining another copy of the previous state.</p>
 */
public final class ChestCavitySnapshot {
    private final boolean opened;
    private final ResourceLocation inventoryType;
    private final UUID compatibilityId;
    private final Map<ResourceLocation, Float> organScores;
    private final ListTag inventoryTag;

    private ChestCavitySnapshot(
            boolean opened,
            ResourceLocation inventoryType,
            UUID compatibilityId,
            Map<ResourceLocation, Float> organScores,
            ListTag inventoryTag
    ) {
        this.opened = opened;
        this.inventoryType = inventoryType;
        this.compatibilityId = compatibilityId;
        this.organScores = Collections.unmodifiableMap(new HashMap<>(organScores));
        this.inventoryTag = inventoryTag;
    }

    public static ChestCavitySnapshot capture(ChestCavityInstance instance) {
        ChestCavityInventory inventory = instance.inventory;
        ListTag inventoryTag = inventory == null ? new ListTag() : ContainerNbtUtil.save(inventory);
        return new ChestCavitySnapshot(
                instance.opened,
                instance.inventoryType,
                instance.compatibilityId,
                instance.getOrganScores(),
                inventoryTag
        );
    }

    public boolean isOpened() {
        return this.opened;
    }

    public ResourceLocation getInventoryType() {
        return this.inventoryType;
    }

    public float getOrganScore(ResourceLocation id) {
        return this.organScores.getOrDefault(id, 0.0F);
    }

    public Map<ResourceLocation, Float> getOrganScores() {
        return this.organScores;
    }

    public boolean hasOrganScoreChanges(ChestCavityInstance current) {
        return !this.organScores.equals(current.getOrganScores());
    }

    public boolean hasInventoryChanges(ChestCavityInstance current) {
        if (!Objects.equals(this.inventoryType, current.inventoryType)) {
            return true;
        }
        ChestCavityInventory inventory = current.inventory;
        return inventory == null || !this.inventoryTag.equals(ContainerNbtUtil.save(inventory));
    }

    public boolean matches(ChestCavityInstance current) {
        return this.opened == current.opened
                && Objects.equals(this.inventoryType, current.inventoryType)
                && Objects.equals(this.compatibilityId, current.compatibilityId)
                && this.organScores.equals(current.getOrganScores())
                && (current.inventory != null && this.inventoryTag.equals(ContainerNbtUtil.save(current.inventory)));
    }
}
