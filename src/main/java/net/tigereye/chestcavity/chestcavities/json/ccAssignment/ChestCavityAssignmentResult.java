package net.tigereye.chestcavity.chestcavities.json.ccAssignment;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ChestCavityAssignmentResult {
    Map<ResourceLocation, ResourceLocation> chestcavityMap;
    Map<ResourceLocation, ResourceLocation> screenTypeMap;
    public ChestCavityAssignmentResult() {
    }

    public Map<ResourceLocation, ResourceLocation> getChestcavityMap() {
        return chestcavityMap;
    }
    public void setChestcavityMap(Map<ResourceLocation, ResourceLocation> chestcavityMap) {
        this.chestcavityMap = chestcavityMap;
    }
    public Map<ResourceLocation, ResourceLocation> getScreenTypeMap() {
        return screenTypeMap;
    }
    public void setScreenTypeMap(Map<ResourceLocation, ResourceLocation> screenTypeMap) {
        this.screenTypeMap = screenTypeMap;
    }

}
