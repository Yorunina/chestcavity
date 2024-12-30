package net.tigereye.chestcavity.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class CCDamageSources {
    public static final ResourceKey<DamageType> HEARTBLEED;
    public static final ResourceKey<DamageType> ORGAN_REJECTION;

    public CCDamageSources() {
    }
    public static DamageSource of(Level world, ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }

    public static DamageSource of(Level world, ResourceKey<DamageType> key, Entity attacker) {
        return attacker != null ? new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), attacker) : new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }

    static {
        HEARTBLEED = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("chestcavity", "cc_heartbleed"));
        ORGAN_REJECTION = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("chestcavity", "cc_organ_rejection"));
    }
}