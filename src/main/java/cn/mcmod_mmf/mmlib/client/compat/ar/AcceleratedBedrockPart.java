package cn.mcmod_mmf.mmlib.client.compat.ar;

import java.util.Map;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.argon4w.acceleratedrendering.core.CoreFeature;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.IBufferGraph;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.builders.VertexConsumerExtension;
import com.github.argon4w.acceleratedrendering.core.buffers.accelerated.renderers.IAcceleratedRenderer;
import com.github.argon4w.acceleratedrendering.core.meshes.IMesh;
import com.github.argon4w.acceleratedrendering.core.meshes.collectors.CulledMeshCollector;
import com.github.argon4w.acceleratedrendering.features.entities.AcceleratedEntityRenderingFeature;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import cn.mcmod_mmf.mmlib.client.model.bedrock.BedrockPart;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.experimental.ExtensionMethod;
import net.minecraft.util.FastColor;

@ExtensionMethod(VertexConsumerExtension.class)
@Mixin(BedrockPart.class)
public class AcceleratedBedrockPart implements IAcceleratedRenderer<Void> {

	private final Map<IBufferGraph, IMesh> meshes = new Object2ObjectOpenHashMap<>();


	@Inject(method = "compile", at = @At("HEAD"), cancellable = true, remap = false)
	public void compile(PoseStack.Pose pPose, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float red,
			float green, float blue, float alpha, CallbackInfo ci) {
		var extension = pBuffer.getAccelerated();

		if (AcceleratedEntityRenderingFeature.isEnabled()
				&& AcceleratedEntityRenderingFeature.shouldUseAcceleratedPipeline()
				&& (CoreFeature.isRenderingLevel()
						|| (CoreFeature.isRenderingGui() && AcceleratedEntityRenderingFeature.shouldAccelerateInGui()))
				&& extension.isAccelerated()) {
			ci.cancel();
			extension.doRender(this, null, pPose.pose(), pPose.normal(), pPackedLight, pPackedOverlay,
					FastColor.ARGB32.color((int) (alpha * 255.0f), (int) (red * 255.0f), (int) (green * 255.0f),
							(int) (blue * 255.0f)));
		}
	}

	@Unique
	@Override
	public void render(VertexConsumer vertexConsumer, Void context, Matrix4f transform, Matrix3f normal, int light,
			int overlay, int color) {
		var extension = vertexConsumer.getAccelerated();
		var mesh = meshes.get(extension);

		extension.beginTransform(transform, normal);

		if (mesh != null) {
			mesh.write(extension, color, light, overlay);

			extension.endTransform();
			return;
		}

		var culledMeshCollector = new CulledMeshCollector(extension);
		var meshBuilder = extension.decorate(culledMeshCollector);
		var part = (BedrockPart) (Object) this;
		for (var cube : part.getCubes()) {
			for (var polygon : cube.getPolygons()) {
				var polygonNormal = polygon.normal;

				for (var vertex : polygon.vertices) {
					var vertexPosition = vertex.pos;

					meshBuilder.vertex(vertexPosition.x, vertexPosition.y, vertexPosition.z,
							1.0f, 1.0f, 1.0f, 1.0f, vertex.u, vertex.v, overlay, 0, polygonNormal.x, polygonNormal.y,
							polygonNormal.z);
				}
			}
		}

		culledMeshCollector.flush();

		mesh = AcceleratedEntityRenderingFeature.getMeshType().getBuilder().build(culledMeshCollector);

		meshes.put(extension, mesh);
		mesh.write(extension, color, light, overlay);

		extension.endTransform();
	}

}
