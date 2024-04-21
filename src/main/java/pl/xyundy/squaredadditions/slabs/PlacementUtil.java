package pl.xyundy.squaredadditions.slabs;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;

import static pl.xyundy.squaredadditions.slabs.Util.*;
import static pl.xyundy.squaredadditions.slabs.VerticalType.*;
import static net.minecraft.block.LightBlock.LEVEL_15;
import static net.minecraft.block.PaneBlock.cannotConnect;
import static net.minecraft.block.SlabBlock.TYPE;
import static net.minecraft.block.SlabBlock.WATERLOGGED;
import static net.minecraft.block.enums.SlabType.TOP;

public class PlacementUtil {

    public static boolean calcPaneCanConnectToVerticalSlab(Direction paneDirection, BlockState offsetState, boolean sideSolidFullSquare) {
        if (sideSolidFullSquare && !cannotConnect(offsetState)) {
            return true;
        }
        if (offsetState.getBlock() instanceof SlabBlock) {
            switch (paneDirection) {
                case NORTH, SOUTH -> {
                    return offsetState.get(VERTICAL_TYPE) == EAST_WEST;
                }
                case EAST, WEST -> {
                    return offsetState.get(VERTICAL_TYPE) == NORTH_SOUTH;
                }
            }
        }
        return offsetState.getBlock() instanceof PaneBlock || offsetState.isIn(BlockTags.WALLS);
    }

    public static VoxelShape getDynamicOutlineShape(VerticalType verticalType, Direction side, BlockHitResult cast) {
        return switch (verticalType) {
            case FALSE -> {
                switch (side) {
                    case UP -> {
                        yield TOP_SHAPE;
                    }
                    case DOWN -> {
                        yield BOTTOM_SHAPE;
                    }
                    default -> {
                        var yPos = cast.getPos().y;
                        var yOffset = ((yPos % 1) + 1) % 1;
                        if (yOffset > 0.5) yield TOP_SHAPE;
                        else yield BOTTOM_SHAPE;
                    }
                }
            }
            case NORTH_SOUTH -> {
                switch (side) {
                    case NORTH -> {
                        yield VERTICAL_NORTH_SOUTH_TOP_SHAPE;
                    }
                    case SOUTH -> {
                        yield VERTICAL_NORTH_SOUTH_BOTTOM_SHAPE;
                    }
                    default -> {
                        var zPos = cast.getPos().z;
                        var zOffset = ((zPos % 1) + 1) % 1;
                        if (zOffset > 0.5) yield VERTICAL_NORTH_SOUTH_BOTTOM_SHAPE;
                        else yield VERTICAL_NORTH_SOUTH_TOP_SHAPE;
                    }
                }
            }
            case EAST_WEST -> {
                switch (side) {
                    case EAST -> {
                        yield VERTICAL_EAST_WEST_TOP_SHAPE;
                    }
                    case WEST -> {
                        yield VERTICAL_EAST_WEST_BOTTOM_SHAPE;
                    }
                    default -> {
                        var xPos = cast.getPos().x;
                        var xOffset = ((xPos % 1) + 1) % 1;
                        if (xOffset > 0.5) yield VERTICAL_EAST_WEST_TOP_SHAPE;
                        else yield VERTICAL_EAST_WEST_BOTTOM_SHAPE;
                    }
                }
            }
        };
    }

    public static VoxelShape getOutlineShape(BlockState state) {
        SlabType slabType = state.get(Util.TYPE);
        VerticalType verticalType = state.get(VERTICAL_TYPE);
        if (slabType == SlabType.DOUBLE) {
            return VoxelShapes.fullCube(); // double slab is calculated in SlabBlockMixin
        }
        return switch (verticalType) {
            case FALSE -> slabType == TOP ? TOP_SHAPE : BOTTOM_SHAPE;
            case NORTH_SOUTH -> slabType == TOP ? VERTICAL_NORTH_SOUTH_TOP_SHAPE : VERTICAL_NORTH_SOUTH_BOTTOM_SHAPE;
            case EAST_WEST -> slabType == TOP ? VERTICAL_EAST_WEST_TOP_SHAPE : VERTICAL_EAST_WEST_BOTTOM_SHAPE;
        };
    }

    public static BlockState getModelState(BlockState state, VerticalType verticalType, Direction side, BlockHitResult cast) {
        return switch (verticalType) {
            case FALSE -> {
                switch (side) {
                    case UP -> {
                        yield state.getBlock().getDefaultState().with(TYPE, SlabType.TOP);
                    }
                    case DOWN -> {
                        yield state.getBlock().getDefaultState().with(TYPE, SlabType.BOTTOM);
                    }
                    default -> {
                        var yPos = cast.getPos().y;
                        var yOffset = ((yPos % 1) + 1) % 1;
                        if (yOffset > 0.5) yield state.getBlock().getDefaultState().with(TYPE, SlabType.TOP);
                        else yield state.getBlock().getDefaultState().with(TYPE, SlabType.BOTTOM);
                    }
                }
            }
            case NORTH_SOUTH -> {
                switch (side) {
                    case NORTH -> {
                        yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, NORTH_SOUTH).with(TYPE, SlabType.TOP);
                    }
                    case SOUTH -> {
                        yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, NORTH_SOUTH).with(TYPE, SlabType.BOTTOM);
                    }
                    default -> {
                        var zPos = cast.getPos().z;
                        var zOffset = ((zPos % 1) + 1) % 1;
                        if (zOffset > 0.5) yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, NORTH_SOUTH).with(TYPE, SlabType.BOTTOM);
                        else yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, NORTH_SOUTH).with(TYPE, SlabType.TOP);
                    }
                }
            }
            case EAST_WEST -> {
                switch (side) {
                    case EAST -> {
                        yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.TOP);
                    }
                    case WEST -> {
                        yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.BOTTOM);
                    }
                    default -> {
                        var xPos = cast.getPos().x;
                        var xOffset = ((xPos % 1) + 1) % 1;
                        if (xOffset > 0.5) yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.TOP);
                        else yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.BOTTOM);
                    }
                }
            }
        };
    }

    public static BlockHitResult calcRaycast(Entity entity) {
        Vec3d vec3d = entity.getCameraPosVec(0);
        Vec3d vec3d2 = entity.getRotationVec(0);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * 5d, vec3d2.y * 5d, vec3d2.z * 5d);
        return entity.getWorld().raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity));
    }

    public static SlabType calcKleeSlab(BlockState breakState, BlockHitResult cast) {
        Direction side = cast.getSide();
        VerticalType verticalType = breakState.get(VERTICAL_TYPE);
        SlabType breakType = SlabType.DOUBLE;
        if (verticalType != null) {
            switch (verticalType) {
                case FALSE -> breakType = switch (side) {
                    case UP -> SlabType.BOTTOM;
                    case DOWN -> SlabType.TOP;
                    default -> {
                        var yPos = cast.getPos().y;
                        var yOffset = ((yPos % 1) + 1) % 1;
                        if (yOffset > 0.5) yield SlabType.BOTTOM;
                        else yield SlabType.TOP;
                    }
                };
                case NORTH_SOUTH -> breakType = switch (side) {
                    case NORTH -> SlabType.BOTTOM;
                    case SOUTH -> SlabType.TOP;
                    default -> {
                        var zPos = cast.getPos().z;
                        var zOffset = ((zPos % 1) + 1) % 1;
                        if (zOffset > 0.5) yield SlabType.TOP;
                        else yield SlabType.BOTTOM;
                    }
                };
                case EAST_WEST -> breakType = switch (side) {
                    case EAST -> SlabType.BOTTOM;
                    case WEST -> SlabType.TOP;
                    default -> {
                        var xPos = cast.getPos().x;
                        var xOffset = ((xPos % 1) + 1) % 1;
                        if (xOffset > 0.5) yield SlabType.BOTTOM;
                        else yield SlabType.TOP;
                    }
                };
            }
        }
        return breakType;
    }

    public static boolean canReplace(BlockState state, ItemPlacementContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return false;
        if (!Slabs.slabLockPosition.getOrDefault(player, SlabLockEnum.DEFAULT_AUTO).equals(SlabLockEnum.DEFAULT_AUTO)) {
            return false;
        }
        ItemStack itemStack = context.getStack();
        SlabType slabType = state.get(TYPE);
        if (slabType != SlabType.DOUBLE && itemStack.isOf(state.getBlock().asItem())) {
            if (context.canReplaceExisting()) {
                BlockHitResult blockHitResult = PlacementUtil.calcRaycast(player);
                HitPart part = getHitPart(blockHitResult);
                boolean topHalfX = context.getHitPos().x - (double) context.getBlockPos().getX() > 0.5;
                boolean topHalfY = context.getHitPos().y - (double) context.getBlockPos().getY() > 0.5;
                boolean topHalfZ = context.getHitPos().z - (double) context.getBlockPos().getZ() > 0.5;
                Direction direction = context.getSide();
                VerticalType verticalType = state.get(VERTICAL_TYPE);
                if (verticalType != null) {
                    if (verticalType == VerticalType.FALSE) {
                        if (slabType == SlabType.BOTTOM) {
                            if (direction == Direction.UP || topHalfY && direction.getAxis().isHorizontal()) {
                                return part == HitPart.CENTER;
                            }
                        } else {
                            if (direction == Direction.DOWN || !topHalfY && direction.getAxis().isHorizontal()) {
                                return part == HitPart.CENTER;
                            }
                        }
                    } else if (verticalType == NORTH_SOUTH) {
                        if (slabType == SlabType.BOTTOM) {
                            if (direction == Direction.NORTH || !topHalfZ && direction.getAxis().isVertical()) {
                                return part == HitPart.CENTER;
                            }
                        } else {
                            if (direction == Direction.SOUTH || topHalfZ && direction.getAxis().isVertical()) {
                                return part == HitPart.CENTER;
                            }
                        }
                    } else if (verticalType == VerticalType.EAST_WEST) {
                        if (slabType == SlabType.BOTTOM) {
                            if (direction == Direction.EAST || topHalfX && direction.getAxis().isVertical()) {
                                return part == HitPart.CENTER;
                            }
                        } else {
                            if (direction == Direction.WEST || !topHalfX && direction.getAxis().isVertical()) {
                                return part == HitPart.CENTER;
                            }
                        }
                    }
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public static BlockState calcPlacementState(ItemPlacementContext ctx, BlockState state) {
        PlayerEntity player = ctx.getPlayer();
        if (player == null) return null;
        BlockPos blockPos = ctx.getBlockPos();
        FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
        switch (Slabs.slabLockPosition.getOrDefault(player, SlabLockEnum.DEFAULT_AUTO)) {
            case BOTTOM_SLAB -> {
                return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            case TOP_SLAB -> {
                return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            case NORTH_SLAB_VERTICAL -> {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            case SOUTH_SLAB_VERTICAL -> {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            case EAST_SLAB_VERTICAL -> {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
            case WEST_SLAB_VERTICAL -> {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        BlockState blockState = ctx.getWorld().getBlockState(blockPos);
        // Thank you Andrew for this code!
        // horrendous hack to make the client think that it can place down a slab
        // without visual desync or server-client communication
        // (light blocks with a level of 0 are completely invisible and also have no hitbox
        // If I return null, the client does not play the "block place" animation
        if (!(ctx.getWorld() instanceof ServerWorld)) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, false);
            } else {
                return Blocks.LIGHT.getDefaultState().with(LEVEL_15, 0);
            }
        }
        Direction ctxSide = ctx.getSide();
        BlockHitResult blockHitResult = PlacementUtil.calcRaycast(player);
        HitPart part = getHitPart(blockHitResult);
        return switch (ctxSide) {
            case UP -> calcUpPlacement(blockState, state, part, fluidState);
            case DOWN -> calcDownPlacement(blockState, state, part, fluidState);
            case NORTH -> calcNorthPlacement(blockState, state, part, fluidState);
            case SOUTH -> calcSouthPlacement(blockState, state, part, fluidState);
            case EAST -> calcEastPlacement(blockState, state, part, fluidState);
            case WEST -> calcWestPlacement(blockState, state, part, fluidState);
        };
    }

    public static BlockState calcUpPlacement(BlockState blockState, BlockState state, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, false);
            }
            if (part == HitPart.CENTER) {
                return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.BOTTOM) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcDownPlacement(BlockState blockState, BlockState state, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, false);
            }
            if (part == HitPart.CENTER) {
                return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.BOTTOM) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcNorthPlacement(BlockState blockState, BlockState state, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, false);
            }
            if (part == HitPart.CENTER) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.BOTTOM) {
                return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcSouthPlacement(BlockState blockState, BlockState state, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, false);
            }
            if (part == HitPart.CENTER) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.BOTTOM) {
                return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcEastPlacement(BlockState blockState, BlockState state, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, false);
            }
            if (part == HitPart.CENTER) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.BOTTOM) {
                return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }

    public static BlockState calcWestPlacement(BlockState blockState, BlockState state, HitPart part, FluidState fluidState) {
        if (part != null) {
            if (blockState.isOf(state.getBlock())) {
                return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, false);
            }
            if (part == HitPart.CENTER) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.BOTTOM) {
                return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.TOP) {
                return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.LEFT) {
                return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            } else if (part == HitPart.RIGHT) {
                return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
            }
        }
        return null;
    }
}