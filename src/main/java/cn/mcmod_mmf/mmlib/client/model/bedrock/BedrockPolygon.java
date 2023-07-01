package cn.mcmod_mmf.mmlib.client.model.bedrock;

import org.joml.Vector3f;

import net.minecraft.core.Direction;

public class BedrockPolygon {
    public final BedrockVertex[] vertices;
    public final Vector3f normal;

    public BedrockPolygon(BedrockVertex[] vertices, float u1, float v1, float u2, float v2, float texWidth, float texHeight, boolean mirror, Direction direction) {
        this.vertices = vertices;
        vertices[0] = vertices[0].remap(u2 / texWidth, v1 / texHeight);
        vertices[1] = vertices[1].remap(u1 / texWidth, v1 / texHeight);
        vertices[2] = vertices[2].remap(u1 / texWidth, v2 / texHeight);
        vertices[3] = vertices[3].remap(u2 / texWidth, v2 / texHeight);
        if (mirror) {
            int i = vertices.length;
            for (int j = 0; j < i / 2; ++j) {
                BedrockVertex bedrockVertex = vertices[j];
                vertices[j] = vertices[i - 1 - j];
                vertices[i - 1 - j] = bedrockVertex;
            }
        }
        this.normal = direction.step();
        if (mirror) {
            this.normal.mul(-1.0F, 1.0F, 1.0F);
        }
    }
    
    public Vector3f getNormalCopy() {
        return new Vector3f(normal);
    }
}
