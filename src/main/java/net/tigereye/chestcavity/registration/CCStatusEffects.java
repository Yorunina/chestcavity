package net.tigereye.chestcavity.registration;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.mob_effect.OrganProtection;
import net.tigereye.chestcavity.mob_effect.OrganRejection;
import net.tigereye.chestcavity.mob_effect.OrganSlip;
import net.tigereye.chestcavity.mob_effect.SurgicalAnesthesia;

public class CCStatusEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS;
    public static final RegistryObject<MobEffect> ORGAN_REJECTION;
    public static final RegistryObject<MobEffect> ORGAN_SLIP;
    public static final RegistryObject<MobEffect> ORGAN_PROTECTION;
    public static final RegistryObject<MobEffect> SURGICAL_ANESTHESIA;

    public CCStatusEffects() {}

    static {
        MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ChestCavity.MODID);
        ORGAN_REJECTION = MOB_EFFECTS.register("organ_rejection", OrganRejection::new);
        ORGAN_SLIP = MOB_EFFECTS.register("organ_slip", OrganSlip::new);
        ORGAN_PROTECTION = MOB_EFFECTS.register("organ_protection", OrganProtection::new);
        SURGICAL_ANESTHESIA = MOB_EFFECTS.register("surgical_anesthesia", SurgicalAnesthesia::new);
    }
}
