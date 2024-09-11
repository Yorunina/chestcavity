package net.tigereye.chestcavity.forge.events;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.tigereye.chestcavity.chestcavities.organs.OrganManager;
import net.tigereye.chestcavity.chestcavities.types.json.GeneratedChestCavityAssignmentManager;
import net.tigereye.chestcavity.chestcavities.types.json.GeneratedChestCavityTypeManager;
import net.tigereye.chestcavity.registration.CCCommands;

@EventBusSubscriber(
        modid = "chestcavity",
        bus = Bus.FORGE
)
public class CommonForgeEventBusSubscriber {
    public CommonForgeEventBusSubscriber() {
    }

    @SubscribeEvent
    public static void addListeners(AddReloadListenerEvent event) {
        event.addListener(new OrganManager());
        event.addListener(new GeneratedChestCavityTypeManager());
        event.addListener(new GeneratedChestCavityAssignmentManager());
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register((LiteralArgumentBuilder)Commands.m_82127_("chestcavity").then(((LiteralArgumentBuilder)Commands.m_82127_("getscores").executes(CCCommands::getScoresNoArgs)).then(Commands.m_82129_("entity", EntityArgument.m_91449_()).executes(CCCommands::getScores))));
        dispatcher.register((LiteralArgumentBuilder)Commands.m_82127_("chestcavity").then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.m_82127_("resetChestCavity").requires((source) -> {
            return source.m_6761_(2);
        })).executes(CCCommands::resetChestCavityNoArgs)).then(Commands.m_82129_("entity", EntityArgument.m_91449_()).executes(CCCommands::resetChestCavity))));
    }
}
