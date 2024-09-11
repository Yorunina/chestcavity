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
        if (lootContext.m_78936_(LootContextParams.f_81456_)) {
            Entity entity = (Entity)lootContext.m_78953_(LootContextParams.f_81455_);
            Optional<ChestCavityEntity> chestCavityEntity = ChestCavityEntity.of(entity);
            if (!chestCavityEntity.isPresent()) {
                return loot;
            }

            ChestCavityInstance cc = ((ChestCavityEntity)chestCavityEntity.get()).getChestCavityInstance();
            if (cc.opened) {
                return loot;
            }

            Entity killer_ = (Entity)lootContext.m_78953_(LootContextParams.f_81458_);
            int lootingLevel;
            RandomSource random;
            if (killer_ instanceof LivingEntity) {
                LivingEntity killer = (LivingEntity)killer_;
                if (EnchantmentHelper.m_44836_((Enchantment)CCEnchantments.TOMOPHOBIA.get(), killer) > 0) {
                    return loot;
                }

                lootingLevel = EnchantmentHelper.m_44836_(Enchantments.f_44982_, killer);
                lootingLevel += EnchantmentHelper.m_44836_((Enchantment)CCEnchantments.SURGICAL.get(), killer) * 2;
                if (killer.m_21120_(killer.m_7655_()).m_204117_(CCTags.BUTCHERING_TOOL)) {
                    lootingLevel = 10 + 10 * lootingLevel;
                }

                random = lootContext.m_230907_();
            } else {
                lootingLevel = 0;
                random = RandomSource.m_216327_();
            }

            loot.addAll(cc.getChestCavityType().generateLootDrops(random, lootingLevel));
        }

        return loot;
    }

    public static List<ItemStack> modifyEntityLoot(LootContextParamSet type, LootContext lootContext, List<ItemStack> loot) {
        if (lootContext.m_78936_(LootContextParams.f_81458_)) {
            Entity killer_ = (Entity)lootContext.m_78953_(LootContextParams.f_81458_);
            if (killer_ instanceof LivingEntity) {
                LivingEntity killer = (LivingEntity)killer_;
                if (killer.m_21120_(killer.m_7655_()).m_204117_(CCTags.BUTCHERING_TOOL)) {
                    Map<SalvageRecipe, Integer> salvageResults = new HashMap();
                    Iterator<ItemStack> i = loot.iterator();
                    Iterator var8;
                    if (salvageRecipeList == null) {
                        salvageRecipeList = new ArrayList();
                        List<CraftingRecipe> recipes = killer.m_9236_().m_7465_().m_44013_(RecipeType.f_44107_);
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
                                        ItemStack out = recipexx.m_8043_(killer.m_9236_().m_9598_());
                                        out.m_41764_(out.m_41613_() * (count / recipexx.getRequired()));
                                        loot.add(out);
                                    });
                                    break label53;
                                }

                                stackx = (ItemStack)i.next();
                            } while(!stackx.m_204117_(CCTags.SALVAGEABLE));

                            var8 = salvageRecipeList.iterator();

                            while(var8.hasNext()) {
                                SalvageRecipe recipe = (SalvageRecipe)var8.next();
                                if (recipe.getInput().test(stackx)) {
                                    salvageResults.put(recipe, (Integer)salvageResults.getOrDefault(recipe, 0) + stackx.m_41613_());
                                    i.remove();
                                    break;
                                }
                            }
                        }
                    }
                }

                if (EnchantmentHelper.m_44843_((Enchantment)CCEnchantments.MALPRACTICE.get(), killer.m_21120_(killer.m_7655_())) > 0) {
                    Iterator<ItemStack> var10 = loot.iterator();

                    while(var10.hasNext()) {
                        ItemStack stack = (ItemStack)var10.next();
                        if (OrganManager.isTrueOrgan(stack.m_41720_())) {
                            stack.m_41663_((Enchantment)CCEnchantments.MALPRACTICE.get(), 1);
                        }
                    }
                }
            }
        }

        return loot;
    }
}
