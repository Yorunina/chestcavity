package net.tigereye.chestcavity.compat.kubejs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.client.overlay.OverlayHudCatalog;
import net.tigereye.chestcavity.client.overlay.OverlayHudEntry;
import net.tigereye.chestcavity.client.overlay.OverlayHudLineJS;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Client event that decides which scores and item tags appear on the chest cavity overlay.
 *
 * <pre>
 * ChestCavityEvents.overlayHud(event => {
 *     event.addScore('chestcavity:health', line => {
 *         const key = 'tooltips.organ_score.chestcavity:health'
 *         line.text(Text.translate(key).append(' ' + line.value).yellow().underlined())
 *         line.hover([
 *             Text.translate(key).gold(),
 *             Text.translate('tooltips.organ_score.hover.chestcavity:health')
 *         ])
 *     })
 *     event.addTag('kubejs:dragon', line => {
 *         line.text(Text.translate('tooltips.kubejs.tag.kubejs:dragon').append(' ' + line.count + '/3'))
 *         line.hover([Text.of('3: ...')])
 *     })
 * })
 * </pre>
 */
public class OverlayHudJS extends EventJS {
    private final Map<ResourceLocation, OverlayHudEntry> scores = new LinkedHashMap<>();
    private final Map<ResourceLocation, OverlayHudEntry> tags = new LinkedHashMap<>();
    @Nullable
    private Component scoreTitle;
    @Nullable
    private Component tagTitle;

    public OverlayHudJS setScoreTitle(Component title) {
        this.scoreTitle = title;
        return this;
    }

    public OverlayHudJS setTagTitle(Component title) {
        this.tagTitle = title;
        return this;
    }

    public OverlayHudJS addScore(String id, Consumer<OverlayHudLineJS> renderer) {
        ResourceLocation resourceLocation = requireId(id, false, "organ score");
        scores.put(resourceLocation, OverlayHudEntry.score(resourceLocation, requireRenderer(renderer, "Score")));
        return this;
    }

    public OverlayHudJS addTag(String id, Consumer<OverlayHudLineJS> renderer) {
        ResourceLocation resourceLocation = requireId(id, true, "item tag");
        tags.put(resourceLocation, OverlayHudEntry.tag(resourceLocation, requireRenderer(renderer, "Tag")));
        return this;
    }

    public OverlayHudCatalog toCatalog() {
        return new OverlayHudCatalog(scoreTitle, tagTitle, scores.values(), tags.values());
    }

    private static Consumer<OverlayHudLineJS> requireRenderer(Consumer<OverlayHudLineJS> renderer, String kind) {
        if (renderer == null) {
            throw new IllegalArgumentException(kind + " renderer cannot be null");
        }
        return renderer;
    }

    private static ResourceLocation requireId(String id, boolean allowHash, String kind) {
        ResourceLocation resourceLocation = parse(id, allowHash);
        if (resourceLocation == null) {
            throw new IllegalArgumentException("Invalid " + kind + " id: " + id);
        }
        return resourceLocation;
    }

    @Nullable
    private static ResourceLocation parse(String id, boolean allowHash) {
        if (id == null) {
            return null;
        }
        String normalized = id.trim();
        if (allowHash && normalized.startsWith("#")) {
            normalized = normalized.substring(1);
        }
        return ResourceLocation.tryParse(normalized);
    }

}
