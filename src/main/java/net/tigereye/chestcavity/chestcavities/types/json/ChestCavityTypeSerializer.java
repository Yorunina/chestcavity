package net.tigereye.chestcavity.chestcavities.types.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        ChestCavityInventory inv = new ChestCavityInventory();
        int i = 0;
        Iterator<JsonElement> var6 = cctJson.defaultChestCavity.iterator();

        while(var6.hasNext()) {
            JsonElement entry = (JsonElement)var6.next();
            ++i;

            try {
                JsonObject obj = entry.getAsJsonObject();
                if (!obj.has("item")) {
                    ChestCavity.LOGGER.error("Missing item component in entry no." + i + " in " + id.toString() + "'s default chest cavity");
                } else if (!obj.has("position")) {
                    ChestCavity.LOGGER.error("Missing position component in entry no. " + i + " in " + id.toString() + "'s default chest cavity");
                } else {
                    ResourceLocation itemID = new ResourceLocation(obj.get("item").getAsString());
                    Optional<Item> itemOptional = Optional.ofNullable((Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation(obj.get("item").getAsString())));
                    if (itemOptional.isPresent()) {
                        Item item = (Item)itemOptional.get();
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
                        ChestCavity.LOGGER.error("Unknown " + itemID.toString() + " in entry no. " + i + " in " + id.toString() + "'s default chest cavity");
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
        Map<Ingredient, Map<ResourceLocation, Float>> exceptionalOrgans = new HashMap();
        int i = 0;
        Iterator<JsonElement> var5 = cctJson.exceptionalOrgans.iterator();

        while(var5.hasNext()) {
            JsonElement entry = (JsonElement)var5.next();
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
        Map<ResourceLocation, Float> organScores = new HashMap();
        Iterator<JsonElement> var4 = json.iterator();

        while(var4.hasNext()) {
            JsonElement entry = (JsonElement)var4.next();

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
        Iterator<JsonElement> var4 = cctJson.forbiddenSlots.iterator();

        while(var4.hasNext()) {
            JsonElement entry = (JsonElement)var4.next();

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
