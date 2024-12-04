package cn.mcmod_mmf.mmlib.mixin.client.compat.sodium;

import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cn.mcmod_mmf.mmlib.client.render.sections.compat.impl.sodium.SodiumCompatImpl;
import cn.mcmod_mmf.mmlib.client.render.sections.dynamic.DynamicChunkBuffers;

/**
 * @author Argon4W
 */
@Pseudo
@Mixin(SodiumWorldRenderer.class)
public class SodiumWorldRendererMixin {
    @Shadow private RenderSectionManager renderSectionManager;

    @Inject(method = "drawChunkLayer", at = @At("HEAD"), cancellable = true)
    public void drawChunkLayer(RenderType renderLayer, ChunkRenderMatrices matrices, double x, double y, double z, CallbackInfo ci) {
        if (DynamicChunkBuffers.DYNAMIC_CUTOUT_LAYERS.containsValue(renderLayer)) {
            renderSectionManager.renderLayer(matrices, SodiumCompatImpl.DYNAMIC_CUTOUT_PASSES.get(renderLayer), x, y, z);
            ci.cancel();
            return;
        }

        if (DynamicChunkBuffers.DYNAMIC_TRANSLUCENT_LAYERS.containsValue(renderLayer)) {
            renderSectionManager.renderLayer(matrices, SodiumCompatImpl.DYNAMIC_TRANSLUCENT_PASSES.get(renderLayer), x, y, z);
            ci.cancel();
        }
    }
}
