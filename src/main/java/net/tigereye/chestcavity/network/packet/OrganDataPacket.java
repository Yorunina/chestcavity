package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class OrganDataPacket {
    private final int organDataSize;
    private final Map<ResourceLocation, OrganData> organData;

    public OrganDataPacket(Map<ResourceLocation, OrganData> organData) {
        this.organDataSize = organData.size();
        this.organData = organData;
    }

    public static OrganDataPacket decode(FriendlyByteBuf buf) {
        int organCount = buf.readInt();
        Map<ResourceLocation, OrganData> organMap = new HashMap<>();

        for (int i = 0; i < organCount; ++i) {
            ResourceLocation organID = buf.readResourceLocation();
            OrganData organData = new OrganData();
            organData.pseudoOrgan = buf.readBoolean();
            int organAbilityCount = buf.readInt();

            for (int j = 0; j < organAbilityCount; ++j) {
                organData.organScores.put(buf.readResourceLocation(), buf.readFloat());
            }

            organMap.put(organID, organData);
        }

        return new OrganDataPacket(organMap);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.organDataSize);
        this.organData.forEach((id, data) -> {
            buf.writeResourceLocation(id);
            buf.writeBoolean(data.pseudoOrgan);
            buf.writeInt(data.organScores.size());
            data.organScores.forEach((ability, score) -> {
                buf.writeResourceLocation(ability);
                buf.writeFloat(score);
            });
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        AtomicBoolean success = new AtomicBoolean(false);
        contextSupplier.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                OrganManager.OrganData.clear();
                OrganManager.OrganData.putAll(this.organData);
                success.set(true);
            });
        });
        contextSupplier.get().setPacketHandled(true);
        return success.get();
    }
}