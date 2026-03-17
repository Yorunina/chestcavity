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
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentManager;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.chestcavities.json.ccType.ChestCavityTypeManager;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;
import net.tigereye.chestcavity.network.ChestCavityNetwork;
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
        ServerPlayer player = (ServerPlayer) event.getEntity();
        ChestCavityNetwork.INSTANCE.sendTo(new OrganDataPacket(OrganManager.RawOrganData), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        ChestCavityNetwork.INSTANCE.sendTo(new InventoryTypeDataPacket(InventoryTypeManager.RawInventoryTypeData), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        ChestCavityNetwork.INSTANCE.sendTo(new ChestCavityTypeDataPacket(ChestCavityTypeManager.RawChestCavityTypes), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        ChestCavityNetwork.INSTANCE.sendTo(new ChestCavityAssignmentDataPacket(ChestCavityAssignmentManager.RawChestCavityAssignments), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    
    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager manager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backGrounExecutor, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            OrganManager.reloadOrganData(manager);
            ChestCavityAssignmentManager.reloadChestCavityAssignment(manager);
            InventoryTypeManager.reloadInventoryType(manager);
            ChestCavityTypeManager.reloadChestCavityType(manager);
            return null;
        }, backGrounExecutor).thenCompose(preparationBarrier::wait).thenAcceptAsync((pObj) -> {
            if (Environment.get().getDist().isDedicatedServer() && ServerLifecycleHooks.getCurrentServer() != null) {
                ChestCavityNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new OrganDataPacket(OrganManager.RawOrganData));
                ChestCavityNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new InventoryTypeDataPacket(InventoryTypeManager.RawInventoryTypeData));
                ChestCavityNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new ChestCavityTypeDataPacket(ChestCavityTypeManager.RawChestCavityTypes));
                ChestCavityNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new ChestCavityAssignmentDataPacket(ChestCavityAssignmentManager.RawChestCavityAssignments));
            }
        }, executor);
    }
}