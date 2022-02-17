package cn.mcmod_mmf.mmlib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.nio.file.Path;

public class DataGenUtil {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Path createPath(Path path, String namespace, String type, String name) {
        StringBuilder builder = new StringBuilder("data/").append(namespace).append('/').append(type).append('/')
                .append(type).append(".json");
        return path.resolve(builder.toString());
    }

}
