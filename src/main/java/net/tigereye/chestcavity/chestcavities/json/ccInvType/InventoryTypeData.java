package net.tigereye.chestcavity.chestcavities.json.ccInvType;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class InventoryTypeData {
    public List<SlotDefinition> slotDefinitions;
    public ResourceLocation backgroundTexture;
    public SlotDefinition playerInventoryPosition;

    public int getSlotSize() {
        return this.slotDefinitions.size();
    }
    public List<SlotDefinition> getSlotDefinitions() {
        return this.slotDefinitions;
    }
    public SlotDefinition getSlotDefinition(int index) {
        return this.slotDefinitions.get(index);
    }
    public String getSlotType(int index) {
        return this.slotDefinitions.get(index).getType();
    }
    public ResourceLocation getBackgroundTexture() {
        return this.backgroundTexture;
    }
    public SlotDefinition getPlayerInventoryPosition() {
        return this.playerInventoryPosition;
    }
    public void setSlotDefinitions(List<SlotDefinition> slotDefinitions) {
        this.slotDefinitions = slotDefinitions;
    }
    public void setBackgroundTexture(ResourceLocation backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }
    public void setPlayerInventoryPosition(SlotDefinition playerInventoryPosition) {
        this.playerInventoryPosition = playerInventoryPosition;
    }

    public InventoryTypeData(ResourceLocation backgroundTexture, List<SlotDefinition> slotDefinitions, SlotDefinition playerInventoryPosition) {
        this.backgroundTexture = backgroundTexture;
        this.slotDefinitions = slotDefinitions;
        this.playerInventoryPosition = playerInventoryPosition;
    }
}
