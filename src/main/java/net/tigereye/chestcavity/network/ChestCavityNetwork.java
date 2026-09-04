package net.tigereye.chestcavity.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.network.packet.*;

public final class ChestCavityNetwork {
    public static final String NETWORK_PROTOCOL_VERSION = "3";
    private static int packetId = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ChestCavity.MODID, "messages"))
            .networkProtocolVersion(() -> NETWORK_PROTOCOL_VERSION)
            .clientAcceptedVersions(NETWORK_PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(NETWORK_PROTOCOL_VERSION::equals)
            .simpleChannel();
    private static int id() {
        return packetId++;
    }
    public static void register() {

        INSTANCE.messageBuilder(DataSnapshotPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(DataSnapshotPacket::encode)
                .decoder(DataSnapshotPacket::decode)
                .consumerMainThread(DataSnapshotPacket::handle)
                .add();
        INSTANCE.messageBuilder(TargetEntityInventoryTypePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(TargetEntityInventoryTypePacket::encode)
                .decoder(TargetEntityInventoryTypePacket::decode)
                .consumerMainThread(TargetEntityInventoryTypePacket::handle)
                .add();
    }
}
