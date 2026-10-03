package net.tigereye.chestcavity.client.overlay;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
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
    public static final int TEX_SIZE = 256;
    public static final int WINDOW_WIDTH = 140;
    public static final int DRAG_HEIGHT = 14;
    public static final int HEADER_HEIGHT = 16;
    public static final int MAX_LIST_HEIGHT = 88;
    public static final int ROW_HEIGHT = 11;
    public static final int FOLD_SIZE = 12;
    public static final int MARGIN = 4;
    private static final int TEXT_COLOR = 0xE0E0E0;
    private static final int VALUE_COLOR = 0xFFFFFF;

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

    public boolean isVisible() {
        return visible;
    }

    public boolean contains(double mouseX, double mouseY) {
        return visible && mouseX >= x && mouseX < x + WINDOW_WIDTH && mouseY >= y && mouseY < y + height;
    }

    public void prepare(ChestCavityScreen screen) {
        if (!catalogLoaded) {
            reloadCatalog();
        }
        if (catalog.isEmpty()) {
            visible = false;
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
            return;
        }
        int cursor = y;
        blit(graphics, x, cursor, 0, 0, WINDOW_WIDTH, DRAG_HEIGHT);
        cursor += DRAG_HEIGHT;
        if (!catalog.getScores().isEmpty()) {
            cursor = renderSection(
                    graphics,
                    font,
                    cursor,
                    catalog.getScoreTitle(),
                    scoreRows,
                    scoreListHeight,
                    scoreScroll,
                    OverlayHudClient.isScoresCollapsed(),
                    mouseX,
                    mouseY
            );
        }
        if (!catalog.getTags().isEmpty()) {
            renderSection(
                    graphics,
                    font,
                    cursor,
                    catalog.getTagTitle(),
                    tagRows,
                    tagListHeight,
                    tagScroll,
                    OverlayHudClient.isTagsCollapsed(),
                    mouseX,
                    mouseY
            );
        }
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
            scoreRows.add(new Row(I18n.get(entry.getLabel()), formatValue(value)));
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
            if (count > 0) {
                tagRows.add(new Row(I18n.get(entry.getLabel()), "x" + count));
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

    private int renderSection(
            GuiGraphics graphics,
            Font font,
            int headerY,
            String title,
            List<Row> rows,
            int listHeight,
            int scroll,
            boolean collapsed,
            int mouseX,
            int mouseY
    ) {
        blit(graphics, x, headerY, 0, 16, WINDOW_WIDTH, HEADER_HEIGHT);
        if (title != null) {
            String text = I18n.get(title);
            graphics.drawString(font, font.plainSubstrByWidth(text, WINDOW_WIDTH - FOLD_SIZE - 10), x + 4, headerY + 4, TEXT_COLOR, false);
        }
        int foldX = foldX();
        int foldY = headerY + (HEADER_HEIGHT - FOLD_SIZE) / 2;
        int foldV = collapsed ? 16 : 0;
        blit(graphics, foldX, foldY, 140, foldV, FOLD_SIZE, FOLD_SIZE);
        int listY = headerY + HEADER_HEIGHT;
        if (collapsed || listHeight <= 0) {
            return listY;
        }
        blit(graphics, x, listY, 0, 32, WINDOW_WIDTH, listHeight);
        graphics.enableScissor(x + 2, listY, x + WINDOW_WIDTH - 2, listY + listHeight);
        int rowY = listY - scroll + 1;
        for (Row row : rows) {
            if (rowY + ROW_HEIGHT > listY && rowY < listY + listHeight) {
                int valueWidth = font.width(row.value);
                int nameMax = WINDOW_WIDTH - valueWidth - 12;
                graphics.drawString(font, font.plainSubstrByWidth(row.name, Math.max(nameMax, 8)), x + 4, rowY, TEXT_COLOR, false);
                graphics.drawString(font, row.value, x + WINDOW_WIDTH - 4 - valueWidth, rowY, VALUE_COLOR, false);
            }
            rowY += ROW_HEIGHT;
        }
        graphics.disableScissor();
        return listY + listHeight;
    }

    private static void blit(GuiGraphics graphics, int x, int y, int u, int v, int width, int height) {
        graphics.blit(TEXTURE, x, y, u, v, width, height, TEX_SIZE, TEX_SIZE);
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
        return mouseX >= x && mouseX < x + WINDOW_WIDTH && mouseY >= y && mouseY < y + DRAG_HEIGHT;
    }

    private boolean inFoldButton(int headerY, double mouseX, double mouseY) {
        if (headerY < 0) {
            return false;
        }
        int foldX = foldX();
        int foldY = headerY + (HEADER_HEIGHT - FOLD_SIZE) / 2;
        return mouseX >= foldX && mouseX < foldX + FOLD_SIZE && mouseY >= foldY && mouseY < foldY + FOLD_SIZE;
    }

    private boolean inList(int listY, int listHeight, double mouseX, double mouseY) {
        return listHeight > 0
                && mouseX >= x
                && mouseX < x + WINDOW_WIDTH
                && mouseY >= listY
                && mouseY < listY + listHeight;
    }

    private int foldX() {
        return x + WINDOW_WIDTH - FOLD_SIZE - 3;
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

    private static String formatValue(float value) {
        if (value == (int) value) {
            return Integer.toString((int) value);
        }
        String text = String.format(java.util.Locale.ROOT, "%.2f", value);
        int end = text.length();
        while (end > 0 && text.charAt(end - 1) == '0') {
            end--;
        }
        if (end > 0 && text.charAt(end - 1) == '.') {
            end--;
        }
        return text.substring(0, end);
    }

    private record Row(String name, String value) {
    }
}
