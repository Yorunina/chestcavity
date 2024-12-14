package net.tigereye.chestcavity.chestcavities.json.ccType;

import com.google.gson.JsonArray;

import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.DEFAULT_INVENTORY_TYPE_STRING;

public class ChestCavityTypeJsonFormat {
    JsonArray defaultChestCavity;
    JsonArray baseOrganScores;
    JsonArray exceptionalOrgans;
    JsonArray forbiddenSlots;
    String inventoryType = DEFAULT_INVENTORY_TYPE_STRING;
    boolean bossChestCavity = false;
    boolean playerChestCavity = false;
    float dropRateMultiplier = 1.0F;

    public ChestCavityTypeJsonFormat() {
    }
}
