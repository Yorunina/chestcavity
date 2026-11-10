package net.tigereye.chestcavity.client.overlay;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Map;

public final class OverlayHudClient {
    private static Map<ResourceLocation, Float> scores = Map.of();
    @Nullable
    private static Integer posX;
    @Nullable
    private static Integer posY;
    private static boolean scoresCollapsed = true;
    private static boolean tagsCollapsed = true;

    private OverlayHudClient() {
    }

    public static void setScores(Map<ResourceLocation, Float> nextScores) {
        scores = Map.copyOf(nextScores);
    }

    public static void clearScores() {
        scores = Map.of();
    }

    public static Map<ResourceLocation, Float> getScores() {
        return scores;
    }

    public static boolean hasPosition() {
        return posX != null && posY != null;
    }

    public static int getPosX() {
        return posX == null ? 0 : posX;
    }

    public static int getPosY() {
        return posY == null ? 0 : posY;
    }

    public static void setPosition(int x, int y) {
        posX = x;
        posY = y;
    }

    public static boolean isScoresCollapsed() {
        return scoresCollapsed;
    }

    public static boolean isTagsCollapsed() {
        return tagsCollapsed;
    }

    public static void setScoresCollapsed(boolean collapsed) {
        scoresCollapsed = collapsed;
    }

    public static void setTagsCollapsed(boolean collapsed) {
        tagsCollapsed = collapsed;
    }

    public static void resetSession() {
        scores = Map.of();
        posX = null;
        posY = null;
        scoresCollapsed = true;
        tagsCollapsed = true;
    }
}
