package net.tigereye.chestcavity.compat.tinker;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.tigereye.chestcavity.ChestCavity;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.part.IMaterialItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class TinkerItemRegistration {
    private static final ItemDeferredRegisterExtension ITEMS = new ItemDeferredRegisterExtension(ChestCavity.MODID);
    private static final Item.Properties PARTS_PROPS = new Item.Properties();

    private TinkerItemRegistration() {
    }

    public static void init(IEventBus eventBus) {
        OrganToolStats.register();
        ITEMS.register(eventBus);

        eventBus.addListener(TinkerItemRegistration::initMaterialStats);
    }

    public static void initMaterialStats(InterModEnqueueEvent evt) {
        MaterialRegistry.getInstance().registerStatType(CrystalMaterialStats.TYPE);
        MaterialRegistry.getInstance().registerStatType(OrganShellMaterialStats.TYPE);
    }

    public static void addTabItems(CreativeModeTab.Output output) {
        Consumer<ItemStack> outputConsumer = output::accept;
        acceptTool(outputConsumer, TINKER_HEART);
        accept(outputConsumer, HEART_SHELL);
        accept(outputConsumer, CRYSTAL);
        output.accept(HEART_SHELL_CAST.get());
        output.accept(HEART_SHELL_CAST.getRedSand());
        output.accept(HEART_SHELL_CAST.getSand());
        output.accept(CRYSTAL_SHELL_CAST.get());
        output.accept(CRYSTAL_SHELL_CAST.getRedSand());
        output.accept(CRYSTAL_SHELL_CAST.getSand());
    }

    private static void acceptTool(Consumer<ItemStack> output, Supplier<? extends IModifiable> tool) {
        ToolBuildHandler.addVariants(output, tool.get(), "");
    }

    private static void accept(Consumer<ItemStack> output, Supplier<? extends IMaterialItem> item) {
        item.get().addVariants(output, "");
    }

    public static final ItemObject<ModifiableItem> TINKER_HEART = ITEMS.register("tinker_heart", () -> new TinkerOrganItem(PARTS_PROPS, OrganItemDefinition.TINKER_HEART));

    public static final ItemObject<ToolPartItem> HEART_SHELL = ITEMS.register("heart_shell", () -> new ToolPartItem(PARTS_PROPS, OrganShellMaterialStats.ID));
    public static final ItemObject<ToolPartItem> CRYSTAL = ITEMS.register("crystal", () -> new ToolPartItem(PARTS_PROPS, CrystalMaterialStats.ID));
    public static final CastItemObject HEART_SHELL_CAST = ITEMS.registerCast(HEART_SHELL, PARTS_PROPS.stacksTo(64));
    public static final CastItemObject CRYSTAL_SHELL_CAST = ITEMS.registerCast(CRYSTAL, PARTS_PROPS.stacksTo(64));
}
