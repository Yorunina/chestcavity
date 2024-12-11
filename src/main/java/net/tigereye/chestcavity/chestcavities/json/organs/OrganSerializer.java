package net.tigereye.chestcavity.chestcavities.json.organs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.tigereye.chestcavity.ChestCavity;

public class OrganSerializer {
    public OrganSerializer() {
    }

    public Tuple<ResourceLocation, OrganData> read(ResourceLocation id, OrganJsonFormat organJson) {
        if (organJson.itemID == null) {
            throw new JsonSyntaxException("Organ " + id + " must have an item ID");
        } else if (organJson.organScores == null) {
            throw new JsonSyntaxException("Organ " + id + " must have organScores");
        } else {
            OrganData organData = new OrganData();
            ResourceLocation itemID = new ResourceLocation(organJson.itemID);
            organData.pseudoOrgan = organJson.pseudoOrgan;
            organData.organScores = this.readOrganScoresFromJson(id, organJson.organScores);
            return new Tuple(itemID, organData);
        }
    }

    private Map<ResourceLocation, Float> readOrganScoresFromJson(ResourceLocation id, JsonArray json) {
        Map<ResourceLocation, Float> organScores = new HashMap<>();

        for (JsonElement entry : json) {
            try {
                JsonObject obj = entry.getAsJsonObject();
                if (!obj.has("id")) {
                    ChestCavity.LOGGER.error("Missing id component in " + id.toString() + "'s organ scores");
                } else if (!obj.has("value")) {
                    ChestCavity.LOGGER.error("Missing value component in " + id.toString() + "'s organ scores");
                } else {
                    ResourceLocation ability = new ResourceLocation(obj.get("id").getAsString());
                    organScores.put(ability, obj.get("value").getAsFloat());
                }
            } catch (Exception var8) {
                ChestCavity.LOGGER.error("Error parsing " + id.toString() + "'s organ scores!");
            }
        }

        return organScores;
    }
}
