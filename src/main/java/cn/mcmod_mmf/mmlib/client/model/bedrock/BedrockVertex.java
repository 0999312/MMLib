package cn.mcmod_mmf.mmlib.client.model.bedrock;

public class BedrockVertex {
    public final int posIndex;
    public final float u;
    public final float v;

//    public BedrockVertex(float x, float y, float z, float u, float v) {
//        this(new Vector3f(x, y, z), u, v);
//    }

    public BedrockVertex(int index, float u, float v) {
        this.posIndex = index;
        this.u = u;
        this.v = v;
    }

    public BedrockVertex remap(float u, float v) {
        return new BedrockVertex(this.posIndex, u, v);
    }
}
