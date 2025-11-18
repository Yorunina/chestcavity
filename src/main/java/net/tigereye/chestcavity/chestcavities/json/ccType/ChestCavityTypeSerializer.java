package net.tigereye.chestcavity.chestcavities.json.ccType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.chestcavities.types.GeneratedChestCavityType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.GeneratedInventoryTypeData;

public class ChestCavityTypeSerializer {
    public ChestCavityTypeSerializer() {
    }

    public GeneratedChestCavityType read(ResourceLocation id, ChestCavityTypeJsonFormat cctJson) {
        if (cctJson.defaultChestCavity == null) {
            throw new JsonSyntaxException("Chest Cavity Types must have a default chest cavity!");
        } else {
            if (cctJson.exceptionalOrgans == null) {
                cctJson.exceptionalOrgans = new JsonArray();
            }

            if (cctJson.baseOrganScores == null) {
                cctJson.baseOrganScores = new JsonArray();
            }

            GeneratedChestCavityType cct = new GeneratedChestCavityType();
            cct.setDefaultChestCavity(this.readDefaultChestCavityFromJson(id, cctJson));
            cct.setBaseOrganScores(this.readBaseOrganScoresFromJson(id, cctJson));
            cct.setExceptionalOrganList(this.readExceptionalOrgansFromJson(id, cctJson));
            cct.setInventoryType(new ResourceLocation(cctJson.inventoryType));
            return cct;
        }
    }

    private ChestCavityInventory readDefaultChestCavityFromJson(ResourceLocation id, ChestCavityTypeJsonFormat cctJson) {
        InventoryTypeData inventoryTypeData = GeneratedInventoryTypeData.getOrDefault(new ResourceLocation(cctJson.inventoryType), InventoryTypeManager.getDefaultInventoryTypeData());
        ChestCavityInventory inv = new ChestCavityInventory(inventoryTypeData.getSlotSize());
        int i = 0;

        for (JsonElement entry : cctJson.defaultChestCavity) {
            ++i;

            JsonObject obj = entry.getAsJsonObject();
            if (!obj.has("item")) {
                ChestCavity.LOGGER.warn("Missing item component in entry no." + i + " in " + id.toString() + "'s default chest cavity");
            } else if (!obj.has("position")) {
                ChestCavity.LOGGER.warn("Missing position component in entry no. " + i + " in " + id.toString() + "'s default chest cavity");
            } else {
                ResourceLocation itemID = new ResourceLocation(obj.get("item").getAsString());
                Optional<Item> itemOptional = Optional.ofNullable(ForgeRegistries.ITEMS.getValue(itemID));
                if (itemOptional.isPresent()) {
                    Item item = itemOptional.get();
                    ItemStack stack = new ItemStack(item, 1);
                    if (obj.has("count")) {
                        stack.setCount(obj.get("count").getAsInt());
                    }
                    int pos = obj.get("position").getAsInt();
                    if (pos >= inv.getContainerSize()) {
                        ChestCavity.LOGGER.warn("Position component is out of bounds in entry no. " + i + " in " + id.toString() + "'s default chest cavity");
                    } else {
                        inv.setItem(pos, stack);
                    }
                }
            }
        }

        return inv;
    }

    private Map<ResourceLocation, Float> readBaseOrganScoresFromJson(ResourceLocation id, ChestCavityTypeJsonFormat cctJson) {
        return this.readOrganScoresFromJson(id, cctJson.baseOrganScores);
    }

    private Map<Ingredient, Map<ResourceLocation, Float>> readExceptionalOrgansFromJson(ResourceLocation id, ChestCavityTypeJsonFormat cctJson) {
        Map<Ingredient, Map<ResourceLocation, Float>> exceptionalOrgans = new HashMap<>();
        int i = 0;

        for (JsonElement entry : cctJson.exceptionalOrgans) {
            ++i;
            try {
                JsonObject obj = entry.getAsJsonObject();
                if (!obj.has("ingredient")) {
                    ChestCavity.LOGGER.error("Missing ingredient component in entry no." + i + " in " + id.toString() + "'s exceptional organs");
                } else if (!obj.has("value")) {
                    ChestCavity.LOGGER.error("Missing value component in entry no. " + i + " in " + id.toString() + "'s exceptional organs");
                } else {
                    Ingredient ingredient = Ingredient.fromJson(obj.get("ingredient"));
                    exceptionalOrgans.put(ingredient, this.readOrganScoresFromJson(id, obj.get("value").getAsJsonArray()));
                }
            } catch (Exception var9) {
                ChestCavity.LOGGER.error("Error parsing entry no. " + i + " in " + id.toString() + "'s exceptional organs");
            }
        }

        return exceptionalOrgans;
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
