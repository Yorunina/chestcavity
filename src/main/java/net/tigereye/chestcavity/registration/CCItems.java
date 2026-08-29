package net.tigereye.chestcavity.registration;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.items.ChestOpener;
import net.tigereye.chestcavity.items.OrganCatalogue;
import net.tigereye.chestcavity.items.SurgicalBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class CCItems {
    public static final DeferredRegister<Item> ITEMS;
    public static final Item.Properties CHEST_OPENER_SETTINGS;
    public static final RegistryObject<Item> CHEST_OPENER;
    public static final RegistryObject<Item> SURGICAL_BOX;
    public static final RegistryObject<Item> ORGAN_CATALOGUE;

    public static final Collection<RegistryObject<Item>> ITEMS_FOR_TAB_LIST = new ArrayList<>();

    public CCItems() {
    }

    public static RegistryObject<Item> register(final String name, final Supplier<? extends Item> sup) {
        RegistryObject<Item> newItem = ITEMS.register(name, sup);
        ITEMS_FOR_TAB_LIST.add(newItem);
        return newItem;
    }

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ChestCavity.MODID);
        CHEST_OPENER_SETTINGS = (new Item.Properties()).stacksTo(1);
        CHEST_OPENER = register("chest_opener", ChestOpener::new);
        SURGICAL_BOX = register("surgical_box", SurgicalBox::new);
        ORGAN_CATALOGUE = register("organ_catalogue", OrganCatalogue::new);
    }
}
