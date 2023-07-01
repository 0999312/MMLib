package cn.mcmod_mmf.mmlib.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;

import cn.mcmod_mmf.mmlib.client.model.bedrock.BedrockPart;
import cn.mcmod_mmf.mmlib.client.model.pojo.BedrockModelPOJO;
import cn.mcmod_mmf.mmlib.utils.BedrockAnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class BedrockHumanoidModel<T extends LivingEntity> extends BedrockEntityModel<T> {

    public static final float OVERLAY_SCALE = 0.25F;
    public static final float HAT_OVERLAY_SCALE = 0.5F;
    public BedrockPart head;
    public BedrockPart body;
    public BedrockPart rightArm;
    public BedrockPart leftArm;
    public BedrockPart rightLeg;
    public BedrockPart leftLeg;
    public HumanoidModel.ArmPose leftArmPose;
    public HumanoidModel.ArmPose rightArmPose;
    public boolean crouching;
    public float swimAmount;

    public BedrockHumanoidModel() {
        super();
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
    }
    
    public BedrockHumanoidModel(BedrockModelPOJO pojo) {
        super(pojo);
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
    }
    
    @Override
    public void loadModel(BedrockModelPOJO pojo) {
        super.loadModel(pojo);
        this.head = this.getChild("head");
        this.body = this.getChild("body");
        this.rightArm = this.getChild("right_arm");
        this.leftArm = this.getChild("left_arm");
        this.rightLeg = this.getChild("right_leg");
        this.leftLeg = this.getChild("left_leg");
    }

    protected Iterable<BedrockPart> headParts() {
        return ImmutableList.of(this.head);
    }

    protected Iterable<BedrockPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg);
    }

    public void prepareMobModel(T entity, float p_102862_, float p_102863_, float p_102864_) {
        this.swimAmount = entity.getSwimAmount(p_102864_);
        super.prepareMobModel(entity, p_102862_, p_102863_, p_102864_);
    }

    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch) {
        boolean flag = entity.getFallFlyingTicks() > 4;
        boolean flag1 = entity.isVisuallySwimming();
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        if (flag) {
            this.head.xRot = (-(float) Math.PI / 4F);
        } else if (this.swimAmount > 0.0F) {
            if (flag1) {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, (-(float) Math.PI / 4F));
            } else {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, headPitch * ((float) Math.PI / 180F));
            }
        } else {
            this.head.xRot = headPitch * ((float) Math.PI / 180F);
        }

        this.body.yRot = 0.0F;
        this.rightArm.z = 0.0F;
        this.rightArm.x = -5.0F;
        this.leftArm.z = 0.0F;
        this.leftArm.x = 5.0F;
        float f = 1.0F;
        if (flag) {
            f = (float) entity.getDeltaMovement().lengthSqr();
            f /= 0.2F;
            f *= f * f;
        }

        if (f < 1.0F) {
            f = 1.0F;
        }

        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;
        this.rightLeg.zRot = 0.0F;
        this.leftLeg.zRot = 0.0F;
        if (this.riding) {
            this.rightArm.xRot += (-(float) Math.PI / 5F);
            this.leftArm.xRot += (-(float) Math.PI / 5F);
            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = ((float) Math.PI / 10F);
            this.rightLeg.zRot = 0.07853982F;
            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = (-(float) Math.PI / 10F);
            this.leftLeg.zRot = -0.07853982F;
        }

        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;
        boolean flag2 = entity.getMainArm() == HumanoidArm.RIGHT;
        if (entity.isUsingItem()) {
            boolean flag3 = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
            if (flag3 == flag2) {
                this.poseRightArm(entity);
            } else {
                this.poseLeftArm(entity);
            }
        } else {
            boolean flag4 = flag2 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
            if (flag2 != flag4) {
                this.poseLeftArm(entity);
                this.poseRightArm(entity);
            } else {
                this.poseRightArm(entity);
                this.poseLeftArm(entity);
            }
        }

        this.setupAttackAnimation(entity, ageInTicks);
        if (this.crouching) {
            this.body.xRot = 0.5F;
            this.rightArm.xRot += 0.4F;
            this.leftArm.xRot += 0.4F;
            this.rightLeg.z = 4.0F;
            this.leftLeg.z = 4.0F;
            this.rightLeg.y = 12.2F;
            this.leftLeg.y = 12.2F;
            this.head.y = 4.2F;
            this.body.y = 3.2F;
            this.leftArm.y = 5.2F;
            this.rightArm.y = 5.2F;
        } else {
            this.body.xRot = 0.0F;
            this.rightLeg.z = 0.1F;
            this.leftLeg.z = 0.1F;
            this.rightLeg.y = 12.0F;
            this.leftLeg.y = 12.0F;
            this.head.y = 0.0F;
            this.body.y = 0.0F;
            this.leftArm.y = 2.0F;
            this.rightArm.y = 2.0F;
        }

        if (this.rightArmPose != HumanoidModel.ArmPose.SPYGLASS) {
            BedrockAnimationUtils.bobBedrockPart(this.rightArm, ageInTicks, 1.0F);
        }

        if (this.leftArmPose != HumanoidModel.ArmPose.SPYGLASS) {
            BedrockAnimationUtils.bobBedrockPart(this.leftArm, ageInTicks, -1.0F);
        }

        if (this.swimAmount > 0.0F) {
            float f5 = limbSwing % 26.0F;
            HumanoidArm humanoidarm = this.getAttackArm(entity);
            float f1 = humanoidarm == HumanoidArm.RIGHT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
            float f2 = humanoidarm == HumanoidArm.LEFT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
            if (!entity.isUsingItem()) {
                if (f5 < 14.0F) {
                    this.leftArm.xRot = this.rotlerpRad(f2, this.leftArm.xRot, 0.0F);
                    this.rightArm.xRot = Mth.lerp(f1, this.rightArm.xRot, 0.0F);
                    this.leftArm.yRot = this.rotlerpRad(f2, this.leftArm.yRot, (float) Math.PI);
                    this.rightArm.yRot = Mth.lerp(f1, this.rightArm.yRot, (float) Math.PI);
                    this.leftArm.zRot = this.rotlerpRad(f2, this.leftArm.zRot, (float) Math.PI
                            + 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F));
                    this.rightArm.zRot = Mth.lerp(f1, this.rightArm.zRot, (float) Math.PI
                            - 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F));
                } else if (f5 >= 14.0F && f5 < 22.0F) {
                    float f6 = (f5 - 14.0F) / 8.0F;
                    this.leftArm.xRot = this.rotlerpRad(f2, this.leftArm.xRot, ((float) Math.PI / 2F) * f6);
                    this.rightArm.xRot = Mth.lerp(f1, this.rightArm.xRot, ((float) Math.PI / 2F) * f6);
                    this.leftArm.yRot = this.rotlerpRad(f2, this.leftArm.yRot, (float) Math.PI);
                    this.rightArm.yRot = Mth.lerp(f1, this.rightArm.yRot, (float) Math.PI);
                    this.leftArm.zRot = this.rotlerpRad(f2, this.leftArm.zRot, 5.012389F - 1.8707964F * f6);
                    this.rightArm.zRot = Mth.lerp(f1, this.rightArm.zRot, 1.2707963F + 1.8707964F * f6);
                } else if (f5 >= 22.0F && f5 < 26.0F) {
                    float f3 = (f5 - 22.0F) / 4.0F;
                    this.leftArm.xRot = this.rotlerpRad(f2, this.leftArm.xRot,
                            ((float) Math.PI / 2F) - ((float) Math.PI / 2F) * f3);
                    this.rightArm.xRot = Mth.lerp(f1, this.rightArm.xRot,
                            ((float) Math.PI / 2F) - ((float) Math.PI / 2F) * f3);
                    this.leftArm.yRot = this.rotlerpRad(f2, this.leftArm.yRot, (float) Math.PI);
                    this.rightArm.yRot = Mth.lerp(f1, this.rightArm.yRot, (float) Math.PI);
                    this.leftArm.zRot = this.rotlerpRad(f2, this.leftArm.zRot, (float) Math.PI);
                    this.rightArm.zRot = Mth.lerp(f1, this.rightArm.zRot, (float) Math.PI);
                }
            }

            this.leftLeg.xRot = Mth.lerp(this.swimAmount, this.leftLeg.xRot,
                    0.3F * Mth.cos(limbSwing * 0.33333334F + (float) Math.PI));
            this.rightLeg.xRot = Mth.lerp(this.swimAmount, this.rightLeg.xRot, 0.3F * Mth.cos(limbSwing * 0.33333334F));
        }

//        this.hat.copyFrom(this.head);
    }

    protected void poseRightArm(T p_102876_) {
        switch (this.rightArmPose) {
        case EMPTY:
            this.rightArm.yRot = 0.0F;
            break;
        case BLOCK:
            this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F;
            this.rightArm.yRot = (-(float) Math.PI / 6F);
            break;
        case ITEM:
            this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
            this.rightArm.yRot = 0.0F;
            break;
        case THROW_SPEAR:
            this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) Math.PI;
            this.rightArm.yRot = 0.0F;
            break;
        case BOW_AND_ARROW:
            this.rightArm.yRot = -0.1F + this.head.yRot;
            this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
            this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
            this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
            break;
        case CROSSBOW_CHARGE:
            BedrockAnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, p_102876_, true);
            break;
        case CROSSBOW_HOLD:
            BedrockAnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
            break;
        case SPYGLASS:
            this.rightArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (p_102876_.isCrouching() ? 0.2617994F : 0.0F),
                    -2.4F, 3.3F);
            this.rightArm.yRot = this.head.yRot - 0.2617994F;
        default:
            break;
        }

    }

    protected void poseLeftArm(T p_102879_) {
        switch (this.leftArmPose) {
        case EMPTY:
            this.leftArm.yRot = 0.0F;
            break;
        case BLOCK:
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
            this.leftArm.yRot = ((float) Math.PI / 6F);
            break;
        case ITEM:
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
            this.leftArm.yRot = 0.0F;
            break;
        case THROW_SPEAR:
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) Math.PI;
            this.leftArm.yRot = 0.0F;
            break;
        case BOW_AND_ARROW:
            this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
            this.leftArm.yRot = 0.1F + this.head.yRot;
            this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
            this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
            break;
        case CROSSBOW_CHARGE:
            BedrockAnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, p_102879_, false);
            break;
        case CROSSBOW_HOLD:
            BedrockAnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
            break;
        case SPYGLASS:
            this.leftArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (p_102879_.isCrouching() ? 0.2617994F : 0.0F),
                    -2.4F, 3.3F);
            this.leftArm.yRot = this.head.yRot + 0.2617994F;
        default:
            break;
        }

    }

    protected void setupAttackAnimation(T p_102858_, float p_102859_) {
        if (!(this.attackTime <= 0.0F)) {
            HumanoidArm humanoidarm = this.getAttackArm(p_102858_);
            BedrockPart modelpart = this.getArm(humanoidarm);
            float f = this.attackTime;
            this.body.yRot = Mth.sin(Mth.sqrt(f) * ((float) Math.PI * 2F)) * 0.2F;
            if (humanoidarm == HumanoidArm.LEFT) {
                this.body.yRot *= -1.0F;
            }

            this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F;
            this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F;
            this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F;
            this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F;
            this.rightArm.yRot += this.body.yRot;
            this.leftArm.yRot += this.body.yRot;
            this.leftArm.xRot += this.body.yRot;
            f = 1.0F - this.attackTime;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * (float) Math.PI);
            float f2 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
            modelpart.xRot -= f1 * 1.2F + f2;
            modelpart.yRot += this.body.yRot * 2.0F;
            modelpart.zRot += Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
        }
    }

    protected float rotlerpRad(float p_102836_, float p_102837_, float p_102838_) {
        float f = (p_102838_ - p_102837_) % ((float) Math.PI * 2F);
        if (f < -(float) Math.PI) {
            f += ((float) Math.PI * 2F);
        }

        if (f >= (float) Math.PI) {
            f -= ((float) Math.PI * 2F);
        }

        return p_102837_ + p_102836_ * f;
    }

    protected float quadraticArmUpdate(float p_102834_) {
        return -65.0F * p_102834_ + p_102834_ * p_102834_;
    }

    public void copyPropertiesTo(BedrockHumanoidModel<T> p_102873_) {
        super.copyPropertiesTo(p_102873_);
        p_102873_.leftArmPose = this.leftArmPose;
        p_102873_.rightArmPose = this.rightArmPose;
        p_102873_.crouching = this.crouching;
        p_102873_.head.copyFrom(this.head);
        p_102873_.body.copyFrom(this.body);
        p_102873_.rightArm.copyFrom(this.rightArm);
        p_102873_.leftArm.copyFrom(this.leftArm);
        p_102873_.rightLeg.copyFrom(this.rightLeg);
        p_102873_.leftLeg.copyFrom(this.leftLeg);
    }

    public void setAllVisible(boolean p_102880_) {
        this.head.visible = p_102880_;
        this.body.visible = p_102880_;
        this.rightArm.visible = p_102880_;
        this.leftArm.visible = p_102880_;
        this.rightLeg.visible = p_102880_;
        this.leftLeg.visible = p_102880_;
    }

    public void translateToHand(HumanoidArm p_102854_, PoseStack p_102855_) {
        this.getArm(p_102854_).translateAndRotate(p_102855_);
    }

    protected BedrockPart getArm(HumanoidArm p_102852_) {
        return p_102852_ == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }

    public BedrockPart getHead() {
        return this.head;
    }

    protected HumanoidArm getAttackArm(T p_102857_) {
        HumanoidArm humanoidarm = p_102857_.getMainArm();
        return p_102857_.swingingArm == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
    }

}
