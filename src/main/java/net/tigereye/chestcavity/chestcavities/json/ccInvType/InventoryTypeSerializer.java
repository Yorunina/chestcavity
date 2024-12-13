package net.tigereye.chestcavity.chestcavities.json.ccInvType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.util.ArrayList;
import java.util.List;

public class InventoryTypeSerializer {
    public InventoryTypeSerializer() {
    }

    public InventoryTypeData read(ResourceLocation id, InventoryTypeJsonFormat inventoryTypeJsonFormat) {
        List<SlotPosition> slotPositions = new ArrayList<>();
        ResourceLocation backgroundTexture = new ResourceLocation("chestcavity:default_cc_background");
        if (inventoryTypeJsonFormat.slotPositions == null) {
            throw new JsonSyntaxException("Organ " + id + " must have an slotPositions");
        } else {
            for (JsonElement entry : inventoryTypeJsonFormat.slotPositions) {
                JsonObject slotJsonObj = entry.getAsJsonObject();
                JsonElement slotJsonX = slotJsonObj.get("x");
                JsonElement slotJsonY = slotJsonObj.get("y");
                slotPositions.add(new SlotPosition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
            }
        }
        if (inventoryTypeJsonFormat.backgroundTexture != null) {
            backgroundTexture = new ResourceLocation(inventoryTypeJsonFormat.backgroundTexture);
        }
        return new InventoryTypeData(backgroundTexture, slotPositions);
    }
}
