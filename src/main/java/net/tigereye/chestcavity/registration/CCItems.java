package net.tigereye.chestcavity.registration;

import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.items.ChestOpener;
import net.tigereye.chestcavity.items.CreeperAppendix;
import net.tigereye.chestcavity.items.VenomGland;

public class CCItems {
	public static final DeferredRegister<Item> ITEMS;
	public static final Item.Properties CHEST_OPENER_SETTINGS;
	public static final Item.Properties FOOD_ITEM_SETTINGS;
	public static final RegistryObject<Item> CHEST_OPENER;
	public static final RegistryObject<SwordItem> WOODEN_CLEAVER;
	public static final RegistryObject<SwordItem> GOLD_CLEAVER;
	public static final RegistryObject<SwordItem> STONE_CLEAVER;
	public static final RegistryObject<SwordItem> IRON_CLEAVER;
	public static final RegistryObject<SwordItem> DIAMOND_CLEAVER;
	public static final RegistryObject<SwordItem> NETHERITE_CLEAVER;
	public static final RegistryObject<Item> HUMAN_APPENDIX;
	public static final RegistryObject<Item> HUMAN_HEART;
	public static final RegistryObject<Item> HUMAN_INTESTINE;
	public static final RegistryObject<Item> HUMAN_KIDNEY;
	public static final RegistryObject<Item> HUMAN_LIVER;
	public static final RegistryObject<Item> HUMAN_LUNG;
	public static final RegistryObject<Item> HUMAN_MUSCLE;
	public static final RegistryObject<Item> HUMAN_RIB;
	public static final RegistryObject<Item> HUMAN_SPINE;
	public static final RegistryObject<Item> HUMAN_SPLEEN;
	public static final RegistryObject<Item> HUMAN_STOMACH;
	public static final RegistryObject<Item> ROTTEN_APPENDIX;
	public static final RegistryObject<Item> ROTTEN_HEART;
	public static final RegistryObject<Item> ROTTEN_INTESTINE;
	public static final RegistryObject<Item> ROTTEN_KIDNEY;
	public static final RegistryObject<Item> ROTTEN_LIVER;
	public static final RegistryObject<Item> ROTTEN_LUNG;
	public static final RegistryObject<Item> ROTTEN_MUSCLE;
	public static final RegistryObject<Item> ROTTEN_RIB;
	public static final RegistryObject<Item> ROTTEN_SPINE;
	public static final RegistryObject<Item> ROTTEN_SPLEEN;
	public static final RegistryObject<Item> ROTTEN_STOMACH;
	public static final RegistryObject<Item> WITHERED_RIB;
	public static final RegistryObject<Item> WITHERED_SPINE;
	public static final RegistryObject<Item> WRITHING_SOULSAND;
	public static final RegistryObject<Item> ANIMAL_APPENDIX;
	public static final RegistryObject<Item> ANIMAL_HEART;
	public static final RegistryObject<Item> ANIMAL_INTESTINE;
	public static final RegistryObject<Item> ANIMAL_KIDNEY;
	public static final RegistryObject<Item> ANIMAL_LIVER;
	public static final RegistryObject<Item> ANIMAL_LUNG;
	public static final RegistryObject<Item> ANIMAL_MUSCLE;
	public static final RegistryObject<Item> ANIMAL_RIB;
	public static final RegistryObject<Item> ANIMAL_SPINE;
	public static final RegistryObject<Item> ANIMAL_SPLEEN;
	public static final RegistryObject<Item> ANIMAL_STOMACH;
	public static final RegistryObject<Item> AQUATIC_MUSCLE;
	public static final RegistryObject<Item> FISH_MUSCLE;
	public static final RegistryObject<Item> GILLS;
	public static final RegistryObject<Item> LLAMA_LUNG;
	public static final RegistryObject<Item> CARNIVORE_STOMACH;
	public static final RegistryObject<Item> CARNIVORE_INTESTINE;
	public static final RegistryObject<Item> HERBIVORE_RUMEN;
	public static final RegistryObject<Item> HERBIVORE_STOMACH;
	public static final RegistryObject<Item> HERBIVORE_INTESTINE;
	public static final RegistryObject<Item> BRUTISH_MUSCLE;
	public static final RegistryObject<Item> SWIFT_MUSCLE;
	public static final RegistryObject<Item> SPRINGY_MUSCLE;
	public static final RegistryObject<Item> FIREPROOF_APPENDIX;
	public static final RegistryObject<Item> FIREPROOF_HEART;
	public static final RegistryObject<Item> FIREPROOF_INTESTINE;
	public static final RegistryObject<Item> FIREPROOF_KIDNEY;
	public static final RegistryObject<Item> FIREPROOF_LIVER;
	public static final RegistryObject<Item> FIREPROOF_LUNG;
	public static final RegistryObject<Item> FIREPROOF_MUSCLE;
	public static final RegistryObject<Item> FIREPROOF_RIB;
	public static final RegistryObject<Item> FIREPROOF_SPINE;
	public static final RegistryObject<Item> FIREPROOF_SPLEEN;
	public static final RegistryObject<Item> FIREPROOF_STOMACH;
	public static final RegistryObject<Item> SMALL_ANIMAL_APPENDIX;
	public static final RegistryObject<Item> SMALL_ANIMAL_HEART;
	public static final RegistryObject<Item> SMALL_ANIMAL_INTESTINE;
	public static final RegistryObject<Item> SMALL_ANIMAL_KIDNEY;
	public static final RegistryObject<Item> SMALL_ANIMAL_LIVER;
	public static final RegistryObject<Item> SMALL_ANIMAL_LUNG;
	public static final RegistryObject<Item> SMALL_ANIMAL_MUSCLE;
	public static final RegistryObject<Item> SMALL_ANIMAL_RIB;
	public static final RegistryObject<Item> SMALL_ANIMAL_SPINE;
	public static final RegistryObject<Item> SMALL_ANIMAL_SPLEEN;
	public static final RegistryObject<Item> SMALL_ANIMAL_STOMACH;
	public static final RegistryObject<Item> RABBIT_HEART;
	public static final RegistryObject<Item> SMALL_AQUATIC_MUSCLE;
	public static final RegistryObject<Item> SMALL_FISH_MUSCLE;
	public static final RegistryObject<Item> SMALL_SPRINGY_MUSCLE;
	public static final RegistryObject<Item> SMALL_GILLS;
	public static final RegistryObject<Item> SMALL_CARNIVORE_STOMACH;
	public static final RegistryObject<Item> SMALL_CARNIVORE_INTESTINE;
	public static final RegistryObject<Item> SMALL_HERBIVORE_STOMACH;
	public static final RegistryObject<Item> SMALL_HERBIVORE_INTESTINE;
	public static final RegistryObject<Item> INSECT_HEART;
	public static final RegistryObject<Item> INSECT_INTESTINE;
	public static final RegistryObject<Item> INSECT_LUNG;
	public static final RegistryObject<Item> INSECT_MUSCLE;
	public static final RegistryObject<Item> INSECT_STOMACH;
	public static final RegistryObject<Item> INSECT_CAECA;
	public static final RegistryObject<Item> SILK_GLAND;
	public static final RegistryObject<VenomGland> VENOM_GLAND;
	public static final RegistryObject<Item> ENDER_APPENDIX;
	public static final RegistryObject<Item> ENDER_HEART;
	public static final RegistryObject<Item> ENDER_INTESTINE;
	public static final RegistryObject<Item> ENDER_KIDNEY;
	public static final RegistryObject<Item> ENDER_LIVER;
	public static final RegistryObject<Item> ENDER_LUNG;
	public static final RegistryObject<Item> ENDER_MUSCLE;
	public static final RegistryObject<Item> ENDER_RIB;
	public static final RegistryObject<Item> ENDER_SPINE;
	public static final RegistryObject<Item> ENDER_SPLEEN;
	public static final RegistryObject<Item> ENDER_STOMACH;
	public static final RegistryObject<Item> DRAGON_APPENDIX;
	public static final RegistryObject<Item> DRAGON_HEART;
	public static final RegistryObject<Item> DRAGON_KIDNEY;
	public static final RegistryObject<Item> DRAGON_LIVER;
	public static final RegistryObject<Item> DRAGON_LUNG;
	public static final RegistryObject<Item> DRAGON_MUSCLE;
	public static final RegistryObject<Item> DRAGON_RIB;
	public static final RegistryObject<Item> DRAGON_SPINE;
	public static final RegistryObject<Item> DRAGON_SPLEEN;
	public static final RegistryObject<Item> MANA_REACTOR;
	public static final RegistryObject<Item> ACTIVE_BLAZE_ROD;
	public static final RegistryObject<Item> BLAZE_SHELL;
	public static final RegistryObject<Item> BLAZE_CORE;
	public static final RegistryObject<Item> GAS_BLADDER;
	public static final RegistryObject<Item> VOLATILE_STOMACH;
	public static final RegistryObject<Item> GOLEM_CABLE;
	public static final RegistryObject<Item> GOLEM_PLATING;
	public static final RegistryObject<Item> GOLEM_CORE;
	public static final RegistryObject<Item> INNER_FURNACE;
	public static final RegistryObject<Item> PISTON_MUSCLE;
	public static final RegistryObject<Item> IRON_SCRAP;
	public static final RegistryObject<Item> SALTWATER_HEART;
	public static final RegistryObject<Item> SALTWATER_LUNG;
	public static final RegistryObject<Item> SALTWATER_MUSCLE;
	public static final RegistryObject<Item> CREEPER_APPENDIX;
	public static final RegistryObject<Item> SHIFTING_LEAVES;
	public static final RegistryObject<Item> SHULKER_SPLEEN;
	public static final RegistryObject<Item> SAUSAGE_SKIN;
	public static final RegistryObject<Item> MINI_SAUSAGE_SKIN;
	public static final RegistryObject<Item> BURNT_MEAT_CHUNK;
	public static final RegistryObject<Item> RAW_ORGAN_MEAT;
	public static final RegistryObject<Item> COOKED_ORGAN_MEAT;
	public static final RegistryObject<Item> RAW_BUTCHERED_MEAT;
	public static final RegistryObject<Item> COOKED_BUTCHERED_MEAT;
	public static final RegistryObject<Item> RAW_SAUSAGE;
	public static final RegistryObject<Item> COOKED_SAUSAGE;
	public static final RegistryObject<Item> RAW_RICH_SAUSAGE;
	public static final RegistryObject<Item> COOKED_RICH_SAUSAGE;
	public static final RegistryObject<Item> RAW_MINI_SAUSAGE;
	public static final RegistryObject<Item> COOKED_MINI_SAUSAGE;
	public static final RegistryObject<Item> RAW_RICH_MINI_SAUSAGE;
	public static final RegistryObject<Item> COOKED_RICH_MINI_SAUSAGE;
	public static final RegistryObject<Item> ROTTEN_SAUSAGE;
	public static final RegistryObject<Item> RAW_TOXIC_ORGAN_MEAT;
	public static final RegistryObject<Item> COOKED_TOXIC_ORGAN_MEAT;
	public static final RegistryObject<Item> RAW_TOXIC_MEAT;
	public static final RegistryObject<Item> COOKED_TOXIC_MEAT;
	public static final RegistryObject<Item> RAW_TOXIC_SAUSAGE;
	public static final RegistryObject<Item> COOKED_TOXIC_SAUSAGE;
	public static final RegistryObject<Item> RAW_RICH_TOXIC_SAUSAGE;
	public static final RegistryObject<Item> COOKED_RICH_TOXIC_SAUSAGE;
	public static final RegistryObject<Item> RAW_HUMAN_ORGAN_MEAT;
	public static final RegistryObject<Item> COOKED_HUMAN_ORGAN_MEAT;
	public static final RegistryObject<Item> RAW_MAN_MEAT;
	public static final RegistryObject<Item> COOKED_MAN_MEAT;
	public static final RegistryObject<Item> RAW_HUMAN_SAUSAGE;
	public static final RegistryObject<Item> COOKED_HUMAN_SAUSAGE;
	public static final RegistryObject<Item> RAW_RICH_HUMAN_SAUSAGE;
	public static final RegistryObject<Item> COOKED_RICH_HUMAN_SAUSAGE;
	public static final RegistryObject<Item> RAW_ALIEN_ORGAN_MEAT;
	public static final RegistryObject<Item> COOKED_ALIEN_ORGAN_MEAT;
	public static final RegistryObject<Item> RAW_ALIEN_MEAT;
	public static final RegistryObject<Item> COOKED_ALIEN_MEAT;
	public static final RegistryObject<Item> RAW_ALIEN_SAUSAGE;
	public static final RegistryObject<Item> COOKED_ALIEN_SAUSAGE;
	public static final RegistryObject<Item> RAW_RICH_ALIEN_SAUSAGE;
	public static final RegistryObject<Item> COOKED_RICH_ALIEN_SAUSAGE;
	public static final RegistryObject<Item> RAW_DRAGON_ORGAN_MEAT;
	public static final RegistryObject<Item> COOKED_DRAGON_ORGAN_MEAT;
	public static final RegistryObject<Item> RAW_DRAGON_MEAT;
	public static final RegistryObject<Item> COOKED_DRAGON_MEAT;
	public static final RegistryObject<Item> RAW_DRAGON_SAUSAGE;
	public static final RegistryObject<Item> COOKED_DRAGON_SAUSAGE;
	public static final RegistryObject<Item> RAW_RICH_DRAGON_SAUSAGE;
	public static final RegistryObject<Item> COOKED_RICH_DRAGON_SAUSAGE;
	public static final RegistryObject<Item> CUD;

	public CCItems() {
	}

	static {
		ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "chestcavity");
		CHEST_OPENER_SETTINGS = (new Item.Properties()).stacksTo(1);
		FOOD_ITEM_SETTINGS = (new Item.Properties()).stacksTo(64);
		CHEST_OPENER = ITEMS.register("chest_opener", ChestOpener::new);
		WOODEN_CLEAVER = ITEMS.register("wooden_cleaver", () -> {
			return new SwordItem(Tiers.WOOD, 6, -3.2F, new Item.Properties());
		});
		GOLD_CLEAVER = ITEMS.register("stone_cleaver", () -> {
			return new SwordItem(Tiers.GOLD, 6, -3.0F, new Item.Properties());
		});
		STONE_CLEAVER = ITEMS.register("gold_cleaver", () -> {
			return new SwordItem(Tiers.STONE, 7, -3.2F, new Item.Properties());
		});
		IRON_CLEAVER = ITEMS.register("iron_cleaver", () -> {
			return new SwordItem(Tiers.IRON, 6, -3.1F, new Item.Properties());
		});
		DIAMOND_CLEAVER = ITEMS.register("diamond_cleaver", () -> {
			return new SwordItem(Tiers.DIAMOND, 5, -3.0F, new Item.Properties());
		});
		NETHERITE_CLEAVER = ITEMS.register("netherite_cleaver", () -> {
			return new SwordItem(Tiers.NETHERITE, 5, -3.0F, (new Item.Properties()).fireResistant());
		});
		HUMAN_APPENDIX = ITEMS.register("appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_HEART = ITEMS.register("heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_INTESTINE = ITEMS.register("intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_KIDNEY = ITEMS.register("kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_LIVER = ITEMS.register("liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_LUNG = ITEMS.register("lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_MUSCLE = ITEMS.register("muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.HUMAN_MUSCLE_FOOD_COMPONENT));
		});
		HUMAN_RIB = ITEMS.register("rib", () -> {
			return new Item((new Item.Properties()).stacksTo(4));
		});
		HUMAN_SPINE = ITEMS.register("spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		HUMAN_SPLEEN = ITEMS.register("spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_STOMACH = ITEMS.register("stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ROTTEN_APPENDIX = ITEMS.register("rotten_appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_HEART = ITEMS.register("rotten_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_INTESTINE = ITEMS.register("rotten_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_KIDNEY = ITEMS.register("rotten_kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_LIVER = ITEMS.register("rotten_liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_LUNG = ITEMS.register("rotten_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_MUSCLE = ITEMS.register("rotten_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.ROTTEN_MUSCLE_FOOD_COMPONENT));
		});
		ROTTEN_RIB = ITEMS.register("rotten_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(4));
		});
		ROTTEN_SPINE = ITEMS.register("rotten_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		ROTTEN_SPLEEN = ITEMS.register("rotten_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_STOMACH = ITEMS.register("rotten_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		WITHERED_RIB = ITEMS.register("withered_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(4));
		});
		WITHERED_SPINE = ITEMS.register("withered_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		WRITHING_SOULSAND = ITEMS.register("writhing_soulsand", () -> {
			return new Item((new Item.Properties()).stacksTo(16));
		});
		ANIMAL_APPENDIX = ITEMS.register("animal_appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_HEART = ITEMS.register("animal_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_INTESTINE = ITEMS.register("animal_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_KIDNEY = ITEMS.register("animal_kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_LIVER = ITEMS.register("animal_liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_LUNG = ITEMS.register("animal_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_MUSCLE = ITEMS.register("animal_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		ANIMAL_RIB = ITEMS.register("animal_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(4));
		});
		ANIMAL_SPINE = ITEMS.register("animal_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		ANIMAL_SPLEEN = ITEMS.register("animal_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_STOMACH = ITEMS.register("animal_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		AQUATIC_MUSCLE = ITEMS.register("aquatic_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		FISH_MUSCLE = ITEMS.register("fish_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		GILLS = ITEMS.register("gills", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		LLAMA_LUNG = ITEMS.register("llama_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		CARNIVORE_STOMACH = ITEMS.register("carnivore_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		CARNIVORE_INTESTINE = ITEMS.register("carnivore_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HERBIVORE_RUMEN = ITEMS.register("herbivore_rumen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HERBIVORE_STOMACH = ITEMS.register("herbivore_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HERBIVORE_INTESTINE = ITEMS.register("herbivore_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		BRUTISH_MUSCLE = ITEMS.register("brutish_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SWIFT_MUSCLE = ITEMS.register("swift_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SPRINGY_MUSCLE = ITEMS.register("springy_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		FIREPROOF_APPENDIX = ITEMS.register("fireproof_appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_HEART = ITEMS.register("fireproof_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_INTESTINE = ITEMS.register("fireproof_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_KIDNEY = ITEMS.register("fireproof_kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_LIVER = ITEMS.register("fireproof_liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_LUNG = ITEMS.register("fireproof_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_MUSCLE = ITEMS.register("fireproof_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		FIREPROOF_RIB = ITEMS.register("fireproof_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(4));
		});
		FIREPROOF_SPINE = ITEMS.register("fireproof_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		FIREPROOF_SPLEEN = ITEMS.register("fireproof_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_STOMACH = ITEMS.register("fireproof_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_APPENDIX = ITEMS.register("small_animal_appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_HEART = ITEMS.register("small_animal_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_INTESTINE = ITEMS.register("small_animal_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_KIDNEY = ITEMS.register("small_animal_kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_LIVER = ITEMS.register("small_animal_liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_LUNG = ITEMS.register("small_animal_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_MUSCLE = ITEMS.register("small_animal_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_RIB = ITEMS.register("small_animal_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(4));
		});
		SMALL_ANIMAL_SPINE = ITEMS.register("small_animal_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		SMALL_ANIMAL_SPLEEN = ITEMS.register("small_animal_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_STOMACH = ITEMS.register("small_animal_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		RABBIT_HEART = ITEMS.register("rabbit_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_AQUATIC_MUSCLE = ITEMS.register("small_aquatic_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_FISH_MUSCLE = ITEMS.register("small_fish_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_SPRINGY_MUSCLE = ITEMS.register("small_springy_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_GILLS = ITEMS.register("small_gills", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_CARNIVORE_STOMACH = ITEMS.register("small_carnivore_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_CARNIVORE_INTESTINE = ITEMS.register("small_carnivore_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_HERBIVORE_STOMACH = ITEMS.register("small_herbivore_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_HERBIVORE_INTESTINE = ITEMS.register("small_herbivore_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		INSECT_HEART = ITEMS.register("insect_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		INSECT_INTESTINE = ITEMS.register("insect_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		INSECT_LUNG = ITEMS.register("insect_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		INSECT_MUSCLE = ITEMS.register("insect_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.INSECT_MUSCLE_FOOD_COMPONENT));
		});
		INSECT_STOMACH = ITEMS.register("insect_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		INSECT_CAECA = ITEMS.register("insect_caeca", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		SILK_GLAND = ITEMS.register("silk_gland", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		VENOM_GLAND = ITEMS.register("venom_gland", VenomGland::new);
		ENDER_APPENDIX = ITEMS.register("ender_appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_HEART = ITEMS.register("ender_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_INTESTINE = ITEMS.register("ender_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_KIDNEY = ITEMS.register("ender_kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_LIVER = ITEMS.register("ender_liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_LUNG = ITEMS.register("ender_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_MUSCLE = ITEMS.register("ender_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.ALIEN_MUSCLE_FOOD_COMPONENT));
		});
		ENDER_RIB = ITEMS.register("ender_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(4));
		});
		ENDER_SPINE = ITEMS.register("ender_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		ENDER_SPLEEN = ITEMS.register("ender_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_STOMACH = ITEMS.register("ender_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		DRAGON_APPENDIX = ITEMS.register("dragon_appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		DRAGON_HEART = ITEMS.register("dragon_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.DRAGON_HEART_FOOD_COMPONENT));
		});
		DRAGON_KIDNEY = ITEMS.register("dragon_kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		DRAGON_LIVER = ITEMS.register("dragon_liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		DRAGON_LUNG = ITEMS.register("dragon_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		DRAGON_MUSCLE = ITEMS.register("dragon_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16).food(CCFoodComponents.DRAGON_MUSCLE_FOOD_COMPONENT));
		});
		DRAGON_RIB = ITEMS.register("dragon_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(4));
		});
		DRAGON_SPINE = ITEMS.register("dragon_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		DRAGON_SPLEEN = ITEMS.register("dragon_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		MANA_REACTOR = ITEMS.register("mana_reactor", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ACTIVE_BLAZE_ROD = ITEMS.register("active_blaze_rod", () -> {
			return new Item((new Item.Properties()).stacksTo(3));
		});
		BLAZE_SHELL = ITEMS.register("blaze_shell", () -> {
			return new Item((new Item.Properties()).stacksTo(4));
		});
		BLAZE_CORE = ITEMS.register("blaze_core", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		GAS_BLADDER = ITEMS.register("gas_bladder", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		VOLATILE_STOMACH = ITEMS.register("volatile_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		GOLEM_CABLE = ITEMS.register("golem_cable", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		GOLEM_PLATING = ITEMS.register("golem_plating", () -> {
			return new Item((new Item.Properties()).stacksTo(4));
		});
		GOLEM_CORE = ITEMS.register("golem_core", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		INNER_FURNACE = ITEMS.register("inner_furnace", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		PISTON_MUSCLE = ITEMS.register("piston_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16));
		});
		IRON_SCRAP = ITEMS.register("iron_scrap", () -> {
			return new Item(new Item.Properties());
		});
		SALTWATER_HEART = ITEMS.register("saltwater_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		SALTWATER_LUNG = ITEMS.register("saltwater_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		SALTWATER_MUSCLE = ITEMS.register("saltwater_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(16));
		});
		CREEPER_APPENDIX = ITEMS.register("creeper_appendix", CreeperAppendix::new);
		SHIFTING_LEAVES = ITEMS.register("shifting_leaves", () -> {
			return new Item((new Item.Properties()).stacksTo(16));
		});
		SHULKER_SPLEEN = ITEMS.register("shulker_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		SAUSAGE_SKIN = ITEMS.register("sausage_skin", () -> {
			return new Item((new Item.Properties()).stacksTo(64));
		});
		MINI_SAUSAGE_SKIN = ITEMS.register("mini_sausage_skin", () -> {
			return new Item((new Item.Properties()).stacksTo(64));
		});
		BURNT_MEAT_CHUNK = ITEMS.register("burnt_meat_chunk", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.BURNT_MEAT_CHUNK_COMPONENT));
		});
		RAW_ORGAN_MEAT = ITEMS.register("raw_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		COOKED_ORGAN_MEAT = ITEMS.register("cooked_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_ORGAN_MEAT_FOOD_COMPONENT));
		});
		RAW_BUTCHERED_MEAT = ITEMS.register("raw_butchered_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_BUTCHERED_MEAT_FOOD_COMPONENT));
		});
		COOKED_BUTCHERED_MEAT = ITEMS.register("cooked_butchered_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_BUTCHERED_MEAT_FOOD_COMPONENT));
		});
		RAW_SAUSAGE = ITEMS.register("raw_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_SAUSAGE = ITEMS.register("sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_RICH_SAUSAGE = ITEMS.register("raw_rich_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_RICH_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_RICH_SAUSAGE = ITEMS.register("rich_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_RICH_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_MINI_SAUSAGE = ITEMS.register("raw_mini_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_MINI_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_MINI_SAUSAGE = ITEMS.register("mini_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_MINI_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_RICH_MINI_SAUSAGE = ITEMS.register("raw_rich_mini_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_RICH_MINI_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_RICH_MINI_SAUSAGE = ITEMS.register("rich_mini_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_RICH_MINI_SAUSAGE_FOOD_COMPONENT));
		});
		ROTTEN_SAUSAGE = ITEMS.register("rotten_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.ROTTEN_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_TOXIC_ORGAN_MEAT = ITEMS.register("raw_toxic_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		COOKED_TOXIC_ORGAN_MEAT = ITEMS.register("cooked_toxic_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		RAW_TOXIC_MEAT = ITEMS.register("raw_toxic_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_TOXIC_MEAT_FOOD_COMPONENT));
		});
		COOKED_TOXIC_MEAT = ITEMS.register("cooked_toxic_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_TOXIC_MEAT_FOOD_COMPONENT));
		});
		RAW_TOXIC_SAUSAGE = ITEMS.register("raw_toxic_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_TOXIC_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_TOXIC_SAUSAGE = ITEMS.register("toxic_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_TOXIC_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_RICH_TOXIC_SAUSAGE = ITEMS.register("raw_rich_toxic_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_RICH_TOXIC_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_RICH_TOXIC_SAUSAGE = ITEMS.register("rich_toxic_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_RICH_TOXIC_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_HUMAN_ORGAN_MEAT = ITEMS.register("raw_human_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		COOKED_HUMAN_ORGAN_MEAT = ITEMS.register("cooked_human_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		RAW_MAN_MEAT = ITEMS.register("raw_man_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_MAN_MEAT_FOOD_COMPONENT));
		});
		COOKED_MAN_MEAT = ITEMS.register("cooked_man_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_MAN_MEAT_FOOD_COMPONENT));
		});
		RAW_HUMAN_SAUSAGE = ITEMS.register("raw_human_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_HUMAN_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_HUMAN_SAUSAGE = ITEMS.register("human_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_HUMAN_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_RICH_HUMAN_SAUSAGE = ITEMS.register("raw_rich_human_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_RICH_HUMAN_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_RICH_HUMAN_SAUSAGE = ITEMS.register("rich_human_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_RICH_HUMAN_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_ALIEN_ORGAN_MEAT = ITEMS.register("raw_alien_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		COOKED_ALIEN_ORGAN_MEAT = ITEMS.register("cooked_alien_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		RAW_ALIEN_MEAT = ITEMS.register("raw_alien_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_ALIEN_MEAT_FOOD_COMPONENT));
		});
		COOKED_ALIEN_MEAT = ITEMS.register("cooked_alien_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_ALIEN_MEAT_FOOD_COMPONENT));
		});
		RAW_ALIEN_SAUSAGE = ITEMS.register("raw_alien_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_ALIEN_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_ALIEN_SAUSAGE = ITEMS.register("alien_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_ALIEN_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_RICH_ALIEN_SAUSAGE = ITEMS.register("raw_rich_alien_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_RICH_ALIEN_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_RICH_ALIEN_SAUSAGE = ITEMS.register("rich_alien_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_RICH_ALIEN_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_DRAGON_ORGAN_MEAT = ITEMS.register("raw_dragon_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		COOKED_DRAGON_ORGAN_MEAT = ITEMS.register("cooked_dragon_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		RAW_DRAGON_MEAT = ITEMS.register("raw_dragon_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_DRAGON_MEAT_FOOD_COMPONENT));
		});
		COOKED_DRAGON_MEAT = ITEMS.register("cooked_dragon_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_DRAGON_MEAT_FOOD_COMPONENT));
		});
		RAW_DRAGON_SAUSAGE = ITEMS.register("raw_dragon_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_DRAGON_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_DRAGON_SAUSAGE = ITEMS.register("dragon_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_DRAGON_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_RICH_DRAGON_SAUSAGE = ITEMS.register("raw_rich_dragon_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_RICH_DRAGON_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_RICH_DRAGON_SAUSAGE = ITEMS.register("rich_dragon_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_RICH_DRAGON_SAUSAGE_FOOD_COMPONENT));
		});
		CUD = ITEMS.register("cud", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.CUD_FOOD_COMPONENT));
		});
	}
}
