package net.tigereye.chestcavity.chestcavities.json;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.tigereye.chestcavity.chestcavities.json.ccAssignment.ChestCavityAssignmentManager;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.chestcavities.json.ccType.ChestCavityTypeManager;
import net.tigereye.chestcavity.forge.port.SimpleSynchronousResourceReloadListener;

public class GeneratedChestCavityManager implements SimpleSynchronousResourceReloadListener {
    public GeneratedChestCavityManager() {
    }

    public ResourceLocation getFabricId() {
        return new ResourceLocation("chestcavity", "inv_data");
    }

    public void onResourceManagerReload(ResourceManager manager) {
        ChestCavityAssignmentManager.reloadChestCavityAssignment(manager);
        InventoryTypeManager.reloadInventoryType(manager);
        ChestCavityTypeManager.reloadChestCavityType(manager);
    }
}