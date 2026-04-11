package net.tigereye.chestcavity.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.network.packet.*;

public final class ChestCavityNetwork {
    private static int packetId = 0;
    public static SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(ChestCavity.MODID, "messages"))
            .networkProtocolVersion(() -> "1.0")
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
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
        INSTANCE.messageBuilder(OrganDataPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(OrganDataPacket::encode)
                .decoder(OrganDataPacket::decode)
                .consumerMainThread(OrganDataPacket::handle)
                .add();
        INSTANCE.messageBuilder(ChestCavityAssignmentDataPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ChestCavityAssignmentDataPacket::encode)
                .decoder(ChestCavityAssignmentDataPacket::decode)
                .consumerMainThread(ChestCavityAssignmentDataPacket::handle)
                .add();
        INSTANCE.messageBuilder(InventoryTypeDataPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(InventoryTypeDataPacket::encode)
                .decoder(InventoryTypeDataPacket::decode)
                .consumerMainThread(InventoryTypeDataPacket::handle)
                .add();
        INSTANCE.messageBuilder(ChestCavityTypeDataPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ChestCavityTypeDataPacket::encode)
                .decoder(ChestCavityTypeDataPacket::decode)
                .consumerMainThread(ChestCavityTypeDataPacket::handle)
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