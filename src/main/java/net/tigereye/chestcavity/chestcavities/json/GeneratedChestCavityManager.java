package net.tigereye.chestcavity.chestcavities.json;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentManager;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.chestcavities.json.ccType.ChestCavityTypeManager;

public class GeneratedChestCavityManager implements ResourceManagerReloadListener {
    public GeneratedChestCavityManager() {
    }

    public void onResourceManagerReload(ResourceManager manager) {
        ChestCavityAssignmentManager.reloadChestCavityAssignment(manager);
        InventoryTypeManager.reloadInventoryType(manager);
        ChestCavityTypeManager.reloadChestCavityType(manager);
    }
}