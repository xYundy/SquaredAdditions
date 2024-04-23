package pl.xyundy.squaredadditions.mixin.client;

import pl.xyundy.squaredadditions.slabs.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static pl.xyundy.squaredadditions.SquaredAdditionsClient.clientSlabLockPosition;

// Massive thanks to andrew6rant for a lot of code behind this mixin
// https://github.com/Andrew6rant/Auto-Slabs/blob/1.20.x/src/main/java/io/github/andrew6rant/autoslabs/mixin/InGameHudMixin.java
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(
            method = "renderCrosshair(Lnet/minecraft/client/gui/DrawContext;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void squaredadditions$renderSlabCrosshairIcons(DrawContext context, CallbackInfo ci) {
        switch (clientSlabLockPosition) {
            case BOTTOM_SLAB -> RenderUtil.drawSlabIcon(this.client.player, context, 0, 0);
            case TOP_SLAB -> RenderUtil.drawSlabIcon(this.client.player, context, 16, 0);
            case NORTH_SLAB_VERTICAL -> RenderUtil.drawSlabIcon(this.client.player, context, 0, 16);
            case SOUTH_SLAB_VERTICAL -> RenderUtil.drawSlabIcon(this.client.player, context, 16, 16);
            case EAST_SLAB_VERTICAL -> RenderUtil.drawSlabIcon(this.client.player, context, 0, 32);
            case WEST_SLAB_VERTICAL -> RenderUtil.drawSlabIcon(this.client.player, context, 16, 32);
        }
    }
}