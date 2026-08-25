package net.tigereye.chestcavity.compat.jei;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.ChestCavityType;
import net.tigereye.chestcavity.chestcavities.json.ChestCavityDataRepository;
import net.tigereye.chestcavity.compat.jei.recipe.EntityOrgansRecipe;
import net.tigereye.chestcavity.compat.jei.recipe.OrganSourceRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JeiRecipeBuilder {

    public static List<OrganSourceRecipe> buildOrganSourceRecipes() {
        List<OrganSourceRecipe> recipes = new ArrayList<>();
        Map<ResourceLocation, Set<EntityType<?>>> organToEntitiesSetMap = new HashMap<>();

        for (Map.Entry<ResourceLocation, ResourceLocation> assignment : ChestCavityDataRepository.getCurrent().getAssignments().entrySet()) {
            ResourceLocation entityId = assignment.getKey();
            ResourceLocation chestCavityTypeId = assignment.getValue();

            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityId);
            if (entityType == null) continue;

            ChestCavityType chestCavityType = ChestCavityDataRepository.getCurrent().getChestCavityType(chestCavityTypeId);
            if (chestCavityType == null) continue;

            ChestCavityInventory inventory = chestCavityType.getDefaultChestCavity();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);
                if (itemStack.isEmpty()) continue;

                ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
                if (itemId == null) continue;

                organToEntitiesSetMap.computeIfAbsent(itemId, k -> new HashSet<>()).add(entityType);
            }
        }

        for (Map.Entry<ResourceLocation, Set<EntityType<?>>> entry : organToEntitiesSetMap.entrySet()) {
            ResourceLocation itemId = entry.getKey();
            List<EntityType<?>> entities = new ArrayList<>(entry.getValue());

            ItemStack organItem = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId));
            ResourceLocation recipeId = new ResourceLocation(ChestCavity.MODID, "organ_source/" + itemId.getPath());

            recipes.add(new OrganSourceRecipe(recipeId, organItem, entities));
        }

        return recipes;
    }

    public static List<EntityOrgansRecipe> buildEntityOrgansRecipes() {
        List<EntityOrgansRecipe> recipes = new ArrayList<>();

        for (Map.Entry<ResourceLocation, ResourceLocation> assignment : ChestCavityDataRepository.getCurrent().getAssignments().entrySet()) {
            ResourceLocation entityId = assignment.getKey();
            ResourceLocation chestCavityTypeId = assignment.getValue();

            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityId);
            if (entityType == null) continue;

            ChestCavityType chestCavityType = ChestCavityDataRepository.getCurrent().getChestCavityType(chestCavityTypeId);
            if (chestCavityType == null) continue;

            ChestCavityInventory inventory = chestCavityType.getDefaultChestCavity();
            List<ItemStack> organItems = new ArrayList<>();
            Set<ResourceLocation> addedItems = new HashSet<>();

            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack itemStack = inventory.getItem(i);
                if (!itemStack.isEmpty()) {
                    ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(itemStack.getItem());
                    if (itemId != null && !addedItems.contains(itemId)) {
                        addedItems.add(itemId);
                        organItems.add(itemStack.copy());
                    }
                }
            }

            if (!organItems.isEmpty()) {
                ResourceLocation recipeId = new ResourceLocation(ChestCavity.MODID, "entity_organs/" + entityId.getNamespace() + "_" + entityId.getPath());
                recipes.add(new EntityOrgansRecipe(recipeId, entityType, organItems));
            }
        }

        return recipes;
    }
}
