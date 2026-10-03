package net.tigereye.chestcavity.client.overlay;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public final class OverlayScorePacketClient {
    private OverlayScorePacketClient() {
    }

    public static void apply(Map<ResourceLocation, Float> scores, boolean valid) {
        if (!valid) {
            return;
        }
        OverlayHudClient.setScores(scores);
    }
}
