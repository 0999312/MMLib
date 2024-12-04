package cn.mcmod_mmf.mmlib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cn.mcmod_mmf.mmlib.client.model.pojo.CubesItem;
import net.minecraft.resources.ResourceLocation;
import java.nio.file.Path;

public class DataGenUtil {
    public static final Gson DATA_GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(CubesItem.class, new CubesItem.Deserializer()).create();

    public static Path createPath(Path path, String namespace, String type, String name) {
        StringBuilder builder = new StringBuilder("data/").append(namespace).append('/').append(type).append('/')
                .append(type).append(".json");
        return path.resolve(builder.toString());
    }

}
