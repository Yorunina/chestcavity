package net.tigereye.chestcavity.bootstrap;

import net.minecraftforge.eventbus.api.IEventBus;
import net.tigereye.chestcavity.mob_effect.ModPotions;
import net.tigereye.chestcavity.registration.CCAttributes;
import net.tigereye.chestcavity.registration.CCEnchantments;
import net.tigereye.chestcavity.registration.CCItems;
import net.tigereye.chestcavity.registration.CCRegistries;
import net.tigereye.chestcavity.registration.CCStatusEffects;

public final class ChestCavityRegistryBootstrap {
    private ChestCavityRegistryBootstrap() {
    }

    public static void register(IEventBus eventBus) {
        CCItems.ITEMS.register(eventBus);
        CCRegistries.CREATIVE_TABS.register(eventBus);
        CCEnchantments.ENCHANTMENTS.register(eventBus);
        CCStatusEffects.MOB_EFFECTS.register(eventBus);
        CCAttributes.ATTRIBUTE.register(eventBus);
        CCRegistries.MENU_TYPES.register(eventBus);
        ModPotions.register(eventBus);
        eventBus.addListener(CCAttributes::onEntityAttributeModification);
    }
}
