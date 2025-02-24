package cn.mcmod_mmf.mmlib.client.model;

import java.util.Collections;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

public class MultiModelBakedModel implements BakedModel {
	private final BakedModel bakedModel2d;
    private final BakedModel bakedModel3d;

    public MultiModelBakedModel(BakedModel bakedModel2d, BakedModel bakedModel3d) {
        this.bakedModel2d = bakedModel2d;
        this.bakedModel3d = bakedModel3d;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand) {
        return Collections.emptyList();
    }

    @Override
    public boolean usesBlockLight() {
        return bakedModel2d.usesBlockLight();
    }

	@Override
	@Deprecated
    public TextureAtlasSprite getParticleIcon() {
        return this.bakedModel2d.getParticleIcon();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return bakedModel2d.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return bakedModel2d.isGui3d();
    }

    @Override
    public boolean isCustomRenderer() {
        return bakedModel2d.isCustomRenderer();
    }

    @Override
    public ItemOverrides getOverrides() {
        return bakedModel2d.getOverrides();
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext type, PoseStack mat, boolean applyLeftHandTransform) {
        if (this.using2DDisplay(type)) {
            return bakedModel2d.applyTransform(type, mat, applyLeftHandTransform);
        } 
        
        return bakedModel3d.applyTransform(type, mat, applyLeftHandTransform);
    }

	public boolean using2DDisplay(ItemDisplayContext type) {
		return type == ItemDisplayContext.GUI || type == ItemDisplayContext.FIXED || type == ItemDisplayContext.GROUND;
	}
}
