package net.tigereye.chestcavity.chestcavities.json.organs;

import com.google.gson.Gson;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.ChestCavity;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class OrganManager implements ResourceManagerReloadListener {
    private final OrganSerializer SERIALIZER = new OrganSerializer();
    public static Map<ResourceLocation, OrganData> GeneratedOrganData = new HashMap<>();

    public OrganManager() {
    }

    public void onResourceManagerReload(ResourceManager manager) {
        GeneratedOrganData.clear();
        manager.listResources("organs", (path) -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
            try {
                InputStream stream = resource.open();

                try {
                    Reader reader = new InputStreamReader(stream);
                    Tuple<ResourceLocation, OrganData> organDataPair = this.SERIALIZER.read(id, new Gson().fromJson(reader, OrganJsonFormat.class));
                    GeneratedOrganData.put(organDataPair.getA(), organDataPair.getB());
                } catch (Throwable readError) {
                    try {
                        stream.close();
                    } catch (Throwable closeError) {
                        readError.addSuppressed(closeError);
                    }
                    throw readError;
                }
                stream.close();
            } catch (Exception openError) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), openError);
            }
        });
    }

    public static boolean hasEntry(Item item) {
        return GeneratedOrganData.containsKey(ForgeRegistries.ITEMS.getKey(item));
    }

    public static OrganData getEntry(Item item) {
        return GeneratedOrganData.get(ForgeRegistries.ITEMS.getKey(item));
    }


    public static OrganData readNBTOrganData(ItemStack itemStack) {
        CompoundTag nbt = itemStack.getTagElement("organData");
        return nbt != null ? readNBTOrganData(nbt) : null;
    }

    public static OrganData readNBTOrganData(@NotNull CompoundTag nbt) {
        OrganData organData = new OrganData();
        organData.pseudoOrgan = nbt.getBoolean("pseudoOrgan");

        for (String key : nbt.getAllKeys()) {
            if (!key.equals("pseudoOrgan")) {
                organData.organScores.put(new ResourceLocation(key), nbt.getFloat(key));
            }
        }

        return organData;
    }
}
