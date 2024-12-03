package net.tigereye.chestcavity.mixin;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.tigereye.chestcavity.interfaces.CCStatusEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({MobEffect.class})
public class MixinStatusEffect implements CCStatusEffect {
    @Final
    @Shadow
    private MobEffectCategory category;

    public MixinStatusEffect() {
    }

    public boolean CC_IsHarmful() {
        return this.category == MobEffectCategory.HARMFUL;
    }

    public boolean CC_IsBeneficial() {
        return this.category == MobEffectCategory.BENEFICIAL;
    }
}
