package net.tigereye.chestcavity.listeners;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.registration.CCKeybindings;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.util.NetworkUtil;

@Mod.EventBusSubscriber(modid = ChestCavity.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeybindingClientListeners {
    public KeybindingClientListeners() {
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        handleInputEvent(event.getKey(), event.getAction());
    }

    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseButton.Pre event) {
        handleInputEvent(event.getButton(), event.getAction());
    }

    private static void handleInputEvent(int button, int action) {
        var minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) {
            return;
        }

        if (CCKeybindings.UTILITY_ABILITIES.isDown()) {
            for (ResourceLocation i : CCKeybindings.UTILITY_ABILITY_LIST) {
                NetworkUtil.SendC2SChestCavityHotkeyPacket(i);
            }
        }

        if (CCKeybindings.ATTACK_ABILITIES.isDown()) {
            for (ResourceLocation i : CCKeybindings.ATTACK_ABILITY_LIST) {
                NetworkUtil.SendC2SChestCavityHotkeyPacket(i);
            }
        }


        register(CCKeybindings.CREEPY, CCOrganScores.CREEPY);
        register(CCKeybindings.DRAGON_BREATH, CCOrganScores.DRAGON_BREATH);
        register(CCKeybindings.DRAGON_BOMBS, CCOrganScores.DRAGON_BOMBS);
        register(CCKeybindings.FORCEFUL_SPIT, CCOrganScores.FORCEFUL_SPIT);
        register(CCKeybindings.FURNACE_POWERED, CCOrganScores.FURNACE_POWERED);
        register(CCKeybindings.IRON_REPAIR, CCOrganScores.IRON_REPAIR);
        register(CCKeybindings.GHASTLY, CCOrganScores.GHASTLY);
        register(CCKeybindings.GRAZING, CCOrganScores.GRAZING);
        register(CCKeybindings.PYROMANCY, CCOrganScores.PYROMANCY);
        register(CCKeybindings.SHULKER_BULLETS, CCOrganScores.SHULKER_BULLETS);
    }

    public static void register(KeyMapping keybinding, ResourceLocation id) {
        if (keybinding.isDown()) {
            NetworkUtil.SendC2SChestCavityHotkeyPacket(id);
        }
    }
}
