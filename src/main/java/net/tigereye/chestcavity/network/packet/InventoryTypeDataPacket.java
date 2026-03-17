package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class InventoryTypeDataPacket {
    private final Map<ResourceLocation, String> rawData;

    public InventoryTypeDataPacket(Map<ResourceLocation, String> rawData) {
        this.rawData = rawData;
    }

    public static InventoryTypeDataPacket decode(FriendlyByteBuf buf) {
        int inventoryTypeCount = buf.readInt();
        Map<ResourceLocation, String> inventoryTypeMap = new HashMap<>();
        for(int i = 0; i < inventoryTypeCount; i++){
            ResourceLocation key = buf.readResourceLocation();
            String value = buf.readUtf();
            inventoryTypeMap.put(key,value);
        }
        return new InventoryTypeDataPacket(inventoryTypeMap);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(rawData.size());
        rawData.forEach((key, value) -> {
            buf.writeResourceLocation(key);
            buf.writeUtf(value);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        AtomicBoolean success = new AtomicBoolean(false);
        contextSupplier.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                InventoryTypeManager.RawInventoryTypeData = rawData;
                InventoryTypeManager.parseData(rawData);
                success.set(true);
            });
        });
        contextSupplier.get().setPacketHandled(true);
        return success.get();
    }
}