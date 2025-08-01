package net.tigereye.chestcavity.compat.kubejs;

import dev.latvian.mods.kubejs.client.ClientEventJS;
import net.minecraft.world.item.ItemStack;

public class OrganSkillWheelSelectJS extends ClientEventJS {

    public final ItemStack organItem;
    public OrganSkillWheelSelectJS(ItemStack itemStack) {
        super();
        this.organItem = itemStack;
    }

    public ItemStack getOrganItem() {
        return this.organItem;
    }
}
