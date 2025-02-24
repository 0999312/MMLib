package cn.mcmod_mmf.mmlib.client.model.bedrock;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.model.geom.PartPose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public final class BedrockPart {
    private final ObjectList<BedrockCube> cubes;
    private final Map<String, BedrockPart> children;

    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    public float xScale = 1.0F;
    public float yScale = 1.0F;
    public float zScale = 1.0F;

    public boolean visible;
    public boolean mirror;
    public boolean emissive;
    
    private PartPose initialPose = PartPose.ZERO;

     public PartPose getInitialPose() {
        return this.initialPose;
     }

     public void setInitialPose(PartPose p_233561_) {
        this.initialPose = p_233561_;
     }

     public void resetPose() {
        this.loadPose(this.initialPose);
     }

     public void loadPose(PartPose p_171323_) {
        this.x = p_171323_.x;
        this.y = p_171323_.y;
        this.z = p_171323_.z;
        this.xRot = p_171323_.xRot;
        this.yRot = p_171323_.yRot;
        this.zRot = p_171323_.zRot;
        this.xScale = 1.0F;
        this.yScale = 1.0F;
        this.zScale = 1.0F;
     }
     
     public void offsetScale(Vector3f p_253957_) {
         this.xScale += p_253957_.x();
         this.yScale += p_253957_.y();
         this.zScale += p_253957_.z();
      }
    
    public BedrockPart() {
        cubes = new ObjectArrayList<>();
        children = new HashMap<>();
        visible = true;
        emissive = false;
    }

    public void setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int texU, int texV) {
        this.render(poseStack, consumer, texU, texV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int texU, int texV, float red, float green,
            float blue, float alpha) {
        if (this.visible) 
            renderCubes(false, poseStack, consumer, texU, texV, red, green, blue, alpha);
    }
    
    public void renderEmissive(PoseStack poseStack, VertexConsumer consumer, int texU, int texV) {
        this.renderEmissive(poseStack, consumer, texU, texV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void renderEmissive(PoseStack poseStack, VertexConsumer consumer, int texU, int texV, float red, float green,
            float blue, float alpha) {
        if (this.visible) 
            renderCubes(true, poseStack, consumer, texU, texV, red, green, blue, alpha);
    }

    public void renderCubes(boolean renderEmissive, PoseStack poseStack, VertexConsumer consumer, int texU, int texV, float red, float green,
            float blue, float alpha) {
        if (!this.isEmpty()) {
            poseStack.pushPose();
            this.translateAndRotate(poseStack);
//            if(this.emissive == renderEmissive)
                this.compile(poseStack.last(), consumer, texU, texV, red, green, blue, alpha);
            
            for (BedrockPart part : this.children.values()) {
                if(renderEmissive)
                    part.renderEmissive(poseStack, consumer, texU, texV, red, green, blue, alpha);
                else
                    part.render(poseStack, consumer, texU, texV, red, green, blue, alpha);
            }

            poseStack.popPose();
        }
    }
    
    public void translateAndRotate(PoseStack poseStack) {
        poseStack.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);
        if (this.xRot != 0.0F || this.yRot != 0.0F || this.zRot != 0.0F) {
           poseStack.mulPose(new Quaternionf().rotationZYX(this.zRot, this.yRot, this.xRot));
        }
        
        if (this.xScale != 1.0F || this.yScale != 1.0F || this.zScale != 1.0F) {
        	poseStack.scale(this.xScale, this.yScale, this.zScale);
         }
     }

    private void compile(PoseStack.Pose pose, VertexConsumer consumer, int texU, int texV, float red, float green,
            float blue, float alpha) {
        for (BedrockCube bedrockCube : this.getCubes()) {
            bedrockCube.compile(pose, consumer, texU, texV, red, green, blue, alpha);
        }
    }

    public BedrockCube getRandomCube(Random random) {
        return this.getCubes().get(random.nextInt(this.getCubes().size()));
    }
    
    public boolean isEmissive() {
        return emissive;
    }

    public void setEmissive() {
        this.emissive = true;
    }

    public boolean isEmpty() {
        return this.getCubes().isEmpty() && this.children.isEmpty();
    }

    public PartPose storePose() {
        return PartPose.offsetAndRotation(this.x, this.y, this.z, this.xRot, this.yRot, this.zRot);
    }

    public void copyFrom(BedrockPart p_104316_) {
        this.xRot = p_104316_.xRot;
        this.yRot = p_104316_.yRot;
        this.zRot = p_104316_.zRot;
        this.x = p_104316_.x;
        this.y = p_104316_.y;
        this.z = p_104316_.z;
        this.xScale = p_104316_.xScale;
        this.yScale = p_104316_.yScale;
        this.zScale = p_104316_.zScale;
    }

    public void addChild(String name, BedrockPart model) {
        if(this.isEmissive())
            model.setEmissive();
        if(this.getChild(name)!=null) {
        	this.addChild(name+"0", model);
        }
        else {
        this.children.put(name, model);
        }
    }

    public List<BedrockPart> getChildren() {
        return ImmutableList.copyOf(this.children.values());
    }
    
    public Map<String, BedrockPart> getChildrenMap() {
		return this.children;
	}

	public ObjectList<BedrockCube> getCubes() {
		return cubes;
	}

	public BedrockPart getChild(String childPartName) {
		return this.children.get(childPartName);
	}

}
