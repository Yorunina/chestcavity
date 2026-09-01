package net.tigereye.chestcavity.chestcavities.json.organs;

import com.google.gson.Gson;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.DataResourceUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class OrganManager {
    private static final OrganSerializer SERIALIZER = new OrganSerializer();
    private static final Gson GSON = new Gson();
    public static volatile Map<ResourceLocation, OrganData> OrganData = Map.of();
    public static volatile Map<ResourceLocation, String> RawOrganData = Map.of();

    public OrganManager() {
    }

    public static void reloadOrganData(net.minecraft.server.packs.resources.ResourceManager manager) {
        Map<ResourceLocation, String> rawData = DataResourceUtil.readResources(manager, "organs");
        applySnapshot(rawData, parseDataSnapshot(rawData));
    }

    public static Map<ResourceLocation, String> loadRawData(net.minecraft.server.packs.resources.ResourceManager manager) {
        return DataResourceUtil.readResources(manager, "organs");
    }

    public static Map<ResourceLocation, OrganData> parseDataSnapshot(Map<ResourceLocation, String> rawData) {
        Map<ResourceLocation, OrganData> result = new HashMap<>();
        rawData.forEach((id, data) -> {
            try {
                Tuple<ResourceLocation, OrganData> organDataPair =
                        SERIALIZER.read(id, GSON.fromJson(data, OrganJsonFormat.class));
                result.put(organDataPair.getA(), organDataPair.getB());
            } catch (Exception error) {
                ChestCavity.LOGGER.error("Error parsing organ resource " + id, error);
            }
        });
        return result;
    }

    public static void applySnapshot(
            Map<ResourceLocation, String> rawData,
            Map<ResourceLocation, OrganData> parsedData
    ) {
        RawOrganData = Map.copyOf(rawData);
        OrganData = Map.copyOf(parsedData);
    }

    public static boolean hasEntry(Item item) {
        return OrganData.containsKey(ForgeRegistries.ITEMS.getKey(item));
    }

    public static OrganData getEntry(Item item) {
        return OrganData.get(ForgeRegistries.ITEMS.getKey(item));
    }


    public static OrganData readNBTOrganData(ItemStack itemStack) {
        CompoundTag nbt = itemStack.getTagElement("organData");
        return nbt != null ? readNBTOrganData(nbt) : null;
    }

    public static OrganData readNBTOrganData(@NotNull CompoundTag nbt) {
        OrganData organData = new OrganData();
        for (String key : nbt.getAllKeys()) {
            if (!key.equals("pseudoOrgan")) {
                organData.organScores.put(new ResourceLocation(key), nbt.getFloat(key));
            }
        }
        return organData;
    }

    public static void parseData(Map<ResourceLocation, String> rawData) {
        applySnapshot(rawData, parseDataSnapshot(rawData));
    }
}
