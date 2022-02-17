package cn.mcmod_mmf.mmlib.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import cn.mcmod_mmf.mmlib.Main;
import cn.mcmod_mmf.mmlib.client.model.pojo.BedrockModelPOJO;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientUtil {
    public static final HashMap<ResourceLocation, BedrockModelPOJO> MODEL_MAP = Maps.newHashMap();

    private static ResourceManager manager = Minecraft.getInstance().getResourceManager();

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public static void loadModel(ResourceLocation modelLocation) {
        try (InputStream input = manager.getResource(modelLocation).getInputStream();) {
            BedrockModelPOJO pojo = DataGenUtil.GSON.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8),
                    BedrockModelPOJO.class);
            // 先判断是不是 1.10.0 版本基岩版模型文件
            if (pojo.getFormatVersion().equals("1.10.0")) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelLegacy() != null) {
                    MODEL_MAP.put(modelLocation, pojo);
                    return;
                } else {
                    // 否则日志给出提示
                    Main.getLogger().warn("{} model file don't have model field", modelLocation);
                    return;
                }
            }

            // 判定是不是 1.12.0 版本基岩版模型文件
            if (pojo.getFormatVersion().equals("1.12.0")) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelNew() != null) {
                    MODEL_MAP.put(modelLocation, pojo);
                    return;
                } else {
                    // 否则日志给出提示
                    Main.getLogger().warn("{} model file don't have model field", modelLocation);
                    return;
                }
            }

            Main.getLogger().error("{} model version is not 1.10.0 or 1.12.0", modelLocation);

        } catch (IOException ioe) {
            // 可能用来判定错误，打印下
            Main.getLogger().error("Failed to load model: {}", modelLocation);
            ioe.printStackTrace();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static BedrockModelPOJO getModelPOJO(ResourceLocation modelLocation) {
        if (MODEL_MAP.containsKey(modelLocation))
            return MODEL_MAP.get(modelLocation);
        return null;
    }

}
