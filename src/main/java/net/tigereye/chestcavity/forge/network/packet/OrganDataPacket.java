package net.tigereye.chestcavity.forge.network.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.organs.OrganData;
import net.tigereye.chestcavity.chestcavities.organs.OrganManager;

public class OrganDataPacket {
    private final int organDataSize;
    private final Map<ResourceLocation, OrganData> organData;

    public OrganDataPacket(int organDataSize, Map<ResourceLocation, OrganData> organData) {
        this.organDataSize = organDataSize;
        this.organData = organData;
    }

    public static OrganDataPacket decode(FriendlyByteBuf buf) {
        int organCount = buf.readInt();
        Map<ResourceLocation, OrganData> organMap = new HashMap<>();

        for(int i = 0; i < organCount; ++i) {
            ResourceLocation organID = buf.readResourceLocation();
            OrganData organData = new OrganData();
            organData.pseudoOrgan = buf.readBoolean();
            int organAbilityCount = buf.readInt();

            for(int j = 0; j < organAbilityCount; ++j) {
                organData.organScores.put(buf.readResourceLocation(), buf.readFloat());
            }

            organMap.put(organID, organData);
        }

        return new OrganDataPacket(organCount, organMap);
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
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
                return () -> {
                    OrganManager.GeneratedOrganData.clear();
                    OrganManager.GeneratedOrganData.putAll(this.organData);
                    ChestCavity.LOGGER.info("loaded " + this.organDataSize + " organs from server");
                    success.set(true);
                };
            });
        });
        contextSupplier.get().setPacketHandled(true);
        return success.get();
    }
}
