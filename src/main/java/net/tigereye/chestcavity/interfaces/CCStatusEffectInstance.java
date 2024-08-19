package net.tigereye.chestcavity.interfaces;

public interface CCStatusEffectInstance {
    int getDuration();
    void CC_setDuration(int duration);

    void CC_setPermanent(boolean permanent);
}
