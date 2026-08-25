package net.tigereye.chestcavity.chestcavities.json.organs;

import com.google.gson.Gson;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.ChestCavityDataRepository;
import net.tigereye.chestcavity.util.ResourceDataUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class OrganManager {
    private static final OrganSerializer SERIALIZER = new OrganSerializer();
    private static final Gson GSON = new Gson();
    @Deprecated
    public static Map<ResourceLocation, OrganData> OrganData = Map.of();
    @Deprecated
    public static Map<ResourceLocation, String> RawOrganData = Map.of();

    public OrganManager() {
    }

    public static Map<ResourceLocation, String> loadRawData(ResourceManager manager) {
        Map<ResourceLocation, String> rawData = new HashMap<>();
        manager.listResources("organs", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try {
                rawData.put(id, ResourceDataUtil.readUtf8(resource));
            } catch (Exception openError) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id, openError);
            }
        });
        return rawData;
    }

    public static boolean hasEntry(Item item) {
        return ChestCavityDataRepository.getCurrent().getOrgan(ForgeRegistries.ITEMS.getKey(item)) != null;
    }

    public static OrganData getEntry(Item item) {
        return ChestCavityDataRepository.getCurrent().getOrgan(ForgeRegistries.ITEMS.getKey(item));
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

    public static Map<ResourceLocation, OrganData> parseRawData(Map<ResourceLocation, String> rawData) {
        Map<ResourceLocation, OrganData> parsedData = new HashMap<>();
        rawData.forEach((id, data) -> {
            Tuple<ResourceLocation, OrganData> organDataPair = SERIALIZER.read(id, GSON.fromJson(data, OrganJsonFormat.class));
            parsedData.put(organDataPair.getA(), organDataPair.getB());
        });
        return parsedData;
    }

    public static void publish(Map<ResourceLocation, OrganData> parsedData, Map<ResourceLocation, String> rawData) {
        OrganData = Collections.unmodifiableMap(new HashMap<>(parsedData));
        RawOrganData = Collections.unmodifiableMap(new HashMap<>(rawData));
    }
}
