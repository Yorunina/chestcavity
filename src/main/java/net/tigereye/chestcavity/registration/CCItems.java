package net.tigereye.chestcavity.registration;

import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.items.ChestOpener;
import net.tigereye.chestcavity.items.CreeperAppendix;
import net.tigereye.chestcavity.items.SurgicalBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class CCItems {
	public static final DeferredRegister<Item> ITEMS;
	public static final Item.Properties CHEST_OPENER_SETTINGS;
	public static final Item.Properties FOOD_ITEM_SETTINGS;
	public static final RegistryObject<Item> CHEST_OPENER;
	public static final RegistryObject<Item> HUMAN_APPENDIX;
	public static final RegistryObject<Item> HUMAN_HEART;
	public static final RegistryObject<Item> HUMAN_INTESTINE;
	public static final RegistryObject<Item> HUMAN_KIDNEY;
	public static final RegistryObject<Item> HUMAN_LIVER;
	public static final RegistryObject<Item> SURGICAL_BOX;
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
	public static final RegistryObject<Item> FURNACE_POWER;

	public static final Collection<RegistryObject<Item>> ITEMS_FOR_TAB_LIST = new ArrayList<>();

	public CCItems() {
	}

	public static RegistryObject<Item> register(final String name, final Supplier<? extends Item> sup) {
		RegistryObject<Item> newItem = ITEMS.register(name, sup);
		ITEMS_FOR_TAB_LIST.add(newItem);
		return newItem;
	}

	static {
		ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "chestcavity");
		CHEST_OPENER_SETTINGS = (new Item.Properties()).stacksTo(1);
		FOOD_ITEM_SETTINGS = (new Item.Properties()).stacksTo(64);
		CHEST_OPENER = register("chest_opener", ChestOpener::new);
		HUMAN_APPENDIX = register("appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_HEART = register("heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_INTESTINE = register("intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_KIDNEY = register("kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_LIVER = register("liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_LUNG = register("lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_MUSCLE = register("muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.HUMAN_MUSCLE_FOOD_COMPONENT));
		});
		HUMAN_RIB = register("rib", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		HUMAN_SPINE = register("spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		HUMAN_SPLEEN = register("spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HUMAN_STOMACH = register("stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ROTTEN_APPENDIX = register("rotten_appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_HEART = register("rotten_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_INTESTINE = register("rotten_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_KIDNEY = register("rotten_kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_LIVER = register("rotten_liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_LUNG = register("rotten_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_MUSCLE = register("rotten_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.ROTTEN_MUSCLE_FOOD_COMPONENT));
		});
		ROTTEN_RIB = register("rotten_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		ROTTEN_SPINE = register("rotten_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		ROTTEN_SPLEEN = register("rotten_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		ROTTEN_STOMACH = register("rotten_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(Foods.ROTTEN_FLESH));
		});
		WITHERED_RIB = register("withered_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		WITHERED_SPINE = register("withered_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		WRITHING_SOULSAND = register("writhing_soulsand", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		ANIMAL_APPENDIX = register("animal_appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_HEART = register("animal_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_INTESTINE = register("animal_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_KIDNEY = register("animal_kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_LIVER = register("animal_liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_LUNG = register("animal_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_MUSCLE = register("animal_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		ANIMAL_RIB = register("animal_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		ANIMAL_SPINE = register("animal_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		ANIMAL_SPLEEN = register("animal_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ANIMAL_STOMACH = register("animal_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		AQUATIC_MUSCLE = register("aquatic_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		FISH_MUSCLE = register("fish_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		GILLS = register("gills", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		LLAMA_LUNG = register("llama_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		CARNIVORE_STOMACH = register("carnivore_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		CARNIVORE_INTESTINE = register("carnivore_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HERBIVORE_RUMEN = register("herbivore_rumen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HERBIVORE_STOMACH = register("herbivore_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		HERBIVORE_INTESTINE = register("herbivore_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		BRUTISH_MUSCLE = register("brutish_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SWIFT_MUSCLE = register("swift_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SPRINGY_MUSCLE = register("springy_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		FIREPROOF_APPENDIX = register("fireproof_appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_HEART = register("fireproof_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_INTESTINE = register("fireproof_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_KIDNEY = register("fireproof_kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_LIVER = register("fireproof_liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_LUNG = register("fireproof_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_MUSCLE = register("fireproof_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		FIREPROOF_RIB = register("fireproof_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		FIREPROOF_SPINE = register("fireproof_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		FIREPROOF_SPLEEN = register("fireproof_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		FIREPROOF_STOMACH = register("fireproof_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_APPENDIX = register("small_animal_appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_HEART = register("small_animal_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_INTESTINE = register("small_animal_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_KIDNEY = register("small_animal_kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_LIVER = register("small_animal_liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_LUNG = register("small_animal_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_MUSCLE = register("small_animal_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_RIB = register("small_animal_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		SMALL_ANIMAL_SPINE = register("small_animal_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		SMALL_ANIMAL_SPLEEN = register("small_animal_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_ANIMAL_STOMACH = register("small_animal_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		RABBIT_HEART = register("rabbit_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_AQUATIC_MUSCLE = register("small_aquatic_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_FISH_MUSCLE = register("small_fish_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_SPRINGY_MUSCLE = register("small_springy_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_GILLS = register("small_gills", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_CARNIVORE_STOMACH = register("small_carnivore_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_CARNIVORE_INTESTINE = register("small_carnivore_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_HERBIVORE_STOMACH = register("small_herbivore_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		SMALL_HERBIVORE_INTESTINE = register("small_herbivore_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.SMALL_ANIMAL_MUSCLE_FOOD_COMPONENT));
		});
		INSECT_HEART = register("insect_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		INSECT_INTESTINE = register("insect_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		INSECT_LUNG = register("insect_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		INSECT_MUSCLE = register("insect_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.INSECT_MUSCLE_FOOD_COMPONENT));
		});
		INSECT_STOMACH = register("insect_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		INSECT_CAECA = register("insect_caeca", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		SILK_GLAND = register("silk_gland", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_APPENDIX = register("ender_appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_HEART = register("ender_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_INTESTINE = register("ender_intestine", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_KIDNEY = register("ender_kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_LIVER = register("ender_liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_LUNG = register("ender_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_MUSCLE = register("ender_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.ALIEN_MUSCLE_FOOD_COMPONENT));
		});
		ENDER_RIB = register("ender_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		ENDER_SPINE = register("ender_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		ENDER_SPLEEN = register("ender_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ENDER_STOMACH = register("ender_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		DRAGON_APPENDIX = register("dragon_appendix", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		DRAGON_HEART = register("dragon_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.DRAGON_HEART_FOOD_COMPONENT));
		});
		DRAGON_KIDNEY = register("dragon_kidney", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		DRAGON_LIVER = register("dragon_liver", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		DRAGON_LUNG = register("dragon_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		DRAGON_MUSCLE = register("dragon_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.DRAGON_MUSCLE_FOOD_COMPONENT));
		});
		DRAGON_RIB = register("dragon_rib", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		DRAGON_SPINE = register("dragon_spine", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		DRAGON_SPLEEN = register("dragon_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		MANA_REACTOR = register("mana_reactor", () -> {
			return new Item((new Item.Properties()).stacksTo(1).food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		ACTIVE_BLAZE_ROD = register("active_blaze_rod", () -> {
			return new Item((new Item.Properties()).stacksTo(3));
		});
		BLAZE_SHELL = register("blaze_shell", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		BLAZE_CORE = register("blaze_core", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		GAS_BLADDER = register("gas_bladder", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		VOLATILE_STOMACH = register("volatile_stomach", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		GOLEM_CABLE = register("golem_cable", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		GOLEM_PLATING = register("golem_plating", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		GOLEM_CORE = register("golem_core", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		INNER_FURNACE = register("inner_furnace", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		PISTON_MUSCLE = register("piston_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		IRON_SCRAP = register("iron_scrap", () -> {
			return new Item(new Item.Properties());
		});
		SALTWATER_HEART = register("saltwater_heart", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		SALTWATER_LUNG = register("saltwater_lung", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		SALTWATER_MUSCLE = register("saltwater_muscle", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		CREEPER_APPENDIX = register("creeper_appendix", CreeperAppendix::new);
		SURGICAL_BOX = register("surgical_box", SurgicalBox::new);

		SHIFTING_LEAVES = register("shifting_leaves", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		SHULKER_SPLEEN = register("shulker_spleen", () -> {
			return new Item((new Item.Properties()).stacksTo(1));
		});
		SAUSAGE_SKIN = register("sausage_skin", () -> {
			return new Item((new Item.Properties()).stacksTo(64));
		});
		MINI_SAUSAGE_SKIN = register("mini_sausage_skin", () -> {
			return new Item((new Item.Properties()).stacksTo(64));
		});
		BURNT_MEAT_CHUNK = register("burnt_meat_chunk", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.BURNT_MEAT_CHUNK_COMPONENT));
		});
		RAW_ORGAN_MEAT = register("raw_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_ORGAN_MEAT_FOOD_COMPONENT));
		});
		COOKED_ORGAN_MEAT = register("cooked_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_ORGAN_MEAT_FOOD_COMPONENT));
		});
		RAW_BUTCHERED_MEAT = register("raw_butchered_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_BUTCHERED_MEAT_FOOD_COMPONENT));
		});
		COOKED_BUTCHERED_MEAT = register("cooked_butchered_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_BUTCHERED_MEAT_FOOD_COMPONENT));
		});
		RAW_SAUSAGE = register("raw_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_SAUSAGE = register("sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_RICH_SAUSAGE = register("raw_rich_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_RICH_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_RICH_SAUSAGE = register("rich_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_RICH_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_MINI_SAUSAGE = register("raw_mini_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_MINI_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_MINI_SAUSAGE = register("mini_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_MINI_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_RICH_MINI_SAUSAGE = register("raw_rich_mini_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_RICH_MINI_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_RICH_MINI_SAUSAGE = register("rich_mini_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_RICH_MINI_SAUSAGE_FOOD_COMPONENT));
		});
		ROTTEN_SAUSAGE = register("rotten_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.ROTTEN_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_TOXIC_ORGAN_MEAT = register("raw_toxic_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		COOKED_TOXIC_ORGAN_MEAT = register("cooked_toxic_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_TOXIC_ORGAN_MEAT_FOOD_COMPONENT));
		});
		RAW_TOXIC_MEAT = register("raw_toxic_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_TOXIC_MEAT_FOOD_COMPONENT));
		});
		COOKED_TOXIC_MEAT = register("cooked_toxic_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_TOXIC_MEAT_FOOD_COMPONENT));
		});
		RAW_TOXIC_SAUSAGE = register("raw_toxic_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_TOXIC_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_TOXIC_SAUSAGE = register("toxic_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_TOXIC_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_RICH_TOXIC_SAUSAGE = register("raw_rich_toxic_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_RICH_TOXIC_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_RICH_TOXIC_SAUSAGE = register("rich_toxic_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_RICH_TOXIC_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_HUMAN_ORGAN_MEAT = register("raw_human_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		COOKED_HUMAN_ORGAN_MEAT = register("cooked_human_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_HUMAN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		RAW_MAN_MEAT = register("raw_man_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_MAN_MEAT_FOOD_COMPONENT));
		});
		COOKED_MAN_MEAT = register("cooked_man_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_MAN_MEAT_FOOD_COMPONENT));
		});
		RAW_HUMAN_SAUSAGE = register("raw_human_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_HUMAN_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_HUMAN_SAUSAGE = register("human_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_HUMAN_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_RICH_HUMAN_SAUSAGE = register("raw_rich_human_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_RICH_HUMAN_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_RICH_HUMAN_SAUSAGE = register("rich_human_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_RICH_HUMAN_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_ALIEN_ORGAN_MEAT = register("raw_alien_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		COOKED_ALIEN_ORGAN_MEAT = register("cooked_alien_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_ALIEN_ORGAN_MEAT_FOOD_COMPONENT));
		});
		RAW_ALIEN_MEAT = register("raw_alien_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_ALIEN_MEAT_FOOD_COMPONENT));
		});
		COOKED_ALIEN_MEAT = register("cooked_alien_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_ALIEN_MEAT_FOOD_COMPONENT));
		});
		RAW_ALIEN_SAUSAGE = register("raw_alien_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_ALIEN_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_ALIEN_SAUSAGE = register("alien_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_ALIEN_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_RICH_ALIEN_SAUSAGE = register("raw_rich_alien_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_RICH_ALIEN_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_RICH_ALIEN_SAUSAGE = register("rich_alien_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_RICH_ALIEN_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_DRAGON_ORGAN_MEAT = register("raw_dragon_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		COOKED_DRAGON_ORGAN_MEAT = register("cooked_dragon_organ_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_DRAGON_ORGAN_MEAT_FOOD_COMPONENT));
		});
		RAW_DRAGON_MEAT = register("raw_dragon_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_DRAGON_MEAT_FOOD_COMPONENT));
		});
		COOKED_DRAGON_MEAT = register("cooked_dragon_meat", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_DRAGON_MEAT_FOOD_COMPONENT));
		});
		RAW_DRAGON_SAUSAGE = register("raw_dragon_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_DRAGON_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_DRAGON_SAUSAGE = register("dragon_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_DRAGON_SAUSAGE_FOOD_COMPONENT));
		});
		RAW_RICH_DRAGON_SAUSAGE = register("raw_rich_dragon_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.RAW_RICH_DRAGON_SAUSAGE_FOOD_COMPONENT));
		});
		COOKED_RICH_DRAGON_SAUSAGE = register("rich_dragon_sausage", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.COOKED_RICH_DRAGON_SAUSAGE_FOOD_COMPONENT));
		});
		CUD = ITEMS.register("cud", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.CUD_FOOD_COMPONENT));
		});
		FURNACE_POWER = ITEMS.register("furnace_power", () -> {
			return new Item(FOOD_ITEM_SETTINGS.food(CCFoodComponents.FURNACE_POWER_FOOD_COMPONENT));
		});
	}
}
