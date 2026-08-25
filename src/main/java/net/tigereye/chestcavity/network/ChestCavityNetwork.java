package net.tigereye.chestcavity.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.network.packet.*;

public final class ChestCavityNetwork {
    private static final String PROTOCOL_VERSION = "2";
    private static int packetId = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ChestCavity.MODID, "messages"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();
    private static int id() {
        return packetId++;
    }
    public static void register() {

        INSTANCE.messageBuilder(ChestCavityUpdatePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ChestCavityUpdatePacket::encode)
                .decoder(ChestCavityUpdatePacket::decode)
                .consumerMainThread(ChestCavityUpdatePacket::handle)
                .add();
        INSTANCE.messageBuilder(ChestCavityDataSyncPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ChestCavityDataSyncPacket::encode)
                .decoder(ChestCavityDataSyncPacket::decode)
                .consumerMainThread(ChestCavityDataSyncPacket::handle)
                .add();
        INSTANCE.messageBuilder(ReceivedChestCavityUpdatePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(ReceivedChestCavityUpdatePacket::encode)
                .decoder(ReceivedChestCavityUpdatePacket::new)
                .consumerMainThread(ReceivedChestCavityUpdatePacket::handle)
                .add();
        INSTANCE.messageBuilder(TargetEntityInventoryTypePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(TargetEntityInventoryTypePacket::encode)
                .decoder(TargetEntityInventoryTypePacket::decode)
                .consumerMainThread(TargetEntityInventoryTypePacket::handle)
                .add();
    }
}
