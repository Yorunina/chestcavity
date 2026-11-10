package net.tigereye.chestcavity.client.overlay;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tigereye.chestcavity.ChestCavity;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.InventoryTypeData;
import net.tigereye.chestcavity.compat.kubejs.CCEvents;
import net.tigereye.chestcavity.ui.ChestCavityScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class OverlayHudPanel {
    public static final ResourceLocation TEXTURE = new ResourceLocation(ChestCavity.MODID, "textures/gui/overlay.png");
    public static final int TEX_WIDTH = 256;
    public static final int TEX_HEIGHT = 64;
    public static final int WINDOW_WIDTH = 128;
    public static final int DRAG_HEIGHT = 4;
    public static final int HEADER_HEIGHT = 20;
    public static final int MAX_LIST_HEIGHT = 90;
    public static final int ROW_HEIGHT = 18;
    public static final int FOLD_SIZE = 18;
    public static final int MARGIN = 4;
    private static final int CONTENT_INSET = 2;
    private static final int DRAG_WIDTH = 124;
    private static final int ROW_WIDTH = 124;
    private static final int ROW_BORDER = 2;
    private static final int TEXT_INSET = 8;
    private static final int DRAG_U = 0;
    private static final int DRAG_V = 0;
    private static final int HEADER_U = 0;
    private static final int HEADER_V = 4;
    private static final int ROW_U = 0;
    private static final int ROW_V = 24;
    private static final int FOLD_CLOSED_U = 128;
    private static final int FOLD_OPEN_U = 146;
    private static final int FOLD_V = 4;
    private static final int TEXT_COLOR = 0xE0E0E0;
    private static final int SCROLLBAR_WIDTH = 2;
    private static final int SCROLLBAR_MIN_THUMB = 8;
    private static final int SCROLLBAR_TRACK_COLOR = 0x66303030;
    private static final int SCROLLBAR_THUMB_COLOR = 0xFFC0C0C0;

    private OverlayHudCatalog catalog = OverlayHudCatalog.EMPTY;
    private final List<Row> scoreRows = new ArrayList<>();
    private final List<Row> tagRows = new ArrayList<>();
    private boolean visible;
    private int x;
    private int y;
    private int height;
    private int scoreListHeight;
    private int tagListHeight;
    private int scoreScroll;
    private int tagScroll;
    private boolean dragging;
    private int dragOffsetX;
    private int dragOffsetY;
    private boolean catalogLoaded;
    private List<Component> hoveredTooltip = List.of();

    public boolean isVisible() {
        return visible;
    }

    public boolean contains(double mouseX, double mouseY) {
        return visible && mouseX >= x && mouseX < x + WINDOW_WIDTH && mouseY >= y && mouseY < y + height;
    }

    public boolean capturesInput(double mouseX, double mouseY, ChestCavityScreen screen) {
        return contains(mouseX, mouseY);
    }

    public List<Component> getHoveredTooltip() {
        return hoveredTooltip;
    }

    public void prepare(ChestCavityScreen screen) {
        if (!catalogLoaded) {
            reloadCatalog();
        }
        if (catalog.isEmpty()) {
            visible = false;
            hoveredTooltip = List.of();
            return;
        }
        visible = true;
        rebuildRows(screen);
        scoreListHeight = listHeight(catalog.getScores().isEmpty(), OverlayHudClient.isScoresCollapsed(), scoreRows.size());
        tagListHeight = listHeight(catalog.getTags().isEmpty(), OverlayHudClient.isTagsCollapsed(), tagRows.size());
        height = DRAG_HEIGHT;
        if (!catalog.getScores().isEmpty()) {
            height += HEADER_HEIGHT + scoreListHeight;
        }
        if (!catalog.getTags().isEmpty()) {
            height += HEADER_HEIGHT + tagListHeight;
        }
        place(screen);
        scoreScroll = clampScroll(scoreScroll, scoreRows.size(), scoreListHeight);
        tagScroll = clampScroll(tagScroll, tagRows.size(), tagListHeight);
    }

    public void reloadCatalog() {
        catalog = CCEvents.getOverlayHud();
        catalogLoaded = true;
    }

    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (!visible) {
            hoveredTooltip = List.of();
            return;
        }
        int cursor = y;
        blit(graphics, x + CONTENT_INSET, cursor, DRAG_U, DRAG_V, DRAG_WIDTH, DRAG_HEIGHT);
        cursor += DRAG_HEIGHT;
        if (!catalog.getScores().isEmpty()) {
            cursor = renderSection(graphics, font, cursor, catalog.getScoreTitle(), scoreRows, scoreListHeight, scoreScroll, OverlayHudClient.isScoresCollapsed());
        }
        if (!catalog.getTags().isEmpty()) {
            renderSection(graphics, font, cursor, catalog.getTagTitle(), tagRows, tagListHeight, tagScroll, OverlayHudClient.isTagsCollapsed());
        }
        hoveredTooltip = findHoveredTooltip(mouseX, mouseY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0 || !contains(mouseX, mouseY)) {
            return contains(mouseX, mouseY);
        }
        if (inDragBar(mouseX, mouseY)) {
            dragging = true;
            dragOffsetX = (int) mouseX - x;
            dragOffsetY = (int) mouseY - y;
            return true;
        }
        if (inFoldButton(scoreHeaderY(), mouseX, mouseY) && !catalog.getScores().isEmpty()) {
            OverlayHudClient.setScoresCollapsed(!OverlayHudClient.isScoresCollapsed());
            return true;
        }
        if (inFoldButton(tagHeaderY(), mouseX, mouseY) && !catalog.getTags().isEmpty()) {
            OverlayHudClient.setTagsCollapsed(!OverlayHudClient.isTagsCollapsed());
            return true;
        }
        return true;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, ChestCavityScreen screen) {
        return mouseDragged(mouseX, mouseY, button, screen.width, screen.height);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, int screenWidth, int screenHeight) {
        if (!dragging || button != 0) {
            return dragging;
        }
        int nextX = (int) mouseX - dragOffsetX;
        int nextY = (int) mouseY - dragOffsetY;
        OverlayHudClient.setPosition(clampX(nextX, screenWidth), clampY(nextY, screenHeight));
        x = OverlayHudClient.getPosX();
        y = OverlayHudClient.getPosY();
        return true;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging) {
            dragging = false;
            return true;
        }
        return contains(mouseX, mouseY);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (!visible) {
            return false;
        }
        int amount = (int) Math.round(-delta * ROW_HEIGHT);
        if (inList(scoreListY(), scoreListHeight, mouseX, mouseY)) {
            scoreScroll = clampScroll(scoreScroll + amount, scoreRows.size(), scoreListHeight);
            return true;
        }
        if (inList(tagListY(), tagListHeight, mouseX, mouseY)) {
            tagScroll = clampScroll(tagScroll + amount, tagRows.size(), tagListHeight);
            return true;
        }
        return contains(mouseX, mouseY);
    }

    private void rebuildRows(ChestCavityScreen screen) {
        scoreRows.clear();
        tagRows.clear();
        Map<ResourceLocation, Float> scores = OverlayHudClient.getScores();
        for (OverlayHudEntry entry : catalog.getScores()) {
            float value = scores.getOrDefault(entry.getId(), 0.0F);
            if (value == 0.0F) {
                continue;
            }
            OverlayHudLineJS line = new OverlayHudLineJS(value);
            entry.getRenderer().accept(line);
            if (line.shouldShow()) {
                scoreRows.add(new Row(line.getText(), line.getHover()));
            }
        }
        InventoryTypeData inventoryTypeData = screen.getInventoryTypeData();
        int slotSize = inventoryTypeData.getSlotSize();
        for (OverlayHudEntry entry : catalog.getTags()) {
            TagKey<Item> tag = entry.getTag();
            if (tag == null) {
                continue;
            }
            int count = 0;
            for (int i = 0; i < slotSize && i < screen.getMenu().slots.size(); i++) {
                if (Objects.equals(inventoryTypeData.getSlotType(i), "container_slot")) {
                    continue;
                }
                Slot slot = screen.getMenu().slots.get(i);
                ItemStack stack = slot.getItem();
                if (!stack.isEmpty() && stack.is(tag)) {
                    count++;
                }
            }
            if (count <= 0) {
                continue;
            }
            OverlayHudLineJS line = new OverlayHudLineJS(count);
            entry.getRenderer().accept(line);
            if (line.shouldShow()) {
                tagRows.add(new Row(line.getText(), line.getHover()));
            }
        }
    }

    private void place(ChestCavityScreen screen) {
        if (OverlayHudClient.hasPosition()) {
            x = clampX(OverlayHudClient.getPosX(), screen.width);
            y = clampY(OverlayHudClient.getPosY(), screen.height);
            OverlayHudClient.setPosition(x, y);
            return;
        }
        x = clampX(screen.getGuiLeft() - WINDOW_WIDTH - MARGIN, screen.width);
        y = clampY(screen.getGuiTop(), screen.height);
    }

    private int renderSection(GuiGraphics graphics, Font font, int headerY, Component title, List<Row> rows, int listHeight, int scroll, boolean collapsed) {
        blit(graphics, x, headerY, HEADER_U, HEADER_V, WINDOW_WIDTH, HEADER_HEIGHT);
        if (title != null) {
            int textY = headerY + Math.max(0, (HEADER_HEIGHT - font.lineHeight) / 2);
            graphics.drawString(font, title, x + TEXT_INSET, textY, TEXT_COLOR, false);
        }
        blit(graphics, foldX(), headerY, collapsed ? FOLD_OPEN_U : FOLD_CLOSED_U, FOLD_V, FOLD_SIZE, HEADER_HEIGHT);
        int listY = headerY + HEADER_HEIGHT;
        if (collapsed || listHeight <= 0) {
            return listY;
        }
        for (int rowOffset = 0; rowOffset < listHeight; rowOffset += ROW_HEIGHT) {
            blit(graphics, x + CONTENT_INSET, listY + rowOffset, ROW_U, ROW_V, ROW_WIDTH, Math.min(ROW_HEIGHT, listHeight - rowOffset));
        }
        graphics.enableScissor(x + CONTENT_INSET + ROW_BORDER, listY, scrollbarX(), listY + listHeight);
        int rowY = listY - scroll + Math.max(0, (ROW_HEIGHT - font.lineHeight) / 2);
        for (Row row : rows) {
            if (rowY + ROW_HEIGHT > listY && rowY < listY + listHeight) {
                graphics.drawString(font, row.text, x + TEXT_INSET, rowY, TEXT_COLOR, false);
            }
            rowY += ROW_HEIGHT;
        }
        graphics.disableScissor();
        drawScrollbar(graphics, listY, listHeight, rows.size(), scroll);
        return listY + listHeight;
    }

    private void drawScrollbar(GuiGraphics graphics, int listY, int listHeight, int rowCount, int scroll) {
        int contentHeight = rowCount * ROW_HEIGHT;
        if (contentHeight <= listHeight) {
            return;
        }
        int scrollbarX = scrollbarX();
        graphics.fill(scrollbarX, listY, scrollbarX + SCROLLBAR_WIDTH, listY + listHeight, SCROLLBAR_TRACK_COLOR);

        int thumbHeight = Math.max(SCROLLBAR_MIN_THUMB, listHeight * listHeight / contentHeight);
        int maxScroll = contentHeight - listHeight;
        int thumbTravel = listHeight - thumbHeight;
        int thumbOffset = maxScroll <= 0 ? 0 : scroll * thumbTravel / maxScroll;
        graphics.fill(
                scrollbarX,
                listY + thumbOffset,
                scrollbarX + SCROLLBAR_WIDTH,
                listY + thumbOffset + thumbHeight,
                SCROLLBAR_THUMB_COLOR
        );
    }

    private static void blit(GuiGraphics graphics, int x, int y, int u, int v, int width, int height) {
        graphics.blit(TEXTURE, x, y, u, v, width, height, TEX_WIDTH, TEX_HEIGHT);
    }

    private int scrollbarX() {
        return x + CONTENT_INSET + ROW_WIDTH - ROW_BORDER - SCROLLBAR_WIDTH;
    }

    private static int listHeight(boolean missing, boolean collapsed, int rows) {
        if (missing || collapsed) {
            return 0;
        }
        return Math.min(MAX_LIST_HEIGHT, Math.max(rows, 1) * ROW_HEIGHT);
    }

    private static int clampScroll(int scroll, int rows, int listHeight) {
        int max = Math.max(0, rows * ROW_HEIGHT - listHeight);
        if (scroll < 0) {
            return 0;
        }
        return Math.min(scroll, max);
    }

    private int clampX(int value, int screenWidth) {
        return Math.max(0, Math.min(value, Math.max(0, screenWidth - WINDOW_WIDTH)));
    }

    private int clampY(int value, int screenHeight) {
        return Math.max(0, Math.min(value, Math.max(0, screenHeight - height)));
    }

    private boolean inDragBar(double mouseX, double mouseY) {
        return mouseX >= x + CONTENT_INSET && mouseX < x + CONTENT_INSET + DRAG_WIDTH && mouseY >= y && mouseY < y + DRAG_HEIGHT;
    }

    private boolean inFoldButton(int headerY, double mouseX, double mouseY) {
        return headerY >= 0 && mouseX >= foldX() && mouseX < foldX() + FOLD_SIZE && mouseY >= headerY && mouseY < headerY + HEADER_HEIGHT;
    }

    private boolean inList(int listY, int listHeight, double mouseX, double mouseY) {
        return listHeight > 0 && mouseX >= x + CONTENT_INSET && mouseX < x + CONTENT_INSET + ROW_WIDTH && mouseY >= listY && mouseY < listY + listHeight;
    }

    private int foldX() {
        return x + WINDOW_WIDTH - FOLD_SIZE;
    }

    private int scoreHeaderY() {
        return catalog.getScores().isEmpty() ? -1 : y + DRAG_HEIGHT;
    }

    private int scoreListY() {
        return scoreHeaderY() < 0 ? -1 : scoreHeaderY() + HEADER_HEIGHT;
    }

    private int tagHeaderY() {
        if (catalog.getTags().isEmpty()) {
            return -1;
        }
        int cursor = y + DRAG_HEIGHT;
        if (!catalog.getScores().isEmpty()) {
            cursor += HEADER_HEIGHT + scoreListHeight;
        }
        return cursor;
    }

    private int tagListY() {
        return tagHeaderY() < 0 ? -1 : tagHeaderY() + HEADER_HEIGHT;
    }

    private List<Component> findHoveredTooltip(double mouseX, double mouseY) {
        if (!contains(mouseX, mouseY)) {
            return List.of();
        }
        List<Component> tooltip = hoveredRowTooltip(scoreListY(), scoreListHeight, scoreScroll, scoreRows, mouseX, mouseY);
        if (!tooltip.isEmpty()) {
            return tooltip;
        }
        return hoveredRowTooltip(tagListY(), tagListHeight, tagScroll, tagRows, mouseX, mouseY);
    }

    private List<Component> hoveredRowTooltip(int listY, int listHeight, int scroll, List<Row> rows, double mouseX, double mouseY) {
        if (listHeight <= 0 || mouseX < x + CONTENT_INSET + ROW_BORDER || mouseX >= scrollbarX() || mouseY < listY || mouseY >= listY + listHeight) {
            return List.of();
        }
        int index = (int) ((mouseY - listY + scroll) / ROW_HEIGHT);
        if (index < 0 || index >= rows.size()) {
            return List.of();
        }
        return rows.get(index).hover;
    }

    private record Row(Component text, List<Component> hover) {
    }
}
