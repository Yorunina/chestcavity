package net.tigereye.chestcavity.compat.tinker;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.tigereye.chestcavity.ChestCavity;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;

public class TinkerItemRegistration {
    private static final ItemDeferredRegisterExtension ITEMS = new ItemDeferredRegisterExtension(ChestCavity.MODID);
    private static final Item.Properties PARTS_PROPS = new Item.Properties();
    public TinkerItemRegistration() {
    }

    public static void init(IEventBus eventBus) {
//        OrganToolStates.register();
        ITEMS.register(eventBus);
    }
    public static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.accept(TINKER_HEART.get());
//        output.accept(GENESIS_SIMULATION_CORE.get());
//        output.accept(GENESIS_SIMULATION_CORE_CAST.get());
    }
    public static final ItemObject<Item> TINKER_HEART = ITEMS.register("tinker_heart", () -> new TinkerOrganItem(PARTS_PROPS.stacksTo(1), OrganItemDefinition.TINKER_HEART));
//    public static final ItemObject<ToolPartItem> GENESIS_SIMULATION_CORE = ITEMS.register("genesis_simulation_core", () -> new ToolPartItem(PARTS_PROPS.stacksTo(1), HeadMaterialStats.ID));
//    public static final ItemObject<Item> GENESIS_SIMULATION_CORE_CAST = ITEMS.register("genesis_simulation_core_cast", () -> new PartCastItem(PARTS_PROPS.rarity(Rarity.UNCOMMON), GENESIS_SIMULATION_CORE));
}
