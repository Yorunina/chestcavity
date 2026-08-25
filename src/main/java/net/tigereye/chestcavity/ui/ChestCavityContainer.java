package net.tigereye.chestcavity.ui;


import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.tigereye.chestcavity.util.ContainerNbtUtil;
import org.jetbrains.annotations.NotNull;

public class ChestCavityContainer extends SimpleContainer {

    public ChestCavityContainer(int size) {
        super(size);
    }

    @Override
    public void fromTag(ListTag tags) {
        ContainerNbtUtil.load(this, tags);
    }

    @Override
    public @NotNull ListTag createTag() {
        return ContainerNbtUtil.save(this);
    }
}
