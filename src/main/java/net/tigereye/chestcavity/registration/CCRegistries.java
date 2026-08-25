package net.tigereye.chestcavity.registration;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.ui.ChestCavityItemScreenHandler;
import net.tigereye.chestcavity.ui.ChestCavityScreenHandler;

/**
 * Mod registry declarations. Registration is performed by
 * {@code ChestCavityRegistryBootstrap} during mod construction.
 */
public final class CCRegistries {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ChestCavity.MODID);
    public static final RegistryObject<CreativeModeTab> GROUP = CREATIVE_TABS.register(
            "tab",
            () -> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
                    .icon(() -> new ItemStack(CCItems.CHEST_OPENER.get()))
                    .title(Component.translatable("tabs." + ChestCavity.MODID + ".tab"))
                    .displayItems((featureFlagSet, tabOutput) ->
                            CCItems.ITEMS_FOR_TAB_LIST.forEach(registryObject ->
                                    tabOutput.accept(new ItemStack(registryObject.get()))))
                    .build()
    );

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, ChestCavity.MODID);
    public static final RegistryObject<MenuType<ChestCavityScreenHandler>> CHEST_CAVITY_SCREEN_HANDLER =
            MENU_TYPES.register(
                    "chest_cavity_screen",
                    () -> new MenuType<>(ChestCavityScreenHandler::new, FeatureFlags.VANILLA_SET)
            );
    public static final RegistryObject<MenuType<ChestCavityItemScreenHandler>> CHEST_CAVITY_ITEM_SCREEN_HANDLER =
            MENU_TYPES.register(
                    "chest_cavity_item_screen",
                    () -> new MenuType<>(ChestCavityItemScreenHandler::new, FeatureFlags.VANILLA_SET)
            );

    private CCRegistries() {
    }
}
