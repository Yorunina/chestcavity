package net.tigereye.chestcavity.chestcavities.json.organs;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class OrganData {
    public boolean pseudoOrgan;
    public Map<ResourceLocation, Float> organScores = new HashMap<>();

    public OrganData() {
    }
}
