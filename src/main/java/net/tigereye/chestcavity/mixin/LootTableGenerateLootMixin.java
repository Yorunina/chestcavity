package net.tigereye.chestcavity.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.listeners.Listeners;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Mixin({LootTable.class})
public class LootTableGenerateLootMixin {
    @Unique
    List<ItemStack> interceptedLoot = new ArrayList();
    @Unique
    Consumer<ItemStack> interceptedConsumer;

    public LootTableGenerateLootMixin() {
    }

    @ModifyVariable(
            method = {"getRandomItemsRaw"},
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    public Consumer<ItemStack> generateUnprocessedLootMixin_InterceptConsumerReplaceWithList(Consumer<ItemStack> lootConsumer) {
        this.interceptedLoot.clear();
        this.interceptedConsumer = lootConsumer;
        List<ItemStack> var10000 = this.interceptedLoot;
        Objects.requireNonNull(var10000);
        return var10000::add;
    }

    @Inject(
            method = {"getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V"},
            at = {@At("TAIL")}
    )
    public void generateUnprocessedLootMixin_ModifyPopulatedListAndFeedConsumer(LootContext context, Consumer<ItemStack> lootConsumer, CallbackInfo ci) {
        if (ChestCavity.config.DEBUG_MODE) {
            ChestCavity.LOGGER.debug("modifying " + this.interceptedLoot.size() + " drops");
        }

        this.interceptedLoot.addAll(Listeners.addLoot(((LootTable)(Object)this).getParamSet(), context));
        this.interceptedLoot = Listeners.modifyLoot(((LootTable)(Object)this).getParamSet(), context, this.interceptedLoot);
        if (ChestCavity.config.DEBUG_MODE) {
            ChestCavity.LOGGER.debug(this.interceptedLoot.size() + " itemStacks returned");
        }

        Consumer<ItemStack> processedConsumer = LootTable.createStackSplitter(context.getLevel(), this.interceptedConsumer);

        for (ItemStack stack : this.interceptedLoot) {
            processedConsumer.accept(stack);
        }

    }
}
