package net.tigereye.chestcavity.chestcavities.json.ccInvType;

public class ChestCavitySlotDefinition {
    public static final String DEFAULT_SLOT_TYPE = "default";
    public int x;
    public int y;
    public String type = DEFAULT_SLOT_TYPE;
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public String getType() {
        return this.type;
    }

    public ChestCavitySlotDefinition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public ChestCavitySlotDefinition(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
