package net.tigereye.chestcavity.chestcavities.json.ccAssignment;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ChestCavityAssignmentResult {
    Map<ResourceLocation, ResourceLocation> chestcavityMap;
    public ChestCavityAssignmentResult() {
    }

    public Map<ResourceLocation, ResourceLocation> getChestcavityMap() {
        return chestcavityMap;
    }
    public void setChestcavityMap(Map<ResourceLocation, ResourceLocation> chestcavityMap) {
        this.chestcavityMap = chestcavityMap;
    }
}
