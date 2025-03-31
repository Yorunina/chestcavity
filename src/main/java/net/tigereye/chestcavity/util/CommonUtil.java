package net.tigereye.chestcavity.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import java.util.UUID;

public class CommonUtil {
    public static Entity getEntityByUUID(ServerLevel level, UUID uuid){
        return level.getEntities().get(uuid);
    }
}
