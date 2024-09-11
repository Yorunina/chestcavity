package net.tigereye.chestcavity.chestcavities.types.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.ChestCavity;

public class ChestCavityAssignmentSerializer {
    public ChestCavityAssignmentSerializer() {
    }

    public Map<ResourceLocation, ResourceLocation> read(ResourceLocation id, ChestCavityAssignmentJsonFormat ccaJson) {
        if (ccaJson.chestcavity == null) {
            throw new JsonSyntaxException("Chest cavity assignment " + id + " must have a chest cavity type");
        } else if (ccaJson.entities == null) {
            throw new JsonSyntaxException("Chest cavity assignment " + id + " must have a list of entities");
        } else {
            Map<ResourceLocation, ResourceLocation> assignments = new HashMap();
            ResourceLocation chestcavitytype = new ResourceLocation(ccaJson.chestcavity);
            int i = 0;
            Iterator<JsonElement> var6 = ccaJson.entities.iterator();

            while(var6.hasNext()) {
                JsonElement entry = (JsonElement)var6.next();
                ++i;

                try {
                    assignments.put(new ResourceLocation(entry.getAsString()), chestcavitytype);
                } catch (Exception var9) {
                    ChestCavity.LOGGER.error("Error parsing entry no. " + i + " in " + id.toString() + "'s entity list");
                }
            }

            return assignments;
        }
    }
}
