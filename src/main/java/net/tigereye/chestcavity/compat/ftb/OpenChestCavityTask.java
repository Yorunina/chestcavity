package net.tigereye.chestcavity.compat.ftb;

import dev.architectury.registry.registries.RegistrarManager;
import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;
import net.tigereye.chestcavity.chestcavities.json.ChestCavityDataRepository;

import java.util.ArrayList;
import java.util.List;

public class OpenChestCavityTask extends Task {
    private static final ResourceLocation PLAYER = new ResourceLocation("minecraft:player");
    private static final ResourceLocation EMPTY = new ResourceLocation("minecraft:empty");
    public long value;
    public ResourceLocation entity;
    public ResourceLocation inventoryType;


    public OpenChestCavityTask(long id, Quest quest) {
        super(id, quest);
        this.entity = EMPTY;
        this.inventoryType = EMPTY;
        this.value = 1L;
    }

    @Override
    public TaskType getType() {
        return ChestCavityTypes.OPEN_CHEST_CAVITY_TASK_TYPE;
    }

    @Override
    public long getMaxProgress() {
        return this.value;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putLong("value", value);
        nbt.putString("entity", entity.toString());
        nbt.putString("inventoryType", inventoryType.toString());
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);
        this.value = nbt.getLong("value");
        if (this.value == 0) {
            this.value = 1;
        }
        this.entity = new ResourceLocation(nbt.getString("entity"));
        this.inventoryType = new ResourceLocation(nbt.getString("inventoryType"));
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeLong(this.value);
        buffer.writeResourceLocation(this.entity);
        buffer.writeResourceLocation(this.inventoryType);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.value = buffer.readLong();
        this.entity = buffer.readResourceLocation();
        this.inventoryType = buffer.readResourceLocation();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        List<ResourceLocation> ids = new ArrayList<>(ForgeRegistries.ENTITY_TYPES.getKeys());
        ids.add(EMPTY);
        config.addEnum("entity", this.entity, (v) -> {
            this.entity = v;
        }, NameMap.of(EMPTY, ids).nameKey((v) -> {
            String namespace = v.getNamespace();
            return "entity." + namespace + "." + v.getPath();
        }).icon((v) -> {
            SpawnEggItem item = ForgeSpawnEggItem.fromEntityType(ForgeRegistries.ENTITY_TYPES.getValue(v));
            return ItemIcon.getItemIcon(item != null ? item : Items.SPAWNER);
        }).create(), EMPTY);

        List<ResourceLocation> invIds = new ArrayList<>(ChestCavityDataRepository.getCurrent().getInventoryTypes().keySet());
        invIds.add(EMPTY);
        config.addEnum("inventoryType", this.inventoryType, (v) -> {
            this.inventoryType = v;
        }, NameMap.of(EMPTY, invIds).name((v) -> Component.literal(v.getPath())).create(), EMPTY);

        config.addLong("value", this.value, (v) -> {
            this.value = v;
        }, 1L, 1L, Long.MAX_VALUE);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        String namespace = this.entity.getNamespace();
        return Component.translatable("chestcavity.task.ftbquests.open_chest_cavity.title", this.formatMaxProgress(), Component.translatable("entity." + namespace + "." + this.entity.getPath()));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Icon getAltIcon() {
        SpawnEggItem item = ForgeSpawnEggItem.fromEntityType(ForgeRegistries.ENTITY_TYPES.getValue(this.entity));
        return ItemIcon.getItemIcon(item != null ? item : Items.SPAWNER);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onButtonClicked(Button button, boolean canClick) {
    }

    public void chestCavityOpened(TeamData teamData, LivingEntity entity, ResourceLocation inventoryType) {
        if (teamData.isCompleted(this)) return;
        boolean isEntityEmpty = this.entity.equals(EMPTY);
        boolean isInvTypeEmpty = this.inventoryType.equals(EMPTY);

        if (!isEntityEmpty) {
            if (this.entity.equals(RegistrarManager.getId(entity.getType(), Registries.ENTITY_TYPE))) {
                if (isInvTypeEmpty || this.inventoryType.equals(inventoryType)) {
                    teamData.addProgress(this, 1L);
                }
            }
        } else if (!isInvTypeEmpty && this.inventoryType.equals(inventoryType)) {
            teamData.addProgress(this, 1L);
        }
    }
}
