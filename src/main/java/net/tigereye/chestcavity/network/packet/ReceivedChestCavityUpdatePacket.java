package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.util.NetworkUtil;

import java.util.Optional;
import java.util.function.Supplier;

public class ReceivedChestCavityUpdatePacket {
    public ReceivedChestCavityUpdatePacket() {
    }

    public ReceivedChestCavityUpdatePacket(FriendlyByteBuf buf) {
        this();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Optional<ChestCavityEntity> optional = ChestCavityEntity.of(context.getSender());
            optional.ifPresent((chestCavityEntity) -> {
                NetworkUtil.ReadChestCavityReceivedUpdatePacket(chestCavityEntity.getChestCavityInstance());
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}
