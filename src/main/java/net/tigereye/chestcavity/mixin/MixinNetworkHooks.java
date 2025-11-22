package net.tigereye.chestcavity.mixin;

import net.minecraft.network.Connection;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentManager;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;
import net.tigereye.chestcavity.network.ChestCavityNetwork;
import net.tigereye.chestcavity.network.packet.ChestCavityAssignmentDataPacket;
import net.tigereye.chestcavity.network.packet.InventoryTypeDataPacket;
import net.tigereye.chestcavity.network.packet.OrganDataPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

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
            ArrayList<Connection> managers = new ArrayList<>();
            managers.add(manager);
            // 发送器官数据
            int organCount = OrganManager.GeneratedOrganData.size();
            ChestCavityNetwork.INSTANCE.send(PacketDistributor.NMLIST.with(() -> managers), new OrganDataPacket(organCount, OrganManager.GeneratedOrganData));
            // 发送胸腔分配数据
            int assignmentCount = ChestCavityAssignmentManager.GeneratedChestCavityAssignments.size();
            ChestCavityNetwork.INSTANCE.send(PacketDistributor.NMLIST.with(() -> managers), new ChestCavityAssignmentDataPacket(assignmentCount, ChestCavityAssignmentManager.GeneratedChestCavityAssignments));
            // 发送库存类型数据
            int inventoryTypeCount = InventoryTypeManager.GeneratedInventoryTypeData.size();
            ChestCavityNetwork.INSTANCE.send(PacketDistributor.NMLIST.with(() -> managers), new InventoryTypeDataPacket(inventoryTypeCount, InventoryTypeManager.GeneratedInventoryTypeData));
        }
    }
}