package cn.mcmod_mmf.mmlib.utils;

import java.util.HashMap;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import cn.mcmod_mmf.mmlib.Main;
import cn.mcmod_mmf.mmlib.client.RenderUtils;
import cn.mcmod_mmf.mmlib.client.model.bedrock.BedrockVersion;
import cn.mcmod_mmf.mmlib.client.model.pojo.BedrockModelPOJO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

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
        if (MODEL_MAP.containsKey(modelLocation))
            return MODEL_MAP.get(modelLocation);
        return null;
    }

    public static void renderFluidStack(int x, int y, int width, int height, float depth, FluidStack fluidStack) {
        RenderSystem.enableBlend();
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(fluidStack.getFluid().getAttributes().getStillTexture());
        RenderUtils.setColorRGBA(fluidStack.getFluid().getAttributes().getColor(fluidStack));
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        float u1 = sprite.getU0();
        float v1 = sprite.getV0();
        do {
            int currentHeight = Math.min(sprite.getHeight(), height);
            height -= currentHeight;
            float v2 = sprite.getV((16 * currentHeight) / (float) sprite.getHeight());
            int x2 = x;
            int width2 = width;
            do {
                int currentWidth = Math.min(sprite.getWidth(), width2);
                width2 -= currentWidth;
                float u2 = sprite.getU((16 * currentWidth) / (float) sprite.getWidth());
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                bufferbuilder.vertex(x2, y, depth).uv(u1, v1).color(255, 255, 255, 255).endVertex();
                bufferbuilder.vertex(x2, y + currentHeight, depth).uv(u1, v2).color(255, 255, 255, 255).endVertex();
                bufferbuilder.vertex(x2 + currentWidth, y + currentHeight, depth).uv(u2, v2).color(255, 255, 255, 255)
                        .endVertex();
                bufferbuilder.vertex(x2 + currentWidth, y, depth).uv(u2, v1).color(255, 255, 255, 255).endVertex();
                tessellator.end();
                x2 += currentWidth;
            } while (width2 > 0);

            y += currentHeight;
        } while (height > 0);
        RenderSystem.disableBlend();
    }

    public static float convertRotation(float degree) {
        return (float) (degree * Math.PI / 180);
    }
}
