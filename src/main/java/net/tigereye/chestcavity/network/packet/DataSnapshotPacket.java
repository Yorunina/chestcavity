package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.ChestCavityDataManager;
import net.tigereye.chestcavity.chestcavities.json.ChestCavityDataSnapshot;
import net.tigereye.chestcavity.chestcavities.json.DataResourceUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Atomic synchronization packet for all data-driven Chest Cavity definitions.
 */
public final class DataSnapshotPacket {
    private static final int MAX_ENTRIES = 65536;
    private static final int MAX_TOTAL_ENTRIES = MAX_ENTRIES * 4;

    private final long revision;
    private final Map<ResourceLocation, String> organData;
    private final Map<ResourceLocation, String> inventoryTypeData;
    private final Map<ResourceLocation, String> chestCavityTypes;
    private final Map<ResourceLocation, String> chestCavityAssignments;

    public DataSnapshotPacket(ChestCavityDataSnapshot snapshot) {
        this(
                snapshot.getRevision(),
                snapshot.getRawOrganData(),
                snapshot.getRawInventoryTypeData(),
                snapshot.getRawChestCavityTypes(),
                snapshot.getRawChestCavityAssignments()
        );
    }

    public DataSnapshotPacket(
            long revision,
            Map<ResourceLocation, String> organData,
            Map<ResourceLocation, String> inventoryTypeData,
            Map<ResourceLocation, String> chestCavityTypes,
            Map<ResourceLocation, String> chestCavityAssignments
    ) {
        this.revision = revision;
        this.organData = Map.copyOf(organData);
        this.inventoryTypeData = Map.copyOf(inventoryTypeData);
        this.chestCavityTypes = Map.copyOf(chestCavityTypes);
        this.chestCavityAssignments = Map.copyOf(chestCavityAssignments);
    }

    public static DataSnapshotPacket decode(FriendlyByteBuf buf) {
        long revision = buf.readVarLong();
        if (revision < 0) {
            throw new IllegalArgumentException("Invalid Chest Cavity data snapshot revision: " + revision);
        }
        int[] totalEntries = new int[]{0};
        Map<ResourceLocation, String> organData = readDataSet(buf, totalEntries);
        Map<ResourceLocation, String> inventoryTypeData = readDataSet(buf, totalEntries);
        Map<ResourceLocation, String> chestCavityTypes = readDataSet(buf, totalEntries);
        Map<ResourceLocation, String> chestCavityAssignments = readDataSet(buf, totalEntries);
        return new DataSnapshotPacket(
                revision,
                organData,
                inventoryTypeData,
                chestCavityTypes,
                chestCavityAssignments
        );
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarLong(revision);
        writeDataSet(buf, organData);
        writeDataSet(buf, inventoryTypeData);
        writeDataSet(buf, chestCavityTypes);
        writeDataSet(buf, chestCavityAssignments);
    }

    private static Map<ResourceLocation, String> readDataSet(FriendlyByteBuf buf, int[] totalEntries) {
        int count = buf.readVarInt();
        if (count < 0 || count > MAX_ENTRIES) {
            throw new IllegalArgumentException("Invalid Chest Cavity data entry count: " + count);
        }
        totalEntries[0] += count;
        if (totalEntries[0] > MAX_TOTAL_ENTRIES) {
            throw new IllegalArgumentException("Chest Cavity data snapshot contains too many entries");
        }

        Map<ResourceLocation, String> result = new LinkedHashMap<>();
        for (int i = 0; i < count; i++) {
            ResourceLocation id = DataResourceUtil.normalizeId(buf.readResourceLocation());
            String value = buf.readUtf(DataResourceUtil.MAX_JSON_LENGTH);
            if (result.containsKey(id)) {
                throw new IllegalArgumentException("Duplicate Chest Cavity data resource ID: " + id);
            }
            result.put(id, value);
        }
        return result;
    }

    private static void writeDataSet(FriendlyByteBuf buf, Map<ResourceLocation, String> data) {
        if (data.size() > MAX_ENTRIES) {
            throw new IllegalArgumentException("Chest Cavity data entry count exceeds " + MAX_ENTRIES);
        }

        buf.writeVarInt(data.size());
        data.entrySet().stream()
                .sorted(Map.Entry.comparingByKey((left, right) -> left.toString().compareTo(right.toString())))
                .forEach(entry -> {
                    buf.writeResourceLocation(DataResourceUtil.normalizeId(entry.getKey()));
                    buf.writeUtf(entry.getValue(), DataResourceUtil.MAX_JSON_LENGTH);
                });
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                Dist.CLIENT,
                () -> () -> applyClientSnapshot()
        ));
        context.setPacketHandled(true);
        return true;
    }

    private void applyClientSnapshot() {
        try {
            ChestCavityDataSnapshot snapshot = ChestCavityDataManager.buildSnapshot(
                    organData,
                    inventoryTypeData,
                    chestCavityTypes,
                    chestCavityAssignments,
                    revision
            );
            if (snapshot.isUsable()) {
                ChestCavityDataManager.applySnapshot(snapshot);
            } else {
                ChestCavity.LOGGER.warn("Rejected unusable client Chest Cavity data snapshot {}", revision);
            }
        } catch (Exception error) {
            ChestCavity.LOGGER.error("Failed to apply client Chest Cavity data snapshot " + revision, error);
        }
    }
}
