package net.tigereye.chestcavity.registration;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.blocks.CultureVatBlock;

public class CCBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ChestCavity.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ChestCavity.MODID);

    public static final RegistryObject<Block> CULTURE_VAT = BLOCKS.register("culture_vat",
            CultureVatBlock::new);

    public static final RegistryObject<Item> CULTURE_VAT_ITEM = ITEMS.register("culture_vat",
            () -> new BlockItem(CULTURE_VAT.get(), new Item.Properties()));
}
