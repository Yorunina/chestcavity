package net.tigereye.chestcavity.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.network.packet.ChestCavityUpdatePacket;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Batches entity state synchronization at the end of a server tick.
 *
 * <p>State mutation only enqueues an instance. The queue is drained once per
 * server tick, so inventory edits and score recalculations cannot emit a
 * packet for every intermediate change.</p>
 */
@Mod.EventBusSubscriber(modid = ChestCavity.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ChestCavitySyncService {
    private static final Set<ChestCavityInstance> PENDING =
            Collections.newSetFromMap(new IdentityHashMap<>());

    private ChestCavitySyncService() {
    }

    public static void enqueue(ChestCavityInstance instance) {
        if (instance != null) {
            PENDING.add(instance);
        }
    }

    public static void sendNow(ChestCavityInstance instance) {
        if (instance == null || instance.owner == null || instance.owner.level().isClientSide()) {
            return;
        }
        PENDING.remove(instance);
        instance.markSyncPending();
        send(instance);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || PENDING.isEmpty()) {
            return;
        }

        ChestCavityInstance[] pending = PENDING.toArray(new ChestCavityInstance[0]);
        for (ChestCavityInstance instance : pending) {
            PENDING.remove(instance);
        }
        for (ChestCavityInstance instance : pending) {
            if (instance != null && instance.owner != null && !instance.owner.level().isClientSide()) {
                instance.markSyncPending();
                send(instance);
            }
        }
    }

    private static void send(ChestCavityInstance instance) {
        if (instance.owner instanceof ServerPlayer player && player.connection != null) {
            ChestCavityNetwork.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new ChestCavityUpdatePacket(instance)
            );
        }
    }
}
