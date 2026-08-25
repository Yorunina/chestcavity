package net.tigereye.chestcavity.listeners;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tigereye.chestcavity.chestcavities.ChestCavityInventory;
import net.tigereye.chestcavity.chestcavities.instance.ChestCavityInstance;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.registration.CCStatusEffects;
import net.tigereye.chestcavity.service.ChestCavitySurgeryService;
import net.tigereye.chestcavity.service.OrganLookupService;

@Mod.EventBusSubscriber
public class EntityDeathListener {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (level.isClientSide()) return;

        if (entity instanceof AbstractGolemEntity<?,?> golemEntity && golemEntity.hasFlag(GolemFlags.RECYCLE)) return;

        ChestCavityEntity ccEntity = (ChestCavityEntity) entity;
        ChestCavityInstance ccInstance = ccEntity.getChestCavityInstance();
        if (entity instanceof Player) return;
        ChestCavityInventory ccInv = ChestCavitySurgeryService.openChestCavity(ccInstance);

        boolean underOrganSlip = entity.hasEffect(CCStatusEffects.ORGAN_SLIP.get());
        for (int i = 0; i < ccInv.getContainerSize(); ++i) {
            ItemStack curItem = ccInv.getItem(i);
            if (!OrganLookupService.isOriginalOrgan(ccInstance, i, curItem) || underOrganSlip) {
                ccInv.removeItemNoUpdate(i);
                ccInstance.owner.spawnAtLocation(curItem);
            }
        }
    }
}
