package net.tigereye.chestcavity.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

}
