package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ChestCavityAssignmentDataPacket {
    private final int assignmentDataSize;
    private final Map<ResourceLocation, ResourceLocation> assignmentData;

    public ChestCavityAssignmentDataPacket(int assignmentDataSize, Map<ResourceLocation, ResourceLocation> assignmentData) {
        this.assignmentDataSize = assignmentDataSize;
        this.assignmentData = assignmentData;
    }

    public static ChestCavityAssignmentDataPacket decode(FriendlyByteBuf buf) {
        int assignmentCount = buf.readInt();
        Map<ResourceLocation, ResourceLocation> assignmentMap = new HashMap<>();

        for (int i = 0; i < assignmentCount; ++i) {
            ResourceLocation entityID = buf.readResourceLocation();
            ResourceLocation chestCavityTypeID = buf.readResourceLocation();
            assignmentMap.put(entityID, chestCavityTypeID);
        }

        return new ChestCavityAssignmentDataPacket(assignmentCount, assignmentMap);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.assignmentDataSize);
        this.assignmentData.forEach((entityID, chestCavityTypeID) -> {
            buf.writeResourceLocation(entityID);
            buf.writeResourceLocation(chestCavityTypeID);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        AtomicBoolean success = new AtomicBoolean(false);
        contextSupplier.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ChestCavityAssignmentManager.GeneratedChestCavityAssignments.clear();
                ChestCavityAssignmentManager.GeneratedChestCavityAssignments.putAll(this.assignmentData);
                success.set(true);
            });
        });
        contextSupplier.get().setPacketHandled(true);
        return success.get();
    }
}