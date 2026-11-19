package net.tigereye.chestcavity.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.tigereye.chestcavity.registration.CCBlocks;
import org.jetbrains.annotations.Nullable;

public class CultureVatBlock extends Block implements EntityBlock {
    private static final VoxelShape BOTTOM_SHAPE = box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    private static final VoxelShape TOP_SHAPE = box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    private final boolean top;

    public CultureVatBlock(boolean top) {
        super(BlockBehaviour.Properties.of()
                .strength(1.5F)
                .sound(SoundType.GLASS)
                .noOcclusion());
        this.top = top;
    }

    public boolean isTop() {
        return this.top;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.top ? TOP_SHAPE : BOTTOM_SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentState, net.minecraft.core.Direction side) {
        return adjacentState.is(this);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        if (this.top) {
            return null;
        }
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!level.getBlockState(pos.above()).canBeReplaced(context)) {
            return null;
        }
        return this.defaultBlockState();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (!this.top) {
            level.setBlock(pos.above(), CCBlocks.CULTURE_VAT_TOP.get().defaultBlockState(), 3);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        BlockPos base = this.top ? pos.below() : pos;
        BlockEntity blockEntity = level.getBlockEntity(base);
        if (blockEntity instanceof CultureVatBlockEntity vat) {
            return vat.use(player, hand);
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockState updateShape(BlockState state, net.minecraft.core.Direction direction, BlockState neighborState,
                                  net.minecraft.world.level.LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        net.minecraft.core.Direction partnerSide = this.top ? net.minecraft.core.Direction.DOWN : net.minecraft.core.Direction.UP;
        if (direction == partnerSide) {
            return neighborState.getBlock() instanceof CultureVatBlock ? state : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, net.minecraft.world.level.LevelReader level, BlockPos pos) {
        if (this.top) {
            return level.getBlockState(pos.below()).getBlock() instanceof CultureVatBlock;
        }
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!this.top) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof CultureVatBlockEntity vat) {
                    vat.dropContents();
                }
            }
            BlockPos other = this.top ? pos.below() : pos.above();
            if (level.getBlockState(other).getBlock() instanceof CultureVatBlock) {
                level.setBlock(other, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.top ? null : new CultureVatBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (this.top) {
            return null;
        }
        return level.isClientSide
                ? null
                : (tickLevel, pos, tickState, blockEntity) -> {
            if (blockEntity instanceof CultureVatBlockEntity vat) {
                CultureVatBlockEntity.serverTick(tickLevel, vat);
            }
        };
    }
}
