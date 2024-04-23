package pl.xyundy.squaredadditions.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pl.xyundy.squaredadditions.block.entity.MixedSlabBlockEntity;

import static pl.xyundy.squaredadditions.block.ModBlocks.MIXED_SLAB_BLOCK_ENTITY;

public class MixedSlabBlock extends Block implements BlockEntityProvider {

    private BlockState topSlabState;
    private BlockState bottomSlabState;

    public MixedSlabBlock(BlockState topSlabState, BlockState bottomSlabSate, Settings settings) {
        super(settings);

        this.topSlabState = topSlabState;
        this.bottomSlabState = bottomSlabSate;
    }

    public BlockState getDefaultStateWithSlabStates(BlockState bottomSlabState, BlockState topSlabState) {
        this.bottomSlabState = bottomSlabState;
        this.topSlabState = topSlabState;
        return this.getDefaultState();
    }

    public BlockState getBottomSlabState() {
        return bottomSlabState;
    }

    public BlockState getTopSlabState() {
        return topSlabState;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MixedSlabBlockEntity(pos, state, this.topSlabState, this.bottomSlabState);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
