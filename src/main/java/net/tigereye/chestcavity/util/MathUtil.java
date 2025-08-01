package net.tigereye.chestcavity.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;

public class MathUtil {
    public MathUtil() {
    }

    public static float horizontalDistanceTo(Entity entity1, Entity entity2) {
        float f = (float) (entity1.getX() - entity2.getX());
        float h = (float) (entity1.getZ() - entity2.getZ());
        return Mth.sqrt(f * f + h * h);
    }

    public static float getAngle(Vec2 a, Vec2 b) {
        return getAngle(a.x, a.y, b.x, b.y);
    }

    public static float getAngle(double ax, double ay, double bx, double by) {
        return (float) (Math.atan2(by - ay, bx - ax)) + 3.141f;// + (a.x > b.x ? Math.PI : 0));
    }

}
