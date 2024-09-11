package net.tigereye.chestcavity.mixin.forge;

import java.util.ArrayList;
import net.minecraft.network.Connection;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.tigereye.chestcavity.chestcavities.organs.OrganManager;
import net.tigereye.chestcavity.forge.network.ChestCavityNetwork;
import net.tigereye.chestcavity.forge.network.packet.OrganDataPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetworkHooks.class})
public class MixinNetworkHooks {
    public MixinNetworkHooks() {
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"sendMCRegistryPackets"},
            remap = false
    )
    private static void sendServerPackets(Connection manager, String direction, CallbackInfo ci) {
        if (direction.equals("PLAY_TO_CLIENT")) {
            ArrayList<Connection> managers = new ArrayList();
            managers.add(manager);
            int count = OrganManager.GeneratedOrganData.size();
            ChestCavityNetwork.CHANNEL.send(PacketDistributor.NMLIST.with(() -> {
                return managers;
            }), new OrganDataPacket(count, OrganManager.GeneratedOrganData));
        }

    }
}
