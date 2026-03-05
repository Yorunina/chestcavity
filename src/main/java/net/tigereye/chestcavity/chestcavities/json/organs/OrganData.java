package net.tigereye.chestcavity.chestcavities.json.organs;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class OrganData {
    public boolean pseudoOrgan = false;
    public Map<ResourceLocation, Float> organScores = new HashMap<>();

    public OrganData() {
    }

    public boolean mergeOrganScores(OrganData organData) {
        if (organData == null) return false;
        if (organData.pseudoOrgan) this.pseudoOrgan = true;
        organData.organScores.forEach((k, v) -> {
            if (!this.organScores.containsKey(k)) {
                this.organScores.put(k, v);
            } else {
                this.organScores.put(k, this.organScores.get(k) + v);
            }
        });
        return true;
    }

    public boolean isEmpty() {
        return this.organScores.isEmpty();
    }
}
