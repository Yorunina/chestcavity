package net.tigereye.chestcavity.forge.network.packet;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.listeners.OrganActivationListeners;

public class ChestCavityHotkeyPacket {
    ResourceLocation location;

    public ChestCavityHotkeyPacket(ResourceLocation location) {
        this.location = location;
    }

    public ChestCavityHotkeyPacket(FriendlyByteBuf buf) {
        this(buf.readResourceLocation());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.location);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        AtomicBoolean success = new AtomicBoolean(false);
        contextSupplier.get().enqueueWork(() -> {
            Player player = contextSupplier.get().getSender();
            Optional<ChestCavityEntity> optional = ChestCavityEntity.of(player);
            optional.ifPresent((chestCavityEntity) -> {
                OrganActivationListeners.activate(this.location, chestCavityEntity.getChestCavityInstance());
                success.set(true);
            });
        });
        contextSupplier.get().setPacketHandled(true);
        return success.get();
    }
}
