package net.tigereye.chestcavity.registration;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.enchantments.CreativeSurgery;
import net.tigereye.chestcavity.enchantments.PainlessSurgery;
import net.tigereye.chestcavity.enchantments.PlayerSurgery;
import net.tigereye.chestcavity.enchantments.SafeSurgery;
import net.tigereye.chestcavity.enchantments.AdvanceSurgery;

public class CCEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS;
    public static final RegistryObject<Enchantment> PAINLESS_SURGERY;
    public static final RegistryObject<Enchantment> SAFE_SURGERY;
    public static final RegistryObject<Enchantment> CREATIVE_SURGERY;
    public static final RegistryObject<Enchantment> ADVANCE_SURGERY;
    public static final RegistryObject<Enchantment> PLAYER_SURGERY;

    public CCEnchantments() {
    }

    static {
        ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ChestCavity.MODID);
        PAINLESS_SURGERY = ENCHANTMENTS.register("painless_surgery", PainlessSurgery::new);
        SAFE_SURGERY = ENCHANTMENTS.register("safe_surgery", SafeSurgery::new);
        CREATIVE_SURGERY = ENCHANTMENTS.register("creative_surgery", CreativeSurgery::new);
        ADVANCE_SURGERY = ENCHANTMENTS.register("advance_surgery", AdvanceSurgery::new);
        PLAYER_SURGERY = ENCHANTMENTS.register("player_surgery", PlayerSurgery::new);
    }
}
