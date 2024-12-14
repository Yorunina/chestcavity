package net.tigereye.chestcavity.chestcavities.json.ccInvType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.DEFAULT_INVENTORY_TYPE_DATA;


public class InventoryTypeSerializer {

    public InventoryTypeSerializer() {
    }

    public InventoryTypeData read(ResourceLocation id, InventoryTypeJsonFormat inventoryTypeJsonFormat) {
        InventoryTypeData result = DEFAULT_INVENTORY_TYPE_DATA;
        if (inventoryTypeJsonFormat.slotPositions != null) {
            for (JsonElement entry : inventoryTypeJsonFormat.slotPositions) {
                JsonObject slotJsonObj = entry.getAsJsonObject();
                JsonElement slotJsonX = slotJsonObj.get("x");
                JsonElement slotJsonY = slotJsonObj.get("y");
                result.slotPositions.add(new SlotPosition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
            }
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
