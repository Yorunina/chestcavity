package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.json.ChestCavityDataRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class InventoryTypeDataPacket {
    private final long snapshotVersion;
    private final Map<ResourceLocation, String> rawData;

    public InventoryTypeDataPacket(Map<ResourceLocation, String> rawData) {
        this(-1L, rawData);
    }

    public InventoryTypeDataPacket(long snapshotVersion, Map<ResourceLocation, String> rawData) {
        this.snapshotVersion = snapshotVersion;
        this.rawData = rawData;
    }

    public static InventoryTypeDataPacket decode(FriendlyByteBuf buf) {
        long snapshotVersion = buf.readLong();
        int inventoryTypeCount = buf.readInt();
        Map<ResourceLocation, String> inventoryTypeMap = new HashMap<>();
        for(int i = 0; i < inventoryTypeCount; i++){
            ResourceLocation key = buf.readResourceLocation();
            String value = buf.readUtf();
            inventoryTypeMap.put(key,value);
        }
        return new InventoryTypeDataPacket(snapshotVersion, inventoryTypeMap);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeLong(this.snapshotVersion);
        buf.writeInt(rawData.size());
        rawData.forEach((key, value) -> {
            buf.writeResourceLocation(key);
            buf.writeUtf(value);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ChestCavityDataRepository.installInventoryTypeData(this.snapshotVersion, rawData);
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}
