package net.tigereye.chestcavity;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.compat.ftb.ChestCavityQuestEventHandler;
import net.tigereye.chestcavity.compat.tinker.TinkerItemRegistration;
import net.tigereye.chestcavity.config.CCConfig;
import net.tigereye.chestcavity.network.ChestCavityNetwork;
import net.tigereye.chestcavity.registration.*;
import net.tigereye.chestcavity.ui.ChestCavityAlbumScreenHandler;
import net.tigereye.chestcavity.ui.ChestCavityItemScreenHandler;
import net.tigereye.chestcavity.ui.ChestCavityScreenHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(ChestCavity.MODID)
public class ChestCavity {
    public static final String MODID = "chestcavity";

    public static final Logger LOGGER = LogManager.getLogger();
    public static CCConfig config;

    public static final ResourceLocation COMPATIBILITY_TAG = new ResourceLocation(MODID, "organ_compatibility");
    public static boolean KUBEJS_LOADED = false;

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final RegistryObject<CreativeModeTab> GROUP = CREATIVE_TABS.register("tab", () -> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(CCItems.HUMAN_HEART.get()))
            .title(Component.translatable("tabs." + MODID + ".tab"))
            .displayItems((featureFlagSet, tabOutput) -> {
                CCItems.ITEMS_FOR_TAB_LIST.forEach(registryObject -> tabOutput.accept(new ItemStack(registryObject.get())));
                TinkerItemRegistration.addTabItems(tabOutput);
            }).build()
    );

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);
    public static final RegistryObject<MenuType<ChestCavityScreenHandler>> CHEST_CAVITY_SCREEN_HANDLER = MENU_TYPES.register("chest_cavity_screen", () -> new MenuType<>(ChestCavityScreenHandler::new, FeatureFlags.VANILLA_SET));
    public static final RegistryObject<MenuType<ChestCavityItemScreenHandler>> CHEST_CAVITY_ITEM_SCREEN_HANDLER = MENU_TYPES.register("chest_cavity_item_screen", () -> new MenuType<>(ChestCavityItemScreenHandler::new, FeatureFlags.VANILLA_SET));
    public static final RegistryObject<MenuType<ChestCavityAlbumScreenHandler>> CHEST_CAVITY_ALBUM_SCREEN_HANDLER = MENU_TYPES.register("chest_cavity_album_screen", () -> new MenuType<>(ChestCavityAlbumScreenHandler::new, FeatureFlags.VANILLA_SET));


    public ChestCavity() {
        MinecraftForge.EVENT_BUS.register(this);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        AutoConfig.register(CCConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(CCConfig.class).getConfig();
        CCItems.ITEMS.register(eventBus);
        CREATIVE_TABS.register(eventBus);
        CCEnchantments.ENCHANTMENTS.register(eventBus);
        CCListeners.register();
        CCStatusEffects.MOB_EFFECTS.register(eventBus);
        CCTagOrgans.init();
        TinkerItemRegistration.init(eventBus);
        ChestCavityNetwork.register();
        MENU_TYPES.register(eventBus);

        if (ModList.get().isLoaded("kubejs")) {
            KUBEJS_LOADED = true;
        }
        ChestCavityQuestEventHandler.getInstance().init();
    }



}