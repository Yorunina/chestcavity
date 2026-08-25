package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.json.ChestCavityDataRepository;
import net.tigereye.chestcavity.chestcavities.json.ChestCavityDataSnapshot;
import net.tigereye.chestcavity.network.ChestCavityNetworkCodec;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Transfers the complete server-side Chest Cavity resource snapshot in one
 * atomic packet. The packet intentionally has no data snapshot version:
 * resource data is replaced as a complete bundle whenever it is received.
 */
public final class ChestCavityDataSyncPacket {
    private final Map<ResourceLocation, String> rawOrgans;
    private final Map<ResourceLocation, String> rawInventoryTypes;
    private final Map<ResourceLocation, String> rawChestCavityTypes;
    private final Map<ResourceLocation, String> rawAssignments;

    public ChestCavityDataSyncPacket(ChestCavityDataSnapshot snapshot) {
        this(
                snapshot.getRawOrgans(),
                snapshot.getRawInventoryTypes(),
                snapshot.getRawChestCavityTypes(),
                snapshot.getRawAssignments()
        );
    }

    private ChestCavityDataSyncPacket(
            Map<ResourceLocation, String> rawOrgans,
            Map<ResourceLocation, String> rawInventoryTypes,
            Map<ResourceLocation, String> rawChestCavityTypes,
            Map<ResourceLocation, String> rawAssignments
    ) {
        this.rawOrgans = Map.copyOf(rawOrgans);
        this.rawInventoryTypes = Map.copyOf(rawInventoryTypes);
        this.rawChestCavityTypes = Map.copyOf(rawChestCavityTypes);
        this.rawAssignments = Map.copyOf(rawAssignments);
    }

    public static ChestCavityDataSyncPacket decode(FriendlyByteBuf buffer) {
        return new ChestCavityDataSyncPacket(
                ChestCavityNetworkCodec.readRawData(buffer),
                ChestCavityNetworkCodec.readRawData(buffer),
                ChestCavityNetworkCodec.readRawData(buffer),
                ChestCavityNetworkCodec.readRawData(buffer)
        );
    }

    public void encode(FriendlyByteBuf buffer) {
        ChestCavityNetworkCodec.writeRawData(buffer, this.rawOrgans);
        ChestCavityNetworkCodec.writeRawData(buffer, this.rawInventoryTypes);
        ChestCavityNetworkCodec.writeRawData(buffer, this.rawChestCavityTypes);
        ChestCavityNetworkCodec.writeRawData(buffer, this.rawAssignments);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                Dist.CLIENT,
                () -> () -> ChestCavityDataRepository.install(
                        this.rawOrgans,
                        this.rawInventoryTypes,
                        this.rawChestCavityTypes,
                        this.rawAssignments
                )
        ));
        context.setPacketHandled(true);
        return true;
    }
}
