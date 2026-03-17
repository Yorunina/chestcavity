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
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OrganManager {
    private static final OrganSerializer SERIALIZER = new OrganSerializer();
    public static Map<ResourceLocation, OrganData> OrganData = new HashMap<>();
    public static Map<ResourceLocation, String> RawOrganData = new HashMap<>();

    public OrganManager() {
    }

    public static void reloadOrganData(ResourceManager manager) {
        OrganData.clear();
        manager.listResources("organs", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try {
                InputStream stream = resource.open();
                String result = new BufferedReader(new InputStreamReader(stream)).lines().collect(Collectors.joining(System.lineSeparator()));
                RawOrganData.put(id, result);
                stream.close();
            } catch (Exception openError) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), openError);
            }
        });
        parseData(RawOrganData);
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
        OrganData.clear();
        rawData.forEach((id, data) -> {
            Tuple<ResourceLocation, OrganData> organDataPair = SERIALIZER.read(id, new Gson().fromJson(data, OrganJsonFormat.class));
            OrganData.put(organDataPair.getA(), organDataPair.getB());
        });
    }
}
