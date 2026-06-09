package net.tigereye.chestcavity.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.util.ChestCavityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class MixinFoodData {
    @Shadow
    private int tickTimer;
    @Unique
    private ChestCavityEntity ccPlayer = null;

    public MixinFoodData() {
    }

    @Shadow
    public abstract void eat(int var1, float var2);

    @Inject(
            at = {@At("HEAD")},
            method = {"tick"}
    )
    public void chestCavityUpdateMixin(Player player, CallbackInfo info) {
        if (this.ccPlayer == null) {
            ChestCavityEntity.of(player).ifPresent((ccPlayerEntityInterface) -> {
                this.ccPlayer = ccPlayerEntityInterface;
            });
        }

        this.tickTimer = ChestCavityUtil.applySpleenMetabolism(this.ccPlayer.getChestCavityInstance(), this.tickTimer);
    }

    @Redirect(
            method = {"eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"
            )
    )
    public void chestCavityEatMixin(FoodData instance, int pFoodLevelModifier, float pSaturationLevelModifier, @Local(name = "pItem") Item pItem, @Local(name = "pStack") ItemStack pStack, @Local(name = "entity")LivingEntity entity) {
        if (pItem.isEdible()) {
            if (this.ccPlayer != null) {
                FoodProperties itemFoodComponent = pItem.getFoodProperties(pStack, entity);
                if (itemFoodComponent != null) {
                    ChestCavityInstance ccIns = this.ccPlayer.getChestCavityInstance();
                    float saturationGain = ChestCavityUtil.applyNutrition(ccIns, itemFoodComponent);
                    int hungerGain = ChestCavityUtil.applyDigestion(ccIns, itemFoodComponent);
                    this.eat(hungerGain, saturationGain / (2 * hungerGain));
                }
            } else {
                FoodProperties foodproperties = pItem.getFoodProperties(pStack, entity);
                this.eat(foodproperties.getNutrition(), foodproperties.getSaturationModifier());
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
        if (this.ccPlayer != null) {
            float enduranceDiff = this.ccPlayer.getChestCavityInstance().getOrganScore(CCOrganScores.ENDURANCE) - this.ccPlayer.getChestCavityInstance().getChestCavityType().getDefaultOrganScore(CCOrganScores.ENDURANCE);
            float out;
            if (enduranceDiff > 0.0F) {
                out = exhaustion / (1.0F + enduranceDiff / 2.0F);
            } else {
                out = exhaustion * (1.0F - enduranceDiff / 2.0F);
            }
            return out;
        } else {
            return exhaustion;
        }
    }
}
