package net.tigereye.chestcavity.chestcavities.json.ccInvType;

public class TitleSlotDefinition {
    public boolean hide = false;
    public int x;
    public int y;

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isHide() {
        return this.hide;
    }

    public TitleSlotDefinition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TitleSlotDefinition(int x, int y, boolean hide) {
        this.x = x;
        this.y = y;
        this.hide = hide;
    }
}
