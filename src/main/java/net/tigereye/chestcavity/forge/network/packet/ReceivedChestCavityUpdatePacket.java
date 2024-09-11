package net.tigereye.chestcavity.forge.network.packet;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.util.NetworkUtil;

public class ReceivedChestCavityUpdatePacket {
    public ReceivedChestCavityUpdatePacket() {
    }

    public ReceivedChestCavityUpdatePacket(FriendlyByteBuf buf) {
        this();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        AtomicBoolean success = new AtomicBoolean(false);
        ((NetworkEvent.Context)contextSupplier.get()).enqueueWork(() -> {
            Optional<ChestCavityEntity> optional = ChestCavityEntity.of(((NetworkEvent.Context)contextSupplier.get()).getSender());
            optional.ifPresent((chestCavityEntity) -> {
                NetworkUtil.ReadChestCavityReceivedUpdatePacket(chestCavityEntity.getChestCavityInstance());
                success.set(true);
            });
        });
        ((NetworkEvent.Context)contextSupplier.get()).setPacketHandled(true);
        return success.get();
    }
}
