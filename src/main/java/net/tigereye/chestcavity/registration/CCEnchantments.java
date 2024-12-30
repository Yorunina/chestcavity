package net.tigereye.chestcavity.registration;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.enchantments.ONegativeEnchantment;
import net.tigereye.chestcavity.enchantments.TomophobiaEnchantment;

public class CCEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS;
    public static final RegistryObject<Enchantment> O_NEGATIVE;
    public static final RegistryObject<Enchantment> TOMOPHOBIA;

    public CCEnchantments() {
    }

    static {
        ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "chestcavity");
        O_NEGATIVE = ENCHANTMENTS.register("o_negative", ONegativeEnchantment::new);
        TOMOPHOBIA = ENCHANTMENTS.register("tomophobia", TomophobiaEnchantment::new);
    }
}
