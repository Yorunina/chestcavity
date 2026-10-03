package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.client.overlay.OverlayScorePacketClient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class OverlayScorePacket {
    static final int MAX_ENTRIES = 256;

    private final Map<ResourceLocation, Float> scores;
    private final boolean valid;

    public OverlayScorePacket(Map<ResourceLocation, Float> scores) {
        this.scores = copyLimited(scores);
        this.valid = true;
    }

    private OverlayScorePacket(Map<ResourceLocation, Float> scores, boolean valid) {
        this.scores = copyLimited(scores);
        this.valid = valid;
    }

    private static Map<ResourceLocation, Float> copyLimited(Map<ResourceLocation, Float> scores) {
        if (scores.size() <= MAX_ENTRIES) {
            return Map.copyOf(scores);
        }
        Map<ResourceLocation, Float> limited = new LinkedHashMap<>();
        for (Map.Entry<ResourceLocation, Float> entry : scores.entrySet()) {
            limited.put(entry.getKey(), entry.getValue());
            if (limited.size() >= MAX_ENTRIES) {
                break;
            }
        }
        return Map.copyOf(limited);
    }

    public static OverlayScorePacket decode(FriendlyByteBuf buf) {
        try {
            int count = buf.readVarInt();
            if (count < 0 || count > MAX_ENTRIES) {
                return new OverlayScorePacket(Map.of(), false);
            }
            Map<ResourceLocation, Float> scores = new LinkedHashMap<>();
            for (int i = 0; i < count; i++) {
                ResourceLocation id = buf.readResourceLocation();
                float value = buf.readFloat();
                if (id != null) {
                    scores.put(id, value);
                }
            }
            return new OverlayScorePacket(scores, true);
        } catch (Exception error) {
            ChestCavity.LOGGER.warn("Rejected malformed OverlayScorePacket", error);
            return new OverlayScorePacket(Map.of(), false);
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(scores.size());
        scores.forEach((id, value) -> {
            buf.writeResourceLocation(id);
            buf.writeFloat(value);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::applyClient));
        context.setPacketHandled(true);
        return true;
    }

    private void applyClient() {
        OverlayScorePacketClient.apply(scores, valid);
    }
}
