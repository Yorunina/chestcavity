package net.tigereye.chestcavity.chestcavities.instance;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class OrganScoreState {
    private Map<ResourceLocation, Float> baseScores = new HashMap<>();
    private Map<ResourceLocation, Float> scores = new HashMap<>();
    private final Map<ResourceLocation, Map<UUID, OrganScoreModifier>> modifiers = new HashMap<>();
    private final Map<ResourceLocation, Float> previousScores = new HashMap<>();
    private boolean dirty = true;
    private boolean rebuildRequired = true;

    public Map<ResourceLocation, Float> getScores() {
        rebuildIfRequired();
        return scores;
    }

    public float getScore(ResourceLocation id, float defaultValue) {
        rebuildIfRequired();
        return scores.getOrDefault(id, defaultValue);
    }

    public float getScore(ResourceLocation id) {
        return getScore(id, 0.0F);
    }

    public void setScore(ResourceLocation id, float score) {
        baseScores.put(id, score);
        markChanged();
    }

    public void setScores(Map<ResourceLocation, Float> scores) {
        baseScores = new HashMap<>(scores);
        markChanged();
    }

    public void addModifier(ResourceLocation scoreId, OrganScoreModifier modifier) {
        modifiers.computeIfAbsent(scoreId, ignored -> new HashMap<>())
                .put(modifier.id(), modifier);
        markChanged();
    }

    public boolean removeModifier(ResourceLocation scoreId, UUID modifierId) {
        Map<UUID, OrganScoreModifier> scoreModifiers = modifiers.get(scoreId);
        if (scoreModifiers == null || scoreModifiers.remove(modifierId) == null) {
            return false;
        }
        if (scoreModifiers.isEmpty()) {
            modifiers.remove(scoreId);
        }
        markChanged();
        return true;
    }

    public boolean hasModifier(ResourceLocation scoreId, UUID modifierId) {
        Map<UUID, OrganScoreModifier> scoreModifiers = modifiers.get(scoreId);
        return scoreModifiers != null && scoreModifiers.containsKey(modifierId);
    }


    public OrganScoreModifier getModifier(ResourceLocation scoreId, UUID modifierId) {
        Map<UUID, OrganScoreModifier> scoreModifiers = modifiers.get(scoreId);
        return scoreModifiers == null ? null : scoreModifiers.get(modifierId);
    }


    public List<OrganScoreModifier> getModifiers(ResourceLocation scoreId) {
        Map<UUID, OrganScoreModifier> scoreModifiers = modifiers.get(scoreId);
        return scoreModifiers == null ? List.of() : List.copyOf(scoreModifiers.values());
    }

    public void clearModifiers(ResourceLocation scoreId) {
        if (modifiers.remove(scoreId) != null) {
            markChanged();
        }
    }

    public void clearModifiers() {
        if (!modifiers.isEmpty()) {
            modifiers.clear();
            markChanged();
        }
    }

    public void copyModifiersFrom(OrganScoreState other) {
        modifiers.clear();
        other.modifiers.forEach((scoreId, values) ->
                modifiers.put(scoreId, new HashMap<>(values)));
        markChanged();
    }

    public boolean consumeDirty() {
        rebuildIfRequired();
        if (!dirty) {
            return false;
        }
        dirty = false;
        return true;
    }

    public boolean hasScoreChanged(ResourceLocation id) {
        return getPreviousScore(id) != getScore(id, 0.0F);
    }

    public boolean hasScoresChanged() {
        rebuildIfRequired();
        return !previousScores.equals(scores);
    }

    public float getPreviousScore(ResourceLocation id) {
        return previousScores.getOrDefault(id, 0.0F);
    }

    public void commitScores() {
        previousScores.clear();
        previousScores.putAll(scores);
    }

    public Map<ResourceLocation, Float> getPreviousScores() {
        return previousScores;
    }

    private void rebuild() {
        Map<ResourceLocation, Float> effectiveScores = new HashMap<>(baseScores);
        modifiers.forEach((scoreId, scoreModifiers) -> {
            double value = baseScores.getOrDefault(scoreId, 0.0F);

            for (OrganScoreModifier modifier : scoreModifiers.values()) {
                if (modifier.operation() == OrganScoreModifier.Operation.ADDITION) {
                    value += modifier.amount();
                }
            }
            double valueAfterAdditions = value;
            for (OrganScoreModifier modifier : scoreModifiers.values()) {
                if (modifier.operation() == OrganScoreModifier.Operation.MULTIPLY_BASE) {
                    value += valueAfterAdditions * modifier.amount();
                }
            }
            for (OrganScoreModifier modifier : scoreModifiers.values()) {
                if (modifier.operation() == OrganScoreModifier.Operation.MULTIPLY_TOTAL) {
                    value *= 1.0D + modifier.amount();
                }
            }
            effectiveScores.put(scoreId, (float) value);
        });
        scores = effectiveScores;
    }

    private void markChanged() {
        dirty = true;
        rebuildRequired = true;
    }

    private void rebuildIfRequired() {
        if (rebuildRequired) {
            rebuild();
            rebuildRequired = false;
        }
    }
}
