package pl.xyundy.squaredadditions.mixin;

import pl.xyundy.squaredadditions.slabs.PlacementUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalConnectingBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// Massive thanks to andrew6rant for a lot of code behind this mixin
// https://github.com/Andrew6rant/Auto-Slabs/blob/1.20.x/src/main/java/io/github/andrew6rant/autoslabs/mixin/PaneBlockMixin.java
@Mixin(PaneBlock.class)
public class PaneBlockMixin extends HorizontalConnectingBlock {

    public PaneBlockMixin(float radius1, float radius2, float boundingHeight1, float boundingHeight2, float collisionHeight, Settings settings) {
        super(radius1, radius2, boundingHeight1, boundingHeight2, collisionHeight, settings);
    }

    @Inject(method = "getStateForNeighborUpdate(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;",
            at = @At(value = "HEAD"), cancellable = true)
    public final void squaredadditions$neighborConnectsToVerticalSlab(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (direction.getAxis().isHorizontal()) {
            if (state.get(WATERLOGGED)) { // I'm puttting this here because if the axis isn't horizontal, Vanilla code will still properly check the waterlogged state. My inject changes that so I need this
                world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
            cir.setReturnValue(state.with(HorizontalConnectingBlock.FACING_PROPERTIES.get(direction), PlacementUtil.calcPaneCanConnectToVerticalSlab(direction, neighborState, neighborState.isSideSolidFullSquare(world, pos, direction.getOpposite()))));
        }
    }

    @Inject(method = "getPlacementState(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/block/BlockState;",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/BlockView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;",
                    ordinal = 3,
                    shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    public final void squaredadditions$placementConnectsToVerticalSlab(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir, BlockView blockView, BlockPos panePos, FluidState paneFluid,
                                                                BlockPos northPos, BlockPos southPos, BlockPos westPos, BlockPos eastPos, BlockState northState, BlockState southState, BlockState westState) {

        BlockState eastState = blockView.getBlockState(eastPos);
        if (northState.getBlock() instanceof SlabBlock || southState.getBlock() instanceof SlabBlock || westState.getBlock() instanceof SlabBlock || eastState.getBlock() instanceof SlabBlock) {
            cir.setReturnValue(((PaneBlock)(Object)this).getDefaultState()
                    .with(NORTH, PlacementUtil.calcPaneCanConnectToVerticalSlab(Direction.NORTH, northState, northState.isSideSolidFullSquare(blockView, northPos, Direction.SOUTH)))
                    .with(SOUTH, PlacementUtil.calcPaneCanConnectToVerticalSlab(Direction.SOUTH, southState, southState.isSideSolidFullSquare(blockView, southPos, Direction.NORTH)))
                    .with(WEST, PlacementUtil.calcPaneCanConnectToVerticalSlab(Direction.WEST, westState, westState.isSideSolidFullSquare(blockView, westPos, Direction.EAST)))
                    .with(EAST, PlacementUtil.calcPaneCanConnectToVerticalSlab(Direction.EAST, eastState, eastState.isSideSolidFullSquare(blockView, eastPos, Direction.WEST)))
                    .with(WATERLOGGED, paneFluid.getFluid() == Fluids.WATER));
        }
    }
}