package cn.mcmod_mmf.mmlib.client.model.bedrock;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;

import net.minecraft.core.Direction;

public class BedrockCubeBox implements BedrockCube {
    public final float minX;
    public final float minY;
    public final float minZ;
    public final float maxX;
    public final float maxY;
    public final float maxZ;
    
    private final BedrockPolygon[] polygons;
    private final Vector3f[] vectors;

    public BedrockCubeBox(float texOffX, float texOffY, float x, float y, float z, float width, float height, float depth, float delta, boolean mirror, float texWidth, float texHeight) {
        this.minX = x;
        this.minY = y;
        this.minZ = z;
        this.maxX = x + width;
        this.maxY = y + height;
        this.maxZ = z + depth;
        this.polygons = new BedrockPolygon[6];
        this.vectors = new Vector3f[8];
        
        float xEnd = x + width;
        float yEnd = y + height;
        float zEnd = z + depth;
        x = x - delta;
        y = y - delta;
        z = z - delta;
        xEnd = xEnd + delta;
        yEnd = yEnd + delta;
        zEnd = zEnd + delta;

        if (mirror) {
            float tmp = xEnd;
            xEnd = x;
            x = tmp;
        }
        
        x /= 16.0F;
        y /= 16.0F;
        z /= 16.0F;
        xEnd /= 16.0F;
        yEnd /= 16.0F;
        zEnd /= 16.0F;

        this.vectors[0] = new Vector3f(x, y, z);
        this.vectors[1] = new Vector3f(xEnd, y, z);
        this.vectors[2] = new Vector3f(xEnd, yEnd, z);
        this.vectors[3] = new Vector3f(x, yEnd, z);
        this.vectors[4] = new Vector3f(x, y, zEnd);
        this.vectors[5] = new Vector3f(xEnd, y, zEnd);
        this.vectors[6] = new Vector3f(xEnd, yEnd, zEnd);
        this.vectors[7] = new Vector3f(x, yEnd, zEnd);

        BedrockVertex vertex1 = new BedrockVertex(0, 0.0F, 0.0F);
        BedrockVertex vertex2 = new BedrockVertex(1, 0.0F, 8.0F);
        BedrockVertex vertex3 = new BedrockVertex(2, 8.0F, 8.0F);
        BedrockVertex vertex4 = new BedrockVertex(3, 8.0F, 0.0F);
        BedrockVertex vertex5 = new BedrockVertex(4, 0.0F, 0.0F);
        BedrockVertex vertex6 = new BedrockVertex(5, 0.0F, 8.0F);
        BedrockVertex vertex7 = new BedrockVertex(6, 8.0F, 8.0F);
        BedrockVertex vertex8 = new BedrockVertex(7, 8.0F, 0.0F);

        float p1 = texOffX + depth;
        float p2 = texOffX + depth + width;
        float p3 = texOffX + depth + width + width;
        float p4 = texOffX + depth + width + depth;
        float p5 = texOffX + depth + width + depth + width;
        float p6 = texOffY + depth;
        float p7 = texOffY + depth + height;
        float p8 = texOffY;
        float p9 = texOffX;

        this.polygons[2] = new BedrockPolygon(new BedrockVertex[]{vertex6, vertex5, vertex1, vertex2}, p1, p8, p2, p6, texWidth, texHeight, mirror, Direction.DOWN);
        this.polygons[3] = new BedrockPolygon(new BedrockVertex[]{vertex3, vertex4, vertex8, vertex7}, p2, p6, p3, p8, texWidth, texHeight, mirror, Direction.UP);
        this.polygons[1] = new BedrockPolygon(new BedrockVertex[]{vertex1, vertex5, vertex8, vertex4}, p9, p6, p1, p7, texWidth, texHeight, mirror, Direction.WEST);
        this.polygons[4] = new BedrockPolygon(new BedrockVertex[]{vertex2, vertex1, vertex4, vertex3}, p1, p6, p2, p7, texWidth, texHeight, mirror, Direction.NORTH);
        this.polygons[0] = new BedrockPolygon(new BedrockVertex[]{vertex6, vertex2, vertex3, vertex7}, p2, p6, p4, p7, texWidth, texHeight, mirror, Direction.EAST);
        this.polygons[5] = new BedrockPolygon(new BedrockVertex[]{vertex5, vertex6, vertex7, vertex8}, p4, p6, p5, p7, texWidth, texHeight, mirror, Direction.SOUTH);
    }

    @Override
    public void compile(PoseStack.Pose pose, VertexConsumer consumer, int texU, int texV, float red, float green, float blue, float alpha) {
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();

        for (BedrockPolygon polygon : this.polygons) {
            Vector3f vector3f = polygon.normal.copy();
            vector3f.transform(matrix3f);
            float nx = vector3f.x();
            float ny = vector3f.y();
            float nz = vector3f.z();

            for (BedrockVertex vertex : polygon.vertices) {
                Vector4f vector4f = new Vector4f(this.vectors[vertex.posIndex]);
                vector4f.transform(matrix4f);
                consumer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), red, green, blue, alpha, vertex.u, vertex.v, texV, texU, nx, ny, nz);
            }
        }
    }
}
