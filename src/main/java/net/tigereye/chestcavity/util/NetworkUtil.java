package net.tigereye.chestcavity.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.network.ChestCavityNetwork;
import net.tigereye.chestcavity.network.packet.ChestCavityUpdatePacket;
import net.tigereye.chestcavity.network.packet.ReceivedChestCavityUpdatePacket;

public class NetworkUtil {
    public static boolean SendS2CChestCavityUpdatePacket(ChestCavityInstance cc) {
        cc.markSyncPending();
        return SendS2CChestCavityUpdatePacket(cc, true);
    }

    public static boolean SendS2CChestCavityUpdatePacket(ChestCavityInstance cc, boolean buf) {
        if (!cc.owner.level().isClientSide()) {
            LivingEntity owner = cc.owner;
            if (owner instanceof ServerPlayer spe) {
                if (spe.connection != null) {
                    ChestCavityNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> spe), new ChestCavityUpdatePacket(cc));
                }
                return true;
            }
        }

        return false;
    }

    public static void ReadChestCavityReceivedUpdatePacket(ChestCavityInstance cc) {
        cc.acknowledgeSync();
    }

    public static boolean SendC2SChestCavityReceivedUpdatePacket(ChestCavityInstance cc) {
        ChestCavityNetwork.INSTANCE.sendToServer(new ReceivedChestCavityUpdatePacket());
        return SendS2CChestCavityUpdatePacket(cc, cc.isSyncPending());
    }

}
