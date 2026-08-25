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
import net.tigereye.chestcavity.util.ResourceDataUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class OrganManager {
    private static final OrganSerializer SERIALIZER = new OrganSerializer();
    private static final Gson GSON = new Gson();
    public static Map<ResourceLocation, OrganData> OrganData = new HashMap<>();
    public static Map<ResourceLocation, String> RawOrganData = new HashMap<>();

    public OrganManager() {
    }

    public static void reloadOrganData(ResourceManager manager) {
        RawOrganData.clear();
        manager.listResources("organs", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try {
                RawOrganData.put(id, ResourceDataUtil.readUtf8(resource));
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
            Tuple<ResourceLocation, OrganData> organDataPair = SERIALIZER.read(id, GSON.fromJson(data, OrganJsonFormat.class));
            OrganData.put(organDataPair.getA(), organDataPair.getB());
        });
    }
}
