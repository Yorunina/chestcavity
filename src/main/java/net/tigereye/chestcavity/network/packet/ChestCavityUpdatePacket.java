package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.client.ClientChestCavityState;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.DataResourceUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ChestCavityUpdatePacket {
    private static final int MAX_ORGAN_SCORES = 65536;
    private final boolean opened;
    private final int organScoreSize;
    private final Map<ResourceLocation, Float> organScoresMap;

    public ChestCavityUpdatePacket(ChestCavityInstance cc) {
        this(cc.opened, cc.getOrganScores());
    }

    public ChestCavityUpdatePacket(boolean opened, Map<ResourceLocation, Float> organScoresMap) {
        this.opened = opened;
        this.organScoreSize = organScoresMap.size();
        this.organScoresMap = Map.copyOf(organScoresMap);
    }

    public static ChestCavityUpdatePacket decode(FriendlyByteBuf buf) {
        Map<ResourceLocation, Float> organScores = new HashMap<>();
        boolean open = buf.readBoolean();
        int entries = buf.readInt();
        if (entries < 0 || entries > MAX_ORGAN_SCORES) {
            throw new IllegalArgumentException("Invalid organ score entry count: " + entries);
        }

        for (int i = 0; i < entries; ++i) {
            organScores.put(new ResourceLocation(buf.readUtf(DataResourceUtil.MAX_JSON_LENGTH)), buf.readFloat());
        }

        return new ChestCavityUpdatePacket(open, organScores);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.opened);
        buf.writeInt(this.organScoreSize);
        this.organScoresMap.forEach((id, value) -> {
            buf.writeUtf(id.toString(), DataResourceUtil.MAX_JSON_LENGTH);
            buf.writeFloat(value);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                Dist.CLIENT,
                () -> () -> ClientChestCavityState.apply(opened, organScoresMap)
        ));
        context.setPacketHandled(true);
        return true;
    }

}
