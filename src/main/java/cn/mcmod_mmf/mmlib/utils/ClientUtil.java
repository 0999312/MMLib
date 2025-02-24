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
import cn.mcmod_mmf.mmlib.client.model.bedrock.BedrockVersion;
import cn.mcmod_mmf.mmlib.client.model.pojo.BedrockModelPOJO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
public class ClientUtil {
	public static final HashMap<ResourceLocation, BedrockModelPOJO> MODEL_MAP = Maps.newHashMap();

	@OnlyIn(Dist.CLIENT)
	public static void loadModel(ResourceLocation modelLocation, JsonElement element) {
		BedrockModelPOJO pojo = DataGenUtil.DATA_GSON.fromJson(element, BedrockModelPOJO.class);

		if (pojo.getFormatVersion() == null) {
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

	public static void renderFluidStack(int x, int y, int width, int height, float depth, FluidStack fluid) {
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture());
		int color = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor();

		float alpha = (float) (color >> 24 & 255) / 255.0F;
		float red = (float) (color >> 16 & 0xFF) / 255.0F;
		float green = (float) (color >> 8 & 0xFF) / 255.0F;
		float blue = (float) (color & 0xFF) / 255.0F;

		int xTileCount = width / 16;
		int xRemainder = width - (xTileCount * 16);
		int yTileCount = height / 16;
		int yRemainder = height - (yTileCount * 16);
		float uMin = sprite.getU0();
		float uMax = sprite.getU1();
		float vMin = sprite.getV0();
		float vMax = sprite.getV1();
		float uDif = uMax - uMin;
		float vDif = vMax - vMin;
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		Tesselator tessellator = Tesselator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		for (int xTile = 0; xTile <= xTileCount; xTile++) {
			int resultWidth = (xTile == xTileCount) ? xRemainder : 16;
			if (resultWidth == 0) {
				break;
			}
			int x1 = x + (xTile * 16);
			int maskRight = 16 - resultWidth;
			int shiftedX = x1 + 16 - maskRight;
			float uLocalDif = uDif * maskRight / 16;

			for (int yTile = 0; yTile <= yTileCount; yTile++) {
				int resultHeight = (yTile == yTileCount) ? yRemainder : 16;
				if (resultHeight == 0) {
					break;
				}
				int y1 = y - ((yTile + 1) * 16);
				int maskTop = 16 - resultHeight;
				float vLocalDif = vDif * maskTop / 16;
				bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
				bufferbuilder.vertex( x1, y1 + 16, 0).uv(uMin + uLocalDif, vMax).color(red, green, blue, alpha);
				bufferbuilder.vertex( shiftedX, y1 + 16, 0).uv(uMax, vMax).color(red, green, blue, alpha);
				bufferbuilder.vertex( shiftedX, y1 + maskTop, 0).uv(uMax, vMin + vLocalDif).color(red, green, blue, alpha);
				bufferbuilder.vertex( x1, y1 + maskTop, 0).uv(uMin + uLocalDif, vMin + vLocalDif).color(red, green, blue, alpha);
				tessellator.end();
			}
		}
		bufferbuilder.unsetDefaultColor();
		RenderSystem.disableBlend();
	}
}
