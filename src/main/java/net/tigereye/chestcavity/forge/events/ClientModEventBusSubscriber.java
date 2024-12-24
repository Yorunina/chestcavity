package net.tigereye.chestcavity.forge.events;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.tigereye.chestcavity.registration.CCItems;
import net.tigereye.chestcavity.registration.CCKeybindings;

@EventBusSubscriber(
        modid = "chestcavity",
        value = {Dist.CLIENT},
        bus = Bus.MOD
)
public class ClientModEventBusSubscriber {
    public ClientModEventBusSubscriber() {
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(CCKeybindings.UTILITY_ABILITIES);
        event.register(CCKeybindings.ATTACK_ABILITIES);
        event.register(CCKeybindings.CREEPY);
        event.register(CCKeybindings.DRAGON_BREATH);
        event.register(CCKeybindings.DRAGON_BOMBS);
        event.register(CCKeybindings.FORCEFUL_SPIT);
        event.register(CCKeybindings.IRON_REPAIR);
        event.register(CCKeybindings.PYROMANCY);
        event.register(CCKeybindings.GHASTLY);
        event.register(CCKeybindings.GRAZING);
        event.register(CCKeybindings.SHULKER_BULLETS);
        event.register(CCKeybindings.SILK);
    }

    @SubscribeEvent
    public static void creativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(CCItems.CHEST_OPENER);
        } else if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(CCItems.WOODEN_CLEAVER);
            event.accept(CCItems.STONE_CLEAVER);
            event.accept(CCItems.IRON_CLEAVER);
            event.accept(CCItems.DIAMOND_CLEAVER);
            event.accept(CCItems.NETHERITE_CLEAVER);
        } else if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(CCItems.BURNT_MEAT_CHUNK);
            event.accept(CCItems.RAW_ORGAN_MEAT);
            event.accept(CCItems.COOKED_ORGAN_MEAT);
            event.accept(CCItems.RAW_BUTCHERED_MEAT);
            event.accept(CCItems.COOKED_BUTCHERED_MEAT);
            event.accept(CCItems.RAW_SAUSAGE);
            event.accept(CCItems.COOKED_SAUSAGE);
            event.accept(CCItems.RAW_RICH_SAUSAGE);
            event.accept(CCItems.COOKED_RICH_SAUSAGE);
            event.accept(CCItems.RAW_MINI_SAUSAGE);
            event.accept(CCItems.COOKED_MINI_SAUSAGE);
            event.accept(CCItems.RAW_RICH_MINI_SAUSAGE);
            event.accept(CCItems.COOKED_RICH_MINI_SAUSAGE);
            event.accept(CCItems.ROTTEN_SAUSAGE);
            event.accept(CCItems.RAW_TOXIC_ORGAN_MEAT);
            event.accept(CCItems.COOKED_TOXIC_ORGAN_MEAT);
            event.accept(CCItems.RAW_TOXIC_MEAT);
            event.accept(CCItems.COOKED_TOXIC_MEAT);
            event.accept(CCItems.RAW_TOXIC_SAUSAGE);
            event.accept(CCItems.COOKED_TOXIC_SAUSAGE);
            event.accept(CCItems.RAW_RICH_TOXIC_SAUSAGE);
            event.accept(CCItems.COOKED_RICH_TOXIC_SAUSAGE);
            event.accept(CCItems.RAW_HUMAN_ORGAN_MEAT);
            event.accept(CCItems.COOKED_HUMAN_ORGAN_MEAT);
            event.accept(CCItems.RAW_MAN_MEAT);
            event.accept(CCItems.COOKED_MAN_MEAT);
            event.accept(CCItems.RAW_HUMAN_SAUSAGE);
            event.accept(CCItems.COOKED_HUMAN_SAUSAGE);
            event.accept(CCItems.RAW_RICH_HUMAN_SAUSAGE);
            event.accept(CCItems.COOKED_RICH_HUMAN_SAUSAGE);
            event.accept(CCItems.RAW_ALIEN_ORGAN_MEAT);
            event.accept(CCItems.COOKED_ALIEN_ORGAN_MEAT);
            event.accept(CCItems.RAW_ALIEN_MEAT);
            event.accept(CCItems.COOKED_ALIEN_MEAT);
            event.accept(CCItems.RAW_ALIEN_SAUSAGE);
            event.accept(CCItems.COOKED_ALIEN_SAUSAGE);
            event.accept(CCItems.RAW_RICH_ALIEN_SAUSAGE);
            event.accept(CCItems.COOKED_RICH_ALIEN_SAUSAGE);
            event.accept(CCItems.RAW_DRAGON_ORGAN_MEAT);
            event.accept(CCItems.COOKED_DRAGON_ORGAN_MEAT);
            event.accept(CCItems.RAW_DRAGON_MEAT);
            event.accept(CCItems.COOKED_DRAGON_MEAT);
            event.accept(CCItems.RAW_DRAGON_SAUSAGE);
            event.accept(CCItems.COOKED_DRAGON_SAUSAGE);
            event.accept(CCItems.RAW_RICH_DRAGON_SAUSAGE);
            event.accept(CCItems.COOKED_RICH_DRAGON_SAUSAGE);
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(CCItems.SAUSAGE_SKIN);
            event.accept(CCItems.MINI_SAUSAGE_SKIN);
            event.accept(CCItems.IRON_SCRAP);
        }

    }
}
