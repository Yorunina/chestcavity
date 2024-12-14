package net.tigereye.chestcavity.chestcavities.json.ccType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.util.*;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.types.GeneratedChestCavityType;

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

            if (cctJson.forbiddenSlots == null) {
                cctJson.forbiddenSlots = new JsonArray();
            }

            GeneratedChestCavityType cct = new GeneratedChestCavityType();
            cct.setForbiddenSlots(this.readForbiddenSlotsFromJson(id, cctJson));
            cct.setDefaultChestCavity(this.readDefaultChestCavityFromJson(id, cctJson, cct.getForbiddenSlots()));
            cct.setBaseOrganScores(this.readBaseOrganScoresFromJson(id, cctJson));
            cct.setExceptionalOrganList(this.readExceptionalOrgansFromJson(id, cctJson));
            cct.setDropRateMultiplier(cctJson.dropRateMultiplier);
            cct.setPlayerChestCavity(cctJson.playerChestCavity);
            cct.setBossChestCavity(cctJson.bossChestCavity);
            return cct;
        }
    }

    private ChestCavityInventory readDefaultChestCavityFromJson(ResourceLocation id, ChestCavityTypeJsonFormat cctJson, List<Integer> forbiddenSlots) {
        ChestCavityInventory inv = new ChestCavityInventory(27);
        int i = 0;

        for (JsonElement entry : cctJson.defaultChestCavity) {
            ++i;
            try {
                JsonObject obj = entry.getAsJsonObject();
                if (!obj.has("item")) {
                    ChestCavity.LOGGER.error("Missing item component in entry no." + i + " in " + id.toString() + "'s default chest cavity");
                } else if (!obj.has("position")) {
                    ChestCavity.LOGGER.error("Missing position component in entry no. " + i + " in " + id.toString() + "'s default chest cavity");
                } else {
                    ResourceLocation itemID = new ResourceLocation(obj.get("item").getAsString());
                    Optional<Item> itemOptional = Optional.ofNullable(ForgeRegistries.ITEMS.getValue(new ResourceLocation(obj.get("item").getAsString())));
                    if (itemOptional.isPresent()) {
                        Item item = itemOptional.get();
                        ItemStack stack;
                        int pos;
                        if (obj.has("count")) {
                            pos = obj.get("count").getAsInt();
                            stack = new ItemStack(item, pos);
                        } else {
                            stack = new ItemStack(item, item.getMaxStackSize());
                        }

                        pos = obj.get("position").getAsInt();
                        if (pos >= inv.getContainerSize()) {
                            ChestCavity.LOGGER.error("Position component is out of bounds in entry no. " + i + " in " + id.toString() + "'s default chest cavity");
                        } else if (forbiddenSlots.contains(pos)) {
                            ChestCavity.LOGGER.error("Position component is forbidden in entry no. " + i + " in " + id.toString() + "'s default chest cavity");
                        } else {
                            inv.setItem(pos, stack);
                        }
                    } else {
                        ChestCavity.LOGGER.error("Unknown " + itemID + " in entry no. " + i + " in " + id.toString() + "'s default chest cavity");
                    }
                }
            } catch (Exception var14) {
                ChestCavity.LOGGER.error("Error parsing entry no. " + i + " in " + id.toString() + "'s default chest cavity");
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

    private List<Integer> readForbiddenSlotsFromJson(ResourceLocation id, ChestCavityTypeJsonFormat cctJson) {
        ArrayList<Integer> list = new ArrayList();

        for (JsonElement entry : cctJson.forbiddenSlots) {
            try {
                int slot = entry.getAsInt();
                list.add(slot);
            } catch (Exception var7) {
                ChestCavity.LOGGER.error("Error parsing " + id.toString() + "'s organ scores!");
            }
        }

        return list;
    }
}
