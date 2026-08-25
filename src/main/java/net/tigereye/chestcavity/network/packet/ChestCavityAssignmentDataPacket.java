package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ChestCavityAssignmentDataPacket {
    private final Map<ResourceLocation, String> rawData;

    public ChestCavityAssignmentDataPacket(Map<ResourceLocation, String> rawData) {
        this.rawData = rawData;
    }

    public static ChestCavityAssignmentDataPacket decode(FriendlyByteBuf buf) {
        int assignmentCount = buf.readInt();
        Map<ResourceLocation, String> assignmentMap = new HashMap<>();
        for(int i = 0; i < assignmentCount; i++){
            ResourceLocation key = buf.readResourceLocation();
            String value = buf.readUtf();
            assignmentMap.put(key,value);
        }
        return new ChestCavityAssignmentDataPacket(assignmentMap);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.rawData.size());
        this.rawData.forEach((entityID, chestCavityTypeID) -> {
            buf.writeResourceLocation(entityID);
            buf.writeUtf(chestCavityTypeID);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ChestCavityAssignmentManager.RawChestCavityAssignments = this.rawData;
                ChestCavityAssignmentManager.parseData(this.rawData);
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}
