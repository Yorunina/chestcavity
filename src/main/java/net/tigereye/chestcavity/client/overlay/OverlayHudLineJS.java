package net.tigereye.chestcavity.client.overlay;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class OverlayHudLineJS {
    private final int count;
    private final float value;
    private final boolean defaultVisible;
    private Component text = Component.empty();
    private final List<Component> hover = new ArrayList<>();
    private Boolean visible;

    public OverlayHudLineJS(int count) {
        this(count, 0.0F, count > 0);
    }

    public OverlayHudLineJS(float value) {
        this(0, value, value != 0.0F);
    }

    private OverlayHudLineJS(int count, float value, boolean defaultVisible) {
        this.count = count;
        this.value = value;
        this.defaultVisible = defaultVisible;
    }

    public int getCount() {
        return count;
    }

    public float getValue() {
        return value;
    }

    public OverlayHudLineJS text(Object value) {
        this.text = toComponent(value);
        return this;
    }

    public OverlayHudLineJS hover(Object value) {
        hover.clear();
        appendHover(value);
        return this;
    }

    public OverlayHudLineJS hover(Object... values) {
        hover.clear();
        if (values != null) {
            for (Object nested : values) {
                appendHover(nested);
            }
        }
        return this;
    }

    public OverlayHudLineJS visible(boolean value) {
        this.visible = value;
        return this;
    }

    public Component getText() {
        return text;
    }

    public List<Component> getHover() {
        return List.copyOf(hover);
    }

    public boolean shouldShow() {
        return visible != null ? visible : defaultVisible;
    }

    private void appendHover(Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof Component component) {
            hover.add(component);
            return;
        }
        if (value instanceof Component[] components) {
            for (Component component : components) {
                if (component != null) {
                    hover.add(component);
                }
            }
            return;
        }
        if (value instanceof Object[] values) {
            for (Object nested : values) {
                appendHover(nested);
            }
            return;
        }
        if (value instanceof Iterable<?> iterable && !(value instanceof CharSequence)) {
            for (Object nested : iterable) {
                appendHover(nested);
            }
            return;
        }
        hover.add(toComponent(value));
    }

    private static Component toComponent(Object value) {
        if (value == null) {
            return Component.empty();
        }
        if (value instanceof Component component) {
            return component;
        }
        return Component.literal(String.valueOf(value));
    }
}
