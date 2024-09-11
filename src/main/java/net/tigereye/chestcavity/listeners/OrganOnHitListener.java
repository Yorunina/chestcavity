package net.tigereye.chestcavity.listeners;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;

public interface OrganOnHitListener {
    float onHit(DamageSource var1, LivingEntity var2, LivingEntity var3, ChestCavityInstance var4, ItemStack var5, float var6);
}
