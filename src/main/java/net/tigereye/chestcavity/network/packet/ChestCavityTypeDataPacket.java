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

public class ChestCavityTypeDataPacket {
    private final long snapshotVersion;
    private final Map<ResourceLocation, String> rawData;

    public ChestCavityTypeDataPacket(Map<ResourceLocation, String> rawData) {
        this(-1L, rawData);
    }

    public ChestCavityTypeDataPacket(long snapshotVersion, Map<ResourceLocation, String> rawData) {
        this.snapshotVersion = snapshotVersion;
        this.rawData = rawData;
    }

    public static ChestCavityTypeDataPacket decode(FriendlyByteBuf buf) {
        long snapshotVersion = buf.readLong();
        int chestCavityTypeCount = buf.readInt();
        Map<ResourceLocation, String> chestCavityTypeMap = new HashMap<>();
        for(int i = 0; i < chestCavityTypeCount; i++){
            ResourceLocation key = buf.readResourceLocation();
            String value = buf.readUtf();
            chestCavityTypeMap.put(key,value);
        }
        return new ChestCavityTypeDataPacket(snapshotVersion, chestCavityTypeMap);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeLong(this.snapshotVersion);
        buf.writeInt(this.rawData.size());
        this.rawData.forEach((entityID, data) -> {
            buf.writeResourceLocation(entityID);
            buf.writeUtf(data);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ChestCavityDataRepository.installChestCavityTypeData(this.snapshotVersion, this.rawData);
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}
