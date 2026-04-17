package net.tigereye.chestcavity.registration;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.ChestCavity;

@Mod.EventBusSubscriber
public class CCAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTE;
    public static final RegistryObject<Attribute> CLIMB_SPEED;

    public CCAttributes() {}

    static {
        ATTRIBUTE = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ChestCavity.MODID);
        CLIMB_SPEED = ATTRIBUTE.register("climb_speed", () -> new RangedAttribute("chestcavity.attribute.climb_speed.name", 0.0D, 0.0D, 1024.0D).setSyncable(true));
    }

    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(type -> {
            ATTRIBUTE.getEntries().forEach(entry -> {
                event.add(type, entry.get(), entry.get().getDefaultValue());
            });
        });
    }
}
