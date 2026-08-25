package net.tigereye.chestcavity.service;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.compat.kubejs.CCEvents;
import net.tigereye.chestcavity.listeners.OrganUpdateListeners;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.util.NetworkUtil;

import java.util.Map;
import java.util.Objects;

/**
 * Coordinates inventory evaluation and applies the resulting score changes.
 */
public final class ChestCavityEvaluationService {
    private ChestCavityEvaluationService() {
    }

    public static void evaluate(ChestCavityInstance cc) {
        if (cc.owner == null || cc.owner.level().isClientSide()) {
            return;
        }

        Map<ResourceLocation, Float> organScores = cc.getOrganScores();
        if (!cc.opened) {
            organScores.clear();
            if (cc.getChestCavityType().getDefaultOrganScores() != null) {
                organScores.putAll(cc.getChestCavityType().getDefaultOrganScores());
            }
        } else {
            cc.getChestCavityType().loadBaseOrganScores(organScores);
            InventoryTypeData inventoryTypeData = cc.getInventoryTypeData();

            for (int i = 0; i < cc.inventory.getContainerSize(); i++) {
                String slotType = inventoryTypeData.getSlotType(i);
                ItemStack itemStack = cc.inventory.getItem(i);
                if (Objects.equals(slotType, "container_slot") || itemStack.isEmpty()) {
                    continue;
                }

                OrganData data = OrganLookupService.lookupOrgan(itemStack, cc.getChestCavityType());
                if (data.isEmpty()) {
                    continue;
                }
                data.organScores.forEach((key, value) -> OrganLookupService.addOrganScore(
                        key,
                        value * Math.min((float) itemStack.getCount() / (float) itemStack.getMaxStackSize(), 1.0F),
                        organScores
                ));

                if (!data.pseudoOrgan && !OrganLookupService.getCompatibility(cc, itemStack)) {
                    OrganLookupService.addOrganScore(CCOrganScores.INCOMPATIBILITY, 1.0F, organScores);
                }
            }
        }

        CCEvents.postEvaluateChestCavity(cc);
        updateEffectsAndSync(cc);
    }

    public static void updateEffectsAndSync(ChestCavityInstance cc) {
        if (cc.owner == null || cc.owner.level().isClientSide()) {
            return;
        }
        if (cc.hasOrganScoreChangesSinceSnapshot()) {
            cc.markDirty();
            OrganUpdateListeners.call(cc.owner, cc);
            CCEvents.postUpdateCCScore(cc);
            NetworkUtil.SendS2CChestCavityUpdatePacket(cc);
        }
        cc.commitSnapshot();
    }
}
