package net.tigereye.chestcavity.chestcavities.json.organs;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public class OrganData {
    public boolean pseudoOrgan;
    public Map<ResourceLocation, Float> organScores = new HashMap<>();

    public OrganData() {
    }
}
