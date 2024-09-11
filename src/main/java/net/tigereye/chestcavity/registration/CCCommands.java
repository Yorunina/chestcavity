package net.tigereye.chestcavity.registration;

import com.mojang.brigadier.context.CommandContext;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.tigereye.chestcavity.interfaces.ChestCavityEntity;
import net.tigereye.chestcavity.util.ChestCavityUtil;

public class CCCommands {
    public CCCommands() {
    }

    public static void register() {
    }

    public static int getScoresNoArgs(CommandContext<CommandSourceStack> context) {
        Entity entity;
        try {
            entity = ((CommandSourceStack)context.getSource()).getEntity();
        } catch (Exception var3) {
            ((CommandSourceStack)context.getSource()).sendFailure(Component.translatable("getScores failed to get entity"));
            return -1;
        }

        Optional<ChestCavityEntity> optional = ChestCavityEntity.of(entity);
        if (optional.isPresent()) {
            ChestCavityUtil.outputOrganScoresString((string) -> {
                ((CommandSourceStack)context.getSource()).sendSuccess(() -> {
                    return Component.translatable(string);
                }, false);
            }, ((ChestCavityEntity)optional.get()).getChestCavityInstance());
            return 1;
        } else {
            return 0;
        }
    }

    public static int getScores(CommandContext<CommandSourceStack> context) {
        Entity entity;
        try {
            entity = EntityArgument.getEntity(context, "entity");
        } catch (Exception var3) {
            ((CommandSourceStack)context.getSource()).sendFailure(Component.translatable("getScores failed to get entity"));
            return -1;
        }

        Optional<ChestCavityEntity> optional = ChestCavityEntity.of(entity);
        if (optional.isPresent()) {
            ChestCavityUtil.outputOrganScoresString((string) -> {
                ((CommandSourceStack)context.getSource()).sendSuccess(() -> {
                    return Component.translatable(string);
                }, false);
            }, ((ChestCavityEntity)optional.get()).getChestCavityInstance());
            return 1;
        } else {
            return 0;
        }
    }

    public static int resetChestCavityNoArgs(CommandContext<CommandSourceStack> context) {
        Entity entity;
        try {
            entity = ((CommandSourceStack)context.getSource()).getEntity();
        } catch (Exception var3) {
            ((CommandSourceStack)context.getSource()).sendFailure(Component.translatable("resetChestCavity failed to get entity"));
            return -1;
        }

        Optional<ChestCavityEntity> optional = ChestCavityEntity.of(entity);
        if (optional.isPresent()) {
            ChestCavityUtil.generateChestCavityIfOpened(((ChestCavityEntity)optional.get()).getChestCavityInstance());
            return 1;
        } else {
            return 0;
        }
    }

    public static int resetChestCavity(CommandContext<CommandSourceStack> context) {
        Entity entity;
        try {
            entity = EntityArgument.getEntity(context, "entity");
        } catch (Exception var3) {
            ((CommandSourceStack)context.getSource()).sendFailure(Component.translatable("getChestCavity failed to get entity"));
            return -1;
        }

        Optional<ChestCavityEntity> optional = ChestCavityEntity.of(entity);
        if (optional.isPresent()) {
            ChestCavityUtil.generateChestCavityIfOpened(((ChestCavityEntity)optional.get()).getChestCavityInstance());
            return 1;
        } else {
            return 0;
        }
    }
}
