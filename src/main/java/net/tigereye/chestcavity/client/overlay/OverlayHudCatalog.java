package net.tigereye.chestcavity.client.overlay;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class OverlayHudCatalog {
    public static final OverlayHudCatalog EMPTY = new OverlayHudCatalog(null, null, List.of(), List.of());

    @Nullable
    private final String scoreTitle;
    @Nullable
    private final String tagTitle;
    private final List<OverlayHudEntry> scores;
    private final List<OverlayHudEntry> tags;

    public OverlayHudCatalog(
            @Nullable String scoreTitle,
            @Nullable String tagTitle,
            Collection<OverlayHudEntry> scores,
            Collection<OverlayHudEntry> tags
    ) {
        this.scoreTitle = scoreTitle;
        this.tagTitle = tagTitle;
        this.scores = List.copyOf(new ArrayList<>(scores));
        this.tags = List.copyOf(new ArrayList<>(tags));
    }

    @Nullable
    public String getScoreTitle() {
        return scoreTitle;
    }

    @Nullable
    public String getTagTitle() {
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
