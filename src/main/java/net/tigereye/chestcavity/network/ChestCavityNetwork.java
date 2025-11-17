package net.tigereye.chestcavity.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.network.packet.ChestCavityHotkeyPacket;
import net.tigereye.chestcavity.network.packet.ChestCavityUpdatePacket;
import net.tigereye.chestcavity.network.packet.OrganDataPacket;
import net.tigereye.chestcavity.network.packet.ReceivedChestCavityUpdatePacket;

public final class ChestCavityNetwork {
    private static int packetId = 0;
    public static SimpleChannel INSTANCE;
    private static int id() {
        return packetId++;
    }
    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ChestCavity.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        INSTANCE = net;

        net.messageBuilder(ChestCavityUpdatePacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ChestCavityUpdatePacket::encode)
                .decoder(ChestCavityUpdatePacket::decode)
                .consumerMainThread(ChestCavityUpdatePacket::handle)
                .add();
        net.messageBuilder(OrganDataPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(OrganDataPacket::encode)
                .decoder(OrganDataPacket::decode)
                .consumerMainThread(OrganDataPacket::handle)
                .add();
        net.messageBuilder(ChestCavityHotkeyPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(ChestCavityHotkeyPacket::encode)
                .decoder(ChestCavityHotkeyPacket::new)
                .consumerMainThread(ChestCavityHotkeyPacket::handle)
                .add();
        net.messageBuilder(ReceivedChestCavityUpdatePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(ReceivedChestCavityUpdatePacket::encode)
                .decoder(ReceivedChestCavityUpdatePacket::new)
                .consumerMainThread(ReceivedChestCavityUpdatePacket::handle)
                .add();
    }
}
