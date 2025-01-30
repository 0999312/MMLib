package cn.mcmod_mmf.mmlib.client.compat;

import java.util.Map;

import org.joml.Vector3f;

import com.github.argon4w.acceleratedrendering.core.buffers.builders.IVertexConsumerExtension;
import com.github.argon4w.acceleratedrendering.core.meshes.IMesh;
import com.github.argon4w.acceleratedrendering.core.meshes.MeshCollector;
import com.github.argon4w.acceleratedrendering.core.utils.TextureUtils;
import com.github.argon4w.acceleratedrendering.features.entities.AcceleratedEntityRenderingFeature;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import cn.mcmod_mmf.mmlib.Main;
import cn.mcmod_mmf.mmlib.client.model.bedrock.BedrockCube;
import cn.mcmod_mmf.mmlib.client.model.bedrock.BedrockPart;
import cn.mcmod_mmf.mmlib.client.model.bedrock.BedrockPolygon;
import cn.mcmod_mmf.mmlib.client.model.bedrock.BedrockVertex;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class AcceleratedRenderingBedrockPart {
	private final BedrockPart part;
	private final Map<RenderType, IMesh> meshes = new Object2ObjectOpenHashMap<>();
	public AcceleratedRenderingBedrockPart(BedrockPart part) {
		this.part = part;
	}
	
	public boolean compile(PoseStack.Pose pose, VertexConsumer consumer, int packedLight, int overlay, int color) {
		 IVertexConsumerExtension extension = (IVertexConsumerExtension) consumer;
		 	
	        if (!AcceleratedEntityRenderingFeature.isEnabled()) {
	            return false;
	        }

	        if (!AcceleratedEntityRenderingFeature.shouldUseAcceleratedPipeline()) {
	            return false;
	        }

//	        if (!extension.supportAcceleratedRendering()) {
//	            return false;
//	        }

	        extension.beginTransform(pose);

	        for (RenderType renderType : extension.getRenderTypes()) {
	            IMesh mesh = meshes.get(renderType);
	            
	            if (mesh != null) {
	                mesh.write(extension, color, packedLight, overlay);
	                continue;
	            }
	            
	            
	            IMesh.Builder builder = AcceleratedEntityRenderingFeature.getMeshBuilder();
	            MeshCollector meshCollector = builder.newMeshCollector(renderType);
	            NativeImage image = TextureUtils.downloadTexture(renderType);

	            for (BedrockCube cube : part.cubes) {

	                for (BedrockPolygon polygon : cube.polygons) {

	                    Vector3f normal = polygon.normal;

	                    if (shouldCull(polygon.vertices, image)) {
	                        continue;
	                    }

	                    for (BedrockVertex vertex : polygon.vertices) {

	                        meshCollector.addVertex(
	                        		vertex.pos.x,
	                        		vertex.pos.y,
	                        		vertex.pos.z,
	                                color,
	                                vertex.u,
	                                vertex.v,
	                                overlay,
	                                packedLight,
	                                normal.x,
	                                normal.y,
	                                normal.z
	                        );
	                    }
	                }
	            }

	            mesh = builder.build(meshCollector);
	            meshes.put(renderType, mesh);
	            mesh.write(extension, color, packedLight, overlay);

	            if (image != null) {
	                image.close();
	            }
	        }

	        extension.endTransform();
			return true;
	}
	
	 public static boolean shouldCull(BedrockVertex[] vertices, NativeImage image) {
	        if (image == null) {
	            return false;
	        }

	        float minU = 1.0f;
	        float minV = 1.0f;

	        float maxU = 0.0f;
	        float maxV = 0.0f;
	        
	        if (vertices.length == 4) {
	            Vector3f vertex0 = new Vector3f(vertices[0].pos);
	            Vector3f vector1 = new Vector3f(vertices[1].pos).sub(vertex0);
	            Vector3f vector2 = new Vector3f(vertices[2].pos).sub(vertex0);
	            Vector3f vector3 = new Vector3f(vertices[3].pos).sub(vertex0);

	            float length1 = vector1.cross(vector2).length();
	            float length2 = vector1.cross(vector3).length();

	            if (length1 == 0 && length2 == 0) {
	                return true;
	            }
	        }

	        for (BedrockVertex vertex : vertices) {
	            float u = vertex.u;
	            float v = vertex.v;

	            u = u < 0 ? 1.0f + u : u;
	            v = v < 0 ? 1.0f + v : v;

	            minU = Math.min(minU, u);
	            minV = Math.min(minV, v);
	            maxU = Math.max(maxU, u);
	            maxV = Math.max(maxV, v);
	        }

	        int minX = Math.max(0, Mth.floor(minU * image.getWidth()));
	        int minY = Math.max(0, Mth.floor(minV * image.getHeight()));

	        int maxX = Math.min(image.getWidth(), Mth.ceil(maxU * image.getWidth()));
	        int maxY = Math.min(image.getHeight(), Mth.ceil(maxV * image.getHeight()));

	        for (int x = minX; x < maxX; x++) {
	            for (int y = minY; y < maxY; y++) {
	                if ((image.getPixelRGBA(x, y) & 0xFF) != 0) {
	                    return false;
	                }
	            }
	        }

	        return true;
	    }
}
