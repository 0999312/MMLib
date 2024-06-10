package cn.mcmod_mmf.mmlib.utils;

import java.util.HashMap;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import cn.mcmod_mmf.mmlib.Main;
import cn.mcmod_mmf.mmlib.client.model.bedrock.BedrockVersion;
import cn.mcmod_mmf.mmlib.client.model.pojo.BedrockModelPOJO;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientUtil {
    public static final HashMap<ResourceLocation, BedrockModelPOJO> MODEL_MAP = Maps.newHashMap();

    @OnlyIn(Dist.CLIENT)
    public static void loadModel(ResourceLocation modelLocation, JsonElement element) {
        BedrockModelPOJO pojo = DataGenUtil.DATA_GSON.fromJson(element, BedrockModelPOJO.class);

        if(pojo.getFormatVersion() == null) {
            Main.getLogger().error("Failed to load model: {}, it's not a Bedrock Model!", modelLocation);
            return;
        } else {
            // 先判断是不是 1.10.0 版本基岩版模型文件
            if (pojo.getFormatVersion().equals(BedrockVersion.LEGACY.getVersion())) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelLegacy() != null) {
                    Main.getLogger().info("Loaded 1.10.0 version model : {}", modelLocation);
                    MODEL_MAP.put(modelLocation, pojo);
                    return;
                } else {
                    // 否则日志给出提示
                    Main.getLogger().warn("{} model file don't have model field", modelLocation);
                    return;
                }
            }

            // 判定是不是 1.12.0 版本基岩版模型文件
            if (pojo.getFormatVersion().compareTo(BedrockVersion.NEW.getVersion()) >= 0) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelNew() != null) {
                    MODEL_MAP.put(modelLocation, pojo);
                    Main.getLogger().info("Loaded {} version model : {}", pojo.getFormatVersion(), modelLocation);
                    return;
                } else {
                    // 否则日志给出提示
                    Main.getLogger().warn("{} model file don't have model field", modelLocation);
                    return;
                }
            }

            Main.getLogger().error("{} model version is not 1.10.0 or new version bedrock model", modelLocation);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static BedrockModelPOJO getModelPOJO(ResourceLocation modelLocation) {
        return MODEL_MAP.get(modelLocation);
    }

    public static float convertRotation(float degree) {
        return (float) (degree * Math.PI / 180);
    }
}
