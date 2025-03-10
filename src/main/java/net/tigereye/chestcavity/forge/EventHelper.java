package net.tigereye.chestcavity.forge;

import net.minecraftforge.event.TickEvent;

import java.util.function.Consumer;

public class EventHelper {
    public static final EventList<Consumer<TickEvent.ClientTickEvent>> CLIENT_TICK = new EventList();

    public EventHelper() {
    }

    public static <A> void executeEventList(EventList<Consumer<A>> list, A a) {
        list.forEach((consumer) -> {
            consumer.accept(a);
        });
    }
}
