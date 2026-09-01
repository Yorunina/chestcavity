package net.tigereye.chestcavity.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.network.ChestCavityNetwork;
import net.tigereye.chestcavity.network.packet.ChestCavityUpdatePacket;
import net.tigereye.chestcavity.network.packet.ReceivedChestCavityUpdatePacket;

public class NetworkUtil {
    private static final long STATE_PACKET_RESEND_INTERVAL_TICKS = 20L;
    private static final int MAX_STATE_PACKET_RETRIES = 3;

    public static boolean SendS2CChestCavityUpdatePacket(ChestCavityInstance cc) {
        cc.updatePacket = true;
        return SendS2CChestCavityUpdatePacket(cc, true);
    }

    /**
     * Sends the newest state when no packet is in flight, or after the resend
     * cooldown expires. The wire format remains the original opened + scores
     * payload; all coalescing state is server-local.
     */
    public static boolean SendS2CChestCavityUpdatePacket(ChestCavityInstance cc, boolean immediate) {
        if (cc == null || cc.owner == null || cc.owner.level().isClientSide()) {
            return false;
        }

        LivingEntity owner = cc.owner;
        if (!(owner instanceof ServerPlayer serverPlayer) || serverPlayer.connection == null) {
            return false;
        }

        long gameTime = owner.level().getGameTime();
        boolean resendDue = cc.updatePacketInFlight
                && gameTime - cc.updatePacketLastSentTick >= STATE_PACKET_RESEND_INTERVAL_TICKS;
        if (cc.updatePacketInFlight && !resendDue) {
            return false;
        }
        if (cc.updatePacketInFlight
                && resendDue
                && cc.updatePacketRetryCount >= MAX_STATE_PACKET_RETRIES) {
            if (cc.stateMatchesLastPacket()) {
                cc.updatePacketInFlight = false;
                cc.updatePacket = false;
                return false;
            }
            cc.updatePacketInFlight = false;
        }
        if (!cc.updatePacket && !cc.updatePacketInFlight) {
            return false;
        }
        if (!immediate && cc.updatePacketLastSentTick != Long.MIN_VALUE
                && gameTime - cc.updatePacketLastSentTick < 1L) {
            return false;
        }

        ChestCavityNetwork.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> serverPlayer),
                new ChestCavityUpdatePacket(cc)
        );
        cc.markStatePacketSent(gameTime);
        return true;
    }

    public static void ReadChestCavityReceivedUpdatePacket(ChestCavityInstance cc) {
        if (cc == null) {
            return;
        }
        cc.updatePacketInFlight = false;
        if (!cc.stateMatchesLastPacket()) {
            cc.updatePacket = true;
        }
    }

    public static boolean SendC2SChestCavityReceivedUpdatePacket(ChestCavityInstance cc) {
        if (cc == null) {
            return false;
        }
        ChestCavityNetwork.INSTANCE.sendToServer(new ReceivedChestCavityUpdatePacket());
        return true;
    }

}
