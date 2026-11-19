package net.tigereye.chestcavity.registration;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.blocks.CultureVatBlockEntity;

public class CCBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ChestCavity.MODID);

    public static final RegistryObject<BlockEntityType<CultureVatBlockEntity>> CULTURE_VAT =
            BLOCK_ENTITIES.register("culture_vat", () -> BlockEntityType.Builder
                    .of(CultureVatBlockEntity::new, CCBlocks.CULTURE_VAT.get())
                    .build(null));
}
