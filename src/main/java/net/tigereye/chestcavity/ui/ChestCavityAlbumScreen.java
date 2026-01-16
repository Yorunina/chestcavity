package net.tigereye.chestcavity.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.network.ChestCavityNetwork;
import net.tigereye.chestcavity.network.packet.RequestAlbumPagePacket;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ChestCavityAlbumScreen extends AbstractContainerScreen<ChestCavityAlbumScreenHandler> {
    public static List<String> ALBUM_ITEM_IDS = new ArrayList<>();

    private static final ResourceLocation BACKGROUND = new ResourceLocation(ChestCavity.MODID, "textures/gui/album.png");
    private static final ResourceLocation ARROW_LEFT = new ResourceLocation(ChestCavity.MODID, "textures/gui/album_previous.png");
    private static final ResourceLocation ARROW_RIGHT = new ResourceLocation(ChestCavity.MODID, "textures/gui/album_next.png");

    private final int pageIndex;
    private final int pageMaxIndex;
    private static final int IDS_PER_PAGE = 30;

    public ChestCavityAlbumScreen(ChestCavityAlbumScreenHandler abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 306;
        this.imageHeight = 257;
        this.pageIndex = 0;
        this.pageMaxIndex = getMaxPage();
    }

    @Override
    protected void init() {
        super.init();
        if (pageIndex > 0) {
            ArrowWidget widget = addRenderableWidget(new ArrowWidget(leftPos + 18, topPos + 5, 16, 16, ARROW_LEFT));
            widget.setOnClickResponder(this::clickPrevPage);
        }
        if (pageIndex < pageMaxIndex) {
            ArrowWidget widget = addRenderableWidget(new ArrowWidget(leftPos + 265, topPos + 4, 16, 16, ARROW_RIGHT));
            widget.setOnClickResponder(this::clickNextPage);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_D || keyCode == GLFW.GLFW_KEY_RIGHT) {
            changePage(1);
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_A || keyCode == GLFW.GLFW_KEY_LEFT) {
            changePage(-1);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        Matrix4f pose = graphics.pose().last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(pose, leftPos, topPos, 0).uv(0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(pose, leftPos, topPos + imageHeight, 0).uv(0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(pose, leftPos + imageWidth, topPos + imageHeight, 0).uv(1.0F, 1.0F).endVertex();
        bufferBuilder.vertex(pose, leftPos + imageWidth, topPos, 0).uv(1.0F, 0.0F).endVertex();
        tesselator.end();
    }

    private static int getMaxPage() {
        return (int)Math.ceil((double)ALBUM_ITEM_IDS.size() / IDS_PER_PAGE);
    }

    public static List<String> getPageIds(int pageIndex) {
        int startIndex = pageIndex * IDS_PER_PAGE;
        int endIndex = Math.min((pageIndex + 1) * IDS_PER_PAGE, ALBUM_ITEM_IDS.size());
        return ALBUM_ITEM_IDS.subList(startIndex, endIndex);
    }

    public static int getPageSize(int pageIndex) {
        int startIndex = pageIndex * IDS_PER_PAGE;
        int endIndex = Math.min((pageIndex + 1) * IDS_PER_PAGE, ALBUM_ITEM_IDS.size());
        return endIndex - startIndex;
    }

    protected void clickPrevPage(ArrowWidget widget) {
        changePage(-1);
    }

    protected void clickNextPage(ArrowWidget widget) {
        changePage(1);
    }

    protected void changePage(int indexOffset) {
        int nextIndex = this.pageIndex + indexOffset;
        if (nextIndex < 0 || nextIndex > pageMaxIndex) return;
        ChestCavityNetwork.INSTANCE.sendToServer(new RequestAlbumPagePacket(nextIndex));
    }



    protected static final class ArrowWidget extends AbstractWidget {
        private final ResourceLocation location;
        private ClickResponder clickResponder = widget -> {};

        public ArrowWidget(int x, int y, int width, int height, ResourceLocation location) {
            super(x, y, width, height, CommonComponents.EMPTY);
            this.location = location;
        }

        public void setOnClickResponder(ClickResponder responder) {
            this.clickResponder = responder;
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, location);
            Matrix4f pose = graphics.pose().last().pose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferBuilder = tesselator.getBuilder();
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferBuilder.vertex(pose, getX(), getY(), 0).uv(0.0F, 0.0F).endVertex();
            bufferBuilder.vertex(pose, getX(), getY() + width, 0).uv(0.0F, 1.0F).endVertex();
            bufferBuilder.vertex(pose, getX() + height, getY() + width, 0).uv(1.0F, 1.0F).endVertex();
            bufferBuilder.vertex(pose, getX() + height, getY(), 0).uv(1.0F, 0.0F).endVertex();
            tesselator.end();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            clickResponder.onClick(this);
        }

        @FunctionalInterface
        interface ClickResponder {
            void onClick(ArrowWidget widget);
        }
    }

}
