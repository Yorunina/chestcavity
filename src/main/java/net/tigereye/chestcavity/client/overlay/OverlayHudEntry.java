package net.tigereye.chestcavity.client.overlay;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public final class OverlayHudEntry {
    private final ResourceLocation id;
    @Nullable
    private final TagKey<Item> tag;
    private final Consumer<OverlayHudLineJS> renderer;

    private OverlayHudEntry(
            ResourceLocation id,
            @Nullable TagKey<Item> tag,
            Consumer<OverlayHudLineJS> renderer
    ) {
        this.id = id;
        this.tag = tag;
        this.renderer = renderer;
    }

    public static OverlayHudEntry score(ResourceLocation id, Consumer<OverlayHudLineJS> renderer) {
        return new OverlayHudEntry(id, null, renderer);
    }

    public static OverlayHudEntry tag(ResourceLocation id, Consumer<OverlayHudLineJS> renderer) {
        return new OverlayHudEntry(id, TagKey.create(Registries.ITEM, id), renderer);
    }

    public ResourceLocation getId() {
        return id;
    }

    @Nullable
    public TagKey<Item> getTag() {
        return tag;
    }

    public Consumer<OverlayHudLineJS> getRenderer() {
        return renderer;
    }
}
