package net.tigereye.chestcavity.chestcavities.json.screen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.tigereye.chestcavity.ChestCavity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenTypeSerializer {
    public ScreenTypeSerializer() {
    }

    public List<SlotPosition> read(ResourceLocation id, ScreenTypeJsonFormat organJson) {
        List<SlotPosition> slotPositions = new ArrayList<>();
        if (organJson.slotPositions == null) {
            throw new JsonSyntaxException("Organ " + id + " must have an slotPositions");
        } else {
            for (JsonElement entry : organJson.slotPositions) {
                JsonObject slotJsonObj = entry.getAsJsonObject();
                JsonElement slotJsonX = slotJsonObj.get("x");
                JsonElement slotJsonY = slotJsonObj.get("y");
                slotPositions.add(new SlotPosition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
            }
            return slotPositions;
        }
    }
}
