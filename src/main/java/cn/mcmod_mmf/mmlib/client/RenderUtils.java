package cn.mcmod_mmf.mmlib.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidStack;

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
