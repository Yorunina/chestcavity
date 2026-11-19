package net.tigereye.chestcavity.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.tigereye.chestcavity.recipes.CulturingRecipe;
import net.tigereye.chestcavity.registration.CCBlockEntities;
import net.tigereye.chestcavity.registration.CCRecipeTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CultureVatBlockEntity extends BlockEntity implements IFluidHandler {
    public static final int CAPACITY = 4000;
    private static final int SYNC_INTERVAL = 20;

    private final FluidTank tank = new FluidTank(CAPACITY) {
        @Override
        protected void onContentsChanged() {
            CultureVatBlockEntity.this.setChanged();
            CultureVatBlockEntity.this.sync();
        }
    };
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> this);

    private ItemStack organ = ItemStack.EMPTY;
    private int progress;
    private int syncTimer;

    public CultureVatBlockEntity(BlockPos pos, BlockState state) {
        super(CCBlockEntities.CULTURE_VAT.get(), pos, state);
    }

    public FluidStack getFluid() {
        return this.tank.getFluid();
    }

    public ItemStack getOrgan() {
        return this.organ;
    }

    public int getProgress() {
        return this.progress;
    }

    public float getProgressRatio() {
        CulturingRecipe recipe = this.findRecipe();
        return recipe == null ? 0.0F : (float) this.progress / (float) recipe.getTicks();
    }

    public static void serverTick(Level level, CultureVatBlockEntity vat) {
        if (vat.organ.isEmpty()) {
            if (vat.progress != 0) {
                vat.progress = 0;
            }
            return;
        }
        CulturingRecipe recipe = vat.findRecipe();
        if (recipe == null) {
            if (vat.progress != 0) {
                vat.progress = 0;
                vat.setChanged();
            }
            return;
        }
        vat.progress++;
        if (vat.progress >= recipe.getTicks()) {
            vat.tank.drain(recipe.getFluid().getAmount(), FluidAction.EXECUTE);
            vat.organ = recipe.getResultItem(level.registryAccess()).copy();
            vat.progress = 0;
            vat.setChanged();
            vat.sync();
            level.playSound(null, vat.worldPosition, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 0.6F, 1.0F);
        } else if (++vat.syncTimer >= SYNC_INTERVAL) {
            vat.syncTimer = 0;
            vat.sync();
        }
    }

    private @Nullable CulturingRecipe findRecipe() {
        if (this.level == null || this.organ.isEmpty() || this.tank.getFluid().isEmpty()) {
            return null;
        }
        for (CulturingRecipe recipe : this.level.getRecipeManager().getAllRecipesFor(CCRecipeTypes.CULTURING.get())) {
            if (recipe.matches(this.tank.getFluid(), this.organ)) {
                return recipe;
            }
        }
        return null;
    }

    public InteractionResult use(Player player, InteractionHand hand) {
        if (this.level == null || this.level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        ItemStack held = player.getItemInHand(hand);
        if (FluidUtil.interactWithFluidHandler(player, hand, this)) {
            return InteractionResult.SUCCESS;
        }
        if (!this.organ.isEmpty()) {
            if (player.isShiftKeyDown() && held.isEmpty()) {
                player.setItemInHand(hand, this.organ.copy());
                this.organ = ItemStack.EMPTY;
                this.progress = 0;
                this.setChanged();
                this.sync();
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        if (held.isEmpty() || this.tank.getFluid().isEmpty() || this.findRecipeFor(held) == null) {
            return InteractionResult.PASS;
        }
        this.organ = held.copyWithCount(1);
        held.shrink(1);
        this.progress = 0;
        this.level.playSound(null, this.worldPosition, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 1.2F);
        this.setChanged();
        this.sync();
        return InteractionResult.SUCCESS;
    }

    private @Nullable CulturingRecipe findRecipeFor(ItemStack stack) {
        if (this.level == null) {
            return null;
        }
        for (CulturingRecipe recipe : this.level.getRecipeManager().getAllRecipesFor(CCRecipeTypes.CULTURING.get())) {
            if (recipe.getIngredient().test(stack)) {
                return recipe;
            }
        }
        return null;
    }

    private void sync() {
        if (this.level != null && !this.level.isClientSide) {
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.fluidHandler.invalidate();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.tank.readFromNBT(tag.getCompound("Tank"));
        this.organ = ItemStack.of(tag.getCompound("Organ"));
        this.progress = tag.getInt("Progress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Tank", this.tank.writeToNBT(new CompoundTag()));
        if (!this.organ.isEmpty()) {
            tag.put("Organ", this.organ.save(new CompoundTag()));
        }
        tag.putInt("Progress", this.progress);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        CompoundTag tag = packet.getTag();
        if (tag != null) {
            this.load(tag);
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return this.fluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return this.tank.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.tank.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return this.tank.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int filled = this.tank.fill(resource, action);
        if (filled > 0 && action.execute()) {
            this.setChanged();
            this.sync();
        }
        return filled;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack drained = this.tank.drain(resource, action);
        if (!drained.isEmpty() && action.execute()) {
            this.setChanged();
            this.sync();
        }
        return drained;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack drained = this.tank.drain(maxDrain, action);
        if (!drained.isEmpty() && action.execute()) {
            this.setChanged();
            this.sync();
        }
        return drained;
    }
}
