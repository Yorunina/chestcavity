package net.tigereye.chestcavity.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Bounds-checked codecs shared by all Chest Cavity network packets.
 */
public final class ChestCavityNetworkCodec {
    public static final int MAX_MAP_ENTRIES = 4096;
    public static final int MAX_STRING_LENGTH = 32767;
    public static final int MAX_RESOURCE_ID_LENGTH = 256;

    private ChestCavityNetworkCodec() {
    }

    public static void writeRawData(FriendlyByteBuf buffer, Map<ResourceLocation, String> data) {
        requireEntryCount(data.size());
        buffer.writeVarInt(data.size());
        data.forEach((id, value) -> {
            writeResourceLocation(buffer, id);
            buffer.writeUtf(requireString(value), MAX_STRING_LENGTH);
        });
    }

    public static Map<ResourceLocation, String> readRawData(FriendlyByteBuf buffer) {
        int count = readEntryCount(buffer);
        Map<ResourceLocation, String> data = new HashMap<>(count);
        for (int index = 0; index < count; index++) {
            ResourceLocation id = readResourceLocation(buffer);
            String value = buffer.readUtf(MAX_STRING_LENGTH);
            data.put(id, value);
        }
        return data;
    }

    public static void writeOrganScores(FriendlyByteBuf buffer, Map<ResourceLocation, Float> scores) {
        requireEntryCount(scores.size());
        buffer.writeVarInt(scores.size());
        scores.forEach((id, value) -> {
            writeResourceLocation(buffer, id);
            buffer.writeFloat(value);
        });
    }

    public static Map<ResourceLocation, Float> readOrganScores(FriendlyByteBuf buffer) {
        int count = readEntryCount(buffer);
        Map<ResourceLocation, Float> scores = new HashMap<>(count);
        for (int index = 0; index < count; index++) {
            scores.put(readResourceLocation(buffer), buffer.readFloat());
        }
        return scores;
    }

    public static void writeResourceLocation(FriendlyByteBuf buffer, ResourceLocation id) {
        if (id == null) {
            throw new IllegalArgumentException("Chest Cavity resource IDs cannot be null");
        }
        buffer.writeUtf(requireResourceId(id.toString()), MAX_RESOURCE_ID_LENGTH);
    }

    public static ResourceLocation readResourceLocation(FriendlyByteBuf buffer) {
        String value = buffer.readUtf(MAX_RESOURCE_ID_LENGTH);
        try {
            return new ResourceLocation(value);
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("Invalid Chest Cavity resource ID: " + value, error);
        }
    }

    private static int readEntryCount(FriendlyByteBuf buffer) {
        int count = buffer.readVarInt();
        requireEntryCount(count);
        return count;
    }

    private static void requireEntryCount(int count) {
        if (count < 0 || count > MAX_MAP_ENTRIES) {
            throw new IllegalArgumentException("Invalid Chest Cavity network entry count: " + count);
        }
    }

    private static String requireString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Chest Cavity network strings cannot be null");
        }
        if (value.length() > MAX_STRING_LENGTH) {
            throw new IllegalArgumentException("Chest Cavity network string exceeds " + MAX_STRING_LENGTH + " characters");
        }
        return value;
    }

    private static String requireResourceId(String value) {
        if (value.length() > MAX_RESOURCE_ID_LENGTH) {
            throw new IllegalArgumentException("Chest Cavity resource ID exceeds " + MAX_RESOURCE_ID_LENGTH + " characters");
        }
        return value;
    }
}
