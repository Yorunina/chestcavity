package net.tigereye.chestcavity.forge.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tigereye.chestcavity.forge.network.packet.ChestCavityHotkeyPacket;
import net.tigereye.chestcavity.forge.network.packet.ChestCavityUpdatePacket;
import net.tigereye.chestcavity.forge.network.packet.OrganDataPacket;
import net.tigereye.chestcavity.forge.network.packet.ReceivedChestCavityUpdatePacket;

public final class ChestCavityNetwork {
    private static final String VERSION = "2.16.2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("chestcavity", "main"), () -> {
        return "2.16.2";
    }, "2.16.2"::equals, "2.16.2"::equals);

    public ChestCavityNetwork() {
    }

    public static void init() {
        int index = 0;
        CHANNEL.messageBuilder(ChestCavityUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT).encoder(ChestCavityUpdatePacket::encode).decoder(ChestCavityUpdatePacket::decode).consumerMainThread(ChestCavityUpdatePacket::handle).add();
        CHANNEL.messageBuilder(OrganDataPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT).encoder(OrganDataPacket::encode).decoder(OrganDataPacket::decode).consumerMainThread(OrganDataPacket::handle).add();
        CHANNEL.messageBuilder(ChestCavityHotkeyPacket.class, index++, NetworkDirection.PLAY_TO_SERVER).encoder(ChestCavityHotkeyPacket::encode).decoder(ChestCavityHotkeyPacket::new).consumerMainThread(ChestCavityHotkeyPacket::handle).add();
        CHANNEL.messageBuilder(ReceivedChestCavityUpdatePacket.class, index++, NetworkDirection.PLAY_TO_SERVER).encoder(ReceivedChestCavityUpdatePacket::encode).decoder(ReceivedChestCavityUpdatePacket::new).consumerMainThread(ReceivedChestCavityUpdatePacket::handle).add();
    }
}
