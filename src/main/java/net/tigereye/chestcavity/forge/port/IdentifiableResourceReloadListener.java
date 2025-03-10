package net.tigereye.chestcavity.forge.port;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.Collection;
import java.util.Collections;

public interface IdentifiableResourceReloadListener extends PreparableReloadListener {
    ResourceLocation getFabricId();

    default Collection<ResourceLocation> getFabricDependencies() {
        return Collections.emptyList();
    }
}
