package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavitySnapshot;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.network.ChestCavityNetworkCodec;
import net.tigereye.chestcavity.util.NetworkUtil;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ChestCavityUpdatePacket {
    private final boolean opened;
    private final Map<ResourceLocation, Float> organScoresMap;

    public ChestCavityUpdatePacket(ChestCavityInstance cc) {
        this(cc.createSnapshot());
    }

    private ChestCavityUpdatePacket(ChestCavitySnapshot snapshot) {
        this(snapshot.isOpened(), snapshot.getOrganScores());
    }

    public ChestCavityUpdatePacket(boolean opened, Map<ResourceLocation, Float> organScoresMap) {
        this.opened = opened;
        this.organScoresMap = Map.copyOf(organScoresMap);
    }

    public static ChestCavityUpdatePacket decode(FriendlyByteBuf buf) {
        boolean open = buf.readBoolean();
        return new ChestCavityUpdatePacket(open, ChestCavityNetworkCodec.readOrganScores(buf));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.opened);
        ChestCavityNetworkCodec.writeOrganScores(buf, this.organScoresMap);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                Optional<ChestCavityEntity> optional = ChestCavityEntity.of();
                optional.ifPresent((chestCavityEntity) -> {
                    ChestCavityInstance instance = chestCavityEntity.getChestCavityInstance();
                    instance.applyRemoteState(this.opened, this.organScoresMap);
                    NetworkUtil.SendC2SChestCavityReceivedUpdatePacket(instance);
                });
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}
