package net.tigereye.chestcavity.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.organs.OrganManager;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.recipes.SalvageRecipe;
import net.tigereye.chestcavity.registration.CCEnchantments;
import net.tigereye.chestcavity.registration.CCTags;

public class LootRegister {
    private static List<SalvageRecipe> salvageRecipeList;

    public LootRegister() {
    }

    public static void register() {
    }

    public static List<ItemStack> addEntityLoot(LootContextParamSet type, LootContext lootContext) {
        List<ItemStack> loot = new ArrayList();
        if (lootContext.hasParam(LootContextParams.LAST_DAMAGE_PLAYER)) {
            Entity entity = (Entity)lootContext.getParam(LootContextParams.THIS_ENTITY);
            Optional<ChestCavityEntity> chestCavityEntity = ChestCavityEntity.of(entity);
            if (!chestCavityEntity.isPresent()) {
                return loot;
            }

            ChestCavityInstance cc = ((ChestCavityEntity)chestCavityEntity.get()).getChestCavityInstance();
            if (cc.opened) {
                return loot;
            }

            Entity killer_ = (Entity)lootContext.getParam(LootContextParams.KILLER_ENTITY);
            int lootingLevel;
            RandomSource random;
            if (killer_ instanceof LivingEntity) {
                LivingEntity killer = (LivingEntity)killer_;
                if (EnchantmentHelper.getEnchantmentLevel((Enchantment)CCEnchantments.TOMOPHOBIA.get(), killer) > 0) {
                    return loot;
                }

                lootingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.MOB_LOOTING, killer);
                lootingLevel += EnchantmentHelper.getEnchantmentLevel((Enchantment)CCEnchantments.SURGICAL.get(), killer) * 2;
                if (killer.getItemInHand(killer.getUsedItemHand()).is(CCTags.BUTCHERING_TOOL)) {
                    lootingLevel = 10 + 10 * lootingLevel;
                }

                random = lootContext.getRandom();
            } else {
                lootingLevel = 0;
                random = RandomSource.create();
            }

            loot.addAll(cc.getChestCavityType().generateLootDrops(random, lootingLevel));
        }

        return loot;
    }

    public static List<ItemStack> modifyEntityLoot(LootContextParamSet type, LootContext lootContext, List<ItemStack> loot) {
        if (lootContext.hasParam(LootContextParams.KILLER_ENTITY)) {
            Entity killer_ = (Entity)lootContext.getParam(LootContextParams.KILLER_ENTITY);
            if (killer_ instanceof LivingEntity) {
                LivingEntity killer = (LivingEntity)killer_;
                if (killer.getItemInHand(killer.getUsedItemHand()).is(CCTags.BUTCHERING_TOOL)) {
                    Map<SalvageRecipe, Integer> salvageResults = new HashMap();
                    Iterator<ItemStack> i = loot.iterator();
                    Iterator var8;
                    if (salvageRecipeList == null) {
                        salvageRecipeList = new ArrayList();
                        List<CraftingRecipe> recipes = killer.level().getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
                        var8 = recipes.iterator();

                        while(var8.hasNext()) {
                            CraftingRecipe recipex = (CraftingRecipe)var8.next();
                            if (recipex instanceof SalvageRecipe) {
                                salvageRecipeList.add((SalvageRecipe)recipex);
                            }
                        }
                    }

                    label53:
                    while(true) {
                        while(true) {
                            ItemStack stackx;
                            do {
                                if (!i.hasNext()) {
                                    salvageResults.forEach((recipexx, count) -> {
                                        ItemStack out = recipexx.m_8043_(killer.getServer().registryAccess());
                                        out.setCount(out.getCount() * (count / recipexx.getRequired()));
                                        loot.add(out);
                                    });
                                    break label53;
                                }

                                stackx = (ItemStack)i.next();
                            } while(!stackx.is(CCTags.SALVAGEABLE));

                            var8 = salvageRecipeList.iterator();

                            while(var8.hasNext()) {
                                SalvageRecipe recipe = (SalvageRecipe)var8.next();
                                if (recipe.getInput().test(stackx)) {
                                    salvageResults.put(recipe, (Integer)salvageResults.getOrDefault(recipe, 0) + stackx.getCount());
                                    i.remove();
                                    break;
                                }
                            }
                        }
                    }
                }

                if (EnchantmentHelper.getTagEnchantmentLevel((Enchantment)CCEnchantments.MALPRACTICE.get(), killer.getItemInHand(killer.getUsedItemHand())) > 0) {
                    Iterator<ItemStack> var10 = loot.iterator();

                    while(var10.hasNext()) {
                        ItemStack stack = (ItemStack)var10.next();
                        if (OrganManager.isTrueOrgan(stack.getItem())) {
                            stack.enchant((Enchantment)CCEnchantments.MALPRACTICE.get(), 1);
                        }
                    }
                }
            }
        }

        return loot;
    }
}
