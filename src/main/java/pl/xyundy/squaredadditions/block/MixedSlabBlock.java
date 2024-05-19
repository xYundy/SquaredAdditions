package pl.xyundy.squaredadditions.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pl.xyundy.squaredadditions.block.entity.MixedSlabBlockEntity;

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

    @Override
    public void onSteppedOn(World world, BlockPos blockPos, BlockState blockState, Entity entity) {
        //TODO support vertical blocks - should I run function from both blocks, or random or what?
        this.topSlabState.getBlock().onSteppedOn(world, blockPos, blockState, entity);
    }

    @Override
    public float getBlastResistance() {
        //TODO check if correct slabs are saved
        return Math.max(this.topSlabState.getBlock().getBlastResistance(), this.bottomSlabState.getBlock().getBlastResistance());
    }
}
