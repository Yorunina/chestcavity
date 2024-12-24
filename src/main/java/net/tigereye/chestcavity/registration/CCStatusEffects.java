package net.tigereye.chestcavity.registration;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.mob_effect.CCStatusEffect;
import net.tigereye.chestcavity.mob_effect.OrganRejection;
import net.tigereye.chestcavity.mob_effect.Ruminating;

public class CCStatusEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS;
    public static final RegistryObject<MobEffect> ORGAN_REJECTION;
    public static final RegistryObject<MobEffect> ARROW_DODGE_COOLDOWN;
    public static final RegistryObject<MobEffect> DRAGON_BOMB_COOLDOWN;
    public static final RegistryObject<MobEffect> DRAGON_BREATH_COOLDOWN;
    public static final RegistryObject<MobEffect> EXPLOSION_COOLDOWN;
    public static final RegistryObject<MobEffect> FORCEFUL_SPIT_COOLDOWN;
    public static final RegistryObject<MobEffect> GHASTLY_COOLDOWN;
    public static final RegistryObject<MobEffect> IRON_REPAIR_COOLDOWN;
    public static final RegistryObject<MobEffect> PYROMANCY_COOLDOWN;
    public static final RegistryObject<MobEffect> RUMINATING;
    public static final RegistryObject<MobEffect> SHULKER_BULLET_COOLDOWN;
    public static final RegistryObject<MobEffect> SILK_COOLDOWN;
    public static final RegistryObject<MobEffect> VENOM_COOLDOWN;
    public static final RegistryObject<MobEffect> WATER_VULNERABILITY;

    public CCStatusEffects() {
    }

    static {
        MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "chestcavity");
        ORGAN_REJECTION = MOB_EFFECTS.register("organ_rejection", OrganRejection::new);
        ARROW_DODGE_COOLDOWN = MOB_EFFECTS.register("arrow_dodge_cooldown", () -> {
            return new CCStatusEffect(MobEffectCategory.NEUTRAL, 0);
        });
        DRAGON_BOMB_COOLDOWN = MOB_EFFECTS.register("dragon_bomb_cooldown", () -> {
            return new CCStatusEffect(MobEffectCategory.NEUTRAL, 0);
        });
        DRAGON_BREATH_COOLDOWN = MOB_EFFECTS.register("dragon_breath_cooldown", () -> {
            return new CCStatusEffect(MobEffectCategory.NEUTRAL, 0);
        });
        EXPLOSION_COOLDOWN = MOB_EFFECTS.register("explosion_cooldown", () -> {
            return new CCStatusEffect(MobEffectCategory.NEUTRAL, 0);
        });
        FORCEFUL_SPIT_COOLDOWN = MOB_EFFECTS.register("forceful_spit_cooldown", () -> {
            return new CCStatusEffect(MobEffectCategory.NEUTRAL, 0);
        });
        GHASTLY_COOLDOWN = MOB_EFFECTS.register("ghastly_cooldown", () -> {
            return new CCStatusEffect(MobEffectCategory.NEUTRAL, 0);
        });
        IRON_REPAIR_COOLDOWN = MOB_EFFECTS.register("iron_repair_cooldown", () -> {
            return new CCStatusEffect(MobEffectCategory.NEUTRAL, 0);
        });
        PYROMANCY_COOLDOWN = MOB_EFFECTS.register("pyromancy_cooldown", () -> {
            return new CCStatusEffect(MobEffectCategory.NEUTRAL, 0);
        });
        RUMINATING = MOB_EFFECTS.register("ruminating", Ruminating::new);
        SHULKER_BULLET_COOLDOWN = MOB_EFFECTS.register("shulker_bullet_cooldown", () -> {
            return new CCStatusEffect(MobEffectCategory.NEUTRAL, 0);
        });
        SILK_COOLDOWN = MOB_EFFECTS.register("silk_cooldown", () -> {
            return new CCStatusEffect(MobEffectCategory.NEUTRAL, 0);
        });
        VENOM_COOLDOWN = MOB_EFFECTS.register("venom_cooldown", () -> {
            return new CCStatusEffect(MobEffectCategory.NEUTRAL, 0);
        });
        WATER_VULNERABILITY = MOB_EFFECTS.register("water_vulnerability", () -> {
            return new CCStatusEffect(MobEffectCategory.NEUTRAL, 0);
        });
    }
}
