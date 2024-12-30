package net.tigereye.chestcavity.listeners;

import java.util.Iterator;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.forge.EventHelper;
import net.tigereye.chestcavity.registration.CCKeybindings;
import net.tigereye.chestcavity.registration.CCOrganScores;
import net.tigereye.chestcavity.util.NetworkUtil;

public class KeybindingClientListeners {
    public KeybindingClientListeners() {
    }

    public static void register() {
        EventHelper.CLIENT_TICK.register((client) -> {
            label20:
            while(true) {
                if (CCKeybindings.UTILITY_ABILITIES.consumeClick()) {
                    if (Minecraft.getInstance().player == null) {
                        continue;
                    }

                    Iterator<ResourceLocation> var1 = CCKeybindings.UTILITY_ABILITY_LIST.iterator();

                    while(true) {
                        if (!var1.hasNext()) {
                            continue label20;
                        }

                        ResourceLocation i = var1.next();
                        NetworkUtil.SendC2SChestCavityHotkeyPacket(i);
                    }
                }

                return;
            }
        });
        EventHelper.CLIENT_TICK.register((client) -> {
            label20:
            while(true) {
                if (CCKeybindings.ATTACK_ABILITIES.consumeClick()) {
                    if (Minecraft.getInstance().player == null) {
                        continue;
                    }

                    Iterator<ResourceLocation> var1 = CCKeybindings.ATTACK_ABILITY_LIST.iterator();

                    while(true) {
                        if (!var1.hasNext()) {
                            continue label20;
                        }

                        ResourceLocation i = var1.next();
                        NetworkUtil.SendC2SChestCavityHotkeyPacket(i);
                    }
                }

                return;
            }
        });
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
        register(CCKeybindings.SILK, CCOrganScores.SILK);
    }

    public static void register(KeyMapping keybinding, ResourceLocation id) {
        EventHelper.CLIENT_TICK.register((client) -> {
            while(keybinding.consumeClick()) {
                if (Minecraft.getInstance().player != null) {
                    NetworkUtil.SendC2SChestCavityHotkeyPacket(id);
                }
            }

        });
    }
}
