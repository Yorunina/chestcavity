package net.tigereye.chestcavity.chestcavities.json.ccType;

import com.google.gson.JsonArray;

public class ChestCavityTypeJsonFormat {
    JsonArray defaultChestCavity;
    JsonArray baseOrganScores;
    JsonArray exceptionalOrgans;
    JsonArray forbiddenSlots;
    int inventorySize = 27;
    boolean bossChestCavity = false;
    boolean playerChestCavity = false;
    float dropRateMultiplier = 1.0F;

    public ChestCavityTypeJsonFormat() {
    }
}
