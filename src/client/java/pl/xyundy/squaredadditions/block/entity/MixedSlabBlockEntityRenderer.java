package pl.xyundy.squaredadditions.block.entity;

import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.random.Random;

public class MixedSlabBlockEntityRenderer implements BlockEntityRenderer<MixedSlabBlockEntity> {

    public MixedSlabBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(MixedSlabBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BakedModelManager manager = MinecraftClient.getInstance().getBakedModelManager();
        BlockModels blockModels = manager.getBlockModels();
        BakedModel bottomSlabModel = blockModels.getModel(entity.bottomSlabState);
        BakedModel topSlabModel = blockModels.getModel(entity.topSlabState);


        if (null != topSlabModel && null != bottomSlabModel) {
            int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
            VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(entity.getCachedState(), false));
            matrices.push();
            for (int i = 0; i <= 5; i++) { // render every model face except the inner ones
                for (BakedQuad quad : bottomSlabModel.getQuads(null, ModelHelper.faceFromIndex(i), Random.create())) {
                    consumer.quad(matrices.peek(), quad, 1f, 1f, 1f, lightAbove, overlay);
                }
                for (BakedQuad quad : topSlabModel.getQuads(null, ModelHelper.faceFromIndex(i), Random.create())) {
                    consumer.quad(matrices.peek(), quad, 1f, 1f, 1f, lightAbove, overlay);
                }
            }
            matrices.pop();
        }
    }
}
