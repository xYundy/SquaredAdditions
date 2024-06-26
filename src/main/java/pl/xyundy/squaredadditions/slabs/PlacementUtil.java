package pl.xyundy.squaredadditions.slabs;

import net.minecraft.block.*;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;
import pl.xyundy.squaredadditions.block.MixedSlabBlock;

import static net.minecraft.block.enums.SlabType.BOTTOM;
import static net.minecraft.state.property.Properties.SLAB_TYPE;
import static pl.xyundy.squaredadditions.block.ModBlocks.MIXED_SLAB_BLOCK;
import static pl.xyundy.squaredadditions.slabs.Util.*;
import static pl.xyundy.squaredadditions.slabs.VerticalType.*;
import static net.minecraft.block.PaneBlock.cannotConnect;
import static net.minecraft.block.SlabBlock.TYPE;
import static net.minecraft.block.SlabBlock.WATERLOGGED;
import static net.minecraft.block.enums.SlabType.TOP;

// Massive thanks to andrew6rant for big part of this code
// https://github.com/Andrew6rant/Auto-Slabs/blob/1.20.x/src/main/java/io/github/andrew6rant/autoslabs/PlacementUtil.java
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
                        if (zOffset > 0.5)
                            yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, NORTH_SOUTH).with(TYPE, SlabType.BOTTOM);
                        else
                            yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, NORTH_SOUTH).with(TYPE, SlabType.TOP);
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
                        if (xOffset > 0.5)
                            yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.TOP);
                        else
                            yield state.getBlock().getDefaultState().with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(TYPE, SlabType.BOTTOM);
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
        Item item = itemStack.getItem();

        SlabType slabType = state.get(TYPE);
        if (slabType != SlabType.DOUBLE && (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof SlabBlock)) {
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

        if (blockState.isOf(state.getBlock())) {
            return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, false);
        }
        if (blockState.getBlock() instanceof SlabBlock && blockState.getBlock().getDefaultState() != state) {
            return getMixedBlockState(blockState, state);
        }

        Direction ctxSide = ctx.getSide();
        BlockHitResult blockHitResult = PlacementUtil.calcRaycast(player);
        HitPart part = getHitPart(blockHitResult);

        if (null == part) {
            return null;
        }

        return calcPlacement(ctxSide, state, part, fluidState);
    }

    private static BlockState calcPlacement(Direction placementDirection, BlockState state, HitPart part, FluidState fluidState) {
        if (part == HitPart.CENTER) {
            switch (placementDirection) {
                case DOWN -> {
                    return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                case UP -> {
                    return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                case NORTH -> {
                    return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                case SOUTH -> {
                    return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                case WEST -> {
                    return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                case EAST -> {
                    return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
            }
        }

        if (part == HitPart.BOTTOM) {
            switch (placementDirection) {
                case DOWN, UP -> {
                    return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                default -> {
                    return state.with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
            }
        }

        if (part == HitPart.TOP) {
            switch (placementDirection) {
                case DOWN, UP -> {
                    return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                default -> {
                    return state.with(TYPE, SlabType.TOP).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
            }
        }

        if (part == HitPart.LEFT) {
            switch (placementDirection) {
                case UP, NORTH -> {
                    return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                case DOWN, SOUTH -> {
                    return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                case EAST -> {
                    return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                case WEST -> {
                    return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
            }
        }

        if (part == HitPart.RIGHT) {
            switch (placementDirection) {
                case UP, NORTH -> {
                    return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                case DOWN, SOUTH -> {
                    return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, VerticalType.EAST_WEST).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                case EAST -> {
                    return state.with(TYPE, SlabType.TOP).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
                case WEST -> {
                    return state.with(TYPE, SlabType.BOTTOM).with(VERTICAL_TYPE, NORTH_SOUTH).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
                }
            }
        }
        return null;
    }

    private static BlockState getMixedBlockState(BlockState currentSlabState, BlockState newSlabState) {
        switch (currentSlabState.get(SLAB_TYPE)) {
            case BOTTOM -> {
                return ((MixedSlabBlock) MIXED_SLAB_BLOCK).getDefaultStateWithSlabStates(currentSlabState, newSlabState.with(SLAB_TYPE, TOP).with(VERTICAL_TYPE, currentSlabState.get(VERTICAL_TYPE)));
            }
            case TOP -> {
                return ((MixedSlabBlock) MIXED_SLAB_BLOCK).getDefaultStateWithSlabStates(newSlabState.with(SLAB_TYPE, BOTTOM).with(VERTICAL_TYPE, currentSlabState.get(VERTICAL_TYPE)), currentSlabState);
            }
            default -> {
                return null;
            }
        }
    }
}