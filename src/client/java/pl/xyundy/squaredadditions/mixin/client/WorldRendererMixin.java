package pl.xyundy.squaredadditions.mixin.client;

import pl.xyundy.squaredadditions.slabs.RenderUtil;
import pl.xyundy.squaredadditions.slabs.SlabLockEnum;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static pl.xyundy.squaredadditions.SquaredAdditionsClient.clientSlabLockPosition;
import static pl.xyundy.squaredadditions.config.SquaredAdditionsConfig.showEnhancedSlabLines;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @Shadow @Nullable private ClientWorld world;

    @Unique private static HitResult squaredadditions$captureCrosshairTarget;
    @Unique private static BlockState squaredadditions$captureBlockState;

    @Inject(method = "drawBlockOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;DDDLnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", at = @At("HEAD"))
    private void squaredadditions$drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, CallbackInfo ci) {
        squaredadditions$captureCrosshairTarget = this.client.crosshairTarget;
        squaredadditions$captureBlockState = state;
    }

    @Inject(method = "drawCuboidShapeOutline(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/shape/VoxelShape;DDDFFFF)V", at = @At("HEAD"))
    private static void squaredadditions$drawBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if (!showEnhancedSlabLines) return;
        if (clientSlabLockPosition.equals(SlabLockEnum.VANILLA_PLACEMENT)) return;
        Vec3d camDif = new Vec3d(offsetX, offsetY, offsetZ);
        if (squaredadditions$captureCrosshairTarget != null && squaredadditions$captureBlockState != null) {
            RenderUtil.renderOverlay(matrices, vertexConsumer, camDif, squaredadditions$captureBlockState, shape, squaredadditions$captureCrosshairTarget, red, green, blue, alpha);
        }
    }
}