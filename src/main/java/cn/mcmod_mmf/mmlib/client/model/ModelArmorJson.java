package cn.mcmod_mmf.mmlib.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import cn.mcmod_mmf.mmlib.client.model.pojo.CustomModelPOJO;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ModelArmorJson extends ModelBipedJson {
	protected final EquipmentSlotType slot;
	public ModelRenderer bootRight;
	public ModelRenderer bootLeft;
	public ModelArmorJson(CustomModelPOJO pojo,EquipmentSlotType slot) {
		super(pojo);
		this.slot = slot;
		this.bootRight = new ModelRenderer(this);
		this.bootLeft = new ModelRenderer(this);
		
		if(modelMap.containsKey("bootRight")){
			modelMap.get("bootRight").setPos(1.9F, 12.0F, 0.0F);
        	this.bootRight.addChild(modelMap.get("bootRight"));
        }
    	this.rightLeg.addChild(bootRight);
		if(modelMap.containsKey("bootLeft")){
			modelMap.get("bootLeft").setPos(-1.9F, 12.0F, 0.0F);
        	this.bootLeft.addChild(modelMap.get("bootLeft"));
        }
    	this.leftLeg.addChild(bootLeft);
	}
	@Override
	public void renderToBuffer(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, float r, float g, float b, float a) {
		
		ModelRenderer helm = modelMap.containsKey("head")? modelMap.get("head") : new ModelRenderer(this);
		ModelRenderer bodyArmor = modelMap.containsKey("body")? modelMap.get("body") : new ModelRenderer(this);
		ModelRenderer armRight = modelMap.containsKey("armRight")? modelMap.get("armRight") : new ModelRenderer(this);
		ModelRenderer armLeft = modelMap.containsKey("armLeft")? modelMap.get("armLeft") : new ModelRenderer(this);
		ModelRenderer legRight = modelMap.containsKey("legRight")? modelMap.get("legRight") : new ModelRenderer(this);
		ModelRenderer legLeft =  modelMap.containsKey("legLeft")? modelMap.get("legLeft") : new ModelRenderer(this);
		
		helm.visible = slot == EquipmentSlotType.HEAD;
		bodyArmor.visible = slot == EquipmentSlotType.CHEST;
		armRight.visible = slot == EquipmentSlotType.CHEST;
		armLeft.visible = slot == EquipmentSlotType.CHEST;
		legRight.visible = slot == EquipmentSlotType.LEGS;
		legLeft.visible = slot == EquipmentSlotType.LEGS;
		bootRight.visible = slot == EquipmentSlotType.FEET;
		bootLeft.visible = slot == EquipmentSlotType.FEET;
		this.hat.visible = false;

		
		super.renderToBuffer(ms, buffer, light, overlay, r, g, b, a);
	}
	
	@Override
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch) {
		if (!(entity instanceof ArmorStandEntity)) {
			super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			return;
		}

		ArmorStandEntity entityIn = (ArmorStandEntity) entity;
		this.head.xRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getX();
		this.head.yRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getY();
		this.head.zRot = ((float) Math.PI / 180F) * entityIn.getHeadPose().getZ();
		this.head.setPos(0.0F, 1.0F, 0.0F);
		this.body.xRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getX();
		this.body.yRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getY();
		this.body.zRot = ((float) Math.PI / 180F) * entityIn.getBodyPose().getZ();
		this.leftArm.xRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getX();
		this.leftArm.yRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getY();
		this.leftArm.zRot = ((float) Math.PI / 180F) * entityIn.getLeftArmPose().getZ();
		this.rightArm.xRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getX();
		this.rightArm.yRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getY();
		this.rightArm.zRot = ((float) Math.PI / 180F) * entityIn.getRightArmPose().getZ();
		this.leftLeg.xRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getX();
		this.leftLeg.yRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getY();
		this.leftLeg.zRot = ((float) Math.PI / 180F) * entityIn.getLeftLegPose().getZ();
		this.leftLeg.setPos(2F, 11.0F, 0.0F);
		this.rightLeg.xRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getX();
		this.rightLeg.yRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getY();
		this.rightLeg.zRot = ((float) Math.PI / 180F) * entityIn.getRightLegPose().getZ();
		this.rightLeg.setPos(-2F, 11.0F, 0.0F);
		this.hat.copyFrom(this.head);
	}
}
