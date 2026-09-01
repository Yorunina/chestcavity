package net.tigereye.chestcavity.chestcavities.json;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentManager;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.chestcavities.json.ccType.ChestCavityTypeManager;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.network.ChestCavityNetwork;
import net.tigereye.chestcavity.network.packet.DataSnapshotPacket;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber(modid = ChestCavity.MODID)
public class ChestCavityDataManager implements PreparableReloadListener {
    private static final Object SNAPSHOT_LOCK = new Object();
    private static final AtomicLong NEXT_REVISION = new AtomicLong();
    private static volatile ChestCavityDataSnapshot currentSnapshot;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new ChestCavityDataManager());
    }

    @SubscribeEvent
    public static void playerConnected(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        ChestCavityDataSnapshot snapshot = currentSnapshot;
        if (snapshot != null && snapshot.isUsable()) {
            ChestCavityNetwork.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new DataSnapshotPacket(snapshot)
            );
        } else {
            ChestCavity.LOGGER.warn("Skipping data snapshot for {} because no usable snapshot is available", player.getGameProfile().getName());
        }
    }

    @Override
    public CompletableFuture<Void> reload(
            PreparationBarrier preparationBarrier,
            ResourceManager manager,
            ProfilerFiller preparationsProfiler,
            ProfilerFiller reloadProfiler,
            Executor backgroundExecutor,
            Executor executor
    ) {
        return CompletableFuture.supplyAsync(() -> {
            long revision = NEXT_REVISION.incrementAndGet();
            return buildSnapshot(manager, revision);
        }, backgroundExecutor).thenCompose(preparationBarrier::wait).thenAcceptAsync(snapshot -> {
            if (!snapshot.isUsable()) {
                ChestCavity.LOGGER.error("Chest Cavity data reload produced no usable default snapshot; keeping previous data");
                return;
            }

            applySnapshot(snapshot);
            if (ServerLifecycleHooks.getCurrentServer() != null) {
                ChestCavityNetwork.INSTANCE.send(
                        PacketDistributor.ALL.noArg(),
                        new DataSnapshotPacket(snapshot)
                );
            }
        }, executor);
    }

    public static ChestCavityDataSnapshot buildSnapshot(ResourceManager manager, long revision) {
        return buildSnapshot(
                OrganManager.loadRawData(manager),
                InventoryTypeManager.loadRawData(manager),
                ChestCavityTypeManager.loadRawData(manager),
                ChestCavityAssignmentManager.loadRawData(manager),
                revision
        );
    }

    public static ChestCavityDataSnapshot buildSnapshot(
            Map<ResourceLocation, String> rawOrganData,
            Map<ResourceLocation, String> rawInventoryTypeData,
            Map<ResourceLocation, String> rawChestCavityTypes,
            Map<ResourceLocation, String> rawChestCavityAssignments,
            long revision
    ) {
        Map<ResourceLocation, InventoryTypeData> inventoryTypeData =
                InventoryTypeManager.parseDataSnapshot(rawInventoryTypeData);
        Map<ResourceLocation, ChestCavityType> chestCavityTypes =
                ChestCavityTypeManager.parseDataSnapshot(rawChestCavityTypes, inventoryTypeData);
        Map<ResourceLocation, OrganData> organData =
                OrganManager.parseDataSnapshot(rawOrganData);
        Map<ResourceLocation, ResourceLocation> chestCavityAssignments =
                ChestCavityAssignmentManager.parseDataSnapshot(rawChestCavityAssignments);

        boolean usable = inventoryTypeData.containsKey(InventoryTypeManager.DEFAULT_INVENTORY_TYPE)
                && chestCavityTypes.containsKey(
                net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstanceFactory.DEFAULT_CHEST_CAVITY_TYPE
        );
        if (!usable) {
            ChestCavity.LOGGER.error(
                    "Chest Cavity data snapshot is missing required defaults (inventoryType={}, chestCavityType={})",
                    inventoryTypeData.containsKey(InventoryTypeManager.DEFAULT_INVENTORY_TYPE),
                    chestCavityTypes.containsKey(
                            net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstanceFactory.DEFAULT_CHEST_CAVITY_TYPE
                    )
            );
        }

        return new ChestCavityDataSnapshot(
                revision,
                rawOrganData,
                rawInventoryTypeData,
                rawChestCavityTypes,
                rawChestCavityAssignments,
                organData,
                inventoryTypeData,
                chestCavityTypes,
                chestCavityAssignments,
                usable
        );
    }

    public static void applySnapshot(ChestCavityDataSnapshot snapshot) {
        if (!snapshot.isUsable()) {
            ChestCavity.LOGGER.warn("Rejected unusable Chest Cavity data snapshot {}", snapshot.getRevision());
            return;
        }
        synchronized (SNAPSHOT_LOCK) {
            OrganManager.applySnapshot(snapshot.getRawOrganData(), snapshot.getOrganData());
            InventoryTypeManager.applySnapshot(snapshot.getRawInventoryTypeData(), snapshot.getInventoryTypeData());
            ChestCavityTypeManager.applySnapshot(snapshot.getRawChestCavityTypes(), snapshot.getChestCavityTypes());
            ChestCavityAssignmentManager.applySnapshot(
                    snapshot.getRawChestCavityAssignments(),
                    snapshot.getChestCavityAssignments()
            );
            currentSnapshot = snapshot;
        }
    }

}
