package net.tigereye.chestcavity.mob_effect;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.registration.CCStatusEffects;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS
            = DeferredRegister.create(ForgeRegistries.POTIONS, ChestCavity.MODID);

    public static final RegistryObject<Potion> ORGAN_SLIP = POTIONS.register("organ_slip",
            () -> new Potion(new MobEffectInstance(CCStatusEffects.ORGAN_SLIP.get(),400,0)));
    public static final RegistryObject<Potion> LONG_ORGAN_SLIP = POTIONS.register("long_organ_slip",
            () -> new Potion(new MobEffectInstance(CCStatusEffects.ORGAN_SLIP.get(),1600,0)));

    public static final RegistryObject<Potion> SURGICAL_ANESTHESIA = POTIONS.register("surgical_anesthesia",
            () -> new Potion(new MobEffectInstance(CCStatusEffects.SURGICAL_ANESTHESIA.get(),100,0)));
    public static final RegistryObject<Potion> LONG_SURGICAL_ANESTHESIA = POTIONS.register("long_surgical_anesthesia",
            () -> new Potion(new MobEffectInstance(CCStatusEffects.SURGICAL_ANESTHESIA.get(),200,0)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
