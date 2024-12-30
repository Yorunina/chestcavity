package net.tigereye.chestcavity.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.ITeleporter;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstanceFactory;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.items.ChestOpener;
import net.tigereye.chestcavity.listeners.OrganFoodEffectListeners;
import net.tigereye.chestcavity.registration.CCItems;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.util.ChestCavityUtil;
import net.tigereye.chestcavity.util.NetworkUtil;
import net.tigereye.chestcavity.util.OrganUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeManager.DEFAULT_INVENTORY_TYPE_STRING;

@Mixin(
        value = {LivingEntity.class},
        priority = 900
)
public abstract class MixinLivingEntity extends Entity implements ChestCavityEntity {
    @Unique
    private ChestCavityInstance chestCavityInstance;
    @Unique
    private static final EntityDataAccessor<String> DATA_INVENTORY_TYPE = SynchedEntityData.defineId(MixinLivingEntity.class, EntityDataSerializers.STRING);;
    @Shadow
    protected abstract int decreaseAirSupply(int var1);
    @Shadow
    public abstract void addAdditionalSaveData(CompoundTag pCompound);

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
        this.chestCavityInstance = ChestCavityInstanceFactory.newChestCavityInstance(entityType, (LivingEntity)(Object)this);
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"defineSynchedData"}
    )
    public void chestCavityLivingEntitySyncDataMixin(CallbackInfo info) {
        this.entityData.define(DATA_INVENTORY_TYPE, DEFAULT_INVENTORY_TYPE_STRING);
    }



    @Inject(
            at = {@At("HEAD")},
            method = {"baseTick"}
    )
    public void chestCavityLivingEntityBaseTickMixin(CallbackInfo info) {
        ChestCavityUtil.onTick(this.chestCavityInstance);
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"baseTick"}
    )
    protected void chestCavityLivingEntityBaseTickBreathAirMixin(CallbackInfo info) {
        if (!this.isEyeInFluid(FluidTags.WATER) || this.level().getBlockState(this.blockPosition()).is(Blocks.BUBBLE_COLUMN)) {
            this.setAirSupply(ChestCavityUtil.applyBreathOnLand(this.chestCavityInstance, this.getAirSupply(), this.decreaseAirSupply(0)));
        }

    }

    @ModifyVariable(
            at = @At(
                    value = "CONSTANT",
                    args = {"floatValue=0.0F"},
                    ordinal = 0
            ),
            ordinal = 0,
            method = {"actuallyHurt"},
            argsOnly = true
    )
    public float chestCavityLivingEntityOnHitMixin(float amount, DamageSource source) {
        if (source.getEntity() instanceof LivingEntity) {
            Optional<ChestCavityEntity> cce = ChestCavityEntity.of(source.getEntity());
            if (cce.isPresent()) {
                amount = ChestCavityUtil.onHit(cce.get().getChestCavityInstance(), source, (LivingEntity)(Object)this, amount);
            }
        }
        return amount;
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"decreaseAirSupply"},
            cancellable = true
    )
    protected void chestCavityLivingEntityGetNextAirUnderwaterMixin(int air, CallbackInfoReturnable<Integer> info) {
        info.setReturnValue(ChestCavityUtil.applyBreathInWater(this.chestCavityInstance, air, info.getReturnValueI()));
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"getDamageAfterArmorAbsorb"},
            cancellable = true
    )
    public void chestCavityLivingEntityDamageMixin(DamageSource source, float amount, CallbackInfoReturnable<Float> info) {
        info.setReturnValue(ChestCavityUtil.applyDefenses(this.chestCavityInstance, source, info.getReturnValueF()));
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"dropEquipment"}
    )
    public void chestCavityLivingEntityDropInventoryMixin(CallbackInfo info) {
        ChestCavityUtil.onDeath(this);
    }

    @ModifyVariable(
            at = @At("HEAD"),
            method = {"addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"},
            ordinal = 0,
            argsOnly = true
    )
    public MobEffectInstance chestCavityLivingEntityAddStatusEffectMixin(MobEffectInstance effect) {
        return ChestCavityUtil.onAddStatusEffect(this.chestCavityInstance, effect);
    }

    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/food/FoodProperties;getEffects()Ljava/util/List;"
            ),
            method = {"addEatEffect"}
    )
    public List<Pair<MobEffectInstance, Float>> chestCavityLivingEntityApplyFoodEffectsMixin(FoodProperties instance, ItemStack stack, Level world, LivingEntity targetEntity) {
        List<Pair<MobEffectInstance, Float>> list = instance.getEffects();
        Optional<ChestCavityEntity> option = ChestCavityEntity.of(targetEntity);
        if (option.isPresent()) {
            list = new LinkedList(list);
            OrganFoodEffectListeners.call(list, stack, world, targetEntity, option.get().getChestCavityInstance());
        }

        return list;
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
        return g * ChestCavityUtil.applySwimSpeedInWater(this.chestCavityInstance);
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"getJumpPower"},
            cancellable = true
    )
    public void chestCavityLivingEntityJumpVelocityMixin(CallbackInfoReturnable<Float> info) {
        info.setReturnValue(ChestCavityUtil.applyLeaping(this.chestCavityInstance, info.getReturnValueF()));
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
        this.chestCavityInstance.fromTag(tag, (LivingEntity)(Object)this);
        this.entityData.set(DATA_INVENTORY_TYPE, this.chestCavityInstance.getInventoryType().toString());
    }

    @Inject(
            method = {"addAdditionalSaveData"},
            at = {@At("TAIL")}
    )
    private void writeCustomDataToNbt(CompoundTag tag, CallbackInfo callbackInfo) {
        this.chestCavityInstance.toTag(tag, (LivingEntity)(Object)this);
    }

    @Mixin({net.minecraft.world.entity.Mob.class})
    private abstract static class Mob extends LivingEntity {
        protected Mob(EntityType<? extends LivingEntity> entityType, Level world) {
            super(entityType, world);
        }

        @Inject(
                at = {@At("HEAD")},
                method = {"checkAndHandleImportantInteractions"},
                cancellable = true
        )
        protected void chestCavityLivingEntityInteractMobMixin(net.minecraft.world.entity.player.Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
            if (player.getItemInHand(hand).getItem() == CCItems.CHEST_OPENER.get()) {
                ((ChestOpener)player.getItemInHand(hand).getItem()).openChestCavity(player, this);
                info.setReturnValue(InteractionResult.SUCCESS);
            }

        }
    }

    @Mixin({net.minecraft.world.entity.player.Player.class})
    public abstract static class Player extends LivingEntity {
        protected Player(EntityType<? extends LivingEntity> entityType, Level world) {
            super(entityType, world);
        }

        @ModifyVariable(
                at = @At(
                        value = "CONSTANT",
                        args = {"floatValue=0.0F"},
                        ordinal = 0
                ),
                ordinal = 0,
                method = {"actuallyHurt"},
                argsOnly = true
        )
        public float chestCavityPlayerEntityOnHitMixin(float amount, DamageSource source) {
            if (source.getEntity() instanceof LivingEntity) {
                Optional<ChestCavityEntity> cce = ChestCavityEntity.of(source.getEntity());
                if (cce.isPresent()) {
                    amount = ChestCavityUtil.onHit(cce.get().getChestCavityInstance(), source, this, amount);
                }
            }

            return amount;
        }

        @Inject(
                at = {@At("HEAD")},
                method = {"interactOn"},
                cancellable = true
        )
        void chestCavityPlayerEntityInteractPlayerMixin(Entity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
            if (entity instanceof LivingEntity && ChestCavity.config.CAN_OPEN_OTHER_PLAYERS) {
                net.minecraft.world.entity.player.Player player = (net.minecraft.world.entity.player.Player)(Object)this;
                ItemStack stack = player.getItemInHand(hand);
                if (stack.getItem() == CCItems.CHEST_OPENER.get()) {
                    ((ChestOpener)stack.getItem()).openChestCavity(player, (LivingEntity)entity);
                    info.setReturnValue(InteractionResult.SUCCESS);
                    info.cancel();
                }
            }

        }

        @Inject(
                at = {@At("RETURN")},
                method = {"getDestroySpeed"},
                cancellable = true
        )
        void chestCavityPlayerEntityGetBlockBreakingSpeedMixin(BlockState block, CallbackInfoReturnable<Float> cir) {
            cir.setReturnValue(ChestCavityUtil.applyNervesToMining(((ChestCavityEntity)this).getChestCavityInstance(), (Float)cir.getReturnValue()));
        }
    }

    @Mixin({net.minecraft.world.entity.animal.Cow.class})
    private abstract static class Cow extends Animal {
        protected Cow(EntityType<? extends Animal> entityType, Level world) {
            super(entityType, world);
        }

        @Inject(
                method = {"mobInteract"},
                at = {@At(
                        value = "RETURN",
                        ordinal = 0
                )}
        )
        protected void interactMob(net.minecraft.world.entity.player.Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
            OrganUtil.milkSilk(this);
        }
    }

    @Mixin({net.minecraft.world.entity.monster.Creeper.class})
    private abstract static class Creeper extends Monster {
        @Shadow
        private int swell;

        protected Creeper(EntityType<? extends Monster> entityType, Level world) {
            super(entityType, world);
        }

        @Inject(
                at = {@At("HEAD")},
                method = {"tick"}
        )
        protected void chestCavityCreeperTickMixin(CallbackInfo info) {
            if (this.isAlive() && this.swell > 1) {
                ChestCavityEntity.of(this).ifPresent((cce) -> {
                    if (cce.getChestCavityInstance().opened && cce.getChestCavityInstance().getOrganScore(CCOrganScores.CREEPY) <= 0.0F) {
                        this.swell = 1;
                    }

                });
            }

        }
    }

    @Mixin({ServerPlayer.class})
    private abstract static class Server extends net.minecraft.world.entity.player.Player {
        public Server(Level world, BlockPos pos, float yaw, GameProfile profile) {
            super(world, pos, yaw, profile);
        }

        @Inject(
                method = {"restoreFrom"},
                at = {@At("TAIL")}
        )
        public void copyFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo callbackInfo) {
            ChestCavityEntity.of(this).ifPresent((chestCavityEntity) -> {
                ChestCavityEntity.of(oldPlayer).ifPresent((oldCCPlayerEntityInterface) -> {
                    chestCavityEntity.getChestCavityInstance().clone(oldCCPlayerEntityInterface.getChestCavityInstance());
                });
            });
        }

        @Inject(
                at = {@At("RETURN")},
                method = {"changeDimension"},
                remap = false
        )
        public void chestCavityEntityMoveToWorldMixin(ServerLevel destination, ITeleporter teleporter, CallbackInfoReturnable<Entity> info) {
            Entity entity = info.getReturnValue();
            if (entity instanceof ChestCavityEntity && !entity.level().isClientSide) {
                NetworkUtil.SendS2CChestCavityUpdatePacket(((ChestCavityEntity)entity).getChestCavityInstance());
            }

        }
    }

    @Mixin({net.minecraft.world.entity.animal.Sheep.class})
    private abstract static class Sheep extends Animal {
        protected Sheep(EntityType<? extends Animal> entityType, Level world) {
            super(entityType, world);
        }

        @Inject(
                method = {"shear"},
                at = {@At("HEAD")}
        )
        protected void chestCavitySheared(SoundSource shearedSoundCategory, CallbackInfo info) {
            OrganUtil.shearSilk(this);
        }
    }

    @Mixin({WitherBoss.class})
    private abstract static class Wither extends Monster {
        protected Wither(EntityType<? extends Monster> entityType, Level world) {
            super(entityType, world);
        }

        @Inject(
                method = {"dropCustomDeathLoot"},
                at = {@At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/world/entity/boss/wither/WitherBoss;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
                )},
                cancellable = true
        )
        protected void chestCavityPreventNetherStarDrop(DamageSource source, int lootingMultiplier, boolean allowDrops, CallbackInfo info) {
            Optional<ChestCavityEntity> chestCavityEntity = ChestCavityEntity.of(this);
            if (chestCavityEntity.isPresent()) {
                ChestCavityInstance cc = chestCavityEntity.get().getChestCavityInstance();
                if (cc.opened && cc.inventory.countItem(Items.NETHER_STAR) == 0) {
                    info.cancel();
                }
            }

        }
    }
}
