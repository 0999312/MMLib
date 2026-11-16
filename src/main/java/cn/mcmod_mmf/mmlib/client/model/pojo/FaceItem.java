package cn.mcmod_mmf.mmlib.client.model.pojo;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Objects;

public class FaceItem {
    @SerializedName("uv")
    private float[] uv;

    @SerializedName("uv_size")
    private float[] uvSize;

    @SerializedName("uv_rotation")
    @JsonAdapter(Adapter.class)
    private Integer uvRotation;

    public float[] getUv() {
        return uv;
    }

    public float[] getUvSize() {
        return uvSize;
    }

    public int getUvRotation() { return uvRotation == null ? 0 : uvRotation; }

    public static FaceItem single16X() {
        FaceItem face = new FaceItem();
        face.uv = new float[]{0, 0};
        face.uvSize = new float[]{16, 16};
        return face;
    }

    public static FaceItem empty() {
        FaceItem face = new FaceItem();
        face.uv = new float[]{0, 0};
        face.uvSize = new float[]{0, 0};
        return face;
    }

    public static class Adapter extends TypeAdapter<Integer> {
        @Override
        public void write(JsonWriter out, Integer value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.value(value);
        }

        @Override
        public Integer read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            int val = in.nextInt();
            if (val != 0 && val != 90 && val != 180 && val != 270) {
                throw new JsonParseException("Invalid uv_rotation value: " + val);
            }
            return val;
        }
    }
}
