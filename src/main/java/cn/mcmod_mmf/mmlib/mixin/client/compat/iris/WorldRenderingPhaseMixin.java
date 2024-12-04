package cn.mcmod_mmf.mmlib.mixin.client.compat.iris;

import net.irisshaders.iris.pipeline.WorldRenderingPhase;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cn.mcmod_mmf.mmlib.client.render.sections.dynamic.DynamicChunkBuffers;

/**
 * @author Argon4W
 */
@Pseudo
@Mixin(WorldRenderingPhase.class)
public class WorldRenderingPhaseMixin {
    @Inject(method = "fromTerrainRenderType", at = @At("HEAD"), cancellable = true)
    private static void fromTerrainRenderType(RenderType renderType, CallbackInfoReturnable<WorldRenderingPhase> cir) {
        if (DynamicChunkBuffers.DYNAMIC_CUTOUT_LAYERS.containsValue(renderType)) {
            cir.setReturnValue(WorldRenderingPhase.TERRAIN_CUTOUT_MIPPED);
            return;
        }

        if (DynamicChunkBuffers.DYNAMIC_TRANSLUCENT_LAYERS.containsValue(renderType)) {
            cir.setReturnValue(WorldRenderingPhase.TERRAIN_TRANSLUCENT);
        }
    }
}
