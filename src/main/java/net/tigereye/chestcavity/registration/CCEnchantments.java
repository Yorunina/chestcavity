package net.tigereye.chestcavity.registration;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.enchantments.CreativeSurgery;
import net.tigereye.chestcavity.enchantments.PainlessSurgery;
import net.tigereye.chestcavity.enchantments.SafeSurgery;

public class CCEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS;
    public static final RegistryObject<Enchantment> PAINLESS_SURGERY;
    public static final RegistryObject<Enchantment> SAFE_SURGERY;
    public static final RegistryObject<Enchantment> CREATIVE_SURGERY;


    public CCEnchantments() {
    }

    static {
        ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, "chestcavity");
        PAINLESS_SURGERY = ENCHANTMENTS.register("painless_surgery", PainlessSurgery::new);
        SAFE_SURGERY = ENCHANTMENTS.register("safe_surgery", SafeSurgery::new);
        CREATIVE_SURGERY = ENCHANTMENTS.register("creative_surgery", CreativeSurgery::new);

    }
}
