package net.tigereye.chestcavity.items;

import java.util.Iterator;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.listeners.OrganOnHitListener;
import net.tigereye.chestcavity.registration.CCFoodComponents;
import net.tigereye.chestcavity.registration.CCStatusEffects;
import net.tigereye.chestcavity.util.OrganUtil;

public class VenomGland extends Item implements OrganOnHitListener {
    public VenomGland() {
        super((new Item.Properties()).m_41487_(1).m_41489_(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
    }

    public float onHit(DamageSource source, LivingEntity attacker, LivingEntity target, ChestCavityInstance cc, ItemStack organ, float damage) {
        if (attacker.m_21120_(attacker.m_7655_()).m_41619_() || source.m_269533_(DamageTypeTags.f_268524_) && source.m_7640_() instanceof LlamaSpit) {
            if (attacker.m_21023_((MobEffect)CCStatusEffects.VENOM_COOLDOWN.get())) {
                MobEffectInstance cooldown = attacker.m_21124_((MobEffect)CCStatusEffects.VENOM_COOLDOWN.get());
                if (cooldown.m_19557_() != ChestCavity.config.VENOM_COOLDOWN) {
                    return damage;
                }
            }

            List<MobEffectInstance> effects = OrganUtil.getStatusEffects(organ);
            if (!effects.isEmpty()) {
                Iterator<MobEffectInstance> var8 = effects.iterator();

                while(var8.hasNext()) {
                    MobEffectInstance effect = (MobEffectInstance)var8.next();
                    target.m_7292_(effect);
                }
            } else {
                target.m_7292_(new MobEffectInstance(MobEffects.f_19614_, 200, 0));
            }

            attacker.m_7292_(new MobEffectInstance((MobEffect)CCStatusEffects.VENOM_COOLDOWN.get(), ChestCavity.config.VENOM_COOLDOWN, 0));
            if (attacker instanceof Player) {
                ((Player)attacker).m_36399_(0.1F);
            }
        }

        return damage;
    }

    public void m_7373_(ItemStack itemStack, Level world, List<Component> tooltip, TooltipFlag tooltipContext) {
        super.m_7373_(itemStack, world, tooltip, tooltipContext);
        if (!OrganUtil.getStatusEffects(itemStack).isEmpty()) {
            PotionUtils.m_43555_(itemStack, tooltip, 1.0F);
        }

    }
}
