package net.tigereye.chestcavity.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.network.packet.OverlayScorePacket;
import net.tigereye.chestcavity.ui.ChestCavityScreenHandler;

import java.util.Map;
import java.util.WeakHashMap;

public final class OverlayScoreSync {
    private static final WeakHashMap<ChestCavityInstance, Map<ResourceLocation, Float>> LAST_SENT = new WeakHashMap<>();

    private OverlayScoreSync() {
    }

    public static void sendTo(ServerPlayer player, ChestCavityInstance cc) {
        if (player == null || cc == null) {
            return;
        }
        Map<ResourceLocation, Float> snapshot = snapshot(cc);
        LAST_SENT.put(cc, snapshot);
        ChestCavityNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new OverlayScorePacket(snapshot));
    }

    public static void notifyIfViewing(ChestCavityInstance cc) {
        if (cc == null || cc.owner == null || cc.owner.level() == null || cc.owner.level().isClientSide()) {
            return;
        }
        MinecraftServer server = cc.owner.getServer();
        if (server == null) {
            return;
        }
        Map<ResourceLocation, Float> snapshot = snapshot(cc);
        Map<ResourceLocation, Float> previous = LAST_SENT.get(cc);
        if (snapshot.equals(previous)) {
            return;
        }
        boolean sent = false;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.containerMenu instanceof ChestCavityScreenHandler handler
                    && handler.getChestCavityInventory() == cc.inventory) {
                ChestCavityNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new OverlayScorePacket(snapshot));
                sent = true;
            }
        }
        if (sent) {
            LAST_SENT.put(cc, snapshot);
        }
    }

    private static Map<ResourceLocation, Float> snapshot(ChestCavityInstance cc) {
        return Map.copyOf(cc.getOrganScoreState().getScores());
    }
}
