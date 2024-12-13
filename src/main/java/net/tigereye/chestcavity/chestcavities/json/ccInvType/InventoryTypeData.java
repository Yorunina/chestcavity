package net.tigereye.chestcavity.chestcavities.json.ccInvType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class InventoryTypeData {
    public List<SlotPosition> slotPositions;
    public ResourceLocation backgroundTexture;

    public static List<SlotPosition> getDefaultInventoryTypeSlotPositions() {
        int n, m;
        List<SlotPosition> slotPositions = new ArrayList<>();
        for(n = 0; n < 3; ++n) {
            for(m = 0; m < 9 && n * 9 + m < 9; ++m) {
                slotPositions.add(new SlotPosition(8 + m * 18, 18 + n * 18));
            }
        }
        return slotPositions;
    }
    public int getSlotSize() {
        return this.slotPositions.size();
    }
    public List<SlotPosition> getSlotPosition() {
        return this.slotPositions;
    }
    public ResourceLocation getBackgroundTexture() {
        return this.backgroundTexture;
    }


    public static final InventoryTypeData DEFAULT_INVENTORY_TYPE = new InventoryTypeData(new ResourceLocation("chestcavity", "default_cc_background"), getDefaultInventoryTypeSlotPositions());
    public InventoryTypeData(ResourceLocation backgroundTexture, List<SlotPosition> slotPositions) {
        this.backgroundTexture = backgroundTexture;
        this.slotPositions = slotPositions;
    }
}
