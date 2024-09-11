package net.tigereye.chestcavity.listeners;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class Listeners {
    public Listeners() {
    }

    public static List<ItemStack> addLoot(LootContextParamSet type, LootContext context) {
        return AddLootSubEventListener(type, context);
    }

    public static List<ItemStack> modifyLoot(LootContextParamSet type, LootContext context, List<ItemStack> loot) {
        return ModifyLootSubEventListener(type, context, loot);
    }

    public static void register() {
    }

    private static List<ItemStack> AddLootSubEventListener(LootContextParamSet type, LootContext context) {
        List<ItemStack> loot = new ArrayList();
        if (type == LootContextParamSets.ENTITY) {
            loot.addAll(LootRegister.addEntityLoot(type, context));
        }

        return loot;
    }

    private static List<ItemStack> ModifyLootSubEventListener(LootContextParamSet type, LootContext context, List<ItemStack> loot) {
        if (type == LootContextParamSets.ENTITY) {
            loot = LootRegister.modifyEntityLoot(type, context, loot);
        }

        return loot;
    }
}
