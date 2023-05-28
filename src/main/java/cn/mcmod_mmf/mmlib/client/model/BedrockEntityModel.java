package cn.mcmod_mmf.mmlib.client.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import cn.mcmod_mmf.mmlib.client.model.bedrock.BedrockModel;
import cn.mcmod_mmf.mmlib.client.model.bedrock.BedrockPart;
import cn.mcmod_mmf.mmlib.client.model.bedrock.BedrockVersion;
import cn.mcmod_mmf.mmlib.client.model.pojo.BedrockModelPOJO;
import cn.mcmod_mmf.mmlib.client.model.pojo.BonesItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
/**
 * Simple Bedrock Entity Model, No need for other change.
 */
public class BedrockEntityModel<T extends Entity> extends EntityModel<T> implements BedrockModel {

    protected final HashMap<String, BedrockPart> modelMap = new HashMap<>();

    private final HashMap<String, BonesItem> indexBones = new HashMap<>();

    private final List<BedrockPart> shouldRender = new LinkedList<>();

    private AABB renderBoundingBox;

    public BedrockEntityModel() {
        super(RenderType::entityTranslucentCull);
        renderBoundingBox = new AABB(-1, 0, -1, 1, 2, 1);
    }

    public BedrockEntityModel(BedrockModelPOJO pojo, BedrockVersion version) {
        super(RenderType::entityTranslucent);
        if (version == BedrockVersion.LEGACY) {
            loadLegacyModel(pojo);
        }
        if (version == BedrockVersion.NEW) {
            loadNewModel(pojo);
        }
    }


    @Override
    @ParametersAreNonnullByDefault
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        for (BedrockPart model : this.getShouldRender()) {
            model.render(poseStack, buffer, packedLight, packedOverlay);
        }
    }

    @Override
    public HashMap<String, BedrockPart> getModelMap() {
        return this.modelMap;
    }

    @Override
    public HashMap<String, BonesItem> getIndexBones() {
        return this.indexBones;
    }

    @Override
    public List<BedrockPart> getShouldRender() {
        return this.shouldRender;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return this.renderBoundingBox;
    }

    @Override
    public void setRenderBoundingBox(AABB aabb) {
        this.renderBoundingBox = aabb;
    }

    @Override
    public void setupAnim(T p_102618_, float p_102619_, float p_102620_, float p_102621_, float p_102622_,
            float p_102623_) {
        // TODO Auto-generated method stub
        
    }

}
