package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class OrganDataPacket {
    private final Map<ResourceLocation, String> rawData;

    public OrganDataPacket(Map<ResourceLocation, String> rawData) {
        this.rawData = rawData;
    }

    public static OrganDataPacket decode(FriendlyByteBuf buf) {
        int organCount = buf.readInt();
        Map<ResourceLocation, String> organMap = new HashMap<>();
        for(int i = 0; i < organCount; i++){
            ResourceLocation key = buf.readResourceLocation();
            String value = buf.readUtf();
            organMap.put(key,value);
        }
        return new OrganDataPacket(organMap);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.rawData.size());
        this.rawData.forEach((id, data) -> {
            buf.writeResourceLocation(id);
            buf.writeUtf(data);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        AtomicBoolean success = new AtomicBoolean(false);
        contextSupplier.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                OrganManager.RawOrganData = this.rawData;
                OrganManager.parseData(this.rawData);
                success.set(true);
            });
        });
        contextSupplier.get().setPacketHandled(true);
        return success.get();
    }
}