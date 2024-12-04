package cn.mcmod_mmf.mmlib.client.render.sections.cache;

import com.mojang.math.Transformation;
import net.minecraft.client.resources.model.BakedModel;

/**
 * @author Argon4W
 */
public interface BakedModelCache {
    BakedModel getTransformedModel(Transformation transformation);
    int size();
}
