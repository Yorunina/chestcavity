package net.tigereye.chestcavity.util;

import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.network.ChestCavityNetwork;
import net.tigereye.chestcavity.network.packet.ReceivedChestCavityUpdatePacket;

public class NetworkUtil {
    public static void ReadChestCavityReceivedUpdatePacket(ChestCavityInstance cc) {
        cc.acknowledgeSync();
    }

    public static boolean SendC2SChestCavityReceivedUpdatePacket(ChestCavityInstance cc) {
        ChestCavityNetwork.INSTANCE.sendToServer(new ReceivedChestCavityUpdatePacket());
        return cc != null;
    }

}
