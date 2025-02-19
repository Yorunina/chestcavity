package net.tigereye.chestcavity.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({SimpleContainer.class})
public abstract class MixinSimpleContainer implements Container {
    @Shadow @Final private NonNullList<ItemStack> items;

    public MixinSimpleContainer() {
    }

    @Unique
    public void setItemNoUpdate(int pIndex, ItemStack pStack) {
        this.items.set(pIndex, pStack);
        if (!pStack.isEmpty() && pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }
    }

}
