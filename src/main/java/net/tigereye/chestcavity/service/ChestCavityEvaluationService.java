package net.tigereye.chestcavity.service;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.compat.kubejs.CCEvents;
import net.tigereye.chestcavity.listeners.OrganUpdateListeners;
import net.tigereye.chestcavity.network.ChestCavitySyncService;
import net.tigereye.chestcavity.registration.CCOrganScores;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Coordinates inventory evaluation and applies the resulting score changes.
 */
public final class ChestCavityEvaluationService {
    private static final Set<ChestCavityInstance> EFFECT_UPDATES_IN_PROGRESS =
            Collections.newSetFromMap(new IdentityHashMap<>());
    private static final Set<ChestCavityInstance> EFFECT_UPDATES_PENDING =
            Collections.newSetFromMap(new IdentityHashMap<>());

    private ChestCavityEvaluationService() {
    }

    public static void evaluate(ChestCavityInstance cc) {
        if (cc.owner == null || cc.owner.level().isClientSide()) {
            return;
        }

        Map<ResourceLocation, Float> organScores = new HashMap<>();
        if (!cc.opened) {
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

        cc.beginOrganScoreMutationBatch();
        try {
            cc.replaceBaseOrganScores(organScores);
            CCEvents.postEvaluateChestCavity(cc);
        } finally {
            cc.endOrganScoreMutationBatch();
        }
    }

    public static void updateEffectsAndSync(ChestCavityInstance cc) {
        if (cc.owner == null || cc.owner.level().isClientSide()) {
            return;
        }
        if (!EFFECT_UPDATES_IN_PROGRESS.add(cc)) {
            EFFECT_UPDATES_PENDING.add(cc);
            return;
        }
        try {
            boolean publishUpdateEvent = true;
            do {
                EFFECT_UPDATES_PENDING.remove(cc);
                if (cc.hasOrganScoreChangesSinceSnapshot()) {
                    cc.markDirty();
                    OrganUpdateListeners.call(cc.owner, cc);
                    cc.commitSnapshot();
                    if (publishUpdateEvent) {
                        CCEvents.postUpdateCCScore(cc);
                        publishUpdateEvent = false;
                    }
                    ChestCavitySyncService.enqueue(cc);
                } else {
                    cc.commitSnapshot();
                }
            } while (EFFECT_UPDATES_PENDING.remove(cc));
        } finally {
            EFFECT_UPDATES_IN_PROGRESS.remove(cc);
        }
    }
}
