package net.tigereye.chestcavity.service;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.IChestCavityType;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;
import java.util.Map;

/**
 * Resolves organ data and compatibility metadata without coordinating
 * inventory evaluation or entity side effects.
 */
public final class OrganLookupService {
    private OrganLookupService() {
    }

    public static void addOrganScore(ResourceLocation id, float value, Map<ResourceLocation, Float> organScores) {
        organScores.put(id, organScores.getOrDefault(id, 0.0F) + value);
    }

    public static OrganData lookupOrgan(ItemStack itemStack, IChestCavityType cavityType) {
        OrganData organData = new OrganData();

        if (cavityType != null) {
            OrganData exceptionalOrganData = cavityType.catchExceptionalOrgan(itemStack);
            organData.mergeOrganScores(exceptionalOrganData);
        }

        OrganData nbtOrganData = OrganManager.readNBTOrganData(itemStack);
        organData.mergeOrganScores(nbtOrganData);

        Item item = itemStack.getItem();
        if (OrganManager.hasEntry(item)) {
            OrganData managedOrganData = OrganManager.getEntry(item);
            organData.mergeOrganScores(managedOrganData);
        }

        return organData;
    }

    public static boolean getCompatibility(ChestCavityInstance cavity, ItemStack itemStack) {
        if (itemStack == null || itemStack == ItemStack.EMPTY) {
            return true;
        }

        CompoundTag tag = itemStack.getTag();
        if (tag == null || !tag.contains(ChestCavity.COMPATIBILITY_TAG)) {
            return true;
        }

        CompoundTag compatibilityTag = tag.getCompound(ChestCavity.COMPATIBILITY_TAG);
        return compatibilityTag.hasUUID("owner")
                && compatibilityTag.getUUID("owner").equals(cavity.compatibilityId);
    }

    public static boolean isOriginalOrgan(ChestCavityInstance cavity, int slot, ItemStack itemStack) {
        if (itemStack == null || itemStack == ItemStack.EMPTY) {
            return true;
        }

        CompoundTag tag = itemStack.getTag();
        if (tag != null && tag.contains(ChestCavity.COMPATIBILITY_TAG)) {
            CompoundTag compatibilityTag = tag.getCompound(ChestCavity.COMPATIBILITY_TAG);
            return compatibilityTag.hasUUID("owner")
                    && compatibilityTag.getUUID("owner").equals(cavity.compatibilityId);
        }

        ChestCavityInventory defaultInventory = cavity.getChestCavityType().getDefaultChestCavity();
        ItemStack defaultItem = defaultInventory.getItem(slot);
        return ItemStack.isSameItemSameTags(itemStack, defaultItem)
                && itemStack.getCount() == defaultItem.getCount();
    }

    public static void setOrganCompatibility(ChestCavityInstance cavity, ItemStack itemStack) {
        if (itemStack == ItemStack.EMPTY) {
            return;
        }

        CompoundTag tag = new CompoundTag();
        tag.putUUID("owner", cavity.compatibilityId);
        tag.putString(
                "name",
                cavity.owner instanceof net.minecraft.world.entity.player.Player
                        ? cavity.owner.getName().getString()
                        : cavity.owner.getType().getDescriptionId()
        );
        itemStack.addTagElement(ChestCavity.COMPATIBILITY_TAG, tag);
    }
}
