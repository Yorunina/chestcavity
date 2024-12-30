package net.tigereye.chestcavity.items;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
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

import java.util.List;

public class VenomGland extends Item implements OrganOnHitListener {
    public VenomGland() {
        super((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
    }

    public float onHit(DamageSource source, LivingEntity attacker, LivingEntity target, ChestCavityInstance cc, ItemStack organ, float damage) {
        if (attacker.getItemInHand(attacker.getUsedItemHand()).isEmpty() || source.is(DamageTypeTags.IS_PROJECTILE) && source.getEntity() instanceof LlamaSpit) {
            if (attacker.hasEffect(CCStatusEffects.VENOM_COOLDOWN.get())) {
                MobEffectInstance cooldown = attacker.getEffect(CCStatusEffects.VENOM_COOLDOWN.get());
                if (cooldown.getDuration() != ChestCavity.config.VENOM_COOLDOWN) {
                    return damage;
                }
            }

            List<MobEffectInstance> effects = OrganUtil.getStatusEffects(organ);
            if (!effects.isEmpty()) {

                for (MobEffectInstance effect : effects) {
                    target.addEffect(effect);
                }
            } else {
                target.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
            }

            attacker.addEffect(new MobEffectInstance(CCStatusEffects.VENOM_COOLDOWN.get(), ChestCavity.config.VENOM_COOLDOWN, 0));
            if (attacker instanceof Player) {
                ((Player)attacker).causeFoodExhaustion(0.1F);
            }
        }

        return damage;
    }

    public void appendHoverText(ItemStack itemStack, Level world, List<Component> tooltip, TooltipFlag tooltipContext) {
        super.appendHoverText(itemStack, world, tooltip, tooltipContext);
        if (!OrganUtil.getStatusEffects(itemStack).isEmpty()) {
            PotionUtils.addPotionTooltip(itemStack, tooltip, 1.0F);
        }

    }
}
