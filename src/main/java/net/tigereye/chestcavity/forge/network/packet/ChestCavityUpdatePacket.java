package net.tigereye.chestcavity.forge.network.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.util.NetworkUtil;

public class ChestCavityUpdatePacket {
    private final boolean opened;
    private final int organScoreSize;
    private final Map<ResourceLocation, Float> organScoresMap;

    public ChestCavityUpdatePacket(ChestCavityInstance cc) {
        this(cc.opened, cc.getOrganScores().size(), cc.getOrganScores());
    }

    public ChestCavityUpdatePacket(boolean opened, int organScoreSize, Map<ResourceLocation, Float> organScoresMap) {
        this.opened = opened;
        this.organScoreSize = organScoreSize;
        this.organScoresMap = organScoresMap;
    }

    public static ChestCavityUpdatePacket decode(FriendlyByteBuf buf) {
        Map<ResourceLocation, Float> organScores = new HashMap<>();
        boolean open = buf.readBoolean();
        int entries = buf.readInt();

        for(int i = 0; i < entries; ++i) {
            organScores.put(new ResourceLocation(buf.readUtf()), buf.readFloat());
        }

        return new ChestCavityUpdatePacket(open, entries, organScores);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.opened);
        buf.writeInt(this.organScoreSize);
        this.organScoresMap.forEach((id, value) -> {
            buf.writeUtf(id.toString());
            buf.writeFloat(value);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        AtomicBoolean success = new AtomicBoolean(false);
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
                return () -> {
                    Optional<ChestCavityEntity> optional = ChestCavityEntity.of(Minecraft.getInstance().player);
                    optional.ifPresent((chestCavityEntity) -> {
                        ChestCavityInstance instance = chestCavityEntity.getChestCavityInstance();
                        instance.opened = this.opened;
                        instance.setOrganScores(this.organScoresMap);
                        success.set(true);
                        NetworkUtil.SendC2SChestCavityReceivedUpdatePacket(instance);
                    });
                };
            });
        });
        ctx.get().setPacketHandled(true);
        return success.get();
    }
}
