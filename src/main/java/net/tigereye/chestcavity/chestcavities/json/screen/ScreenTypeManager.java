package net.tigereye.chestcavity.chestcavities.json.screen;

import com.google.gson.Gson;
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenTypeManager implements SimpleSynchronousResourceReloadListener {
    private final ScreenTypeSerializer SERIALIZER = new ScreenTypeSerializer();
    public static Map<ResourceLocation, List<SlotPosition>> GeneratedScreenTypeData = new HashMap<>();

    public ScreenTypeManager() {
    }

    public ResourceLocation getFabricId() {
        return new ResourceLocation("chestcavity", "organs");
    }

    public void onResourceManagerReload(ResourceManager manager) {
        GeneratedScreenTypeData.clear();
        ChestCavity.LOGGER.info("Loading screenType.");
        manager.listResources("organs", (path) -> {
            return path.getPath().endsWith(".json");
        }).forEach((id, resource) -> {
            try {
                InputStream stream = resource.open();
                try {
                    Reader reader = new InputStreamReader(stream);
                    List<SlotPosition> slotPositions = this.SERIALIZER.read(id, (ScreenTypeJsonFormat)(new Gson()).fromJson(reader, ScreenTypeJsonFormat.class));
                    GeneratedScreenTypeData.put(id, slotPositions);
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
        ChestCavity.LOGGER.info("Loaded " + GeneratedScreenTypeData.size() + " organs.");
    }
}
