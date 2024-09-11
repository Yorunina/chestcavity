package net.tigereye.chestcavity.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
import net.tigereye.chestcavity.registration.CCEnchantments;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.registration.CCStatusEffects;

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

            Component text = Component.m_237110_("organscore." + organ.m_135827_() + "." + organ.m_135815_(), new Object[]{Component.m_237115_(tier)});
            tooltip.add(text);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void displayCompatibility(ItemStack itemStack, Level world, List<Component> tooltip, TooltipFlag tooltipContext) {
        CompoundTag tag = itemStack.m_41784_();
        boolean uuidMatch = false;
        int compatLevel = 0;
        MinecraftServer server = null;
        if (world != null) {
            server = world.m_7654_();
        }

        if (server == null) {
            server = Minecraft.m_91087_().m_91092_();
        }

        if (server != null) {
            Player serverPlayer = ((MinecraftServer)server).m_6846_().m_11255_(Minecraft.m_91087_().f_91074_.m_6302_());
            if (serverPlayer instanceof ChestCavityEntity) {
                ChestCavityEntity ccPlayer = (ChestCavityEntity)serverPlayer;
                UUID ccID = ccPlayer.getChestCavityInstance().compatibility_id;
                compatLevel = ChestCavityUtil.getCompatibilityLevel(ccPlayer.getChestCavityInstance(), itemStack);
            }
        } else {
            compatLevel = -1;
        }

        String textString;
        if (EnchantmentHelper.m_44843_((Enchantment)CCEnchantments.MALPRACTICE.get(), itemStack) > 0) {
            textString = "Unsafe to use";
        } else if (tag != null && tag.m_128441_(ChestCavity.COMPATIBILITY_TAG.toString()) && EnchantmentHelper.m_44843_((Enchantment)CCEnchantments.O_NEGATIVE.get(), itemStack) <= 0) {
            tag = tag.m_128469_(ChestCavity.COMPATIBILITY_TAG.toString());
            String name = tag.m_128461_("name");
            textString = "Only Compatible With: " + name;
        } else {
            textString = "Safe to Use";
        }

        MutableComponent text = MutableComponent.m_237204_(ComponentContents.f_237124_);
        if (compatLevel > 0) {
            text.m_130940_(ChatFormatting.GREEN);
        } else if (compatLevel == 0) {
            text.m_130940_(ChatFormatting.RED);
        } else {
            text.m_130940_(ChatFormatting.YELLOW);
        }

        text.m_130946_(textString);
        tooltip.add(text);
    }

    public static void explode(LivingEntity entity, float explosionYield) {
        if (!entity.m_9236_().f_46443_) {
            entity.m_9236_().m_254849_((Entity)null, entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), (float)Math.sqrt((double)explosionYield), ExplosionInteraction.MOB);
            spawnEffectsCloud(entity);
        }

    }

    public static List<MobEffectInstance> getStatusEffects(ItemStack organ) {
        CompoundTag tag = organ.m_41784_();
        if (!tag.m_128425_("CustomPotionEffects", 9)) {
            return new ArrayList();
        } else {
            ListTag NbtList = tag.m_128437_("CustomPotionEffects", 10);
            List<MobEffectInstance> list = new ArrayList();

            for(int i = 0; i < NbtList.size(); ++i) {
                CompoundTag NbtCompound = NbtList.m_128728_(i);
                MobEffectInstance statusEffectInstance = MobEffectInstance.m_19560_(NbtCompound);
                if (statusEffectInstance != null) {
                    list.add(statusEffectInstance);
                }
            }

            return list;
        }
    }

    public static void milkSilk(LivingEntity entity) {
        if (!entity.m_21023_((MobEffect)CCStatusEffects.SILK_COOLDOWN.get())) {
            ChestCavityEntity.of(entity).ifPresent((cce) -> {
                if (cce.getChestCavityInstance().opened) {
                    ChestCavityInstance cc = cce.getChestCavityInstance();
                    float silk = cc.getOrganScore(CCOrganScores.SILK);
                    if (silk > 0.0F && spinWeb(entity, cc, silk)) {
                        entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.SILK_COOLDOWN.get(), ChestCavity.config.SILK_COOLDOWN, 0, false, false, true));
                    }
                }

            });
        }

    }

    public static void queueDragonBombs(LivingEntity entity, ChestCavityInstance cc, int bombs) {
        if (entity instanceof Player) {
            ((Player)entity).m_36399_((float)bombs * 0.6F);
        }

        for(int i = 0; i < bombs; ++i) {
            cc.projectileQueue.add(OrganUtil::spawnDragonBomb);
        }

        entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.DRAGON_BOMB_COOLDOWN.get(), ChestCavity.config.DRAGON_BOMB_COOLDOWN, 0, false, false, true));
    }

    public static void queueForcefulSpit(LivingEntity entity, ChestCavityInstance cc, int projectiles) {
        if (entity instanceof Player) {
            ((Player)entity).m_36399_((float)projectiles * 0.1F);
        }

        for(int i = 0; i < projectiles; ++i) {
            cc.projectileQueue.add(OrganUtil::spawnSpit);
        }

        entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.FORCEFUL_SPIT_COOLDOWN.get(), ChestCavity.config.FORCEFUL_SPIT_COOLDOWN, 0, false, false, true));
    }

    public static void queueGhastlyFireballs(LivingEntity entity, ChestCavityInstance cc, int ghastly) {
        if (entity instanceof Player) {
            ((Player)entity).m_36399_((float)ghastly * 0.3F);
        }

        for(int i = 0; i < ghastly; ++i) {
            cc.projectileQueue.add(OrganUtil::spawnGhastlyFireball);
        }

        entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.GHASTLY_COOLDOWN.get(), ChestCavity.config.GHASTLY_COOLDOWN, 0, false, false, true));
    }

    public static void queuePyromancyFireballs(LivingEntity entity, ChestCavityInstance cc, int pyromancy) {
        if (entity instanceof Player) {
            ((Player)entity).m_36399_((float)pyromancy * 0.1F);
        }

        for(int i = 0; i < pyromancy; ++i) {
            cc.projectileQueue.add(OrganUtil::spawnPyromancyFireball);
        }

        entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.PYROMANCY_COOLDOWN.get(), ChestCavity.config.PYROMANCY_COOLDOWN, 0, false, false, true));
    }

    public static void queueShulkerBullets(LivingEntity entity, ChestCavityInstance cc, int shulkerBullets) {
        if (entity instanceof Player) {
            ((Player)entity).m_36399_((float)shulkerBullets * 0.3F);
        }

        for(int i = 0; i < shulkerBullets; ++i) {
            cc.projectileQueue.add(OrganUtil::spawnShulkerBullet);
        }

        entity.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.SHULKER_BULLET_COOLDOWN.get(), ChestCavity.config.SHULKER_BULLET_COOLDOWN, 0, false, false, true));
    }

    public static void setStatusEffects(ItemStack organ, ItemStack potion) {
        List<MobEffectInstance> potionList = PotionUtils.m_43547_(potion);
        List<MobEffectInstance> list = new ArrayList();
        Iterator<MobEffectInstance> var4 = potionList.iterator();

        while(var4.hasNext()) {
            MobEffectInstance effect = (MobEffectInstance)var4.next();
            MobEffectInstance effectCopy = new MobEffectInstance(effect);
            ((CCStatusEffectInstance)effectCopy).CC_setDuration(Math.max(1, effectCopy.m_19557_() / 4));
            list.add(effectCopy);
        }

        setStatusEffects(organ, (List)list);
    }

    public static void setStatusEffects(ItemStack organ, List<MobEffectInstance> list) {
        CompoundTag tag = organ.m_41784_();
        ListTag NbtList = new ListTag();

        for(int i = 0; i < list.size(); ++i) {
            MobEffectInstance effect = (MobEffectInstance)list.get(i);
            if (effect != null) {
                CompoundTag NbtCompound = new CompoundTag();
                NbtList.add(effect.m_19555_(NbtCompound));
            }
        }

        tag.m_128365_("CustomPotionEffects", NbtList);
    }

    public static void shearSilk(LivingEntity entity) {
        ChestCavityEntity.of(entity).ifPresent((cce) -> {
            if (cce.getChestCavityInstance().opened) {
                float silk = cce.getChestCavityInstance().getOrganScore(CCOrganScores.SILK);
                if (silk > 0.0F) {
                    ItemStack stack;
                    ItemEntity itemEntity;
                    if (silk >= 2.0F) {
                        stack = new ItemStack(Items.f_41863_, (int)silk / 2);
                        itemEntity = new ItemEntity(entity.m_9236_(), entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), stack);
                        entity.m_9236_().m_7967_(itemEntity);
                    }

                    if (silk % 2.0F >= 1.0F) {
                        stack = new ItemStack(Items.f_42401_);
                        itemEntity = new ItemEntity(entity.m_9236_(), entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), stack);
                        entity.m_9236_().m_7967_(itemEntity);
                    }
                }
            }

        });
    }

    public static void spawnEffectsCloud(LivingEntity entity) {
        Collection<MobEffectInstance> collection = entity.m_21220_();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaEffectCloudEntity = new AreaEffectCloud(entity.m_9236_(), entity.m_20185_(), entity.m_20186_(), entity.m_20189_());
            areaEffectCloudEntity.m_19712_(2.5F);
            areaEffectCloudEntity.m_19732_(-0.5F);
            areaEffectCloudEntity.m_19740_(10);
            areaEffectCloudEntity.m_19734_(areaEffectCloudEntity.m_19748_() / 2);
            areaEffectCloudEntity.m_19738_(-areaEffectCloudEntity.m_19743_() / (float)areaEffectCloudEntity.m_19748_());
            Iterator<MobEffectInstance> var3 = collection.iterator();

            while(var3.hasNext()) {
                MobEffectInstance statusEffectInstance = (MobEffectInstance)var3.next();
                areaEffectCloudEntity.m_19716_(new MobEffectInstance(statusEffectInstance));
            }

            entity.m_9236_().m_7967_(areaEffectCloudEntity);
        }

    }

    public static void spawnSilk(LivingEntity entity) {
        entity.m_19998_(Items.f_42401_);
    }

    public static void spawnSpit(LivingEntity entity) {
        Vec3 entityFacing = entity.m_20154_().m_82541_();
        Llama fakeLlama = new Llama(EntityType.f_20466_, entity.m_9236_());
        fakeLlama.m_20343_(entity.m_20185_(), entity.m_20186_(), entity.m_20189_());
        fakeLlama.m_146926_(entity.m_146909_());
        fakeLlama.m_146922_(entity.m_146908_());
        fakeLlama.f_20883_ = entity.f_20883_;
        LlamaSpit llamaSpitEntity = new LlamaSpit(entity.m_9236_(), fakeLlama);
        llamaSpitEntity.m_5602_(entity);
        llamaSpitEntity.m_20334_(entityFacing.f_82479_ * 2.0, entityFacing.f_82480_ * 2.0, entityFacing.f_82481_ * 2.0);
        entity.m_9236_().m_7967_(llamaSpitEntity);
        entityFacing = entityFacing.m_82490_(-0.1);
        entity.m_5997_(entityFacing.f_82479_, entityFacing.f_82480_, entityFacing.f_82481_);
    }

    public static void spawnDragonBomb(LivingEntity entity) {
        Vec3 entityFacing = entity.m_20154_().m_82541_();
        DragonFireball fireballEntity = new DragonFireball(entity.m_9236_(), entity, entityFacing.f_82479_, entityFacing.f_82480_, entityFacing.f_82481_);
        fireballEntity.m_20248_(fireballEntity.m_20185_(), entity.m_20227_(0.5) + 0.3, fireballEntity.m_20189_());
        entity.m_9236_().m_7967_(fireballEntity);
        entityFacing = entityFacing.m_82490_(-0.2);
        entity.m_5997_(entityFacing.f_82479_, entityFacing.f_82480_, entityFacing.f_82481_);
    }

    public static void spawnDragonBreath(LivingEntity entity) {
        Optional<ChestCavityEntity> optional = ChestCavityEntity.of(entity);
        if (!optional.isEmpty()) {
            ChestCavityEntity cce = (ChestCavityEntity)optional.get();
            ChestCavityInstance cc = cce.getChestCavityInstance();
            float breath = cc.getOrganScore(CCOrganScores.DRAGON_BREATH);
            double range = Math.sqrt((double)(breath / 2.0F)) * 5.0;
            HitResult result = entity.m_19907_(range, 0.0F, false);
            Vec3 pos = result.m_82450_();
            double x = pos.f_82479_;
            double y = pos.f_82480_;
            double z = pos.f_82481_;
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(x, y, z);

            while(entity.m_9236_().m_46859_(mutable)) {
                --y;
                if (y < 0.0) {
                    return;
                }

                mutable.m_122169_(x, y, z);
            }

            y = (double)(Mth.m_14107_(y) + 1);
            AreaEffectCloud breathEntity = new AreaEffectCloud(entity.m_9236_(), x, y, z);
            breathEntity.m_19718_(entity);
            breathEntity.m_19712_((float)Math.max(range / 2.0, Math.min(range, (double)MathUtil.horizontalDistanceTo(breathEntity, entity))));
            breathEntity.m_19734_(200);
            breathEntity.m_19724_(ParticleTypes.f_123799_);
            breathEntity.m_19716_(new MobEffectInstance(MobEffects.f_19602_));
            entity.m_9236_().m_7967_(breathEntity);
        }

    }

    public static void spawnGhastlyFireball(LivingEntity entity) {
        Vec3 entityFacing = entity.m_20154_().m_82541_();
        LargeFireball fireballEntity = new LargeFireball(entity.m_9236_(), entity, entityFacing.f_82479_, entityFacing.f_82480_, entityFacing.f_82481_, 1);
        fireballEntity.m_20248_(fireballEntity.m_20185_(), entity.m_20227_(0.5) + 0.3, fireballEntity.m_20189_());
        entity.m_9236_().m_7967_(fireballEntity);
        entityFacing = entityFacing.m_82490_(-0.8);
        entity.m_5997_(entityFacing.f_82479_, entityFacing.f_82480_, entityFacing.f_82481_);
    }

    public static void spawnPyromancyFireball(LivingEntity entity) {
        Vec3 entityFacing = entity.m_20154_().m_82541_();
        SmallFireball smallFireballEntity = new SmallFireball(entity.m_9236_(), entity, entityFacing.f_82479_ + entity.m_217043_().m_188583_() * 0.1, entityFacing.f_82480_, entityFacing.f_82481_ + entity.m_217043_().m_188583_() * 0.1);
        smallFireballEntity.m_20248_(smallFireballEntity.m_20185_(), entity.m_20227_(0.5) + 0.3, smallFireballEntity.m_20189_());
        entity.m_9236_().m_7967_(smallFireballEntity);
        entityFacing = entityFacing.m_82490_(-0.2);
        entity.m_5997_(entityFacing.f_82479_, entityFacing.f_82480_, entityFacing.f_82481_);
    }

    public static void spawnShulkerBullet(LivingEntity entity) {
        TargetingConditions targetPredicate = TargetingConditions.m_148352_();
        targetPredicate.m_26883_((double)(ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE * 2));
        LivingEntity target = entity.m_9236_().m_45963_(LivingEntity.class, targetPredicate, entity, entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), new AABB(entity.m_20185_() - (double)ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE, entity.m_20186_() - (double)ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE, entity.m_20189_() - (double)ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE, entity.m_20185_() + (double)ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE, entity.m_20186_() + (double)ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE, entity.m_20189_() + (double)ChestCavity.config.SHULKER_BULLET_TARGETING_RANGE));
        if (target != null) {
            ShulkerBullet shulkerBulletEntity = new ShulkerBullet(entity.m_9236_(), entity, target, Axis.Y);
            shulkerBulletEntity.m_20248_(shulkerBulletEntity.m_20185_(), entity.m_20227_(0.5) + 0.3, shulkerBulletEntity.m_20189_());
            entity.m_9236_().m_7967_(shulkerBulletEntity);
        }

    }

    public static boolean spinWeb(LivingEntity entity, ChestCavityInstance cc, float silkScore) {
        int hungerCost = 0;
        if (entity instanceof Player player) {
            if (player.m_36324_().m_38702_() < 6) {
                return false;
            }
        }

        if (silkScore >= 2.0F) {
            BlockPos pos = entity.m_20183_().m_121945_(entity.m_6350_().m_122424_());
            if (entity.m_20193_().m_8055_(pos).m_60795_()) {
                if (silkScore >= 3.0F) {
                    hungerCost = 16;
                    silkScore -= 3.0F;
                    entity.m_20193_().m_7731_(pos, Blocks.f_50041_.m_49966_(), 2);
                } else {
                    hungerCost = 8;
                    silkScore -= 2.0F;
                    entity.m_20193_().m_7731_(pos, Blocks.f_50033_.m_49966_(), 2);
                }
            }
        }

        while(silkScore >= 1.0F) {
            --silkScore;
            hungerCost += 4;
            cc.projectileQueue.add(OrganUtil::spawnSilk);
        }

        if (player != null) {
            player.m_36324_().m_38703_((float)hungerCost);
        }

        return hungerCost > 0;
    }

    public static boolean teleportRandomly(LivingEntity entity, float range) {
        if (!entity.m_9236_().m_5776_() && entity.m_6084_()) {
            for(int i = 0; i < ChestCavity.config.MAX_TELEPORT_ATTEMPTS; ++i) {
                double d = entity.m_20185_() + (entity.m_217043_().m_188500_() - 0.5) * (double)range;
                double e = Math.max(1.0, entity.m_20186_() + (entity.m_217043_().m_188500_() - 0.5) * (double)range);
                double f = entity.m_20189_() + (entity.m_217043_().m_188500_() - 0.5) * (double)range;
                if (teleportTo(entity, d, e, f)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean teleportTo(LivingEntity entity, double x, double y, double z) {
        if (entity.m_20159_()) {
            entity.m_8127_();
        }

        BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos(x, y, z);

        BlockState blockState;
        for(blockState = entity.m_9236_().m_8055_(targetPos); targetPos.m_123342_() > 0 && !blockState.m_280555_() && !blockState.m_278721_(); blockState = entity.m_9236_().m_8055_(targetPos)) {
            targetPos.m_122173_(Direction.DOWN);
        }

        if (targetPos.m_123342_() <= 0) {
            return false;
        } else {
            targetPos.m_122173_(Direction.UP);
            blockState = entity.m_9236_().m_8055_(targetPos);

            for(BlockState blockState2 = entity.m_9236_().m_8055_(targetPos.m_7494_()); blockState.m_280555_() || blockState.m_278721_() || blockState2.m_280555_() || blockState2.m_278721_(); blockState2 = entity.m_9236_().m_8055_(targetPos.m_7494_())) {
                targetPos.m_122173_(Direction.UP);
                blockState = entity.m_9236_().m_8055_(targetPos);
            }

            if (entity.m_9236_().m_6042_().f_63856_() && targetPos.m_123342_() >= entity.m_9236_().m_141928_()) {
                return false;
            } else {
                entity.m_20324_(x, (double)targetPos.m_123342_() + 0.1, z);
                if (!entity.m_20067_()) {
                    entity.m_9236_().m_6263_((Player)null, entity.f_19854_, entity.f_19855_, entity.f_19856_, SoundEvents.f_11852_, entity.m_5720_(), 1.0F, 1.0F);
                    entity.m_5496_(SoundEvents.f_11852_, 1.0F, 1.0F);
                }

                return true;
            }
        }
    }
}
