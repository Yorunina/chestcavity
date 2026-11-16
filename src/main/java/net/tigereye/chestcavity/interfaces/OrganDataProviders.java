package net.tigereye.chestcavity.interfaces;

import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class OrganDataProviders {
    private static final List<OrganDataProvider> BUILT_IN_PROVIDERS = List.of(
            new NbtOrganDataProvider(),
            new ManagedOrganDataProvider()
    );
    private static final List<OrganDataProvider> PROVIDERS = new CopyOnWriteArrayList<>();

    private OrganDataProviders() {
    }

    public static void register(OrganDataProvider provider) {
        if (provider != null && !PROVIDERS.contains(provider)) {
            PROVIDERS.add(provider);
        }
    }

    public static OrganData getOrganData(ItemStack itemStack) {
        OrganData organData = null;
        for (OrganDataProvider provider : BUILT_IN_PROVIDERS) {
            organData = mergeProviderData(itemStack, provider, organData);
        }
        for (OrganDataProvider provider : PROVIDERS) {
            organData = mergeProviderData(itemStack, provider, organData);
        }
        return organData;
    }

    private static OrganData mergeProviderData(ItemStack itemStack, OrganDataProvider provider, OrganData organData) {
        try {
            OrganData providedData = provider.getOrganData(itemStack);
            if (providedData == null) return organData;
            if (organData == null) organData = new OrganData();
            organData.mergeOrganScores(providedData);
        } catch (Exception error) {
            ChestCavity.LOGGER.error("Error reading dynamic organ data", error);
        }
        return organData;
    }
}
