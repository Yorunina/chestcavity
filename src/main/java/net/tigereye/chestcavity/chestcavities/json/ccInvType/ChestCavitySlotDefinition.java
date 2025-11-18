package net.tigereye.chestcavity.chestcavities.json.ccInvType;

public class ChestCavitySlotDefinition {
    public static final String DEFAULT_SLOT_TYPE = "default";
    public int id;
    public int x;
    public int y;
    public SlotDefinition relativePosition;
    public String type = DEFAULT_SLOT_TYPE;

    public int getId() {
        return this.id;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRelativeX() {
        return this.relativePosition.getX();
    }
    public int getRelativeY() {
        return this.relativePosition.getY();
    }

    public SlotDefinition getRelativePosition() {
        return this.relativePosition;
    }

    public ChestCavitySlotDefinition(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.relativePosition = new SlotDefinition(x / 18, y / 18);
    }
}