package net.tigereye.chestcavity.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstanceFactory;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCStatusEffects;
import net.tigereye.chestcavity.service.OrganEffectService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.DEFAULT_INVENTORY_TYPE_STRING;

@Mixin(value = LivingEntity.class, priority = 900)
public abstract class MixinLivingEntity extends Entity implements ChestCavityEntity {
    @Unique
    private ChestCavityInstance chestCavityInstance;
    @Unique
    private static final EntityDataAccessor<String> DATA_INVENTORY_TYPE = SynchedEntityData.defineId(MixinLivingEntity.class, EntityDataSerializers.STRING);

    @Shadow
    public abstract void addAdditionalSaveData(CompoundTag pCompound);

    @Shadow
    protected abstract int increaseAirSupply(int pCurrentAir);

    @Shadow
    public abstract boolean hasEffect(MobEffect pEffect);

    protected MixinLivingEntity(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    public InventoryTypeData getInventoryTypeData() {
        return InventoryTypeManager.getInventoryTypeData(new ResourceLocation(this.entityData.get(DATA_INVENTORY_TYPE)));
    }

    public void setInventoryTypeData(ResourceLocation id) {
        this.entityData.set(DATA_INVENTORY_TYPE, id.toString());
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"<init>"}
    )
    public void chestCavityLivingEntityConstructorMixin(EntityType<? extends LivingEntity> entityType, Level world, CallbackInfo info) {
        if (!world.isClientSide()) {
            this.chestCavityInstance = ChestCavityInstanceFactory.newChestCavityInstance(entityType, (LivingEntity) (Object) this);
        }
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"defineSynchedData"}
    )
    public void chestCavityLivingEntitySyncDataMixin(CallbackInfo info) {
        this.entityData.define(DATA_INVENTORY_TYPE, DEFAULT_INVENTORY_TYPE_STRING);
    }



    @Inject(at = @At("TAIL"), method = "baseTick")
    protected void chestCavityLivingEntityBaseTickBreathAirMixin(CallbackInfo info) {
        if (this.level().isClientSide) return;

        if (this.hasEffect(CCStatusEffects.ORGAN_PROTECTION.get())) return;

        if (!this.isEyeInFluid(FluidTags.WATER) || this.level().getBlockState(this.blockPosition()).is(Blocks.BUBBLE_COLUMN)) {
            this.setAirSupply(OrganEffectService.applyBreathOnLand(this.chestCavityInstance, this.getAirSupply(), this.increaseAirSupply(0)));
        }
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"decreaseAirSupply"},
            cancellable = true
    )
    protected void chestCavityLivingEntityGetNextAirUnderwaterMixin(int air, CallbackInfoReturnable<Integer> info) {
        if (this.level().isClientSide) {
            return;
        }
        info.setReturnValue(OrganEffectService.applyBreathInWater(this.chestCavityInstance, air, info.getReturnValueI()));
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"getDamageAfterArmorAbsorb"},
            cancellable = true
    )
    public void chestCavityLivingEntityDamageMixin(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        if (this.level().isClientSide) {
            return;
        }
        info.setReturnValue(OrganEffectService.applyDefenses(this.chestCavityInstance, source, info.getReturnValueF()));
    }


    @ModifyVariable(
            at = @At("HEAD"),
            method = {"addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"},
            ordinal = 0,
            argsOnly = true
    )
    public MobEffectInstance chestCavityLivingEntityAddStatusEffectMixin(MobEffectInstance effect) {
        if (this.level().isClientSide) {
            return effect;
        }
        return OrganEffectService.onAddStatusEffect(this.chestCavityInstance, effect);
    }

    @ModifyArg(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"
            ),
            method = {"travel"},
            index = 0,
            require = 0
    )
    protected float chestCavityLivingEntityWaterTravelMixin(float g) {
        if (this.level().isClientSide) {
            return g;
        }
        return g * OrganEffectService.applySwimSpeedInWater(this.chestCavityInstance);
    }

    public ChestCavityInstance getChestCavityInstance() {
        return this.chestCavityInstance;
    }

    public void setChestCavityInstance(ChestCavityInstance chestCavityInstance) {
        this.chestCavityInstance = chestCavityInstance;
    }

    @Inject(
            method = {"readAdditionalSaveData"},
            at = {@At("TAIL")}
    )
    private void readCustomDataFromNbt(CompoundTag tag, CallbackInfo callbackInfo) {
        if (this.level().isClientSide) {
            return;
        }
        this.chestCavityInstance.fromTag(tag, (LivingEntity) (Object) this);
    }

    @Inject(
            method = {"addAdditionalSaveData"},
            at = {@At("TAIL")}
    )
    private void writeCustomDataToNbt(CompoundTag tag, CallbackInfo callbackInfo) {
        if (this.level().isClientSide) {
            return;
        }
        this.chestCavityInstance.toTag(tag, (LivingEntity) (Object) this);
    }

}
