package net.tigereye.chestcavity.registration;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tigereye.chestcavity.ChestCavity;

@Mod.EventBusSubscriber(modid = ChestCavity.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCAttributes {
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ChestCavity.MODID);
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
    public static final RegistryObject<Attribute> ADDITIONAL_SLOT = ATTRIBUTES.register("additional_slot", () -> (new RangedAttribute("attribute.chestcavity.additional_slot", 0, -256, 256).setSyncable(true)));

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity -> {
            e.add(entity, ADDITIONAL_SLOT.get());
        });
    }
}
