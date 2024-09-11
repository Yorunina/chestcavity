package net.tigereye.chestcavity.forge;

import java.util.ArrayList;

public class EventList<A> extends ArrayList<A> {
    EventList() {
    }

    public void register(A a) {
        super.add(a);
    }
}
