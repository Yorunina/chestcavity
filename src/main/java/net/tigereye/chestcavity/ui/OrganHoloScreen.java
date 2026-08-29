package net.tigereye.chestcavity.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganData;
import net.tigereye.chestcavity.chestcavities.json.organs.OrganManager;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;
import se.mickelus.mutil.gui.*;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.gui.impl.GuiHorizontalScrollable;
import se.mickelus.mutil.gui.impl.GuiVerticalLayoutGroup;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A standalone organ catalogue styled after tetra's holosphere schematic page.
 * It intentionally uses mUtil's GUI primitives so it remains compatible with the
 * same rendering/animation conventions used by HoloSchematicGui.
 */
public class OrganHoloScreen extends Screen {
    private static final int GUI_WIDTH = 320;
    private static final int GUI_HEIGHT = 240;
    private static final int GUI_COLOR = 0xffffff;
    private static final int GUI_MUTED = 0x7f7f7f;
    private static final int GUI_HOVER = 0xffffcc;
    private static final int GUI_POSITIVE = 0x55ff55;
    private static final int GUI_NEGATIVE = 0xff5555;
    private final GuiElement root;
    private final OrganPage page;

    public OrganHoloScreen() {
        super(Component.translatable("gui.chestcavity.organ_holo.title"));
        root = new GuiElement(0, 0, GUI_WIDTH, GUI_HEIGHT);
        page = new OrganPage(0, -2, GUI_WIDTH, 205);
        root.addChild(page);
    }

    @Override
    protected void init() {
        super.init();
        page.reload();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        int left = (width - GUI_WIDTH) / 2;
        int top = (height - GUI_HEIGHT) / 2;
        updateRootFocus(mouseX, mouseY);
        root.draw(graphics, left, top, width, height, mouseX, mouseY, 1f);
        List<Component> tooltip = root.getTooltipLines();
        if (tooltip != null && !tooltip.isEmpty()) {
            graphics.renderTooltip(font, tooltip, java.util.Optional.empty(), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        updateRootFocus((int) mouseX, (int) mouseY);
        return root.onMouseClick((int) mouseX, (int) mouseY, button)
                || super.mouseClicked(mouseX, mouseY, button);
    }

    private void updateRootFocus(int mouseX, int mouseY) {
        root.updateFocusState((width - GUI_WIDTH) / 2, (height - GUI_HEIGHT) / 2, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return root.onMouseScroll(mouseX, mouseY, delta)
                || super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (root.onKeyPress(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (root.onCharType(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static final class OrganPage extends GuiElement {
        private final OrganList list;
        private final OrganDetail detail;
        private OrganEntry selected;
        private OrganEntry hovered;

        private OrganPage(int x, int y, int width, int height) {
            super(x, y, width, height);

            GuiRect upper = new GuiRect(0, 2, width, 1, GUI_COLOR);
            upper.setOpacity(.3f);
            addChild(upper);
            GuiRect lower = new GuiRect(0, 18, width, 1, GUI_COLOR);
            lower.setOpacity(.3f);
            addChild(lower);

            GuiString title = new GuiString(0, 6, I18n.get("gui.chestcavity.organ_holo.title"));
            title.setColor(GUI_COLOR);
            title.setAttachment(GuiAttachment.topCenter);
            addChild(title);

            list = new OrganList(0, 26, width, this::onOrganHovered,
                    this::onOrganBlurred, this::onOrganSelected);
            addChild(list);
            GuiRect divider = new GuiRect(0, 92, width, 1, GUI_COLOR);
            divider.setOpacity(.3f);
            addChild(divider);
            detail = new OrganDetail(0, 101, width);
            addChild(detail);
        }

        private void reload() {
            list.reload();
            detail.update(null);
        }

        private void onOrganSelected(@Nullable OrganEntry entry) {
            selected = entry;
            updateDetail();
        }

        private void onOrganHovered(@Nullable OrganEntry entry) {
            hovered = entry;
            updateDetail();
        }

        private void onOrganBlurred(@Nullable OrganEntry entry) {
            if (entry != null && entry.equals(hovered)) {
                hovered = null;
                updateDetail();
            }
        }

        private void updateDetail() {
            detail.update(hovered != null ? hovered : selected);
        }

    }

    private static final class OrganList extends GuiElement {
        private final GuiHorizontalScrollable scroll;
        private final GuiElement content;
        private final OrganFilterButton filterButton;
        private final OrganSortButton sortButton;
        private final Consumer<OrganEntry> onHover;
        private final Consumer<OrganEntry> onBlur;
        private final Consumer<OrganEntry> onSelect;
        private final List<ResourceLocation> properties = new ArrayList<>();
        private final List<KeyframeAnimation> itemAnimations = new ArrayList<>();
        private List<OrganEntry> allEntries = new ArrayList<>();
        private OrganEntry selected;

        private OrganList(int x, int y, int width, Consumer<OrganEntry> onHover,
                          Consumer<OrganEntry> onBlur, Consumer<OrganEntry> onSelect) {
            super(x, y, width, 68);
            this.onHover = onHover;
            this.onBlur = onBlur;
            this.onSelect = onSelect;

            scroll = new GuiHorizontalScrollable(0, 14, width, 54).setGlobal(true);
            content = new GuiElement(0, 0, 0, 50);
            scroll.addChild(content);

            addChild(scroll);

            GuiElement controls = new GuiElement(0, 0, width, 11);
            filterButton = new OrganFilterButton(0, 0, this::updateFilter);
            sortButton = new OrganSortButton(130, 1, ignored -> updateSort(ignored));
            controls.addChild(filterButton);
            controls.addChild(sortButton);
            addChild(controls);
        }

        private void reload() {
            Set<ResourceLocation> ids = new LinkedHashSet<>();
            allEntries = OrganManager.OrganData.entrySet().stream()
                    .map(entry -> {
                        Item item = ForgeRegistries.ITEMS.getValue(entry.getKey());
                        return item == null ? null : new OrganEntry(entry.getKey(), new ItemStack(item), entry.getValue());
                    })
                    .filter(java.util.Objects::nonNull)
                    .sorted(Comparator.comparing(entry -> entry.stack.getHoverName().getString()))
                    .collect(Collectors.toCollection(ArrayList::new));
            allEntries.forEach(entry -> ids.addAll(entry.data.organScores.keySet()));
            properties.clear();
            properties.addAll(ids);
            properties.sort(Comparator.comparing(OrganSortButton::shortName));
            sortButton.updateProperties(properties);
            rebuild();
        }

        private void updateFilter(String ignored) {
            rebuild();
        }

        private void updateSort(ResourceLocation ignored) {
            rebuild();
        }

        @Override
        public boolean onMouseClick(int x, int y, int button) {
            // When the popover is open, consume every click outside its own
            // hitbox before dispatching to the item scroller. Otherwise the
            // underlying organ item can be selected while the user is merely
            // trying to dismiss the menu.
            if (sortButton.isPopoverVisible()) {
                if (sortButton.isPopoverFocused()) {
                    return super.onMouseClick(x, y, button);
                }
                sortButton.closePopover();
                return true;
            }
            boolean handled = super.onMouseClick(x, y, button);
            return handled;
        }

        private void rebuild() {
            itemAnimations.forEach(KeyframeAnimation::stop);
            itemAnimations.clear();
            content.clearChildren();
            String filter = filterButton.getFilter().toLowerCase(Locale.ROOT);
            List<OrganEntry> visible = allEntries.stream()
                    .filter(entry -> filter.isEmpty()
                            || entry.stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains(filter)
                            || entry.id.toString().toLowerCase(Locale.ROOT).contains(filter))
                    .filter(entry -> !sortButton.hasActiveProperty()
                            || entry.data.organScores.getOrDefault(sortButton.getProperty(), 0f) != 0f)
                    .sorted(sortButton.comparator())
                    .toList();
            if (selected != null && visible.stream().noneMatch(selected::equals)) {
                selected = null;
                onSelect.accept(null);
            }
            for (int i = 0; i < visible.size(); i++) {
                OrganEntry entry = visible.get(i);
                OrganListItem item = new OrganListItem(4 + (i / 2) * 20, 6 + (i % 2) * 20,
                        entry, entry.equals(selected), onHover, onBlur, selectedEntry -> {
                    selected = selectedEntry;
                    updateSelection();
                    onSelect.accept(selectedEntry);
                });
                content.addChild(item);
                item.setOpacity(0);
                itemAnimations.add(new KeyframeAnimation(80, item)
                        .applyTo(new Applier.Opacity(0, 1),
                                new Applier.TranslateY(item.getY() - 5, item.getY()))
                        .withDelay(40 + 40 * (i / 2)));
            }
            content.setWidth(Math.max(getWidth(), visible.isEmpty() ? 32 : 8 + ((visible.size() + 1) / 2) * 21));
            scroll.markDirty();
            itemAnimations.forEach(KeyframeAnimation::start);
        }

        private void updateSelection() {
            content.getChildren(OrganListItem.class).forEach(item -> item.setSelected(item.entry.equals(selected)));
        }
    }

    private static final class OrganEntry {
        private final ResourceLocation id;
        private final ItemStack stack;
        private final OrganData data;

        private OrganEntry(ResourceLocation id, ItemStack stack, OrganData data) {
            this.id = id;
            this.stack = stack;
            this.data = data;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof OrganEntry other && id.equals(other.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

    private static class OrganItemBackdrop extends GuiElement {
        private int color = GUI_MUTED;

        private OrganItemBackdrop(int x, int y) {
            super(x, y, 16, 16);
        }

        protected void setColor(int color) {
            this.color = color;
        }

        @Override
        public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth,
                         int screenHeight, int mouseX, int mouseY, float opacity) {
            super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
            int left = refX + x;
            int top = refY + y;
            drawRect(graphics, left + 1, top + 1, left + 15, top + 15, 0x101010, .78f * opacity);
            drawRect(graphics, left + 2, top, left + 5, top + 1, color, .9f * opacity);
            drawRect(graphics, left + 1, top + 1, left + 3, top + 2, color, .9f * opacity);
            drawRect(graphics, left + 1, top + 2, left + 2, top + 4, color, .9f * opacity);
            drawRect(graphics, left + 11, top, left + 14, top + 1, color, .9f * opacity);
            drawRect(graphics, left + 13, top + 1, left + 15, top + 2, color, .9f * opacity);
            drawRect(graphics, left + 14, top + 2, left + 15, top + 4, color, .9f * opacity);
            drawRect(graphics, left + 2, top + 15, left + 5, top + 16, color, .9f * opacity);
            drawRect(graphics, left + 1, top + 14, left + 3, top + 15, color, .9f * opacity);
            drawRect(graphics, left + 1, top + 12, left + 2, top + 14, color, .9f * opacity);
            drawRect(graphics, left + 11, top + 15, left + 14, top + 16, color, .9f * opacity);
            drawRect(graphics, left + 13, top + 14, left + 15, top + 15, color, .9f * opacity);
            drawRect(graphics, left + 14, top + 12, left + 15, top + 14, color, .9f * opacity);
        }
    }

    private static final class OrganListItem extends GuiClickable {
        private final OrganEntry entry;
        private final OrganItemBackdrop backdrop;
        private final GuiItem icon;
        private final Consumer<OrganEntry> onHover;
        private final Consumer<OrganEntry> onBlur;
        private boolean selected;

        private OrganListItem(int x, int y, OrganEntry entry, boolean selected,
                              Consumer<OrganEntry> onHover, Consumer<OrganEntry> onBlur,
                              Consumer<OrganEntry> onClick) {
            super(x, y, 16, 16, () -> onClick.accept(entry));
            this.entry = entry;
            this.selected = selected;
            this.onHover = onHover;
            this.onBlur = onBlur;
            backdrop = new OrganItemBackdrop(0, 0);
            addChild(backdrop);
            icon = new GuiItem(0, 0).setTooltip(false).setCountVisibility(GuiItem.CountMode.never);
            icon.setItem(entry.stack);
            addChild(icon);
            setSelected(selected);
        }

        @Override
        public List<Component> getTooltipLines() {
            if (!hasFocus()) {
                return null;
            }
            return new ArrayList<>(entry.stack.getTooltipLines(Minecraft.getInstance().player,
                    net.minecraft.world.item.TooltipFlag.Default.NORMAL));
        }

        private void setSelected(boolean selected) {
            this.selected = selected;
            backdrop.setColor(selected ? GUI_COLOR : GUI_MUTED);
        }

        @Override
        protected void onFocus() {
            backdrop.setColor(GUI_HOVER);
            onHover.accept(entry);
        }

        @Override
        protected void onBlur() {
            backdrop.setColor(selected ? GUI_COLOR : GUI_MUTED);
            onBlur.accept(entry);
        }
    }

    private abstract static class OrganGlyph extends GuiElement {
        protected int color = GUI_COLOR;

        private OrganGlyph(int x, int y) {
            super(x, y, 16, 16);
        }

        protected void setColor(int color) {
            this.color = color;
        }
    }

    private static final class OrganSortGlyph extends OrganGlyph {
        private OrganSortGlyph(int x, int y) {
            super(x, y);
        }

        @Override
        public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth,
                         int screenHeight, int mouseX, int mouseY, float opacity) {
            super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
            int left = refX + x;
            int top = refY + y;
            drawRect(graphics, left + 3, top + 4, left + 13, top + 5, color, opacity);
            drawRect(graphics, left + 5, top + 6, left + 11, top + 7, color, opacity);
            drawRect(graphics, left + 7, top + 8, left + 9, top + 9, color, opacity);
            drawRect(graphics, left + 3, top + 11, left + 13, top + 12, color, opacity);
            drawRect(graphics, left + 5, top + 9, left + 7, top + 11, color, opacity);
            drawRect(graphics, left + 9, top + 9, left + 11, top + 11, color, opacity);
        }
    }

    private static final class OrganDetail extends GuiElement {
        private final GuiElement content;
        private final KeyframeAnimation showAnimation;
        private OrganEntry entry;

        private OrganDetail(int x, int y, int width) {
            super(x, y, width, 110);
            content = new GuiElement(0, 0, width, 160);
            addChild(content);
            showAnimation = new KeyframeAnimation(100, content)
                    .applyTo(new Applier.Opacity(0, 1), new Applier.TranslateY(-7, 0));
            content.setVisible(false);
        }

        private void update(@Nullable OrganEntry entry) {
            this.entry = entry;
            if (entry == null) {
                showAnimation.stop();
                content.setVisible(false);
                return;
            }
            showAnimation.stop();
            content.clearChildren();
            content.setVisible(true);
            content.setOpacity(0);
            content.setY(-7);
            GuiString header = new GuiString(22, 4, entry.stack.getHoverName().getString());
            header.setColor(GUI_COLOR);
            content.addChild(header);

            GuiItem icon = new GuiItem(0, 0).setTooltip(false).setCountVisibility(GuiItem.CountMode.never);
            icon.setItem(entry.stack);
            content.addChild(icon);

            List<Map.Entry<ResourceLocation, Float>> scores = entry.data.organScores.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .toList();
            for (int i = 0; i < scores.size(); i++) {
                Map.Entry<ResourceLocation, Float> score = scores.get(i);
                OrganStatBar bar = new OrganStatBar((i % 3) * 106, 28 + (i / 3) * 18,
                        score.getKey(), score.getValue());
                content.addChild(bar);
            }

            int acquisitionY = 28 + ((scores.size() + 2) / 3) * 18 + 4;
            GuiString acquisitionLabel = new GuiString(0, acquisitionY,
                    I18n.get("gui.chestcavity.organ_holo.obtain_method"));
            acquisitionLabel.setColor(GUI_COLOR);
            content.addChild(acquisitionLabel);

            String acquisitionKey = "gui.chestcavity.organ_holo.obtain." + entry.id;
            String acquisition = I18n.exists(acquisitionKey)
                    ? I18n.get(acquisitionKey)
                    : acquisitionKey;
            GuiText acquisitionText = new GuiText(0, acquisitionY + 11, width, acquisition);
            acquisitionText.setColor(GUI_MUTED);
            content.addChild(acquisitionText);
            showAnimation.start();
        }
    }

    private static final class OrganStatBar extends GuiElement {
        private final ResourceLocation property;
        private final float value;

        private OrganStatBar(int x, int y, ResourceLocation property, float value) {
            super(x, y, 100, 15);
            this.property = property;
            this.value = value;
        }

        @Override
        public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight,
                         int mouseX, int mouseY, float opacity) {
            String key = "tooltips.organ_score." + property;
            String label = I18n.exists(key) ? I18n.get(key) : property.getPath();
            OrganStatText text = new OrganStatText(0, 0, label);
            text.setColor(GUI_COLOR);
            text.draw(graphics, refX + x, refY + y, screenWidth, screenHeight, mouseX, mouseY, opacity);

            int barX = refX + x;
            int barY = refY + y + 6;
            int barWidth = 94;
            int center = barX + barWidth / 2;
            drawRect(graphics, barX, barY, barX + barWidth, barY + 1, GUI_COLOR, .14f * opacity);
            drawRect(graphics, center, barY - 1, center + 1, barY + 2, GUI_MUTED, opacity);
            float clamped = Math.max(-3f, Math.min(3f, value));
            if (clamped < 0) {
                int length = Math.round((-clamped / 3f) * (barWidth / 2f));
                drawRect(graphics, center - length, barY, center, barY + 1, GUI_COLOR, opacity);
            } else if (clamped > 0) {
                int length = Math.round((clamped / 3f) * (barWidth / 2f));
                drawRect(graphics, center + 1, barY, center + 1 + length, barY + 1, GUI_COLOR, opacity);
            }
            String valueText = String.format(Locale.ROOT, "%+.1f", value);
            OrganStatText valueLabel = new OrganStatText(94, 0, valueText, GuiAttachment.topRight);
            valueLabel.setColor(value < 0 ? GUI_NEGATIVE : value > 0 ? GUI_POSITIVE : GUI_MUTED);
            valueLabel.draw(graphics, refX + x, refY + y, screenWidth, screenHeight, mouseX, mouseY, opacity);
        }

        @Override
        public List<Component> getTooltipLines() {
            return null;
        }
    }

    private static class OrganStatText extends GuiString {
        private final boolean rightAligned;

        private OrganStatText(int x, int y, String string) {
            super(x, y, string);
            this.rightAligned = false;
        }

        private OrganStatText(int x, int y, String string, GuiAttachment attachment) {
            super(x, y, string, attachment);
            this.rightAligned = attachment == GuiAttachment.topRight;
        }

        @Override
        public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight,
                         int mouseX, int mouseY, float opacity) {
            graphics.pose().pushPose();
            int drawX = rightAligned
                    ? x - Math.round(fontRenderer.width(string) * .75f)
                    : x;
            graphics.pose().translate(refX + drawX, refY + y, 0);
            graphics.pose().scale(.75f, .75f, .75f);
            drawString(graphics, string, 0, 0, color, opacity * getOpacity(), drawShadow);
            graphics.pose().popPose();
        }
    }

    private static final class OrganFilterButton extends GuiElement {
        private static final int RESERVED_WIDTH = 120;
        private final GuiString label;
        private final Consumer<String> onChange;
        private String filter = "";
        private boolean focused;
        private final GuiRect inputBackground;

        private OrganFilterButton(int x, int y, Consumer<String> onChange) {
            super(x, y, RESERVED_WIDTH, 11);
            this.onChange = onChange;
            inputBackground = new GuiRect(0, 0, RESERVED_WIDTH, 11, 0x404040);
            inputBackground.setOpacity(.65f);
            addChild(inputBackground);
            label = new GuiString(3, 1, RESERVED_WIDTH - 3,
                    I18n.get("gui.chestcavity.organ_holo.filter"));
            label.setColor(GUI_MUTED);
            addChild(label);
        }

        private String getFilter() {
            return filter;
        }

        @Override
        public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth,
                         int screenHeight, int mouseX, int mouseY, float opacity) {
            super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);

            if (focused) {
                int left = refX + x;
                int top = refY + y;
                drawRect(graphics, left, top, left + RESERVED_WIDTH, top + 1,
                        GUI_HOVER, opacity * getOpacity());
                drawRect(graphics, left, top + 10, left + RESERVED_WIDTH, top + 11,
                        GUI_HOVER, opacity * getOpacity());
                drawRect(graphics, left, top, left + 1, top + 11,
                        GUI_HOVER, opacity * getOpacity());
                drawRect(graphics, left + RESERVED_WIDTH - 1, top,
                        left + RESERVED_WIDTH, top + 11, GUI_HOVER,
                        opacity * getOpacity());
            }

            // Keep the caret visible long enough to be noticed while still
            // matching the usual Minecraft/Tetra blinking rhythm.
            if (focused && System.currentTimeMillis() % 800L < 400L) {
                String visibleFilter = Minecraft.getInstance().font.plainSubstrByWidth(
                        filter, RESERVED_WIDTH - 3);
                int cursorX = refX + x + 3 + Minecraft.getInstance().font.width(visibleFilter);
                int cursorY = refY + y + 1;
                drawRect(graphics, cursorX, cursorY, cursorX + 1, cursorY + 9,
                        GUI_COLOR, opacity * getOpacity());
            }
        }

        @Override
        public boolean onMouseClick(int x, int y, int button) {
            setFocused(button == GLFW.GLFW_MOUSE_BUTTON_1 && hasFocus());
            return focused;
        }

        @Override
        public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
            if (!focused) {
                if (keyCode == GLFW.GLFW_KEY_F) {
                    setFocused(true);
                    return true;
                }
                return false;
            }
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                filter = StringUtils.chop(filter);
                changed();
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_ESCAPE) {
                setFocused(false);
                return true;
            }
            return false;
        }

        @Override
        public boolean onCharType(char character, int modifiers) {
            if (!focused) {
                if (character == 'f') {
                    setFocused(true);
                    return true;
                }
                return false;
            }
            filter += character;
            changed();
            return true;
        }

        private void changed() {
            updateLabel();
            onChange.accept(filter);
        }

        private void setFocused(boolean focused) {
            this.focused = focused;
            updateLabel();
        }

        private void updateLabel() {
            label.setString(filter.isEmpty()
                    ? focused ? "" : I18n.get("gui.chestcavity.organ_holo.filter")
                    : filter);
            label.setColor(filter.isEmpty() && !focused ? GUI_MUTED : GUI_COLOR);
        }
    }

    private static final class OrganSortButton extends GuiElement {
        private final OrganSortGlyph icon;
        private final GuiString label;
        private final Consumer<ResourceLocation> onChange;
        private final SortPopover popover;
        private ResourceLocation property;
        private boolean ascending = true;

        private OrganSortButton(int x, int y, Consumer<ResourceLocation> onChange) {
            super(x, y, 52, 9);
            this.onChange = onChange;
            icon = new OrganSortGlyph(-3, -3);
            label = new GuiString(11, 0, I18n.get("gui.chestcavity.organ_holo.sort.none"));
            popover = new SortPopover(0, 11, this::select);
            addChild(icon);
            addChild(label);
            addChild(popover);
        }

        private void updateProperties(List<ResourceLocation> properties) {
            popover.update(properties);
        }

        private boolean hasActiveProperty() {
            return property != null;
        }

        @Nullable
        private ResourceLocation getProperty() {
            return property;
        }

        private Comparator<OrganEntry> comparator() {
            if (property == null) {
                return Comparator.comparing(entry -> entry.stack.getHoverName().getString());
            }
            Comparator<OrganEntry> comparator = Comparator.comparingDouble(entry ->
                    entry.data.organScores.getOrDefault(property, 0f));
            return ascending ? comparator.thenComparing(entry -> entry.stack.getHoverName().getString())
                    : comparator.reversed().thenComparing(entry -> entry.stack.getHoverName().getString());
        }

        private void select(@Nullable ResourceLocation selectedProperty) {
            if (selectedProperty == null) {
                property = null;
                label.setString(I18n.get("gui.chestcavity.organ_holo.sort.none"));
            } else if (selectedProperty.equals(property)) {
                ascending = !ascending;
                label.setString((ascending ? "↑ " : "↓ ") + shortName(selectedProperty));
            } else {
                property = selectedProperty;
                ascending = false;
                label.setString("↓ " + shortName(selectedProperty));
            }
            setWidth(11 + label.getWidth());
            icon.setColor(GUI_COLOR);
            onChange.accept(property);
        }

        private static String shortName(ResourceLocation id) {
            String key = "tooltips.organ_score." + id;
            return I18n.exists(key) ? I18n.get(key) : id.getPath();
        }

        private boolean isPopoverVisible() {
            return popover.isVisible();
        }

        private boolean isPopoverFocused() {
            return popover.hasFocus();
        }

        private void closePopover() {
            popover.setVisible(false);
            icon.setColor(GUI_COLOR);
        }

        @Override
        public boolean onMouseClick(int x, int y, int button) {
            if (popover.isVisible() && super.onMouseClick(x, y, button)) {
                return true;
            }
            if (hasFocus()) {
                popover.setVisible(!popover.isVisible());
                icon.setColor(popover.isVisible() ? GUI_HOVER : GUI_COLOR);
                return true;
            }
            return false;
        }

        @Override
        public boolean hasFocus() {
            return super.hasFocus() || popover.hasFocus();
        }
    }

    private static final class SortPopover extends GuiElement {
        private static final int VISIBLE_ITEMS = 10;
        private static final int ITEM_HEIGHT = 10;
        private static final int ITEM_SPACING = 3;
        private static final int VIEWPORT_HEIGHT =
                VISIBLE_ITEMS * ITEM_HEIGHT + (VISIBLE_ITEMS - 1) * ITEM_SPACING;
        private static final int POPOVER_HEIGHT = VIEWPORT_HEIGHT + 12;
        private final GuiVerticalLayoutGroup items;
        private final ClipRectGui viewport;
        private final GuiRect backdrop;
        private final Consumer<ResourceLocation> onSelect;
        private List<ResourceLocation> properties = new ArrayList<>();
        private double scrollOffset;
        private int maxScroll;

        private SortPopover(int x, int y, Consumer<ResourceLocation> onSelect) {
            super(x, y - 3, 160, POPOVER_HEIGHT);
            this.onSelect = onSelect;
            backdrop = new GuiRect(0, 0, 160, POPOVER_HEIGHT, 0);
            backdrop.setOpacity(.9f);
            addChild(backdrop);
            addChild(new GuiRect(1, 1, 6, 1, GUI_COLOR));
            addChild(new GuiRect(-1, 1, 6, 1, GUI_COLOR).setAttachment(GuiAttachment.topRight));
            addChild(new GuiRect(-1, -1, 6, 1, GUI_COLOR).setAttachment(GuiAttachment.bottomRight));
            addChild(new GuiRect(1, -1, 6, 1, GUI_COLOR).setAttachment(GuiAttachment.bottomLeft));
            viewport = new ClipRectGui(6, 6, 148, VIEWPORT_HEIGHT);
            addChild(viewport);
            items = new GuiVerticalLayoutGroup(0, 0, 0, 3);
            viewport.addChild(items);
            setVisible(false);
        }

        private void update(List<ResourceLocation> properties) {
            this.properties = new ArrayList<>(properties);
            this.scrollOffset = 0;
            rebuild();
        }

        private void rebuild() {
            items.clearChildren();
            items.addChild(new SortItem(0, 0, null, I18n.get("gui.chestcavity.organ_holo.sort.none"), onSelect));
            for (ResourceLocation id : properties) {
                items.addChild(new SortItem(0, 0, id, OrganSortButton.shortName(id), onSelect));
            }
            items.forceLayout();
            int maxWidth = items.getChildren().stream().mapToInt(GuiElement::getWidth).max().orElse(50);
            items.getChildren().forEach(child -> child.setWidth(maxWidth));
            setWidth(maxWidth + 12);
            viewport.setWidth(getWidth() - 12);
            viewport.setHeight(VIEWPORT_HEIGHT);
            backdrop.setWidth(getWidth());
            backdrop.setHeight(POPOVER_HEIGHT);
            maxScroll = Math.max(0, items.getHeight() - viewport.getHeight());
            scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));
            items.setY(-(int) Math.round(scrollOffset));
        }

        @Override
        protected void drawChildren(GuiGraphics graphics, int refX, int refY, int screenWidth,
                                    int screenHeight, int mouseX, int mouseY, float opacity) {
            // GuiItem enables depth testing while rendering item models.  Keep
            // this popover on a dedicated positive z layer so it is always
            // drawn above those item textures, matching tetra's ZOffsetGui.
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 200);
            super.drawChildren(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
            graphics.pose().popPose();
        }

        @Override
        public boolean onMouseScroll(double mouseX, double mouseY, double distance) {
            if (!hasFocus()) {
                return false;
            }
            double oldOffset = scrollOffset;
            scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - distance * 12));
            items.setY(-(int) Math.round(scrollOffset));
            return oldOffset != scrollOffset || maxScroll > 0;
        }

        @Override
        public boolean onMouseClick(int x, int y, int button) {
            if (super.onMouseClick(x, y, button)) {
                return true;
            }
            return hasFocus();
        }

        private static final class SortItem extends GuiClickable {
            private final GuiString label;

            private SortItem(int x, int y, @Nullable ResourceLocation id, String text, Consumer<ResourceLocation> onSelect) {
                super(x, y, 40, 10, () -> onSelect.accept(id));
                label = new GuiString(0, 0, text);
                addChild(label);
                setWidth(label.getWidth());
            }

            @Override
            protected void onFocus() {
                label.setColor(GUI_HOVER);
            }

            @Override
            protected void onBlur() {
                label.setColor(GUI_COLOR);
            }
        }
    }
}
