package net.tigereye.chestcavity.registration;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.mob_effect.FurnacePower;
import net.tigereye.chestcavity.mob_effect.OrganRejection;
import net.tigereye.chestcavity.mob_effect.OrganSlip;
import net.tigereye.chestcavity.mob_effect.Ruminating;

public class CCStatusEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS;
    public static final RegistryObject<MobEffect> ORGAN_REJECTION;
    public static final RegistryObject<MobEffect> ARROW_DODGE_COOLDOWN;
    public static final RegistryObject<MobEffect> DRAGON_BOMB_COOLDOWN;
    public static final RegistryObject<MobEffect> DRAGON_BREATH_COOLDOWN;
    public static final RegistryObject<MobEffect> EXPLOSION_COOLDOWN;
    public static final RegistryObject<MobEffect> FORCEFUL_SPIT_COOLDOWN;
    public static final RegistryObject<MobEffect> FURNACE_POWER;
    public static final RegistryObject<MobEffect> GHASTLY_COOLDOWN;
    public static final RegistryObject<MobEffect> PYROMANCY_COOLDOWN;
    public static final RegistryObject<MobEffect> RUMINATING;
    public static final RegistryObject<MobEffect> ORGAN_SLIP;
    public static final RegistryObject<MobEffect> SHULKER_BULLET_COOLDOWN;
    public static final RegistryObject<MobEffect> WATER_VULNERABILITY;

    public CCStatusEffects() {
    }

    static {
        MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, "chestcavity");
        ORGAN_REJECTION = MOB_EFFECTS.register("organ_rejection", OrganRejection::new);
        FURNACE_POWER = MOB_EFFECTS.register("furnace_power", FurnacePower::new);
        RUMINATING = MOB_EFFECTS.register("ruminating", Ruminating::new);
        ORGAN_SLIP = MOB_EFFECTS.register("organSlip", OrganSlip::new);
    }
}
