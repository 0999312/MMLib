package cn.mcmod_mmf.mmlib.client.model;

import java.util.Map;

import com.google.gson.JsonElement;

import cn.mcmod_mmf.mmlib.Main;
import cn.mcmod_mmf.mmlib.utils.ClientUtil;
import cn.mcmod_mmf.mmlib.utils.DataGenUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

public class BedrockModelResourceLoader extends SimpleJsonResourceReloadListener {
    private final String resource_path;
    public BedrockModelResourceLoader(String path) {
        super(DataGenUtil.DATA_GSON, path);
        this.resource_path = path;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
        ClientUtil.MODEL_MAP.clear();
        Main.getLogger().info("Started Loading Bedrock Model from : {}", resource_path);
        if(map.isEmpty())
            Main.getLogger().error("{} is an empty folder!", resource_path);
        for(var entry : map.entrySet()) {
            Main.getLogger().info("Loading Bedrock Model Loading : {}", entry.getKey().toString());
            ClientUtil.loadModel(entry.getKey(), entry.getValue());
        }
    }
}