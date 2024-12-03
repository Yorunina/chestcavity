package net.tigereye.chestcavity.chestcavities.organs;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.forge.port.SimpleSynchronousResourceReloadListener;
import org.jetbrains.annotations.NotNull;

public class OrganManager implements SimpleSynchronousResourceReloadListener {
    private static final String RESOURCE_LOCATION = "organs";
    private static final String NBT_KEY = "organData";
    private final OrganSerializer SERIALIZER = new OrganSerializer();
    public static Map<ResourceLocation, OrganData> GeneratedOrganData = new HashMap();

    public OrganManager() {
    }

    public ResourceLocation getFabricId() {
        return new ResourceLocation("chestcavity", "organs");
    }

    public void onResourceManagerReload(ResourceManager manager) {
        GeneratedOrganData.clear();
        ChestCavity.LOGGER.info("Loading organs.");
        manager.listResources("organs", (path) -> {
            return path.getPath().endsWith(".json");
        }).forEach((id, resource) -> {
            try {
                InputStream stream = resource.open();

                try {
                    Reader reader = new InputStreamReader(stream);
                    Tuple<ResourceLocation, OrganData> organDataPair = this.SERIALIZER.read(id, (OrganJsonFormat)(new Gson()).fromJson(reader, OrganJsonFormat.class));
                    GeneratedOrganData.put((ResourceLocation)organDataPair.getA(), (OrganData)organDataPair.getB());
                } catch (Throwable var7) {
                    try {
                        stream.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }

                    throw var7;
                }

                stream.close();
            } catch (Exception var8) {
                ChestCavity.LOGGER.error("Error occurred while loading resource json " + id.toString(), var8);
            }

        });
        ChestCavity.LOGGER.info("Loaded " + GeneratedOrganData.size() + " organs.");
    }

    public static boolean hasEntry(Item item) {
        return GeneratedOrganData.containsKey(ForgeRegistries.ITEMS.getKey(item));
    }

    public static OrganData getEntry(Item item) {
        return (OrganData)GeneratedOrganData.get(ForgeRegistries.ITEMS.getKey(item));
    }

    public static boolean isTrueOrgan(Item item) {
        if (hasEntry(item)) {
            return !getEntry(item).pseudoOrgan;
        } else {
            return false;
        }
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
