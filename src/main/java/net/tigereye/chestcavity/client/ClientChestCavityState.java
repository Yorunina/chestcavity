package net.tigereye.chestcavity.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstanceFactory;
import net.tigereye.chestcavity.chestcavities.json.ccType.ChestCavityTypeManager;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.util.NetworkUtil;

import net.minecraft.resources.ResourceLocation;
import java.util.Map;

/**
 * Client-only application of the existing local-player chest cavity state.
 */
public final class ClientChestCavityState {
    private ClientChestCavityState() {
    }

    public static void apply(boolean opened, Map<ResourceLocation, Float> organScores) {
        Player player = Minecraft.getInstance().player;
        if (player == null || !(player instanceof ChestCavityEntity chestCavityEntity)) {
            return;
        }

        ChestCavityInstance instance = chestCavityEntity.getChestCavityInstance();
        if (instance == null) {
            if (!ChestCavityTypeManager.ChestCavityTypes.containsKey(
                    ChestCavityInstanceFactory.DEFAULT_CHEST_CAVITY_TYPE
            )) {
                ChestCavity.LOGGER.warn("Received chest cavity state before client data snapshot was ready");
                return;
            }
            instance = ChestCavityInstanceFactory.newChestCavityInstance(EntityType.PLAYER, player);
            chestCavityEntity.setChestCavityInstance(instance);
        }

        instance.opened = opened;
        instance.setOrganScores(organScores);
        NetworkUtil.SendC2SChestCavityReceivedUpdatePacket(instance);
    }
}
