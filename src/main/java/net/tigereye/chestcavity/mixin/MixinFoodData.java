package net.tigereye.chestcavity.mixin;

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
import org.jetbrains.annotations.Nullable;
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
    public void chestCavityEatMixin(FoodData instance, int p_38708_, float p_38709_, Item item, ItemStack p_38714_, @Nullable LivingEntity entity) {
        if (item.isEdible() && this.ccPlayer != null) {
            FoodProperties itemFoodComponent = item.getFoodProperties(p_38714_, entity);
            if (itemFoodComponent != null) {
                ChestCavityInstance ccIns = this.ccPlayer.getChestCavityInstance();
                float saturationGain = ChestCavityUtil.applyNutrition(ccIns, itemFoodComponent);
                int hungerGain = ChestCavityUtil.applyDigestion(ccIns, itemFoodComponent);
                this.eat(hungerGain, saturationGain / (2 * hungerGain));
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
            float enduranceDif = this.ccPlayer.getChestCavityInstance().getOrganScore(CCOrganScores.ENDURANCE) - this.ccPlayer.getChestCavityInstance().getChestCavityType().getDefaultOrganScore(CCOrganScores.ENDURANCE);
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
