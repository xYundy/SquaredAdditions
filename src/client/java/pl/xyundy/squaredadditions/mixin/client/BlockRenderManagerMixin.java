package pl.xyundy.squaredadditions.mixin.client;

import pl.xyundy.squaredadditions.slabs.PlacementUtil;
import pl.xyundy.squaredadditions.slabs.VerticalType;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static pl.xyundy.squaredadditions.slabs.Util.VERTICAL_TYPE;
import static net.minecraft.block.SlabBlock.TYPE;

// Massive thanks to andrew6rant for a lot of code behind this mixin
// https://github.com/Andrew6rant/Auto-Slabs/blob/1.20.x/src/main/java/io/github/andrew6rant/autoslabs/mixin/BlockRenderManagerMixin.java
@Mixin(BlockRenderManager.class)
public class BlockRenderManagerMixin {

    @Shadow @Final private BlockModels models;

    @Shadow @Final private BlockModelRenderer blockModelRenderer;

    @Shadow @Final private Random random;

    @Inject(method = "renderDamage", at = @At("HEAD"), cancellable = true)
    public void squaredadditions$renderSlabDamage(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer, CallbackInfo ci) {
        if(!(state.getBlock() instanceof SlabBlock)) return;
        if (state.getRenderType() == BlockRenderType.MODEL) {
            if (state.get(TYPE) != SlabType.DOUBLE) return;
            VerticalType verticalType = state.get(VERTICAL_TYPE);
            if (verticalType == null) return;
            ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
            if (clientPlayer == null) return;
            if (clientPlayer.isSneaking()) return;

            BlockHitResult cast = PlacementUtil.calcRaycast(clientPlayer);
            BakedModel bakedModel = this.models.getModel(PlacementUtil.getModelState(state, verticalType, cast.getSide(), cast));
            long l = state.getRenderingSeed(pos);
            this.blockModelRenderer.render(world, bakedModel, state, pos, matrices, vertexConsumer, true, this.random, l, OverlayTexture.DEFAULT_UV);
            ci.cancel();
        }
    }
}