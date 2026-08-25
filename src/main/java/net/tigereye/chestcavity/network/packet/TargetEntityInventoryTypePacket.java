package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.util.TargetEntityInventoryTypeManager;

import java.util.function.Supplier;

public class TargetEntityInventoryTypePacket {
    private final ResourceLocation inventoryType;

    public TargetEntityInventoryTypePacket(ResourceLocation inventoryType) {
        this.inventoryType = inventoryType;
    }

    public static TargetEntityInventoryTypePacket decode(FriendlyByteBuf buf) {
        ResourceLocation inventoryType = buf.readResourceLocation();
        return new TargetEntityInventoryTypePacket(inventoryType);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.inventoryType);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                TargetEntityInventoryTypeManager.setTargetEntityInventoryType(this.inventoryType);
            });
        });
        context.setPacketHandled(true);
        return true;
    }
}
