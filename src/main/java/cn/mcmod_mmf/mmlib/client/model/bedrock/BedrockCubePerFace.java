package cn.mcmod_mmf.mmlib.client.model.bedrock;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;

import cn.mcmod_mmf.mmlib.client.model.pojo.FaceItem;
import cn.mcmod_mmf.mmlib.client.model.pojo.FaceUVsItem;
import cn.mcmod_mmf.mmlib.utils.MathUtil;
import net.minecraft.core.Direction;

public class BedrockCubePerFace implements BedrockCube {
    public final float minX;
    public final float minY;
    public final float minZ;
    public final float maxX;
    public final float maxY;
    public final float maxZ;
    
    private final List<BedrockPolygon> polygons;
    private final Vector3f[] vectors;

    public BedrockCubePerFace(float x, float y, float z, float width, float height, float depth, float delta, float texWidth, float texHeight, FaceUVsItem faces) {
        this.minX = x;
        this.minY = y;
        this.minZ = z;
        this.maxX = x + width;
        this.maxY = y + height;
        this.maxZ = z + depth;
        this.polygons = Lists.newArrayList();
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
        
        BedrockPolygon downQuad = getTexturedQuad(new BedrockVertex[]{vertex6, vertex5, vertex1, vertex2}, texWidth, texHeight, Direction.DOWN, faces);
        if(downQuad != null)
            this.polygons.add(downQuad); 
        BedrockPolygon upQuad = getTexturedQuad(new BedrockVertex[]{vertex3, vertex4, vertex8, vertex7}, texWidth, texHeight, Direction.UP, faces);
        if(upQuad != null)
            this.polygons.add(upQuad); 
        BedrockPolygon westQuad = getTexturedQuad(new BedrockVertex[]{vertex1, vertex5, vertex8, vertex4}, texWidth, texHeight, Direction.WEST, faces);
        if(westQuad != null)
            this.polygons.add(westQuad); 
        BedrockPolygon northQuad = getTexturedQuad(new BedrockVertex[]{vertex2, vertex1, vertex4, vertex3}, texWidth, texHeight, Direction.NORTH, faces);
        if(northQuad != null)
            this.polygons.add(northQuad); 
        BedrockPolygon eastQuad = getTexturedQuad(new BedrockVertex[]{vertex6, vertex2, vertex3, vertex7}, texWidth, texHeight, Direction.EAST, faces);
        if(eastQuad != null)
            this.polygons.add(eastQuad); 
        BedrockPolygon southQuad = getTexturedQuad(new BedrockVertex[]{vertex5, vertex6, vertex7, vertex8}, texWidth, texHeight, Direction.SOUTH, faces);
        if(southQuad != null)
            this.polygons.add(southQuad);     
    }

    private BedrockPolygon getTexturedQuad(BedrockVertex[] positionsIn, float texWidth, float texHeight, Direction direction, FaceUVsItem faces) {
        FaceItem face = faces.getFace(direction);
        if(MathUtil.compareFloat(face.getUvSize()[0], 0.0F) && MathUtil.compareFloat(face.getUvSize()[1], 0.0F))
            return null;
        float u1 = face.getUv()[0];
        float v1 = face.getUv()[1];
        float u2 = u1 + face.getUvSize()[0];
        float v2 = v1 + face.getUvSize()[1];
        return new BedrockPolygon(positionsIn, u1, v1, u2, v2, texWidth, texHeight, false, direction);
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
