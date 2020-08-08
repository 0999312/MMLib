package cn.mcmod_mmf.mmlib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

import cn.mcmod_mmf.mmlib.Main;
import cn.mcmod_mmf.mmlib.client.model.ModelBaseJson;
import cn.mcmod_mmf.mmlib.client.model.ModelBipedJson;
import cn.mcmod_mmf.mmlib.client.model.ModelCustomArmorJson;
import cn.mcmod_mmf.mmlib.client.model.pojo.CustomModelPOJO;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public final class ClientUtils {
	private static final ClientUtils instance = new ClientUtils();

	private ClientUtils() {
	}

	public static ClientUtils getInstance() {
		return instance;
	}

	@SideOnly(Side.CLIENT)
	public void drawTexturedRect(float x, float y, float w, float h, double... uv) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos(x, y + h, 0).tex(uv[0], uv[3]).endVertex();
		worldrenderer.pos(x + w, y + h, 0).tex(uv[1], uv[3]).endVertex();
		worldrenderer.pos(x + w, y, 0).tex(uv[1], uv[2]).endVertex();
		worldrenderer.pos(x, y, 0).tex(uv[0], uv[2]).endVertex();
		tessellator.draw();
	}

	@SideOnly(Side.CLIENT)
	public void drawRepeatedFluidSprite(FluidStack fluid, float x, float y, float w, float h) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
				.getAtlasSprite(fluid.getFluid().getStill(fluid).toString());
		if (sprite != null) {
			int col = fluid.getFluid().getColor(fluid);
			GlStateManager.color((col >> 16 & 255) / 255.0f, (col >> 8 & 255) / 255.0f, (col & 255) / 255.0f, 1);
			int iW = sprite.getIconWidth();
			int iH = sprite.getIconHeight();
			if (iW > 0 && iH > 0)
				drawRepeatedSprite(x, y, w, h, iW, iH, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(),
						sprite.getMaxV());
		}
	}

	@SideOnly(Side.CLIENT)
	public void drawRepeatedSprite(float x, float y, float w, float h, int iconWidth, int iconHeight, float uMin,
			float uMax, float vMin, float vMax) {

		int iterMaxW = (int) (w / iconWidth);
		int iterMaxH = (int) (h / iconHeight);

		float leftoverW = w % iconWidth;
		float leftoverH = h % iconHeight;
		float leftoverWf = leftoverW / iconWidth;
		float leftoverHf = leftoverH / iconHeight;

		float iconUDif = uMax - uMin;
		float iconVDif = vMax - vMin;

		for (int ww = 0; ww < iterMaxW; ww++) {
			for (int hh = 0; hh < iterMaxH; hh++)
				drawTexturedRect(x + ww * iconWidth, y + hh * iconHeight, iconWidth, iconHeight, uMin, uMax, vMin,
						vMax);
			drawTexturedRect(x + ww * iconWidth, y + iterMaxH * iconHeight, iconWidth, leftoverH, uMin, uMax, vMin,
					(vMin + iconVDif * leftoverHf));
		}

		if (leftoverW > 0)
			for (int hh = 0; hh < iterMaxH; hh++)
				drawTexturedRect(x + iterMaxW * iconWidth, y + hh * iconHeight, leftoverW, iconHeight, uMin,
						(uMin + iconUDif * leftoverWf), vMin, vMax);
		drawTexturedRect(x + iterMaxW * iconWidth, y + iterMaxH * iconHeight, leftoverW, leftoverH, uMin,
				(uMin + iconUDif * leftoverWf), vMin, (vMin + iconVDif * leftoverHf));
	}

	public Map<ResourceLocation, CustomModelPOJO> pojo_cache = Maps.newHashMap();
	public Map<ResourceLocation, ModelBase> model_cache = Maps.newHashMap();

	@Nullable
	@SideOnly(Side.CLIENT)
	public CustomModelPOJO loadModel(ResourceLocation modelLocation) {
		InputStream input = null;
		if (pojo_cache.containsKey(modelLocation))
			return pojo_cache.get(modelLocation);
		try {
			input = Minecraft.getMinecraft().getResourceManager().getResource(modelLocation).getInputStream();
			CustomModelPOJO pojo = JSON_Creator.getInstance().GSON
					.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), CustomModelPOJO.class);

			// 先判断是不是 1.10.0 版本基岩版模型文件
			if (!pojo.getFormatVersion().equals("1.10.0")) {
				Main.getLogger().warn("{} model version is not 1.10.0", modelLocation);
				// TODO: 2019/7/26 添加对高版本基岩版模型的兼容
				return null;
			}

			// 如果 model 字段不为空
			if (pojo.getGeometryModel() != null) {
				pojo_cache.put(modelLocation, pojo);
				return pojo;
			}
			// 否则日志给出提示
			Main.getLogger().warn("{} model file don't have model field", modelLocation);
		} catch (IOException ioe) {
			// 可能用来判定错误，打印下
			Main.getLogger().warn("Failed to load model: {}", modelLocation);
			ioe.printStackTrace();
		} finally {
			// 关闭输入流
			IOUtils.closeQuietly(input);
		}

		// 如果前面出了错，返回 Null
		return null;
	}

	@SideOnly(Side.CLIENT)
	public ModelBase getModelBaseFromJSON(ResourceLocation modelLocation) {
		if (model_cache.containsKey(modelLocation))
			return model_cache.get(modelLocation);
		ModelBase model = new ModelBaseJson(loadModel(modelLocation));
		model_cache.put(modelLocation, model);
		return model;
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getModelBipedFromJSON(ResourceLocation modelLocation) {
		if (model_cache.containsKey(modelLocation) && model_cache.get(modelLocation) instanceof ModelBiped)
			return (ModelBiped) model_cache.get(modelLocation);
		ModelBiped model = new ModelBipedJson(loadModel(modelLocation));
		model_cache.put(modelLocation, model);
		return model;
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModelFromJSON(ResourceLocation modelLocation) {
		if (model_cache.containsKey(modelLocation) && model_cache.get(modelLocation) instanceof ModelBiped)
			return (ModelBiped) model_cache.get(modelLocation);
		ModelBiped model = new ModelCustomArmorJson(loadModel(modelLocation));
		model_cache.put(modelLocation, model);
		return model;
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getCustomArmorModel(EntityLivingBase entityLiving, ItemStack itemStack,
			EntityEquipmentSlot armorSlot, ModelBiped model, ModelBiped model1, ModelBiped model2) {
		if (model == null) {
			EntityEquipmentSlot type = ((ItemArmor) itemStack.getItem()).armorType;
			if ((type == EntityEquipmentSlot.CHEST) || (type == EntityEquipmentSlot.FEET)) {
				model = model1;
			} else {
				model = model2;
			}
		}
		if (model != null) {
			model.bipedHead.showModel = (armorSlot == EntityEquipmentSlot.HEAD);
			model.bipedHeadwear.showModel = (armorSlot == EntityEquipmentSlot.HEAD);
			model.bipedBody.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
			model.bipedRightArm.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
			model.bipedLeftArm.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
			model.bipedRightLeg.showModel = (armorSlot == EntityEquipmentSlot.LEGS
					|| armorSlot == EntityEquipmentSlot.FEET);
			model.bipedLeftLeg.showModel = (armorSlot == EntityEquipmentSlot.LEGS
					|| armorSlot == EntityEquipmentSlot.FEET);
			model.isSneak = entityLiving.isSneaking();

			model.isRiding = entityLiving.isRiding();
			model.isChild = entityLiving.isChild();
			ItemStack itemstack = entityLiving.getHeldItemMainhand();
			ItemStack itemstack1 = entityLiving.getHeldItemOffhand();
			ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
			ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;
			if ((itemstack != null) && (!itemstack.isEmpty())) {
				modelbiped$armpose = ModelBiped.ArmPose.ITEM;
				if (entityLiving.getItemInUseCount() > 0) {
					EnumAction enumaction = itemstack.getItemUseAction();
					if (enumaction == EnumAction.BLOCK) {
						modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
					} else if (enumaction == EnumAction.BOW) {
						modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
					}
				}
			}
			if ((itemstack1 != null) && (!itemstack1.isEmpty())) {
				modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;
				if (entityLiving.getItemInUseCount() > 0) {
					EnumAction enumaction1 = itemstack1.getItemUseAction();
					if (enumaction1 == EnumAction.BLOCK) {
						modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
					}
				}
			}
			if (entityLiving.getPrimaryHand() == EnumHandSide.RIGHT) {
				model.rightArmPose = modelbiped$armpose;
				model.leftArmPose = modelbiped$armpose1;
			} else {
				model.rightArmPose = modelbiped$armpose1;
				model.leftArmPose = modelbiped$armpose;
			}
		}
		return model;
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getCustomArmorModel(EntityLivingBase entityLiving, ItemStack itemStack,
			EntityEquipmentSlot armorSlot, ModelBiped model) {
		if (model != null) {
			model.bipedHead.showModel = (armorSlot == EntityEquipmentSlot.HEAD);
			model.bipedHeadwear.showModel = (armorSlot == EntityEquipmentSlot.HEAD);
			model.bipedBody.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
			model.bipedRightArm.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
			model.bipedLeftArm.showModel = (armorSlot == EntityEquipmentSlot.CHEST);
			model.bipedRightLeg.showModel = (armorSlot == EntityEquipmentSlot.LEGS
					|| armorSlot == EntityEquipmentSlot.FEET);
			model.bipedLeftLeg.showModel = (armorSlot == EntityEquipmentSlot.LEGS
					|| armorSlot == EntityEquipmentSlot.FEET);
			model.isSneak = entityLiving.isSneaking();

			model.isRiding = entityLiving.isRiding();
			model.isChild = entityLiving.isChild();
			ItemStack itemstack = entityLiving.getHeldItemMainhand();
			ItemStack itemstack1 = entityLiving.getHeldItemOffhand();
			ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
			ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;
			if ((itemstack != null) && (!itemstack.isEmpty())) {
				modelbiped$armpose = ModelBiped.ArmPose.ITEM;
				if (entityLiving.getItemInUseCount() > 0) {
					EnumAction enumaction = itemstack.getItemUseAction();
					if (enumaction == EnumAction.BLOCK) {
						modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
					} else if (enumaction == EnumAction.BOW) {
						modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
					}
				}
			}
			if ((itemstack1 != null) && (!itemstack1.isEmpty())) {
				modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;
				if (entityLiving.getItemInUseCount() > 0) {
					EnumAction enumaction1 = itemstack1.getItemUseAction();
					if (enumaction1 == EnumAction.BLOCK) {
						modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
					}
				}
			}
			if (entityLiving.getPrimaryHand() == EnumHandSide.RIGHT) {
				model.rightArmPose = modelbiped$armpose;
				model.leftArmPose = modelbiped$armpose1;
			} else {
				model.rightArmPose = modelbiped$armpose1;
				model.leftArmPose = modelbiped$armpose;
			}
		}
		return model;
	}
}
