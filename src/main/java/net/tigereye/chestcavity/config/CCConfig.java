package net.tigereye.chestcavity.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;

@Config(
        name = "chestcavity"
)
public class CCConfig implements ConfigData {
    @Category("forge")
    public boolean DEBUG_MODE = true;
    @Category("core")
    public float ORGAN_BUNDLE_LOOTING_BOOST = 0.04F;
    @Category("core")
    public float UNIVERSAL_DONOR_RATE = 0.1F;
    @Category("core")
    public int ORGAN_REJECTION_DAMAGE = 2;
    @Category("core")
    public int ORGAN_REJECTION_RATE = 600;
    @Category("core")
    public int HEARTBLEED_RATE = 20;
    @Category("core")
    public int KIDNEY_RATE = 60;
    @Category("core")
    public float FILTRATION_DURATION_FACTOR = 1.0F;
    @Category("core")
    public float APPENDIX_LUCK = 0.1F;
    @Category("core")
    public float HEART_HP = 4.0F;
    @Category("core")
    public float MUSCLE_STRENGTH = 1.0F;
    @Category("core")
    public float MUSCLE_SPEED = 0.5F;
    @Category("core")
    public float NERVES_HASTE = 0.1F;
    @Category("core")
    public float BONE_DEFENSE = 0.5F;
    @Category("core")
    public float RISK_OF_PRIONS = 0.01F;
    @Category("core")
    public int CHEST_OPENER_ABSOLUTE_HEALTH_THRESHOLD = 20;
    @Category("core")
    public float CHEST_OPENER_FRACTIONAL_HEALTH_THRESHOLD = 0.5F;
    @Category("core")
    public boolean CAN_OPEN_OTHER_PLAYERS = false;
    @Category("core")
    public boolean KEEP_CHEST_CAVITY = false;
    @Category("core")
    public boolean DISABLE_ORGAN_REJECTION = false;
    @Category("more")
    public int ARROW_DODGE_DISTANCE = 32;
    @Category("more")
    public float BUFF_PURGING_DURATION_FACTOR = 0.5F;
    @Category("more")
    public int CRYSTALSYNTHESIS_RANGE = 32;
    @Category("more")
    public int CRYSTALSYNTHESIS_FREQUENCY = 10;
    @Category("more")
    public float FIREPROOF_DEFENSE = 0.75F;
    @Category("more")
    public float IMPACT_DEFENSE = 0.75F;
    @Category("more")
    public float IRON_REPAIR_PERCENT = 0.25F;
    @Category("more")
    public float LAUNCHING_POWER = 0.1F;
    @Category("more")
    public int MAX_TELEPORT_ATTEMPTS = 5;
    @Category("more")
    public int PHOTOSYNTHESIS_FREQUENCY = 50;
    @Category("more")
    public int RUMINATION_TIME = 400;
    @Category("more")
    public int RUMINATION_GRASS_PER_SQUARE = 2;
    @Category("more")
    public int RUMINATION_SQUARES_PER_STOMACH = 3;
    @Category("more")
    public int SHULKER_BULLET_TARGETING_RANGE = 20;
    @Category("more")
    public float SWIMSPEED_FACTOR = 1.0F;
    @Category("more")
    public float WITHERED_DURATION_FACTOR = 0.5F;
    @Category("cooldown")
    public int ARROW_DODGE_COOLDOWN = 200;
    @Category("cooldown")
    public int DRAGON_BOMB_COOLDOWN = 200;
    @Category("cooldown")
    public int DRAGON_BREATH_COOLDOWN = 200;
    @Category("cooldown")
    public int EXPLOSION_COOLDOWN = 200;
    @Category("cooldown")
    public int FORCEFUL_SPIT_COOLDOWN = 20;
    @Category("cooldown")
    public int GHASTLY_COOLDOWN = 60;
    @Category("cooldown")
    public int IRON_REPAIR_COOLDOWN = 1200;
    @Category("cooldown")
    public int PYROMANCY_COOLDOWN = 78;
    @Category("cooldown")
    public int SHULKER_BULLET_COOLDOWN = 100;
    @Category("cooldown")
    public int SILK_COOLDOWN = 20;
    @Category("cooldown")
    public int VENOM_COOLDOWN = 40;

    public CCConfig() {
    }
}
