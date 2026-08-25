package net.tigereye.chestcavity.chestcavities.json;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.Environment;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.network.ChestCavityNetwork;
import net.tigereye.chestcavity.network.ChestCavitySyncService;
import net.tigereye.chestcavity.network.packet.ChestCavityAssignmentDataPacket;
import net.tigereye.chestcavity.network.packet.ChestCavityTypeDataPacket;
import net.tigereye.chestcavity.network.packet.InventoryTypeDataPacket;
import net.tigereye.chestcavity.network.packet.OrganDataPacket;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber(modid = ChestCavity.MODID)
public class ChestCavityDataManager implements PreparableReloadListener {
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new ChestCavityDataManager());
    }

    @SubscribeEvent
    public static void playerConnected(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        sendDataTo(player);
        if (player instanceof ChestCavityEntity chestCavityEntity) {
            ChestCavitySyncService.sendNow(chestCavityEntity.getChestCavityInstance());
        }
    }

    private static void sendDataTo(ServerPlayer player) {
        ChestCavityDataSnapshot snapshot = ChestCavityDataRepository.getCurrent();
        ChestCavityNetwork.INSTANCE.sendTo(new OrganDataPacket(snapshot.getVersion(), snapshot.getRawOrgans()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        ChestCavityNetwork.INSTANCE.sendTo(new InventoryTypeDataPacket(snapshot.getVersion(), snapshot.getRawInventoryTypes()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        ChestCavityNetwork.INSTANCE.sendTo(new ChestCavityTypeDataPacket(snapshot.getVersion(), snapshot.getRawChestCavityTypes()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        ChestCavityNetwork.INSTANCE.sendTo(new ChestCavityAssignmentDataPacket(snapshot.getVersion(), snapshot.getRawAssignments()), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    private static void broadcastData() {
        ChestCavityDataSnapshot snapshot = ChestCavityDataRepository.getCurrent();
        ChestCavityNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new OrganDataPacket(snapshot.getVersion(), snapshot.getRawOrgans()));
        ChestCavityNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new InventoryTypeDataPacket(snapshot.getVersion(), snapshot.getRawInventoryTypes()));
        ChestCavityNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new ChestCavityTypeDataPacket(snapshot.getVersion(), snapshot.getRawChestCavityTypes()));
        ChestCavityNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new ChestCavityAssignmentDataPacket(snapshot.getVersion(), snapshot.getRawAssignments()));
    }
    
    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager manager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            ChestCavityDataRepository.reload(manager);
            return null;
        }, backgroundExecutor).thenCompose(preparationBarrier::wait).thenAcceptAsync(ignored -> {
            if (Environment.get().getDist().isDedicatedServer() && ServerLifecycleHooks.getCurrentServer() != null) {
                broadcastData();
            }
        }, executor);
    }
}
