package net.tigereye.chestcavity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.tigereye.chestcavity.blocks.CultureVatBlockEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class CultureVatRenderer implements BlockEntityRenderer<CultureVatBlockEntity> {
    private static final float INNER_MIN = 0.14F;
    private static final float INNER_MAX = 0.86F;
    private static final float FLOOR = 0.0625F;
    private static final float CEILING = 0.9375F;
    private static final RenderType FLUID_LAYER = RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS);

    private final ItemRenderer itemRenderer;

    public CultureVatRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(CultureVatBlockEntity vat, float partialTick, PoseStack pose, MultiBufferSource buffers, int light, int overlay) {
        FluidStack fluid = vat.getFluid();
        float fluidHeight = 0.0F;
        if (!fluid.isEmpty()) {
            fluidHeight = FLOOR + (CEILING - FLOOR) * vat.getRenderedFluidLevel();
            renderFluid(fluid, pose, buffers, light, fluidHeight);
        }
        ItemStack organ = vat.getOrgan();
        if (organ.isEmpty()) {
            return;
        }
        float centerY = Math.max(fluidHeight / 2.0F, 0.0F);
        double bob = Math.sin((vat.getLevel() == null ? 0.0D : vat.getLevel().getGameTime()) * 0.08D) * 0.03D;
        pose.pushPose();
        pose.translate(0.5F, centerY + bob, 0.5F);
        pose.mulPose(Axis.YP.rotationDegrees((vat.getLevel() == null ? 0.0F : vat.getLevel().getGameTime() + partialTick) * 1.5F));
        pose.scale(0.5F, 0.5F, 0.5F);
        this.itemRenderer.renderStatic(organ, ItemDisplayContext.GROUND, light, overlay, pose, buffers, vat.getLevel(), 0);
        pose.popPose();
    }

    private static TextureAtlasSprite getSprite(net.minecraft.resources.ResourceLocation texture) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
    }

    private static void renderFluid(FluidStack fluid, PoseStack pose, MultiBufferSource buffers, int light, float top) {
        IClientFluidTypeExtensions properties = IClientFluidTypeExtensions.of(fluid.getFluid());
        TextureAtlasSprite stillSprite = getSprite(properties.getStillTexture(fluid));
        TextureAtlasSprite flowingSprite = getSprite(properties.getFlowingTexture(fluid));
        int color = properties.getTintColor(fluid);
        float red = ((color >> 16) & 0xFF) / 255.0F;
        float green = ((color >> 8) & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >>> 24) & 0xFF) / 255.0F;
        if (alpha <= 0.0F) {
            alpha = 1.0F;
        }
        float stillU0 = stillSprite.getU0();
        float stillU1 = stillSprite.getU1();
        float stillV0 = stillSprite.getV0();
        float stillV1 = stillSprite.getV1();
        float flowingU0 = flowingSprite.getU0();
        float flowingU1 = flowingSprite.getU1();
        float flowingV0 = flowingSprite.getV0();
        float flowingV1 = flowingSprite.getV1();

        pose.pushPose();
        Matrix4f matrix = pose.last().pose();
        Matrix3f normal = pose.last().normal();
        VertexConsumer consumer = buffers.getBuffer(FLUID_LAYER);

        consumer.vertex(matrix, INNER_MIN, top, INNER_MIN).color(red, green, blue, alpha).uv(stillU0, stillV0).overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix, INNER_MIN, top, INNER_MAX).color(red, green, blue, alpha).uv(stillU0, stillV1).overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix, INNER_MAX, top, INNER_MAX).color(red, green, blue, alpha).uv(stillU1, stillV1).overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix, INNER_MAX, top, INNER_MIN).color(red, green, blue, alpha).uv(stillU1, stillV0).overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();

        float bottomV = flowingV0 + (flowingV1 - flowingV0) * (top - FLOOR) / (CEILING - FLOOR);
        addWall(consumer, matrix, normal, INNER_MIN, FLOOR, INNER_MIN, INNER_MAX, FLOOR, INNER_MIN, top, flowingU0, flowingU1, bottomV, flowingV0, red, green, blue, alpha, light, 0.0F, 0.0F, -1.0F);
        addWall(consumer, matrix, normal, INNER_MAX, FLOOR, INNER_MAX, INNER_MIN, FLOOR, INNER_MAX, top, flowingU0, flowingU1, bottomV, flowingV0, red, green, blue, alpha, light, 0.0F, 0.0F, 1.0F);
        addWall(consumer, matrix, normal, INNER_MAX, FLOOR, INNER_MIN, INNER_MAX, FLOOR, INNER_MAX, top, flowingU0, flowingU1, bottomV, flowingV0, red, green, blue, alpha, light, 1.0F, 0.0F, 0.0F);
        addWall(consumer, matrix, normal, INNER_MIN, FLOOR, INNER_MAX, INNER_MIN, FLOOR, INNER_MIN, top, flowingU0, flowingU1, bottomV, flowingV0, red, green, blue, alpha, light, -1.0F, 0.0F, 0.0F);

        pose.popPose();
    }

    private static void addWall(VertexConsumer consumer, Matrix4f matrix, Matrix3f normal,
                                float x1, float y1, float z1, float x2, float y2, float z2,
                                float top, float u0, float u1, float topV, float bottomV,
                                float red, float green, float blue, float alpha, int light,
                                float nx, float ny, float nz) {
        consumer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).uv(u0, topV).overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, nx, ny, nz).endVertex();
        consumer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).uv(u1, topV).overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, nx, ny, nz).endVertex();
        consumer.vertex(matrix, x2, top, z2).color(red, green, blue, alpha).uv(u1, bottomV).overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, nx, ny, nz).endVertex();
        consumer.vertex(matrix, x1, top, z1).color(red, green, blue, alpha).uv(u0, bottomV).overlayCoords(net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, nx, ny, nz).endVertex();
    }
}
