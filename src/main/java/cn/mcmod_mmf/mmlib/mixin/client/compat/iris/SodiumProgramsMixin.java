package cn.mcmod_mmf.mmlib.mixin.client.compat.iris;

import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.irisshaders.iris.pipeline.programs.SodiumPrograms;
import net.irisshaders.iris.shadows.ShadowRenderingState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cn.mcmod_mmf.mmlib.client.render.sections.compat.impl.sodium.SodiumCompatImpl;

/**
 * @author Argon4W
 */
@Pseudo
@Mixin(SodiumPrograms.class)
public class SodiumProgramsMixin {
    @Inject(method = "mapTerrainRenderPass", at = @At("HEAD"), cancellable = true)
    public void mapTerrainRenderType(TerrainRenderPass pass, CallbackInfoReturnable<SodiumPrograms.Pass> cir) {
        if (SodiumCompatImpl.DYNAMIC_CUTOUT_PASSES.containsValue(pass)) {
            cir.setReturnValue(ShadowRenderingState.areShadowsCurrentlyBeingRendered() ? SodiumPrograms.Pass.SHADOW_CUTOUT : SodiumPrograms.Pass.TERRAIN_CUTOUT);
        }

        if (SodiumCompatImpl.DYNAMIC_TRANSLUCENT_PASSES.containsValue(pass)) {
            cir.setReturnValue(ShadowRenderingState.areShadowsCurrentlyBeingRendered() ? SodiumPrograms.Pass.SHADOW_TRANS : SodiumPrograms.Pass.TRANSLUCENT);
        }
    }
}
