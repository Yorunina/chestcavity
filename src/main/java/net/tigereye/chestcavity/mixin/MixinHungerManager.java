package net.tigereye.chestcavity.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.listeners.EffectiveFoodScores;
import net.tigereye.chestcavity.listeners.OrganFoodListeners;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.util.ChestCavityUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({FoodData.class})
public abstract class MixinHungerManager {
        @Shadow
        private int f_38699_;
        @Shadow
        private int f_38696_;
        @Shadow
        private float f_38697_;
        @Shadow
        private float f_38698_;
        @Unique
        private ChestCavityEntity CC_player = null;

        public MixinHungerManager() {
        }

        @Shadow
        public abstract void m_38707_(int var1, float var2);

        @Inject(
                at = {@At("HEAD")},
                method = {"tick"}
        )
        public void chestCavityUpdateMixin(Player player, CallbackInfo info) {
                if (this.CC_player == null) {
                        ChestCavityEntity.of(player).ifPresent((ccPlayerEntityInterface) -> {
                                this.CC_player = ccPlayerEntityInterface;
                        });
                }

                this.f_38699_ = ChestCavityUtil.applySpleenMetabolism(this.CC_player.getChestCavityInstance(), this.f_38699_);
        }

        @Redirect(
                method = {"eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V"},
                at = @At(
                        value = "INVOKE",
                        target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"
                )
        )
        public void chestCavityEatMixin(FoodData instance, int p_38708_, float p_38709_, Item item, ItemStack p_38714_, @Nullable LivingEntity entity) {
                if (item.isEdible() && this.CC_player != null) {
                        FoodProperties itemFoodComponent = item.getFoodProperties();
                        if (itemFoodComponent != null) {
                                EffectiveFoodScores efs = new EffectiveFoodScores(this.CC_player.getChestCavityInstance().getOrganScore(CCOrganScores.DIGESTION), this.CC_player.getChestCavityInstance().getOrganScore(CCOrganScores.NUTRITION));
                                efs = OrganFoodListeners.call(item, itemFoodComponent, this.CC_player, efs);
                                float saturationGain = ChestCavityUtil.applyNutrition(this.CC_player.getChestCavityInstance(), efs.nutrition, item.getFoodProperties().getSaturationModifier()) * (float)item.getFoodProperties().getNutrition() * 2.0F;
                                int hungerGain = ChestCavityUtil.applyDigestion(this.CC_player.getChestCavityInstance(), efs.digestion, item.getFoodProperties().getNutrition());
                                float newSaturation = saturationGain / (float)(hungerGain * 2);
                                this.m_38707_(hungerGain, newSaturation);
                        }
                }

        }

        @ModifyVariable(
                at = @At("HEAD"),
                ordinal = 0,
                method = {"addExhaustion"},
                argsOnly = true
        )
        public float chestCavityAddExhaustionMixin(float exhaustion) {
                if (this.CC_player != null) {
                        if (this.f_38698_ != this.f_38698_) {
                                this.f_38698_ = 0.0F;
                        }

                        float enduranceDif = this.CC_player.getChestCavityInstance().getOrganScore(CCOrganScores.ENDURANCE) - this.CC_player.getChestCavityInstance().getChestCavityType().getDefaultOrganScore(CCOrganScores.ENDURANCE);
                        float out;
                        if (enduranceDif > 0.0F) {
                                out = exhaustion / (1.0F + enduranceDif / 2.0F);
                        } else {
                                out = exhaustion * (1.0F - enduranceDif / 2.0F);
                        }

                        return out;
                } else {
                        return exhaustion;
                }
        }
}
