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
        result.setId(id);
        if (inventoryTypeJsonFormat.slotDefinitions != null) {
            List<ChestCavitySlotDefinition> slotDefinitions = new ArrayList<>();
            for (JsonElement entry : inventoryTypeJsonFormat.slotDefinitions) {
                JsonObject slotJsonObj = entry.getAsJsonObject();
                JsonElement slotJsonX = slotJsonObj.get("x");
                JsonElement slotJsonY = slotJsonObj.get("y");
                if (slotJsonObj.has("type")) {
                    JsonElement slotJsonType = slotJsonObj.get("type");
                    slotDefinitions.add(new ChestCavitySlotDefinition(slotJsonX.getAsInt(), slotJsonY.getAsInt(), slotJsonType.getAsString()));
                } else {
                    slotDefinitions.add(new ChestCavitySlotDefinition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
                }
            }
            result.setSlotDefinitions(slotDefinitions);
        }
        if (inventoryTypeJsonFormat.backgroundTexture != null) {
            result.setBackgroundTexture(new ResourceLocation(inventoryTypeJsonFormat.backgroundTexture));
        }
        if (inventoryTypeJsonFormat.playerInventoryPosition != null) {
            JsonElement slotJsonX = inventoryTypeJsonFormat.playerInventoryPosition.get("x");
            JsonElement slotJsonY = inventoryTypeJsonFormat.playerInventoryPosition.get("y");
            result.setPlayerInventoryPosition(new SlotDefinition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
        }
        if (inventoryTypeJsonFormat.titlePosition != null) {
            JsonElement slotJsonX = inventoryTypeJsonFormat.titlePosition.get("x");
            JsonElement slotJsonY = inventoryTypeJsonFormat.titlePosition.get("y");
            result.setTitlePosition(new SlotDefinition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
        }
        if (inventoryTypeJsonFormat.inventoryLabelPosition != null) {
            JsonElement slotJsonX = inventoryTypeJsonFormat.inventoryLabelPosition.get("x");
            JsonElement slotJsonY = inventoryTypeJsonFormat.inventoryLabelPosition.get("y");
            result.setInventoryLabelPosition(new SlotDefinition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
        }
        if (inventoryTypeJsonFormat.backgroundSize != null) {
            JsonElement slotJsonX = inventoryTypeJsonFormat.backgroundSize.get("x");
            JsonElement slotJsonY = inventoryTypeJsonFormat.backgroundSize.get("y");
            result.setBackgroundSize(new SlotDefinition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
        }
        return result;
    }
}
