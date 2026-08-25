package net.tigereye.chestcavity.service;

import net.minecraft.nbt.ListTag;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;

/**
 * Coordinates chest-cavity opening and inventory mutation workflows.
 */
public final class ChestCavitySurgeryService {
    private ChestCavitySurgeryService() {
    }

    public static void generateChestCavityIfOpened(ChestCavityInstance cc) {
        ListTag tagList = cc.getChestCavityType().getDefaultChestCavity().createTag();
        ChestCavityInventory newInventory = new ChestCavityInventory(cc);
        newInventory.fromTag(tagList);
        cc.replaceInventory(newInventory, cc.getInventoryType(), false);
        cc.getChestCavityType().setOrganCompatibility(cc);
        cc.setOpened(true);
        ChestCavityEvaluationService.evaluate(cc);
    }

    public static ChestCavityInventory openChestCavity(ChestCavityInstance cc) {
        if (!cc.opened) {
            generateChestCavityIfOpened(cc);
        }
        return cc.inventory;
    }
}
