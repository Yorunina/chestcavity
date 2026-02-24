package net.tigereye.chestcavity.chestcavities.json.organs;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class OrganData {
    public boolean pseudoOrgan = false;
    public Map<ResourceLocation, Float> organScores = new HashMap<>();

    public OrganData() {
    }

    public void mergeOrganScores(Map<ResourceLocation, Float> organScores) {
        organScores.forEach((k, v) -> {
            if (!this.organScores.containsKey(k)) {
                this.organScores.put(k, v);
            } else {
                this.organScores.put(k, this.organScores.get(k) + v);
            }
        });
    }

    public boolean isEmpty() {
        return this.organScores.isEmpty();
    }
}
