package net.tigereye.chestcavity.client.overlay;

import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class OverlayHudCatalog {
    public static final OverlayHudCatalog EMPTY = new OverlayHudCatalog(null, null, List.of(), List.of());

    @Nullable
    private final Component scoreTitle;
    @Nullable
    private final Component tagTitle;
    private final List<OverlayHudEntry> scores;
    private final List<OverlayHudEntry> tags;

    public OverlayHudCatalog(
            @Nullable Component scoreTitle,
            @Nullable Component tagTitle,
            Collection<OverlayHudEntry> scores,
            Collection<OverlayHudEntry> tags
    ) {
        this.scoreTitle = scoreTitle;
        this.tagTitle = tagTitle;
        this.scores = List.copyOf(new ArrayList<>(scores));
        this.tags = List.copyOf(new ArrayList<>(tags));
    }

    @Nullable
    public Component getScoreTitle() {
        return scoreTitle;
    }

    @Nullable
    public Component getTagTitle() {
        return tagTitle;
    }

    public List<OverlayHudEntry> getScores() {
        return scores;
    }

    public List<OverlayHudEntry> getTags() {
        return tags;
    }

    public boolean isEmpty() {
        return scores.isEmpty() && tags.isEmpty();
    }
}
