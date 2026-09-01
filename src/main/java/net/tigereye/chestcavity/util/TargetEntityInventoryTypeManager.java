package net.tigereye.chestcavity.util;

import net.minecraft.resources.ResourceLocation;

public class TargetEntityInventoryTypeManager {
    private static ResourceLocation targetEntityInventoryType = null;

    public static void setTargetEntityInventoryType(ResourceLocation inventoryType) {
        targetEntityInventoryType = inventoryType;
    }
    public static ResourceLocation getTargetEntityInventoryType() {
        return targetEntityInventoryType;
    }
}
