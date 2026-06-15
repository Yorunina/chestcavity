package net.tigereye.chestcavity.compat.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RenderHelper {
    public static final RenderHelper INSTANCE = new RenderHelper();
    private final Map<EntityType<?>, LivingEntity> entityCache = new HashMap<>();

    public void renderEntity(GuiGraphics guiGraphics, int x, int y, double scale, double yaw, double pitch, LivingEntity livingEntity) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 50.0);
        poseStack.scale((float) -scale, (float) scale, (float) scale);
        
        poseStack.mulPose(new Quaternionf().rotationZ((float) Math.PI));
        
        float yRot = (float) Math.atan(yaw / 40.0) * 40.0F;
        float xRot = -((float) Math.atan(pitch / 40.0)) * 20.0F;
        
        livingEntity.setYRot(yRot);
        livingEntity.setYBodyRot(yRot);
        livingEntity.setXRot(xRot);
        livingEntity.yHeadRot = yRot;
        livingEntity.yHeadRotO = yRot;

        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, poseStack, bufferSource, 15728880);
        });
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        
        poseStack.popPose();
    }

    public LivingEntity getOrCreateEntity(EntityType<?> entityType) {
        LivingEntity cachedEntity = entityCache.get(entityType);
        if (cachedEntity != null && cachedEntity.isAlive()) {
            return cachedEntity;
        }

        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return null;
        }

        try {
            LivingEntity entity = (LivingEntity) entityType.create(level);
            if (entity != null) {
                entity.setUUID(UUID.randomUUID());
                entityCache.put(entityType, entity);
                return entity;
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    public void clearCache() {
        entityCache.clear();
    }
}