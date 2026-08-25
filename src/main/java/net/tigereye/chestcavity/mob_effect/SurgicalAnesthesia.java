package net.tigereye.chestcavity.mob_effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SurgicalAnesthesia extends MobEffect {
    public SurgicalAnesthesia() {
        super(MobEffectCategory.HARMFUL, 15883605);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "d9d2624b-9c24-401f-8062-c3059104c5be", -1.0F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}