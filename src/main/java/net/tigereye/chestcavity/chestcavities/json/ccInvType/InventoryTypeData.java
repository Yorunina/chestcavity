package net.tigereye.chestcavity.chestcavities.json.ccInvType;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class InventoryTypeData {
    public List<SlotPosition> slotPositions;
    public ResourceLocation backgroundTexture;
    public SlotPosition playerInventoryPosition;

    public int getSlotSize() {
        return this.slotPositions.size();
    }
    public List<SlotPosition> getSlotPosition() {
        return this.slotPositions;
    }
    public ResourceLocation getBackgroundTexture() {
        return this.backgroundTexture;
    }
    public SlotPosition getPlayerInventoryPosition() {
        return this.playerInventoryPosition;
    }
    public void setSlotPositions(List<SlotPosition> slotPositions) {
        this.slotPositions = slotPositions;
    }
    public void setBackgroundTexture(ResourceLocation backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }
    public void setPlayerInventoryPosition(SlotPosition playerInventoryPosition) {
        this.playerInventoryPosition = playerInventoryPosition;
    }

    public InventoryTypeData(ResourceLocation backgroundTexture, List<SlotPosition> slotPositions, SlotPosition playerInventoryPosition) {
        this.backgroundTexture = backgroundTexture;
        this.slotPositions = slotPositions;
        this.playerInventoryPosition = playerInventoryPosition;
    }
}
