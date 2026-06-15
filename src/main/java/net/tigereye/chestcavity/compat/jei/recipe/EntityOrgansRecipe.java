package net.tigereye.chestcavity.compat.jei.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EntityOrgansRecipe {
    private final ResourceLocation id;
    private final EntityType<?> entityType;
    private final List<ItemStack> organItems;

    public EntityOrgansRecipe(ResourceLocation id, EntityType<?> entityType, List<ItemStack> organItems) {
        this.id = id;
        this.entityType = entityType;
        this.organItems = organItems;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public EntityType<?> getEntityType() {
        return this.entityType;
    }

    public List<ItemStack> getOrganItems() {
        return this.organItems;
    }
}