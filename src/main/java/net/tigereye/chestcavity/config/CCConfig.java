package net.tigereye.chestcavity.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.TranslatableText;
import net.tigereye.chestcavity.ChestCavity;

@Config(name = ChestCavity.MODID)
public class CCConfig implements ConfigData {

    @ConfigEntry.Category("core")
    public float ORGAN_BUNDLE_LOOTING_BOOST = .01f;
    @ConfigEntry.Category("core")
    public float UNIVERSAL_DONOR_RATE = .035f;
    @ConfigEntry.Category("core")
    public int ORGAN_REJECTION_DAMAGE = 2; //how much rejecting organs hurts
    @ConfigEntry.Category("core")
    public int ORGAN_REJECTION_RATE = 600; //base speed of organ rejection
    @ConfigEntry.Category("core")
    public int HEARTBLEED_RATE = 20; //how fast you die from lacking a heart in ticks
    @ConfigEntry.Category("core")
    public int KIDNEY_RATE = 60; //how often the kidneys prevent blood poisoning in ticks
    @ConfigEntry.Category("core")
    public float APPENDIX_LUCK = .1f; //how lucky your appendix is
    @ConfigEntry.Category("core")
    public float HEART_HP = 4; //how much health each heart is worth
    @ConfigEntry.Category("core")
    public float MUSCLE_STRENGTH = 1f; //how much 8 stacks of muscles contribute to attack damage
    @ConfigEntry.Category("core")
    public float MUSCLE_SPEED = .5f; //how much 8 stacks of muscles contribute to movement speed
    @ConfigEntry.Category("core")
    public float BONE_DEFENSE = .5f; //damage reduction from 4 stacks of ribs
    @ConfigEntry.Category("core")
    public float RISK_OF_PRIONS = .01f; //risk of debuffs from human-derived foods
    @ConfigEntry.Category("core")
    public boolean CAN_OPEN_OTHER_PLAYERS = false;
    @ConfigEntry.Category("core")
    public boolean KEEP_CHEST_CAVITY = false;
    @ConfigEntry.Category("core")
    public boolean DISABLE_ORGAN_REJECTION = false;

    @ConfigEntry.Category("more")
    public int ARROW_DODGE_DISTANCE = 32; //how far you can teleport when dodging projectiles
    @ConfigEntry.Category("more")
    public float FIREPROOF_DEFENSE = .75f; //damage reduction from 4 stacks of fireproof organs
    @ConfigEntry.Category("more")
    public int MAX_TELEPORT_ATTEMPTS = 5;
    @ConfigEntry.Category("more")
    public int RUMINATION_TIME = 400; //time to eat a unit of grass
    @ConfigEntry.Category("more")
    public int RUMINATION_GRASS_PER_SQUARE = 2; //number of grass units are in a square
    @ConfigEntry.Category("more")
    public int RUMINATION_SQUARES_PER_STOMACH = 3; //number of grass squares a stomach can hold
    @ConfigEntry.Category("more")
    public int SHULKER_BULLET_TARGETING_RANGE = 20;
    @ConfigEntry.Category("more")
    public float SWIMSPEED_FACTOR = 1f; //how much 8 swimming muscles boost swim speed
    @ConfigEntry.Category("more")
    public float WITHERED_DURATION_FACTOR = .5f; //how much withered bones reduce wither duration

    @ConfigEntry.Category("cooldown")
    public int ARROW_DODGE_COOLDOWN = 200; //how often an entity is allowed to dodge projectiles
    @ConfigEntry.Category("cooldown")
    public int EXPLOSION_COOLDOWN = 200; //how often an entity is allowed to try exploding
    @ConfigEntry.Category("cooldown")
    public int FORCEFUL_SPIT_COOLDOWN = 20; //how often an entity is allowed to try exploding
    @ConfigEntry.Category("cooldown")
    public int GHASTLY_COOLDOWN = 60; //how often an entity is allowed to fire ghast bombs
    @ConfigEntry.Category("cooldown")
    public int PYROMANCY_COOLDOWN = 78; //how often an entity is allowed to spew fireballs
    @ConfigEntry.Category("cooldown")
    public int SHULKER_BULLET_COOLDOWN = 100; //how often an entity is allowed to shoot bullets
    @ConfigEntry.Category("cooldown")
    public int SILK_COOLDOWN = 20; //how often an entity is allowed to lay silk
    @ConfigEntry.Category("cooldown")
    public int VENOM_COOLDOWN = 40; //how often an entity is allowed to poison targets

    @ConfigEntry.Category("integration")
    public boolean AGE_OF_EXILE_INTEGRATION = true;
    @ConfigEntry.Category("integration")
    public boolean ANTHROPOPHAGY_INTEGRATION = true;
    @ConfigEntry.Category("integration")
    public boolean BACKROOMS_INTEGRATION = true;
    @ConfigEntry.Category("integration")
    public boolean BEWITCHMENT_INTEGRATION = true;
    @ConfigEntry.Category("integration")
    public boolean BIOME_MAKEOVER_INTEGRATION = true;
    @ConfigEntry.Category("integration")
    public boolean DIREBATS_INTEGRATION = true;
    @ConfigEntry.Category("integration")
    public boolean RATS_MISCHIEF_INTEGRATION = true;
    @ConfigEntry.Category("integration")
    public boolean REQUIEM_INTEGRATION = true;
    @ConfigEntry.Category("integration")
    public boolean SMALL_ENDERMEN_INTEGRATION = true;
    @ConfigEntry.Category("integration")
    public boolean SNOW_MERCY_INTEGRATION = true;
}
