package net.tigereye.chestcavity.chestcavities.json.ccInvType;

public class SlotDefinition {
    public int x;
    public int y;
    public String type = "default";
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public String getType() {
        return this.type;
    }

    public SlotDefinition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public SlotDefinition(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
