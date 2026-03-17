package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.json.ccType.ChestCavityTypeManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ChestCavityTypeDataPacket {
    private final Map<ResourceLocation, String> rawData;

    public ChestCavityTypeDataPacket(Map<ResourceLocation, String> rawData) {
        this.rawData = rawData;
    }

    public static ChestCavityTypeDataPacket decode(FriendlyByteBuf buf) {
        int chestCavityTypeCount = buf.readInt();
        Map<ResourceLocation, String> chestCavityTypeMap = new HashMap<>();
        for(int i = 0; i < chestCavityTypeCount; i++){
            ResourceLocation key = buf.readResourceLocation();
            String value = buf.readUtf();
            chestCavityTypeMap.put(key,value);
        }
        return new ChestCavityTypeDataPacket(chestCavityTypeMap);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.rawData.size());
        this.rawData.forEach((entityID, data) -> {
            buf.writeResourceLocation(entityID);
            buf.writeUtf(data);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        AtomicBoolean success = new AtomicBoolean(false);
        contextSupplier.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ChestCavityTypeManager.RawChestCavityTypes = this.rawData;
                ChestCavityTypeManager.parseData(this.rawData);
                success.set(true);
            });
        });
        contextSupplier.get().setPacketHandled(true);
        return success.get();
    }
}