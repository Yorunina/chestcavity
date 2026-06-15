package net.tigereye.chestcavity.compat.jei.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class OrganSourceRecipe {
    private final ResourceLocation id;
    private final ItemStack organItem;
    private final List<EntityType<?>> sourceEntities;

    public OrganSourceRecipe(ResourceLocation id, ItemStack organItem, List<EntityType<?>> sourceEntities) {
        this.id = id;
        this.organItem = organItem;
        this.sourceEntities = sourceEntities;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public ItemStack getOrganItem() {
        return this.organItem;
    }

    public List<EntityType<?>> getSourceEntities() {
        return this.sourceEntities;
    }
}