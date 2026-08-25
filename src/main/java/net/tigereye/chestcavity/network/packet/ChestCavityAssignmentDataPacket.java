package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.json.ChestCavityDataRepository;
import net.tigereye.chestcavity.network.ChestCavityNetworkCodec;

import java.util.Map;
import java.util.function.Supplier;

public class ChestCavityAssignmentDataPacket {
    private final long snapshotVersion;
    private final Map<ResourceLocation, String> rawData;

    public ChestCavityAssignmentDataPacket(long snapshotVersion, Map<ResourceLocation, String> rawData) {
        this.snapshotVersion = snapshotVersion;
        this.rawData = Map.copyOf(rawData);
    }

    public static ChestCavityAssignmentDataPacket decode(FriendlyByteBuf buf) {
        long snapshotVersion = buf.readLong();
        Map<ResourceLocation, String> assignmentMap = ChestCavityNetworkCodec.readRawData(buf);
        return new ChestCavityAssignmentDataPacket(snapshotVersion, assignmentMap);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeLong(this.snapshotVersion);
        ChestCavityNetworkCodec.writeRawData(buf, this.rawData);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ChestCavityDataRepository.installAssignmentData(this.snapshotVersion, this.rawData);
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}
