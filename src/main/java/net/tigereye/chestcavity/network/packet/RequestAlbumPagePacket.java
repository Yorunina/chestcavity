package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.tigereye.chestcavity.registration.CCItems;
import net.tigereye.chestcavity.ui.ChestCavityAlbumScreenHandler;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;


public class RequestAlbumPagePacket {
    private final int pageIndex;

    public RequestAlbumPagePacket(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(pageIndex);
    }

    public static RequestAlbumPagePacket decode(FriendlyByteBuf buffer) {
        int pageIndex = buffer.readInt();
        return new RequestAlbumPagePacket(pageIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        AtomicBoolean success = new AtomicBoolean(false);
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ItemStack stack = context.getSender().getMainHandItem();
            if (stack.getItem() != CCItems.ORGAN_ALBUM.get()) {
                return;
            }
            NetworkHooks.openScreen(
                    context.getSender(),
                    new SimpleMenuProvider((id, inv, player) -> new ChestCavityAlbumScreenHandler(
                            id,
                            inv,
                            pageIndex),
                            CommonComponents.EMPTY),
                    buffer -> {
                        buffer.writeItem(stack);
                        buffer.writeInt(pageIndex);
                    }
            );
            success.set(true);
        });
        context.setPacketHandled(true);
        return success.get();
    }
}