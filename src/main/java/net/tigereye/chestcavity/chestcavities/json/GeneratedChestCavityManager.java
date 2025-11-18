package net.tigereye.chestcavity.chestcavities.json;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentManager;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.chestcavities.json.ccType.ChestCavityTypeManager;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;

@Mod.EventBusSubscriber(modid = ChestCavity.MODID)
public class GeneratedChestCavityManager implements ResourceManagerReloadListener {
    
    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new GeneratedChestCavityManager());
    }
    
    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        OrganManager.reloadOrganData(manager);
        ChestCavityAssignmentManager.reloadChestCavityAssignment(manager);
        InventoryTypeManager.reloadInventoryType(manager);
        ChestCavityTypeManager.reloadChestCavityType(manager);
    }
}