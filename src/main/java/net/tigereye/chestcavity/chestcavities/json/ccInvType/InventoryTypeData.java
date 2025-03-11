package net.tigereye.chestcavity.chestcavities.json.ccInvType;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class InventoryTypeData {
    public ResourceLocation id;
    public List<ChestCavitySlotDefinition> slotDefinitions;
    public ResourceLocation backgroundTexture;
    public SlotDefinition playerInventoryPosition;
    public TitleSlotDefinition titlePosition;
    public SlotDefinition backgroundSize;
    public TitleSlotDefinition inventoryLabelPosition;

    public ResourceLocation getId() {
        return this.id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public int getSlotSize() {
        return this.slotDefinitions.size();
    }

    public List<ChestCavitySlotDefinition> getSlotDefinitions() {
        return this.slotDefinitions;
    }

    public ChestCavitySlotDefinition getSlotDefinition(int index) {
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

    public void setSlotDefinitions(List<ChestCavitySlotDefinition> slotDefinitions) {
        this.slotDefinitions = slotDefinitions;
    }

    public void setBackgroundTexture(ResourceLocation backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    public void setPlayerInventoryPosition(SlotDefinition playerInventoryPosition) {
        this.playerInventoryPosition = playerInventoryPosition;
    }

    public void setTitlePosition(TitleSlotDefinition titlePosition) {
        this.titlePosition = titlePosition;
    }

    public TitleSlotDefinition getTitlePosition() {
        return this.titlePosition;
    }

    public void setInventoryLabelPosition(TitleSlotDefinition inventoryLabelPosition) {
        this.inventoryLabelPosition = inventoryLabelPosition;
    }

    public TitleSlotDefinition getInventoryLabelPosition() {
        return this.inventoryLabelPosition;
    }

    public void setBackgroundSize(SlotDefinition backgroundSize) {
        this.backgroundSize = backgroundSize;
    }

    public SlotDefinition getBackgroundSize() {
        return this.backgroundSize;
    }

    public InventoryTypeData(ResourceLocation id, ResourceLocation backgroundTexture, List<ChestCavitySlotDefinition> slotDefinitions, SlotDefinition playerInventoryPosition, TitleSlotDefinition titlePosition, TitleSlotDefinition inventoryLabelPosition, SlotDefinition backgroundSize) {
        this.id = id;
        this.backgroundTexture = backgroundTexture;
        this.slotDefinitions = slotDefinitions;
        this.playerInventoryPosition = playerInventoryPosition;
        this.titlePosition = titlePosition;
        this.inventoryLabelPosition = inventoryLabelPosition;
        this.backgroundSize = backgroundSize;
    }
}
