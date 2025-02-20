package net.tigereye.chestcavity.listeners;

public class OrganCustomEventListener {
    public static int slotIndex;
    public OrganCustomEventListener(int slotIndex) {
        this.slotIndex = slotIndex;
    }
    public int getSlotIndex() {
        return slotIndex;
    }
}
