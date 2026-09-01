package net.tigereye.chestcavity.chestcavities.json.ccAssignment;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.DataResourceUtil;

import java.util.HashMap;
import java.util.Map;

public class ChestCavityAssignmentSerializer {
    public ChestCavityAssignmentSerializer() {
    }

    public ChestCavityAssignmentResult read(ResourceLocation id, ChestCavityAssignmentJsonFormat ccaJson) {
        ChestCavityAssignmentResult result = new ChestCavityAssignmentResult();
        if (ccaJson.chestcavity == null) {
            throw new JsonSyntaxException("Chest cavity assignment " + id + " must have a chest cavity type");
        } else if (ccaJson.entities == null) {
            throw new JsonSyntaxException("Chest cavity assignment " + id + " must have a list of entities");
        } else {
            Map<ResourceLocation, ResourceLocation> chestcavityMap = new HashMap<>();
            ResourceLocation chestCavityType = DataResourceUtil.normalizeId(ccaJson.chestcavity);
            int i = 0;
            for (JsonElement entry : ccaJson.entities) {
                ++i;
                try {
                    chestcavityMap.put(new ResourceLocation(entry.getAsString()), chestCavityType);
                } catch (Exception var9) {
                    ChestCavity.LOGGER.error("Error parsing entry no. " + i + " in " + id.toString() + "'s entity list");
                }
            }
            if (!chestcavityMap.isEmpty()) {
                result.setChestcavityMap(chestcavityMap);
            }
            return result;
        }
    }
}
