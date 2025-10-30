package net.tigereye.chestcavity.compat.ftb;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;
import net.minecraft.resources.ResourceLocation;
import net.tigereye.chestcavity.ChestCavity;

public interface ChestCavityTypes {
    TaskType OPEN_CHEST_CAVITY_TASK_TYPE = TaskTypes.register(new ResourceLocation(ChestCavity.MODID, "open_chest_cavity_task"), OpenChestCavityTask::new, () -> Icon.getIcon(new ResourceLocation(ChestCavity.MODID, "textures/item/chest_opener.png")));
    static void init() {
    }
}
