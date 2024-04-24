package pl.xyundy.squaredadditions.block.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MixedSlabBlockEntityRenderer implements BlockEntityRenderer<MixedSlabBlockEntity> {

    public MixedSlabBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(MixedSlabBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        BlockPos pos = entity.getPos();

        BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();


        RenderLayer layerTop = entity.topSlabState.isTransparent(world, pos) ? RenderLayer.getTranslucent() : RenderLayer.getSolid();
        RenderLayer layerBot = entity.bottomSlabState.isTransparent(world, pos) ? RenderLayer.getTranslucent() : RenderLayer.getSolid();

        VertexConsumer vertexConsumerTop = vertexConsumers.getBuffer(layerTop);
        VertexConsumer vertexConsumerBot = vertexConsumers.getBuffer(layerBot);

        manager.getModelRenderer().render(world, manager.getModel(entity.bottomSlabState), entity.bottomSlabState, pos, matrices, vertexConsumerBot, false, world.random, entity.bottomSlabState.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
        manager.getModelRenderer().render(world, manager.getModel(entity.topSlabState),entity.topSlabState, pos, matrices, vertexConsumerTop, false, world.random, entity.topSlabState.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
    }

    @Override
    public boolean rendersOutsideBoundingBox(MixedSlabBlockEntity blockEntity) {
        return true;
    }
}
