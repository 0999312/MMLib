package cn.mcmod_mmf.mmlib.client;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderUtils {
    /**
     * Binds a texture for rendering
     * 
     * @param texture Texture
     */
    public static void bindTexture(ResourceLocation texture) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
    }

    /**
     * Sets up the shader for rendering
     * 
     * @param texture Texture
     * @param red     Red tint
     * @param green   Green tint
     * @param blue    Blue tint
     * @param alpha   Alpha tint
     */
    public static void setup(ResourceLocation texture, float red, float green, float blue, float alpha) {
        bindTexture(texture);
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    /**
     * Sets up the shader for rendering
     * 
     * @param texture Texture
     */
    public static void setup(ResourceLocation texture) {
        setup(texture, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void setColorRGBA(int color) {
        float a = alpha(color) / 255.0F;
        float r = red(color) / 255.0F;
        float g = green(color) / 255.0F;
        float b = blue(color) / 255.0F;
        RenderSystem.setShaderColor(r, g, b, a);
    }

    public static int alpha(int c) {
        return (c >> 24) & 0xFF;
    }

    public static int red(int c) {
        return (c >> 16) & 0xFF;
    }

    public static int green(int c) {
        return (c >> 8) & 0xFF;
    }

    public static int blue(int c) {
        return (c) & 0xFF;
    }
}
