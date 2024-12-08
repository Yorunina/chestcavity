package net.tigereye.chestcavity;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.compat.CrossModContent;
import net.tigereye.chestcavity.config.CCConfig;
import net.tigereye.chestcavity.forge.network.ChestCavityNetwork;
import net.tigereye.chestcavity.registration.*;
import net.tigereye.chestcavity.ui.ChestCavityScreen;
import net.tigereye.chestcavity.ui.ChestCavityScreenHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("chestcavity")
public class ChestCavity {
	public static final String MODID = "chestcavity";
	public static final boolean DEBUG_MODE = false;
	public static final Logger LOGGER = LogManager.getLogger();
	public static CCConfig config;
	private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ChestCavity.MODID);
	private static final ResourceLocation DESERT_PYRAMID_LOOT_TABLE_ID = new ResourceLocation("minecraft", "chests/desert_pyramid");
	public static final DeferredRegister<MenuType<?>> MENU_TYPES;
	public static final RegistryObject<MenuType<ChestCavityScreenHandler>> CHEST_CAVITY_SCREEN_HANDLER;
	public static final ResourceLocation CHEST_CAVITY_SCREEN_ID;
	public static final ResourceLocation COMPATIBILITY_TAG;

	public ChestCavity() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::clientSetup);
		AutoConfig.register(CCConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(CCConfig.class).getConfig();
		CCItems.ITEMS.register(eventBus);
		CCAttributes.register(eventBus);
		CCRecipes.RECIPE_SERIALIZERS.register(eventBus);
		CCRecipes.MCRECIPE_SERIALIZERS.register(eventBus);
		CCRecipes.RECIPE_TYPES.register(eventBus);
		CCEnchantments.ENCHANTMENTS.register(eventBus);
		CCListeners.register();
		CCStatusEffects.MOB_EFFECTS.register(eventBus);
		CCTagOrgans.init();
		CCCommands.register();
		CCNetworkingPackets.register();
		ChestCavityNetwork.init();
		MENU_TYPES.register(eventBus);
		CrossModContent.register();
		eventBus = MinecraftForge.EVENT_BUS;
		eventBus.register(this);
		eventBus.addListener(this::lootTableLoad);
	}

	public void clientSetup(FMLClientSetupEvent event) {
		MenuScreens.register(CHEST_CAVITY_SCREEN_HANDLER.get(), ChestCavityScreen::new);
		ChestCavityClient.onInitializeClient();
	}

	public void lootTableLoad(LootTableLoadEvent event) {
		if (DESERT_PYRAMID_LOOT_TABLE_ID.equals(event.getName())) {
			LootPool.Builder poolBuilder = LootPool.lootPool().setRolls(BinomialDistributionGenerator.binomial(4, 0.25F)).add(LootItem.lootTableItem((ItemLike)CCItems.ROTTEN_RIB.get()));
			event.getTable().addPool(poolBuilder.build());
			poolBuilder = LootPool.lootPool().setRolls(BinomialDistributionGenerator.binomial(1, 0.3F)).add(LootItem.lootTableItem((ItemLike)CCItems.ROTTEN_RIB.get()));
			event.getTable().addPool(poolBuilder.build());
		}

	}

	static {
		MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "chestcavity");
		CHEST_CAVITY_SCREEN_HANDLER = MENU_TYPES.register("chest_cavity_screen", () -> new MenuType<>(ChestCavityScreenHandler::new, FeatureFlags.VANILLA_SET));
		CHEST_CAVITY_SCREEN_ID = new ResourceLocation("chestcavity", "chest_cavity_screen");
		COMPATIBILITY_TAG = new ResourceLocation("chestcavity", "organ_compatibility");
	}


}
