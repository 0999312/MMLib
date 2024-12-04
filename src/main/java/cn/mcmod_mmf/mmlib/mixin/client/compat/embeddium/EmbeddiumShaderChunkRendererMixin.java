package cn.mcmod_mmf.mmlib.mixin.client.compat.embeddium;

import org.embeddedt.embeddium.impl.gl.shader.GlProgram;
import org.embeddedt.embeddium.impl.render.chunk.ShaderChunkRenderer;
import org.embeddedt.embeddium.impl.render.chunk.shader.ChunkShaderInterface;
import org.embeddedt.embeddium.impl.render.chunk.terrain.TerrainRenderPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cn.mcmod_mmf.mmlib.client.render.sections.compat.impl.embeddium.EmbeddiumShaderExtension;

/**
 * @author Argon4W
 */
@Pseudo
@Mixin(ShaderChunkRenderer.class)
public class EmbeddiumShaderChunkRendererMixin {
    @Shadow protected GlProgram<ChunkShaderInterface> activeProgram;

    @Inject(method = "begin", at = @At(value = "INVOKE", target = "Lorg/embeddedt/embeddium/impl/render/chunk/shader/ChunkShaderInterface;setupState()V"))
    public void begin(TerrainRenderPass pass, CallbackInfo ci) {
        if (activeProgram.getInterface() instanceof EmbeddiumShaderExtension extension) {
            extension.eyelib$setTerrainRenderPass(pass);
        }
    }
}
