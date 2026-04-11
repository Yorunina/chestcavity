package net.tigereye.chestcavity.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;

@Config(
        name = "chestcavity"
)
public class CCConfig implements ConfigData {
    @Category("core")
    public int ORGAN_REJECTION_DAMAGE = 2;
    @Category("core")
    public int ORGAN_REJECTION_RATE = 600;
    @Category("core")
    public int KIDNEY_RATE = 60;
    @Category("core")
    public float FILTRATION_DURATION_FACTOR = 1.0F;
    @Category("core")
    public float APPENDIX_LUCK = 1F;
    @Category("core")
    public float HEART_HP = 1.0F;
    @Category("core")
    public float MUSCLE_STRENGTH = 1.0F;
    @Category("core")
    public float MUSCLE_SPEED = 0.25F;
    @Category("core")
    public float NERVES_HASTE = 0.1F;
    @Category("core")
    public float BONE_DEFENSE = 0.5F;
    @Category("core")
    public int CHEST_OPENER_ABSOLUTE_HEALTH_THRESHOLD = 10;
    @Category("core")
    public float CHEST_OPENER_FRACTIONAL_HEALTH_THRESHOLD = 0.1F;
    @Category("core")
    public boolean DISABLE_ORGAN_REJECTION = false;
    @Category("more")
    public float FIREPROOF_DEFENSE = 0.75F;
    @Category("more")
    public float IMPACT_DEFENSE = 0.75F;
    @Category("more")
    public float SWIMSPEED_FACTOR = 1.0F;

    public CCConfig() {
    }
}
