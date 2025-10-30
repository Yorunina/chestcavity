package net.tigereye.chestcavity.compat.ftb;

import dev.ftb.mods.ftbquests.api.QuestFile;
import dev.ftb.mods.ftbquests.events.ClearFileCacheEvent;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbteams.api.Team;
import dev.ftb.mods.ftbteams.data.TeamManagerImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.tigereye.chestcavity.ChestCavity;

import java.util.ArrayList;
import java.util.List;

public class ChestCavityQuestEventHandler {
    private List<OpenChestCavityTask> chestCavityOpenedTaskList = new ArrayList<>();

    public ChestCavityQuestEventHandler init() {
        ChestCavityTypes.init();
        ClearFileCacheEvent.EVENT.register(this::fileCacheClear);
        return this;
    }

    private void fileCacheClear(QuestFile file) {
        if (file.isServerSide()) {
            this.chestCavityOpenedTaskList = null;
        }
    }

    public void onChestCavityOpened(ServerPlayer player, LivingEntity entity) {
        try {
            if (this.chestCavityOpenedTaskList == null) {
                this.chestCavityOpenedTaskList = ServerQuestFile.INSTANCE.collect(OpenChestCavityTask.class);
            }
            if (this.chestCavityOpenedTaskList.isEmpty()) return;
            Team team = TeamManagerImpl.INSTANCE.getTeamForPlayer(player).orElse(null);
            if (team == null) return;
            TeamData teamData = ServerQuestFile.INSTANCE.getOrCreateTeamData(team);
            for (OpenChestCavityTask task : this.chestCavityOpenedTaskList) {
                if (teamData.getProgress(task) < task.getMaxProgress() && teamData.canStartTasks(task.getQuest())) {
                    task.chestCavityOpened(teamData, entity);
                }
            }
        } catch (Exception e) {
            ChestCavity.LOGGER.error("Error while opening chest cavity: ", e);
        }
    }

}
