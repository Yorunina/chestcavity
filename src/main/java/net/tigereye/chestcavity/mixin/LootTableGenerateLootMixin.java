package net.tigereye.chestcavity.mixin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
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
        List var10000 = this.interceptedLoot;
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

        this.interceptedLoot.addAll(Listeners.addLoot(((LootTable)this).m_79122_(), context));
        this.interceptedLoot = Listeners.modifyLoot(((LootTable)this).m_79122_(), context, this.interceptedLoot);
        if (ChestCavity.config.DEBUG_MODE) {
            ChestCavity.LOGGER.debug(this.interceptedLoot.size() + " itemStacks returned");
        }

        Consumer<ItemStack> processedConsumer = LootTable.m_246283_(context.m_78952_(), this.interceptedConsumer);
        Iterator var5 = this.interceptedLoot.iterator();

        while(var5.hasNext()) {
            ItemStack stack = (ItemStack)var5.next();
            processedConsumer.accept(stack);
        }

    }
}
