package net.tigereye.chestcavity.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.forge.network.ChestCavityNetwork;
import net.tigereye.chestcavity.forge.network.packet.ChestCavityHotkeyPacket;
import net.tigereye.chestcavity.forge.network.packet.ChestCavityUpdatePacket;
import net.tigereye.chestcavity.forge.network.packet.ReceivedChestCavityUpdatePacket;

public class NetworkUtil {
    public NetworkUtil() {
    }

    public static boolean SendS2CChestCavityUpdatePacket(ChestCavityInstance cc) {
        cc.updatePacket = true;
        return SendS2CChestCavityUpdatePacket(cc, true);
    }

    public static boolean SendS2CChestCavityUpdatePacket(ChestCavityInstance cc, boolean buf) {
        if (!cc.owner.level().isClientSide()) {
            LivingEntity var3 = cc.owner;
            if (var3 instanceof ServerPlayer spe) {
                ChestCavityNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> {
                    return spe;
                }), new ChestCavityUpdatePacket(cc));
                return true;

            }
        }

        return false;
    }

    public static void ReadChestCavityReceivedUpdatePacket(ChestCavityInstance cc) {
        cc.updatePacket = false;
    }

    public static boolean SendC2SChestCavityReceivedUpdatePacket(ChestCavityInstance cc) {
        ChestCavityNetwork.CHANNEL.sendToServer(new ReceivedChestCavityUpdatePacket());
        return SendS2CChestCavityUpdatePacket(cc, cc.updatePacket);
    }

    public static void SendC2SChestCavityHotkeyPacket(ResourceLocation organScore) {
        ChestCavityNetwork.CHANNEL.sendToServer(new ChestCavityHotkeyPacket(organScore));
    }
}
