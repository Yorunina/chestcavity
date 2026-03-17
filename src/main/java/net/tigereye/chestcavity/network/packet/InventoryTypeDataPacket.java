package net.tigereye.chestcavity.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tigereye.chestcavity.chestcavities.json.ccInvType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class InventoryTypeDataPacket {
    private final int inventoryTypeDataSize;
    private final Map<ResourceLocation, InventoryTypeData> inventoryTypeData;

    public InventoryTypeDataPacket(Map<ResourceLocation, InventoryTypeData> inventoryTypeData) {
        this.inventoryTypeDataSize = inventoryTypeData.size();
        this.inventoryTypeData = inventoryTypeData;
    }

    public static InventoryTypeDataPacket decode(FriendlyByteBuf buf) {
        int inventoryTypeCount = buf.readInt();
        Map<ResourceLocation, InventoryTypeData> inventoryTypeMap = new HashMap<>();

        for (int i = 0; i < inventoryTypeCount; ++i) {
            ResourceLocation inventoryTypeID = buf.readResourceLocation();
            ResourceLocation backgroundTexture = buf.readResourceLocation();
            // playerInventoryPosition
            int playerInventoryPositionX = buf.readInt();
            int playerInventoryPositionY = buf.readInt();
            // titlePosition
            int titlePositionX = buf.readInt();
            int titlePositionY = buf.readInt();
            boolean titlePositionHide = buf.readBoolean();
            // inventoryPosition
            int inventoryPositionX = buf.readInt();
            int inventoryPositionY = buf.readInt();
            boolean inventoryPositionHide = buf.readBoolean();
            // backgroundSize
            int backgroundSizeX = buf.readInt();
            int backgroundSizeY = buf.readInt();
            // slotDefinitions
            int slotCount = buf.readInt();

            List<ChestCavitySlotDefinition> slotDefinitions = new ArrayList<>();
            for (int j = 0; j < slotCount; ++j) {
                int id = buf.readInt();
                int x = buf.readInt();
                int y = buf.readInt();
                String type = buf.readUtf();
                ChestCavitySlotDefinition slotDefinition = new ChestCavitySlotDefinition(id, x, y);
                slotDefinition.setType(type);
                slotDefinitions.add(slotDefinition);
            }

            InventoryTypeData inventoryType = new InventoryTypeData(
                    inventoryTypeID,
                    backgroundTexture,
                    slotDefinitions,
                    new SlotDefinition(playerInventoryPositionX, playerInventoryPositionY),
                    new TitleSlotDefinition(titlePositionX, titlePositionY, titlePositionHide),
                    new TitleSlotDefinition(inventoryPositionX, inventoryPositionY, inventoryPositionHide),
                    new SlotDefinition(backgroundSizeX, backgroundSizeY)
            );

            inventoryTypeMap.put(inventoryTypeID, inventoryType);
        }

        return new InventoryTypeDataPacket(inventoryTypeMap);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.inventoryTypeDataSize);
        this.inventoryTypeData.forEach((inventoryTypeID, inventoryType) -> {
            buf.writeResourceLocation(inventoryTypeID);
            buf.writeResourceLocation(inventoryType.getBackgroundTexture());
            // playerInventoryPosition
            buf.writeInt(inventoryType.getPlayerInventoryPosition().getX());
            buf.writeInt(inventoryType.getPlayerInventoryPosition().getY());
            // titlePosition
            buf.writeInt(inventoryType.getTitlePosition().getX());
            buf.writeInt(inventoryType.getTitlePosition().getY());
            buf.writeBoolean(inventoryType.getTitlePosition().isHide());
            // inventoryPosition
            buf.writeInt(inventoryType.getInventoryLabelPosition().getX());
            buf.writeInt(inventoryType.getInventoryLabelPosition().getY());
            buf.writeBoolean(inventoryType.getInventoryLabelPosition().isHide());
            // backgroundSize
            buf.writeInt(inventoryType.getBackgroundSize().getX());
            buf.writeInt(inventoryType.getBackgroundSize().getY());
            // slotDefinitions
            List<ChestCavitySlotDefinition> slotDefinitions = inventoryType.getSlotDefinitions();
            buf.writeInt(slotDefinitions.size());

            for (ChestCavitySlotDefinition slotDefinition : slotDefinitions) {
                buf.writeInt(slotDefinition.getId());
                buf.writeInt(slotDefinition.getX());
                buf.writeInt(slotDefinition.getY());
                buf.writeUtf(slotDefinition.getType());
            }
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        AtomicBoolean success = new AtomicBoolean(false);
        contextSupplier.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                InventoryTypeManager.InventoryTypeData.clear();
                InventoryTypeManager.InventoryTypeData.putAll(this.inventoryTypeData);
                success.set(true);
            });
        });
        contextSupplier.get().setPacketHandled(true);
        return success.get();
    }
}