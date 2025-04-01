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
            int index = 0;
            for (JsonElement entry : inventoryTypeJsonFormat.slotDefinitions) {
                JsonObject slotJsonObj = entry.getAsJsonObject();
                JsonElement slotJsonX = slotJsonObj.get("x");
                JsonElement slotJsonY = slotJsonObj.get("y");
                ChestCavitySlotDefinition slotDefinition = new ChestCavitySlotDefinition(index, slotJsonX.getAsInt(), slotJsonY.getAsInt());
                if (slotJsonObj.has("type")) {
                    JsonElement slotJsonType = slotJsonObj.get("type");
                    slotDefinition.setType(slotJsonType.getAsString());
                }
                slotDefinitions.add(slotDefinition);
                index++;
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
            if (inventoryTypeJsonFormat.titlePosition.has("hide")) {
                JsonElement slotJsonHide = inventoryTypeJsonFormat.titlePosition.get("hide");
                result.setTitlePosition(new TitleSlotDefinition(slotJsonX.getAsInt(), slotJsonY.getAsInt(), slotJsonHide.getAsBoolean()));
            } else {
                result.setTitlePosition(new TitleSlotDefinition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
            }
        }
        if (inventoryTypeJsonFormat.inventoryLabelPosition != null) {
            JsonElement slotJsonX = inventoryTypeJsonFormat.inventoryLabelPosition.get("x");
            JsonElement slotJsonY = inventoryTypeJsonFormat.inventoryLabelPosition.get("y");
            if (inventoryTypeJsonFormat.inventoryLabelPosition.has("hide")) {
                JsonElement slotJsonHide = inventoryTypeJsonFormat.inventoryLabelPosition.get("hide");
                result.setInventoryLabelPosition(new TitleSlotDefinition(slotJsonX.getAsInt(), slotJsonY.getAsInt(), slotJsonHide.getAsBoolean()));
            } else {
                result.setInventoryLabelPosition(new TitleSlotDefinition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
            }
        }
        if (inventoryTypeJsonFormat.backgroundSize != null) {
            JsonElement slotJsonX = inventoryTypeJsonFormat.backgroundSize.get("x");
            JsonElement slotJsonY = inventoryTypeJsonFormat.backgroundSize.get("y");
            result.setBackgroundSize(new SlotDefinition(slotJsonX.getAsInt(), slotJsonY.getAsInt()));
        }
        return result;
    }
}
