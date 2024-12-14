package net.tigereye.chestcavity.chestcavities.json.ccInvType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;


public class InventoryTypeSerializer {

    public InventoryTypeSerializer() {
    }

    public InventoryTypeData read(ResourceLocation id, InventoryTypeJsonFormat inventoryTypeJsonFormat) {
        InventoryTypeData result = InventoryTypeManager.getDefaultInventoryTypeData();
        if (inventoryTypeJsonFormat.slotPositions != null) {
            List<SlotPosition> slotPositions = new ArrayList<>();
            for (JsonElement entry : inventoryTypeJsonFormat.slotPositions) {
                JsonObject slotJsonObj = entry.getAsJsonObject();
                JsonElement slotJsonX = slotJsonObj.get("x");
                JsonElement slotJsonY = slotJsonObj.get("y");
                slotPositions.add(new SlotPosition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
            }
            result.setSlotPositions(slotPositions);
        }
        if (inventoryTypeJsonFormat.backgroundTexture != null) {
            result.setBackgroundTexture(new ResourceLocation(inventoryTypeJsonFormat.backgroundTexture));
        }
        if (inventoryTypeJsonFormat.playerInventoryPosition != null) {
            JsonElement slotJsonX = inventoryTypeJsonFormat.playerInventoryPosition.get("x");
            JsonElement slotJsonY = inventoryTypeJsonFormat.playerInventoryPosition.get("y");
            result.setPlayerInventoryPosition(new SlotPosition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
        }
        return result;
    }
}
