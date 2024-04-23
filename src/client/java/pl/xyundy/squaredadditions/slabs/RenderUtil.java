package pl.xyundy.squaredadditions.slabs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Vector3f;

import java.util.Objects;

import static pl.xyundy.squaredadditions.SquaredAdditionsClient.clientSlabLockPosition;
import static pl.xyundy.squaredadditions.slabs.Util.*;

// Massive thanks to andrew6rant & Schauwegfor this code
// https://github.com/Andrew6rant/Auto-Slabs/blob/1.20.x/src/main/java/io/github/andrew6rant/autoslabs/Util.java
public class RenderUtil {

    public static void drawSlabIcon(PlayerEntity player, DrawContext context, int u, int v) {
        ItemStack heldItem = player.getStackInHand(player.getActiveHand());
        if (heldItem != null && !heldItem.isEmpty() && heldItem.getItem() instanceof BlockItem && ((BlockItem) heldItem.getItem()).getBlock() instanceof SlabBlock) {
            context.drawTexture(new Identifier("squaredadditions", "textures/gui/squaredadditions_position_lock.png"), (context.getScaledWindowWidth() - 15) / 2, (context.getScaledWindowHeight() - 42) / 2, u, v, 15, 15, 64, 64);
        }
    }

    public static void renderOverlay(MatrixStack matrices, VertexConsumer vertexConsumer, Vec3d camDif1, BlockState state, VoxelShape shape, HitResult hitResult, float red, float green, float blue, float alpha) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (Block.getBlockFromItem(player.getStackInHand(player.getActiveHand()).getItem()) instanceof SlabBlock || (Block.getBlockFromItem(player.getOffHandStack().getItem()) instanceof SlabBlock && player.getMainHandStack().isEmpty())) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {

                BlockHitResult result = (BlockHitResult) hitResult;
                HitPart part = getHitPart(result);
                Vec3d camDif = getCameraOffset(camDif1, shape, result.getSide());

                if (state.getBlock() instanceof SlabBlock) {
                    renderOverlayToDirection(state, result.getSide(), matrices, vertexConsumer, camDif, part, red, green, blue, alpha);
                } else {
                    renderOverlayToDirection(null, result.getSide(), matrices, vertexConsumer, camDif, part, red, green, blue, alpha);
                }
            }
        }
    }

    private static void drawLine(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f start, Vector3f end, Vec3d camDif, float red, float green, float blue, float alpha) {
        Vector3f normal = getNormalAngle(start, end);

        Vector3f startRaw = new Vector3f((float) (start.x + camDif.x), (float) (start.y + camDif.y), (float) (start.z + camDif.z));
        Vector3f endRaw = new Vector3f((float) (end.x + camDif.x), (float) (end.y + camDif.y), (float) (end.z + camDif.z));

        vertexConsumer.vertex(entry.getPositionMatrix(), startRaw.x, startRaw.y, startRaw.z)
                .color(red, green, blue, alpha).normal(entry.getNormalMatrix(), normal.x, normal.y, normal.z).next();

        vertexConsumer.vertex(entry.getPositionMatrix(), endRaw.x, endRaw.y, endRaw.z)
                .color(red, green, blue, alpha).normal(entry.getNormalMatrix(), normal.x, normal.y, normal.z).next();
    }

    private static void renderOverlayToDirection(BlockState state, Direction side, MatrixStack matrixStack, VertexConsumer vertexConsumer, Vec3d camDif, HitPart part, float red, float green, float blue, float alpha) {
        Vector3f vecBottomLeft = null, vecBottomRight = null, vecTopLeft = null, vecTopRight = null,
                vecCenterBottomLeft = null, vecCenterBottomRight = null, vecCenterTopLeft = null, vecCenterTopRight = null,
                vecCenterMiddleLeft = null, vecCenterMiddleRight = null, vecCenterMiddleBottom = null, vecCenterMiddleTop = null;

        //@formatter:off
        switch (side) {
            case DOWN -> {
                vecBottomLeft         = new Vector3f(0f,    0f,    0f);
                vecBottomRight        = new Vector3f(1f,    0f,    0f);
                vecTopLeft            = new Vector3f(0f,    0f,    1f);
                vecTopRight           = new Vector3f(1f,    0f,    1f);
                vecCenterBottomLeft   = new Vector3f(0.25f, 0f,    0.25f);
                vecCenterBottomRight  = new Vector3f(0.75f, 0f,    0.25f);
                vecCenterTopLeft      = new Vector3f(0.25f, 0f,    0.75f);
                vecCenterTopRight     = new Vector3f(0.75f, 0f,    0.75f);
                vecCenterMiddleLeft   = new Vector3f(0.25f, 0f,    0.5f);
                vecCenterMiddleRight  = new Vector3f(0.75f, 0f,    0.5f);
                vecCenterMiddleBottom = new Vector3f(0.5f,  0f,    0.25f);
                vecCenterMiddleTop    = new Vector3f(0.5f,  0f,    0.75f);
            }
            case UP -> {
                vecBottomLeft         = new Vector3f(1f,    1f,    0f);
                vecBottomRight        = new Vector3f(0f,    1f,    0f);
                vecTopLeft            = new Vector3f(1f,    1f,    1f);
                vecTopRight           = new Vector3f(0f,    1f,    1f);
                vecCenterBottomLeft   = new Vector3f(0.75f, 1f,    0.25f);
                vecCenterBottomRight  = new Vector3f(0.25f, 1f,    0.25f);
                vecCenterTopLeft      = new Vector3f(0.75f, 1f,    0.75f);
                vecCenterTopRight     = new Vector3f(0.25f, 1f,    0.75f);
                vecCenterMiddleLeft   = new Vector3f(0.75f, 1f,    0.5f);
                vecCenterMiddleRight  = new Vector3f(0.25f, 1f,    0.5f);
                vecCenterMiddleBottom = new Vector3f(0.5f,  1f,    0.25f);
                vecCenterMiddleTop    = new Vector3f(0.5f,  1f,    0.75f);
            }
            case NORTH -> {
                vecBottomLeft         = new Vector3f(1f,    0f,    0f);
                vecBottomRight        = new Vector3f(0f,    0f,    0f);
                vecTopLeft            = new Vector3f(1f,    1f,    0f);
                vecTopRight           = new Vector3f(0f,    1f,    0f);
                vecCenterBottomLeft   = new Vector3f(0.75f, 0.25f, 0f);
                vecCenterBottomRight  = new Vector3f(0.25f, 0.25f, 0f);
                vecCenterTopLeft      = new Vector3f(0.75f, 0.75f, 0f);
                vecCenterTopRight     = new Vector3f(0.25f, 0.75f, 0f);
                vecCenterMiddleLeft   = new Vector3f(0.75f, 0.5f,  0f);
                vecCenterMiddleRight  = new Vector3f(0.25f, 0.5f,  0f);
                vecCenterMiddleBottom = new Vector3f(0.5f,  0.25f, 0f);
                vecCenterMiddleTop    = new Vector3f(0.5f,  .75f,  0f);
            }
            case SOUTH -> {
                vecBottomLeft         = new Vector3f(0f,    0f,    1f);
                vecBottomRight        = new Vector3f(1f,    0f,    1f);
                vecTopLeft            = new Vector3f(0f,    1f,    1f);
                vecTopRight           = new Vector3f(1f,    1f,    1f);
                vecCenterBottomLeft   = new Vector3f(0.25f, 0.25f, 1f);
                vecCenterBottomRight  = new Vector3f(0.75f, 0.25f, 1f);
                vecCenterTopLeft      = new Vector3f(0.25f, 0.75f, 1f);
                vecCenterTopRight     = new Vector3f(0.75f, 0.75f, 1f);
                vecCenterMiddleLeft   = new Vector3f(0.25f, 0.5f,  1f);
                vecCenterMiddleRight  = new Vector3f(0.75f, 0.5f,  1f);
                vecCenterMiddleBottom = new Vector3f(0.5f,  0.25f, 1f);
                vecCenterMiddleTop    = new Vector3f(0.5f,  0.75f, 1f);
            }
            case WEST -> {
                vecBottomLeft         = new Vector3f(0f,    0f,    0f);
                vecBottomRight        = new Vector3f(0f,    0f,    1f);
                vecTopLeft            = new Vector3f(0f,    1f,    0f);
                vecTopRight           = new Vector3f(0f,    1f,    1f);
                vecCenterBottomLeft   = new Vector3f(0f,    0.25f, 0.25f);
                vecCenterBottomRight  = new Vector3f(0f,    0.25f, 0.75f);
                vecCenterTopLeft      = new Vector3f(0f,    0.75f, 0.25f);
                vecCenterTopRight     = new Vector3f(0f,    0.75f, 0.75f);
                vecCenterMiddleLeft   = new Vector3f(0f,    0.5f,  0.25f);
                vecCenterMiddleRight  = new Vector3f(0f,    0.5f,  0.75f);
                vecCenterMiddleBottom = new Vector3f(0f,    0.25f, 0.5f);
                vecCenterMiddleTop    = new Vector3f(0f,    0.75f, 0.5f);
            }
            case EAST -> {
                vecBottomLeft         = new Vector3f(1f,    0f,    1f);
                vecBottomRight        = new Vector3f(1f,    0f,    0f);
                vecTopLeft            = new Vector3f(1f,    1f,    1f);
                vecTopRight           = new Vector3f(1f,    1f,    0f);
                vecCenterBottomLeft   = new Vector3f(1f,    0.25f, 0.75f);
                vecCenterBottomRight  = new Vector3f(1f,    0.25f, 0.25f);
                vecCenterTopLeft      = new Vector3f(1f,    0.75f, 0.75f);
                vecCenterTopRight     = new Vector3f(1f,    0.75f, 0.25f);
                vecCenterMiddleLeft   = new Vector3f(1f,    0.5f,  0.75f);
                vecCenterMiddleRight  = new Vector3f(1f,    0.5f,  0.25f);
                vecCenterMiddleBottom = new Vector3f(1f,    0.25f, 0.5f);
                vecCenterMiddleTop    = new Vector3f(1f,    0.75f, 0.5f);
            }
        }
        //@formatter:on

        SlabType slabType = null;
        VerticalType verticalType = null;
        if (state != null) {
            slabType = state.get(SlabBlock.TYPE);
            verticalType = state.get(VERTICAL_TYPE);
        }
        MatrixStack.Entry entry = matrixStack.peek();

        switch (clientSlabLockPosition) {
            case DEFAULT_AUTO -> {
                // I have no idea why, but this code only works when
                // in an if chain and not a switch statement
                if (part == HitPart.CENTER) {
                    drawCenterLines(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleLeft, vecCenterMiddleRight, vecCenterMiddleBottom, vecCenterMiddleTop, camDif, slabType, verticalType, side, red, green, blue, alpha);
                } else if (part == HitPart.BOTTOM) {
                    drawTopBottomLines(entry, vertexConsumer, vecBottomLeft, vecBottomRight, vecCenterBottomLeft, vecCenterBottomRight, vecCenterMiddleBottom, slabType, verticalType, side, camDif, red, green, blue, alpha);
                } else if (part == HitPart.TOP) {
                    drawTopBottomLines(entry, vertexConsumer, vecTopLeft, vecTopRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleTop, slabType, verticalType, side, camDif, red, green, blue, alpha);
                } else if (part == HitPart.LEFT) {
                    drawLeftRightLines(entry, vertexConsumer, vecBottomLeft, vecTopLeft, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleLeft, slabType, verticalType, side, camDif, red, green, blue, alpha);
                } else if (part == HitPart.RIGHT) {
                    drawLeftRightLines(entry, vertexConsumer, vecBottomRight, vecTopRight, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleRight, slabType, verticalType, side, camDif, red, green, blue, alpha);
                }
            }
            case BOTTOM_SLAB -> {
                switch (side) {
                    case UP, DOWN -> {
                        drawCenterLines(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleLeft, vecCenterMiddleRight, vecCenterMiddleBottom, vecCenterMiddleTop, camDif, slabType, verticalType, side, red, green, blue, alpha);
                    }
                    case NORTH, SOUTH, EAST, WEST -> {
                        drawTopBottomLines(entry, vertexConsumer, vecBottomLeft, vecBottomRight, vecCenterBottomLeft, vecCenterBottomRight, vecCenterMiddleBottom, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                }
            }
            case TOP_SLAB -> {
                switch (side) {
                    case UP, DOWN -> {
                        drawCenterLines(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleLeft, vecCenterMiddleRight, vecCenterMiddleBottom, vecCenterMiddleTop, camDif, slabType, verticalType, side, red, green, blue, alpha);
                    }
                    case NORTH, SOUTH, EAST, WEST -> {
                        drawTopBottomLines(entry, vertexConsumer, vecTopLeft, vecTopRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleTop, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                }
            }
            case NORTH_SLAB_VERTICAL -> {
                switch (side) {
                    case UP, DOWN -> {
                        drawTopBottomLines(entry, vertexConsumer, vecBottomLeft, vecBottomRight, vecCenterBottomLeft, vecCenterBottomRight, vecCenterMiddleBottom, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                    case NORTH, SOUTH -> {
                        drawCenterLines(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleLeft, vecCenterMiddleRight, vecCenterMiddleBottom, vecCenterMiddleTop, camDif, slabType, verticalType, side, red, green, blue, alpha);
                    }
                    case EAST -> {
                        drawLeftRightLines(entry, vertexConsumer, vecBottomRight, vecTopRight, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleRight, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                    case WEST -> {
                        drawLeftRightLines(entry, vertexConsumer, vecBottomLeft, vecTopLeft, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleLeft, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                }
            }
            case SOUTH_SLAB_VERTICAL -> {
                switch (side) {
                    case UP, DOWN -> {
                        drawTopBottomLines(entry, vertexConsumer, vecTopLeft, vecTopRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleTop, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                    case NORTH, SOUTH -> {
                        drawCenterLines(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleLeft, vecCenterMiddleRight, vecCenterMiddleBottom, vecCenterMiddleTop, camDif, slabType, verticalType, side, red, green, blue, alpha);
                    }
                    case EAST -> {
                        drawLeftRightLines(entry, vertexConsumer, vecBottomLeft, vecTopLeft, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleLeft, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                    case WEST -> {
                        drawLeftRightLines(entry, vertexConsumer, vecBottomRight, vecTopRight, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleRight, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                }
            }
            case EAST_SLAB_VERTICAL -> {
                switch (side) {
                    case UP -> {
                        drawLeftRightLines(entry, vertexConsumer, vecBottomLeft, vecTopLeft, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleLeft, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                    case DOWN -> {
                        drawLeftRightLines(entry, vertexConsumer, vecBottomRight, vecTopRight, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleRight, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                    case EAST, WEST -> {
                        drawCenterLines(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleLeft, vecCenterMiddleRight, vecCenterMiddleBottom, vecCenterMiddleTop, camDif, slabType, verticalType, side, red, green, blue, alpha);
                    }
                    case NORTH -> {
                        drawLeftRightLines(entry, vertexConsumer, vecBottomLeft, vecTopLeft, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleLeft, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                    case SOUTH -> {
                        drawLeftRightLines(entry, vertexConsumer, vecBottomRight, vecTopRight, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleRight, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                }
            }
            case WEST_SLAB_VERTICAL -> {
                switch (side) {
                    case UP -> {
                        drawLeftRightLines(entry, vertexConsumer, vecBottomRight, vecTopRight, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleRight, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                    case DOWN -> {
                        drawLeftRightLines(entry, vertexConsumer, vecBottomLeft, vecTopLeft, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleLeft, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                    case EAST, WEST -> {
                        drawCenterLines(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleLeft, vecCenterMiddleRight, vecCenterMiddleBottom, vecCenterMiddleTop, camDif, slabType, verticalType, side, red, green, blue, alpha);
                    }
                    case NORTH -> {
                        drawLeftRightLines(entry, vertexConsumer, vecBottomRight, vecTopRight, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleRight, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                    case SOUTH -> {
                        drawLeftRightLines(entry, vertexConsumer, vecBottomLeft, vecTopLeft, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleLeft, slabType, verticalType, side, camDif, red, green, blue, alpha);
                    }
                }
            }
        }
    }

    private static void drawLeftRightLines(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vector3f vecCenterMiddleCorner, SlabType slabType, VerticalType verticalType, Direction side, Vec3d camDif, float red, float green, float blue, float alpha) {
        if (verticalType != null && slabType != null) {
            switch (verticalType) {
                case FALSE -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            if (side == Direction.DOWN || side == Direction.UP) {
                                drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
                            } else {
                                drawInternal(entry, vertexConsumer, vecEndCorner, vecStartCorner, vecCenterEndCorner, vecCenterStartCorner, vecCenterMiddleCorner, slabType, camDif, red, green, blue, alpha);
                            }
                        }
                        case DOUBLE ->
                                drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
                    }
                }
                case NORTH_SOUTH -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            if (side == Direction.DOWN || side == Direction.UP) {
                                drawInternal(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, vecCenterMiddleCorner, slabType, camDif, red, green, blue, alpha);
                            } else {
                                drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
                            }
                        }
                        case DOUBLE ->
                                drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
                    }
                }
                case EAST_WEST ->
                        drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
            }
        } else {
            drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
        }
    }

    private static void drawCenterLines(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecCenterBottomLeft, Vector3f vecCenterBottomRight, Vector3f vecCenterTopLeft, Vector3f vecCenterTopRight, Vector3f vecCenterMiddleLeft, Vector3f vecCenterMiddleRight, Vector3f vecCenterMiddleBottom, Vector3f vecCenterMiddleTop, Vec3d camDif, SlabType slabType, VerticalType verticalType, Direction side, float red, float green, float blue, float alpha) {
        if (verticalType != null && slabType != null) {
            switch (verticalType) {
                case FALSE -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            if (side == Direction.DOWN || side == Direction.UP) {
                                drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif, red, green, blue, alpha);
                            } else {
                                drawInternalSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, vecCenterMiddleLeft, vecCenterMiddleRight, slabType, camDif, red, green, blue, alpha);
                            }
                        }
                        case DOUBLE ->
                                drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif, red, green, blue, alpha);
                    }
                }
                case NORTH_SOUTH -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            switch (side) {
                                case DOWN, UP ->
                                        drawInternalSquare(entry, vertexConsumer, vecCenterTopLeft, vecCenterTopRight, vecCenterBottomLeft, vecCenterBottomRight, vecCenterMiddleLeft, vecCenterMiddleRight, slabType, camDif, red, green, blue, alpha);
                                case NORTH, SOUTH ->
                                        drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif, red, green, blue, alpha);
                                case EAST ->
                                        drawInternalSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterTopLeft, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleBottom, vecCenterMiddleTop, slabType, camDif, red, green, blue, alpha);
                                case WEST ->
                                        drawInternalSquare(entry, vertexConsumer, vecCenterBottomRight, vecCenterTopRight, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleBottom, vecCenterMiddleTop, slabType, camDif, red, green, blue, alpha);
                            }
                        }
                        case DOUBLE ->
                                drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif, red, green, blue, alpha);
                    }
                }
                case EAST_WEST -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            switch (side) {
                                case EAST, WEST ->
                                        drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif, red, green, blue, alpha);
                                case SOUTH, DOWN ->
                                        drawInternalSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterTopLeft, vecCenterBottomRight, vecCenterTopRight, vecCenterMiddleBottom, vecCenterMiddleTop, slabType, camDif, red, green, blue, alpha);
                                case NORTH, UP ->
                                        drawInternalSquare(entry, vertexConsumer, vecCenterBottomRight, vecCenterTopRight, vecCenterBottomLeft, vecCenterTopLeft, vecCenterMiddleBottom, vecCenterMiddleTop, slabType, camDif, red, green, blue, alpha);
                            }
                        }
                        case DOUBLE ->
                                drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif, red, green, blue, alpha);
                    }
                }
            }
        } else {
            drawDefaultSquare(entry, vertexConsumer, vecCenterBottomLeft, vecCenterBottomRight, vecCenterTopLeft, vecCenterTopRight, camDif, red, green, blue, alpha);
        }
    }

    private static void drawTopBottomLines(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vector3f vecCenterMiddleCorner, SlabType slabType, VerticalType verticalType, Direction side, Vec3d camDif, float red, float green, float blue, float alpha) {
        if (verticalType != null && slabType != null) {
            switch (verticalType) {
                case FALSE ->
                        drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
                case NORTH_SOUTH -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            switch (side) {
                                case DOWN, UP, NORTH, SOUTH ->
                                        drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
                                case EAST ->
                                        drawInternal(entry, vertexConsumer, vecEndCorner, vecStartCorner, vecCenterEndCorner, vecCenterStartCorner, vecCenterMiddleCorner, slabType, camDif, red, green, blue, alpha);
                                case WEST ->
                                        drawInternal(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, vecCenterMiddleCorner, slabType, camDif, red, green, blue, alpha);
                            }
                        }
                        case DOUBLE ->
                                drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
                    }
                }
                case EAST_WEST -> {
                    switch (slabType) {
                        case BOTTOM, TOP -> {
                            switch (side) {
                                case EAST, WEST ->
                                        drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
                                case NORTH, UP ->
                                        drawInternal(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, vecCenterMiddleCorner, slabType, camDif, red, green, blue, alpha);
                                case SOUTH, DOWN ->
                                        drawInternal(entry, vertexConsumer, vecEndCorner, vecStartCorner, vecCenterEndCorner, vecCenterStartCorner, vecCenterMiddleCorner, slabType, camDif, red, green, blue, alpha);
                            }
                        }
                        case DOUBLE ->
                                drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
                    }
                }
            }
        } else {
            drawDefaultLines(entry, vertexConsumer, vecStartCorner, vecEndCorner, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
        }
    }

    private static void drawInternalSquare(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vector3f vecCenterMiddleStart, Vector3f vecCenterMiddleEnd, SlabType slabType, Vec3d camDif, float red, float green, float blue, float alpha) {
        if (Objects.equals(slabType, SlabType.TOP)) {
            drawLine(entry, vertexConsumer, vecCenterMiddleStart, vecCenterStartCorner, camDif, red, green, blue, alpha);
            drawLine(entry, vertexConsumer, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
            drawLine(entry, vertexConsumer, vecCenterEndCorner, vecCenterMiddleEnd, camDif, red, green, blue, alpha);
        } else if (Objects.equals(slabType, SlabType.BOTTOM)) {
            drawLine(entry, vertexConsumer, vecStartCorner, vecCenterMiddleStart, camDif, red, green, blue, alpha);
            drawLine(entry, vertexConsumer, vecStartCorner, vecEndCorner, camDif, red, green, blue, alpha);
            drawLine(entry, vertexConsumer, vecEndCorner, vecCenterMiddleEnd, camDif, red, green, blue, alpha);
        }
    }

    private static void drawInternal(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vector3f vecCenterMiddleCorner, SlabType slabType, Vec3d camDif, float red, float green, float blue, float alpha) {
        if (Objects.equals(slabType, SlabType.BOTTOM)) {
            drawLine(entry, vertexConsumer, vecEndCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
            drawLine(entry, vertexConsumer, vecCenterEndCorner, vecCenterMiddleCorner, camDif, red, green, blue, alpha);
        } else if (Objects.equals(slabType, SlabType.TOP)) {
            drawLine(entry, vertexConsumer, vecStartCorner, vecCenterStartCorner, camDif, red, green, blue, alpha);
            drawLine(entry, vertexConsumer, vecCenterStartCorner, vecCenterMiddleCorner, camDif, red, green, blue, alpha);
        }
    }

    private static void drawDefaultLines(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vec3d camDif, float red, float green, float blue, float alpha) {
        drawLine(entry, vertexConsumer, vecStartCorner, vecCenterStartCorner, camDif, red, green, blue, alpha);
        drawLine(entry, vertexConsumer, vecEndCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
        drawLine(entry, vertexConsumer, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
    }

    private static void drawDefaultSquare(MatrixStack.Entry entry, VertexConsumer vertexConsumer, Vector3f vecStartCorner, Vector3f vecEndCorner, Vector3f vecCenterStartCorner, Vector3f vecCenterEndCorner, Vec3d camDif, float red, float green, float blue, float alpha) {
        drawLine(entry, vertexConsumer, vecStartCorner, vecEndCorner, camDif, red, green, blue, alpha);
        drawLine(entry, vertexConsumer, vecStartCorner, vecCenterStartCorner, camDif, red, green, blue, alpha);
        drawLine(entry, vertexConsumer, vecEndCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
        drawLine(entry, vertexConsumer, vecCenterStartCorner, vecCenterEndCorner, camDif, red, green, blue, alpha);
    }
}