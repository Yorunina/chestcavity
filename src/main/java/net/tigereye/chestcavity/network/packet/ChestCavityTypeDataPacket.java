package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.json.ccType.ChestCavityTypeManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ChestCavityTypeDataPacket {
    private final int typeDataSize;
    private final Map<ResourceLocation, ChestCavityType> typeData;

    public ChestCavityTypeDataPacket(Map<ResourceLocation, ChestCavityType> typeData) {
        this.typeDataSize = typeData.size();
        this.typeData = typeData;
    }

    public static ChestCavityTypeDataPacket decode(FriendlyByteBuf buf) {
        int assignmentCount = buf.readInt();
        Map<ResourceLocation, ChestCavityType> typeData = new HashMap<>();

        for (int i = 0; i < assignmentCount; ++i) {
            ResourceLocation entityID = buf.readResourceLocation();

        }

        return new ChestCavityTypeDataPacket(typeData);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.typeDataSize);
        this.typeData.forEach((typeId, chestCavityType) -> {
            buf.writeResourceLocation(typeId);
            buf.writeResourceLocation(chestCavityType);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        AtomicBoolean success = new AtomicBoolean(false);
        contextSupplier.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ChestCavityTypeManager.ChestCavityTypes.clear();
                ChestCavityTypeManager.ChestCavityTypes.putAll(this.typeData);
                success.set(true);
            });
        });
        contextSupplier.get().setPacketHandled(true);
        return success.get();
    }
}