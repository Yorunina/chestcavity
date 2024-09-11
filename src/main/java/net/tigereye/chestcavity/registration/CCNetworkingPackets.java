package net.tigereye.chestcavity.registration;

import net.minecraft.resources.ResourceLocation;

public class CCNetworkingPackets {
    public static final ResourceLocation ORGAN_DATA_PACKET_ID = new ResourceLocation("chestcavity", "organ_data");
    public static final ResourceLocation UPDATE_PACKET_ID = new ResourceLocation("chestcavity", "update");
    public static final ResourceLocation RECEIVED_UPDATE_PACKET_ID = new ResourceLocation("chestcavity", "received_update");
    public static final ResourceLocation HOTKEY_PACKET_ID = new ResourceLocation("chestcavity", "hotkey");

    public CCNetworkingPackets() {
    }

    public static void register() {
    }

    public static void registerClient() {
    }
}
