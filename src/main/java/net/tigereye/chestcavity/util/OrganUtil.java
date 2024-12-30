package net.tigereye.chestcavity.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.interfaces.CCStatusEffectInstance;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCStatusEffects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrganUtil {
    public OrganUtil() {
    }

    public static void displayOrganQuality(Map<ResourceLocation, Float> organQualityMap, List<Component> tooltip) {
        organQualityMap.forEach((organ, score) -> {
            String tier;
            if (organ.equals(CCOrganScores.HYDROALLERGENIC)) {
                if (score >= 2.0F) {
                    tier = "quality.chestcavity.severely";
                } else {
                    tier = "";
                }
            } else if (score >= 1.5F) {
                tier = "quality.chestcavity.supernatural";
            } else if ((double)score >= 1.25) {
                tier = "quality.chestcavity.exceptional";
            } else if (score >= 1.0F) {
                tier = "quality.chestcavity.good";
            } else if (score >= 0.75F) {
                tier = "quality.chestcavity.average";
            } else if (score >= 0.5F) {
                tier = "quality.chestcavity.poor";
            } else if (score >= 0.0F) {
                tier = "quality.chestcavity.pathetic";
            } else if (score >= -0.25F) {
                tier = "quality.chestcavity.slightly_reduces";
            } else if (score >= -0.5F) {
                tier = "quality.chestcavity.reduces";
            } else if (score >= -0.75F) {
                tier = "quality.chestcavity.greatly_reduces";
            } else {
                tier = "quality.chestcavity.greatly_reduces";
            }

            Component text = Component.translatable("organscore." + organ.getNamespace() + "." + organ.getPath(), new Object[]{Component.translatable(tier)});
            tooltip.add(text);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void displayCompatibility(ItemStack itemStack, Level world, List<Component> tooltip, TooltipFlag tooltipContext) {
        CompoundTag tag = itemStack.getOrCreateTag();
        boolean isCompat = false;
        MinecraftServer server = null;
        if (world != null) {
            server = world.getServer();
        }

        if (server == null) {
            server = Minecraft.getInstance().getSingleplayerServer();
        }

        if (server != null) {
            Player serverPlayer = server.getPlayerList().getPlayer(Minecraft.getInstance().player.getUUID());
            if (serverPlayer instanceof ChestCavityEntity ccPlayer) {
                isCompat = ChestCavityUtil.getCompatibility(ccPlayer.getChestCavityInstance(), itemStack);
            }
        }

        String textString;
         if (tag.contains(ChestCavity.COMPATIBILITY_TAG.toString())) {
            tag = tag.getCompound(ChestCavity.COMPATIBILITY_TAG.toString());
            String name = tag.getString("name");
            textString = "Only Compatible With: " + name;
        } else {
            textString = "Safe to Use";
        }

        MutableComponent text = MutableComponent.create(ComponentContents.EMPTY);
        if (isCompat) {
            text.withStyle(ChatFormatting.GREEN);
        } else {
            text.withStyle(ChatFormatting.RED);
        }

        text.append(textString);
        tooltip.add(text);
    }

    public static void explode(LivingEntity entity, float explosionYield) {
        if (!entity.level().isClientSide) {
            entity.level().explode(null, entity.getX(), entity.getY(), entity.getZ(), (float)Math.sqrt(explosionYield), ExplosionInteraction.MOB);
            spawnEffectsCloud(entity);
        }

    }

    public static List<MobEffectInstance> getStatusEffects(ItemStack organ) {
        CompoundTag tag = organ.getOrCreateTag();
        if (!tag.contains("CustomPotionEffects", 9)) {
            return new ArrayList();
        } else {
            ListTag NbtList = tag.getList("CustomPotionEffects", 10);
            List<MobEffectInstance> list = new ArrayList();

            for(int i = 0; i < NbtList.size(); ++i) {
                CompoundTag NbtCompound = NbtList.getCompound(i);
                MobEffectInstance statusEffectInstance = MobEffectInstance.load(NbtCompound);
                if (statusEffectInstance != null) {
                    list.add(statusEffectInstance);
                }
            }

            return list;
        }
    }

    public static void milkSilk(LivingEntity entity) {
        if (!entity.hasEffect(CCStatusEffects.SILK_COOLDOWN.get())) {
            ChestCavityEntity.of(entity).ifPresent((cce) -> {
                if (cce.getChestCavityInstance().opened) {
                    ChestCavityInstance cc = cce.getChestCavityInstance();
                    float silk = cc.getOrganScore(CCOrganScores.SILK);
                    if (silk > 0.0F && spinWeb(entity, cc, silk)) {
                        entity.addEffect(new MobEffectInstance(CCStatusEffects.SILK_COOLDOWN.get(), ChestCavity.config.SILK_COOLDOWN, 0, false, false, true));
                    }
                }

            });
        }

    }

    public static void queueDragonBombs(LivingEntity entity, ChestCavityInstance cc, int bombs) {
        if (entity instanceof Player) {
            ((Player)entity).causeFoodExhaustion((float)bombs * 0.6F);
        }

        for(int i = 0; i < bombs; ++i) {
            cc.projectileQueue.add(OrganUtil::spawnDragonBomb);
        }

        entity.addEffect(new MobEffectInstance(CCStatusEffects.DRAGON_BOMB_COOLDOWN.get(), ChestCavity.config.DRAGON_BOMB_COOLDOWN, 0, false, false, true));
    }

    public static void queueForcefulSpit(LivingEntity entity, ChestCavityInstance cc, int projectiles) {
        if (entity instanceof Player) {
            ((Player)entity).causeFoodExhaustion((float)projectiles * 0.1F);
        }

        for(int i = 0; i < projectiles; ++i) {
            cc.projectileQueue.add(OrganUtil::spawnSpit);
        }

        entity.addEffect(new MobEffectInstance(CCStatusEffects.FORCEFUL_SPIT_COOLDOWN.get(), ChestCavity.config.FORCEFUL_SPIT_COOLDOWN, 0, false, false, true));
    }

    public static void queueGhastlyFireballs(LivingEntity entity, ChestCavityInstance cc, int ghastly) {
        if (entity instanceof Player) {
            ((Player)entity).causeFoodExhaustion((float)ghastly * 0.3F);
        }

        for(int i = 0; i < ghastly; ++i) {
            cc.projectileQueue.add(OrganUtil::spawnGhastlyFireball);
        }

        entity.addEffect(new MobEffectInstance(CCStatusEffects.GHASTLY_COOLDOWN.get(), ChestCavity.config.GHASTLY_COOLDOWN, 0, false, false, true));
    }

    public static void queuePyromancyFireballs(LivingEntity entity, ChestCavityInstance cc, int pyromancy) {
        if (entity instanceof Player) {
            ((Player)entity).causeFoodExhaustion((float)pyromancy * 0.1F);
        }

        for(int i = 0; i < pyromancy; ++i) {
            cc.projectileQueue.add(OrganUtil::spawnPyromancyFireball);
        }

        entity.addEffect(new MobEffectInstance((MobEffect)CCStatusEffects.PYROMANCY_COOLDOWN.get(), ChestCavity.config.PYROMANCY_COOLDOWN, 0, false, false, true));
    }

    public static void queueShulkerBullets(LivingEntity entity, ChestCavityInstance cc, int shulkerBullets) {
        if (entity instanceof Player) {
            ((Player)entity).causeFoodExhaustion((float)shulkerBullets * 0.3F);
        }

        for(int i = 0; i < shulkerBullets; ++i) {
            cc.projectileQueue.add(OrganUtil::spawnShulkerBullet);
        }

        entity.addEffect(new MobEffectInstance(CCStatusEffects.SHULKER_BULLET_COOLDOWN.get(), ChestCavity.config.SHULKER_BULLET_COOLDOWN, 0, false, false, true));
    }

    public static void setStatusEffects(ItemStack organ, ItemStack potion) {
        List<MobEffectInstance> potionList = PotionUtils.getCustomEffects(potion);
        List<MobEffectInstance> list = new ArrayList();

        for (MobEffectInstance effect : potionList) {
            MobEffectInstance effectCopy = new MobEffectInstance(effect);
            ((CCStatusEffectInstance) effectCopy).CC_setDuration(Math.max(1, effectCopy.getDuration() / 4));
            list.add(effectCopy);
        }

        setStatusEffects(organ, list);
    }

    public static void setStatusEffects(ItemStack organ, List<MobEffectInstance> list) {
        CompoundTag tag = organ.getOrCreateTag();
        ListTag NbtList = new ListTag();

        for (MobEffectInstance effect : list) {
            if (effect != null) {
                CompoundTag NbtCompound = new CompoundTag();
                NbtList.add(effect.save(NbtCompound));
            }
        }

        tag.put("CustomPotionEffects", NbtList);
    }

    public static void shearSilk(LivingEntity entity) {
        ChestCavityEntity.of(entity).ifPresent((cce) -> {
            if (cce.getChestCavityInstance().opened) {
                float silk = cce.getChestCavityInstance().getOrganScore(CCOrganScores.SILK);
                if (silk > 0.0F) {
                    ItemStack stack;
                    ItemEntity itemEntity;
                    if (silk >= 2.0F) {
                        stack = new ItemStack(Items.COBWEB, (int)silk / 2);
                        itemEntity = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), stack);
                        entity.level().addFreshEntity(itemEntity);
                    }

                    if (silk % 2.0F >= 1.0F) {
                        stack = new ItemStack(Items.STRING);
                        itemEntity = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), stack);
                        entity.level().addFreshEntity(itemEntity);
                    }
                }
            }

        });
    }

    public static void spawnEffectsCloud(LivingEntity entity) {
        Collection<MobEffectInstance> collection = entity.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaEffectCloudEntity = new AreaEffectCloud(entity.level(), entity.getX(), entity.getY(), entity.getZ());
            areaEffectCloudEntity.setRadius(2.5F);
            areaEffectCloudEntity.setRadiusOnUse(-0.5F);
            areaEffectCloudEntity.setWaitTime(10);
            areaEffectCloudEntity.setDuration(areaEffectCloudEntity.getDuration() / 2);
            areaEffectCloudEntity.setRadiusPerTick(-areaEffectCloudEntity.getRadius() / (float)areaEffectCloudEntity.getDuration());

            for (MobEffectInstance statusEffectInstance : collection) {
                areaEffectCloudEntity.addEffect(new MobEffectInstance(statusEffectInstance));
            }

            entity.level().addFreshEntity(areaEffectCloudEntity);
        }

    }

    public static void spawnSilk(LivingEntity entity) {
        entity.spawnAtLocation(Items.STRING);
    }

    public static void spawnSpit(LivingEntity entity) {
        Vec3 entityFacing = entity.getLookAngle().normalize();
        Llama fakeLlama = new Llama(EntityType.LLAMA, entity.level());
        fakeLlama.setPos(entity.getX(), entity.getY(), entity.getZ());
        fakeLlama.setDeltaMovement(entity.getDeltaMovement());
        fakeLlama.setYHeadRot(entity.getYHeadRot());
        fakeLlama.yBodyRot = entity.yBodyRot;
        LlamaSpit llamaSpitEntity = new LlamaSpit(entity.level(), fakeLlama);
        llamaSpitEntity.setOwner(entity);
        llamaSpitEntity.absMoveTo(entityFacing.x * 2.0, entityFacing.y * 2.0, entityFacing.z * 2.0);
        entity.level().addFreshEntity(llamaSpitEntity);
        entityFacing = entityFacing.scale(-0.1);
        entity.absMoveTo(entityFacing.x, entityFacing.y, entityFacing.z);
    }

    public static void spawnDragonBomb(LivingEntity entity) {
        Vec3 entityFacing = entity.getLookAngle().normalize();
        DragonFireball fireballEntity = new DragonFireball(entity.level(), entity, entityFacing.x, entityFacing.y, entityFacing.z);
        fireballEntity.syncPacketPositionCodec(fireballEntity.getX(), entity.getY(0.5) + 0.3, fireballEntity.getZ());
        entity.level().addFreshEntity(fireballEntity);
        entityFacing = entityFacing.scale(-0.2);
        entity.absMoveTo(entityFacing.x, entityFacing.y, entityFacing.z);
    }

    public static void spawnDragonBreath(LivingEntity entity) {
        Optional<ChestCavityEntity> optional = ChestCavityEntity.of(entity);
        if (optional.isPresent()) {
            ChestCavityEntity cce = optional.get();
            ChestCavityInstance cc = cce.getChestCavityInstance();
            float breath = cc.getOrganScore(CCOrganScores.DRAGON_BREATH);
            double range = Math.sqrt(breath / 2.0F) * 5.0;
            HitResult result = entity.pick(range, 0.0F, false);
            Vec3 pos = result.getLocation();
            double x = pos.x;
            double y = pos.y;
            double z = pos.z;
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(x, y, z);

            while(entity.level().isEmptyBlock(mutable)) {
                --y;
                if (y < 0.0) {
                    return;
                }

                mutable.set(x, y, z);
            }

            y = Mth.floor(y) + 1;
            AreaEffectCloud breathEntity = new AreaEffectCloud(entity.level(), x, y, z);
            breathEntity.setOwner(entity);
            breathEntity.setRadius((float)Math.max(range / 2.0, Math.min(range, MathUtil.horizontalDistanceTo(breathEntity, entity))));
            breathEntity.setDuration(200);
            breathEntity.setParticle(ParticleTypes.DRAGON_BREATH);
            breathEntity.addEffect(new MobEffectInstance(MobEffects.HARM));
            entity.level().addFreshEntity(breathEntity);
        }

    }

    public static void spawnGhastlyFireball(LivingEntity entity) {
        Vec3 entityFacing = entity.getLookAngle().normalize();
        LargeFireball fireballEntity = new LargeFireball(entity.level(), entity, entityFacing.x, entityFacing.y, entityFacing.z, 1);
        fireballEntity.syncPacketPositionCodec(fireballEntity.getX(), entity.getY(0.5) + 0.3, fireballEntity.getZ());
        entity.level().addFreshEntity(fireballEntity);
        entityFacing = entityFacing.scale(-0.8);
        entity.absMoveTo(entityFacing.x, entityFacing.y, entityFacing.z);
    }

    public static void spawnPyromancyFireball(LivingEntity entity) {
        Vec3 entityFacing = entity.getLookAngle().normalize();
        SmallFireball smallFireballEntity = new SmallFireball(entity.level(), entity, entityFacing.x + entity.getRandom().nextGaussian() * 0.1, entityFacing.y, entityFacing.z + entity.getRandom().nextGaussian() * 0.1);
        smallFireballEntity.syncPacketPositionCodec(smallFireballEntity.getX(), entity.getY(0.5) + 0.3, smallFireballEntity.getZ());
        entity.level().addFreshEntity(smallFireballEntity);
        entityFacing = entityFacing.scale(-0.2);
        entity.absMoveTo(entityFacing.x, entityFacing.y, entityFacing.z);
    }

    public static void spawnShulkerBullet(LivingEntity entity) {
        TargetingConditions targetPredicate = TargetingConditions.forCombat();
        targetPredicate.range(ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE * 2);
        LivingEntity target = entity.level().getNearestEntity(LivingEntity.class, targetPredicate, entity, entity.getX(), entity.getY(), entity.getZ(), new AABB(entity.getX() - (double)ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE, entity.getY() - (double)ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE, entity.getZ() - (double)ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE, entity.getX() + (double)ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE, entity.getY() + (double)ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE, entity.getZ() + (double)ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE));
        if (target != null) {
            ShulkerBullet shulkerBulletEntity = new ShulkerBullet(entity.level(), entity, target, Axis.Y);
            shulkerBulletEntity.syncPacketPositionCodec(shulkerBulletEntity.getX(), entity.getY(0.5) + 0.3, shulkerBulletEntity.getZ());
            entity.level().addFreshEntity(shulkerBulletEntity);
        }

    }

    public static boolean spinWeb(LivingEntity entity, ChestCavityInstance cc, float silkScore) {
        int hungerCost = 0;
        Player player = null;
        if (entity instanceof Player) {
            player = (Player) entity;
            if (player.getFoodData().getFoodLevel() < 6) {
                return false;
            }
        }

        if (silkScore >= 2.0F) {
            // todo check
            BlockPos pos = entity.blockPosition().offset(entity.getDirection().getNormal());
            if (entity.level().getBlockState(pos).isAir()) {
                if (silkScore >= 3.0F) {
                    hungerCost = 16;
                    silkScore -= 3.0F;
                    entity.level().setBlock(pos, Blocks.WHITE_WOOL.defaultBlockState(), 2);
                } else {
                    hungerCost = 8;
                    silkScore -= 2.0F;
                    entity.level().setBlock(pos, Blocks.COBWEB.defaultBlockState(), 2);
                }
            }
        }

        while(silkScore >= 1.0F) {
            --silkScore;
            hungerCost += 4;
            cc.projectileQueue.add(OrganUtil::spawnSilk);
        }

        if (player != null) {
            player.getFoodData().addExhaustion((float)hungerCost);
        }

        return hungerCost > 0;
    }

    public static boolean teleportRandomly(LivingEntity entity, float range) {
        if (!entity.level().isClientSide() && entity.isAlive()) {
            for(int i = 0; i < ChestCavity.config.MAX_TELEPORT_ATTEMPTS; ++i) {
                double d = entity.getX() + (entity.getRandom().nextDouble() - 0.5) * (double)range;
                double e = Math.max(1.0, entity.getY() + (entity.getRandom().nextDouble() - 0.5) * (double)range);
                double f = entity.getZ() + (entity.getRandom().nextDouble() - 0.5) * (double)range;
                if (teleportTo(entity, d, e, f)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean teleportTo(LivingEntity entity, double x, double y, double z) {
        if (entity.isVehicle()) {
            entity.stopRiding();
        }

        BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos(x, y, z);

        BlockState blockState;
        for(blockState = entity.level().getBlockState(targetPos); targetPos.getY() > 0 && !blockState.blocksMotion() && !blockState.liquid(); blockState = entity.level().getBlockState(targetPos)) {
            targetPos.move(Direction.DOWN);
        }

        if (targetPos.getY() <= 0) {
            return false;
        } else {
            targetPos.move(Direction.UP);
            blockState = entity.level().getBlockState(targetPos);

            for(BlockState blockState2 = entity.level().getBlockState(targetPos.above()); blockState.liquid() || blockState.blocksMotion() || blockState2.liquid() || blockState2.blocksMotion(); blockState2 = entity.level().getBlockState(targetPos.above())) {
                targetPos.move(Direction.UP);
                blockState = entity.level().getBlockState(targetPos);
            }

            if (entity.level().dimensionType().hasCeiling() && targetPos.getY() >= entity.level().getHeight()) {
                return false;
            } else {
                entity.teleportTo(x, (double)targetPos.getY() + 0.1, z);
                if (!entity.isSilent()) {
                    entity.level().playSound((Player)null, entity.xOld, entity.yOld, entity.zOld, SoundEvents.ENDERMAN_TELEPORT, entity.getSoundSource(), 1.0F, 1.0F);
                    entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                }

                return true;
            }
        }
    }
}
