package net.tigereye.chestcavity.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.ITeleporter;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.util.NetworkUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends net.minecraft.world.entity.player.Player {
    public MixinServerPlayer(Level world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(
            method = {"restoreFrom"},
            at = {@At("TAIL")}
    )
    public void copyFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo callbackInfo) {
        ChestCavityEntity.of(this).ifPresent((chestCavityEntity) -> {
            ChestCavityEntity.of(oldPlayer).ifPresent((oldCCPlayerEntityInterface) -> {
                chestCavityEntity.getChestCavityInstance().clone(oldCCPlayerEntityInterface.getChestCavityInstance());
            });
        });
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"changeDimension"},
            remap = false
    )
    public void chestCavityEntityMoveToWorldMixin(ServerLevel destination, ITeleporter teleporter, CallbackInfoReturnable<Entity> info) {
        Entity entity = info.getReturnValue();
        if (entity instanceof ChestCavityEntity && !entity.level().isClientSide) {
            NetworkUtil.SendS2CChestCavityUpdatePacket(((ChestCavityEntity) entity).getChestCavityInstance());
        }
    }
}