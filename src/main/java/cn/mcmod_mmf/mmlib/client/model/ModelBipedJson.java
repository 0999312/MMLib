package cn.mcmod_mmf.mmlib.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import cn.mcmod_mmf.mmlib.client.model.pojo.BonesItem;
import cn.mcmod_mmf.mmlib.client.model.pojo.CubesItem;
import cn.mcmod_mmf.mmlib.client.model.pojo.CustomModelPOJO;

import java.util.HashMap;
import java.util.List;

/**
 * 通过解析基岩版 JSON 实体模型得到的 POJO，将其转换为 ModelBiped 类
 *
 * @author TartaricAcid
 * @date 2019/7/9 14:18
 **/
@OnlyIn(Dist.CLIENT)
public class ModelBipedJson extends BipedModel<LivingEntity> {
    public final AxisAlignedBB renderBoundingBox;
    /**
     * 存储 ModelRender 子模型的 HashMap
     */
    protected final HashMap<String, ModelRenderer> modelMap = Maps.newHashMap();
    /**
     * 存储 Bones 的 HashMap，主要是给后面寻找父骨骼进行坐标转换用的
     */
    protected final HashMap<String, BonesItem> indexBones = Maps.newHashMap();
    /**
     * 哪些模型需要渲染。加载进父骨骼的子骨骼是不需要渲染的
     * 大概用不着这个，所以注释掉了。
     */
//    protected final List<ModelRenderer> shouldRender = Lists.newLinkedList();
    
    public ModelBipedJson(CustomModelPOJO pojo) {
    	super(1F);
        // 材质的长度、宽度
        texWidth = pojo.getGeometryModel().getTexturewidth();
        texHeight = pojo.getGeometryModel().getTextureheight();

        //清理Biped骨架确保后续加载时骨架为空。
        this.hat = new ModelRenderer(this);
        this.head = new ModelRenderer(this);
        this.body = new ModelRenderer(this);
        this.leftArm = new ModelRenderer(this);
        this.rightArm = new ModelRenderer(this);
        this.leftLeg = new ModelRenderer(this);
        this.rightLeg = new ModelRenderer(this);
        
        List<Float> offset = pojo.getGeometryModel().getVisibleBoundsOffset();
        float offsetX = offset.get(0);
        float offsetY = offset.get(1);
        float offsetZ = offset.get(2);
        float width = pojo.getGeometryModel().getVisibleBoundsWidth() / 2.0f;
        float height = pojo.getGeometryModel().getVisibleBoundsHeight() / 2.0f;
        renderBoundingBox = new AxisAlignedBB(offsetX - width, offsetY - height, offsetZ - width, offsetX + width, offsetY + height, offsetZ + width);
        
        // 往 indexBones 里面注入数据，为后续坐标转换做参考
        for (BonesItem bones : pojo.getGeometryModel().getBones()) {
            // 塞索引，这是给后面坐标转换用的
            indexBones.put(bones.getName(), bones);
            // 塞入新建的空 ModelRenderer 实例
            // 因为后面添加 parent 需要，所以先塞空对象，然后二次遍历再进行数据存储
            modelMap.put(bones.getName(), new ModelRenderer(this));
        }

        // 开始往 ModelRenderer 实例里面塞数据
        for (BonesItem bones : pojo.getGeometryModel().getBones()) {
            // 骨骼名称，注意因为后面动画的需要，头部、手部、腿部等骨骼命名必须是固定死的
            String name = bones.getName();
            // 旋转点，可能为空
            @Nullable List<Float> rotation = bones.getRotation();
            // 父骨骼的名称，可能为空
            @Nullable String parent = bones.getParent();
            // 塞进 HashMap 里面的模型对象
            ModelRenderer model = modelMap.get(name);
            // 镜像参数
            model.mirror = bones.isMirror();

            // 旋转点
            model.setPos(convertPivot(bones, 0), convertPivot(bones, 1), convertPivot(bones, 2));
            // Nullable 检查，设置旋转角度
            if (rotation != null) {
                setRotationAngle(model, convertRotation(rotation.get(0)), convertRotation(rotation.get(1)), convertRotation(rotation.get(2)));
            }
            
            // Null 检查，进行父骨骼绑定，Biped模型需要绑定ModelBiped的模型，位置硬编码
            // 放过我愚蠢的else-if吧——射命丸
            // 四肢的旋转点清空并跟随父模型坐标。
            setParentModelRender(model, name, parent);
            
            // 我的天，Cubes 还能为空……
            if (bones.getCubes() == null) {
                continue;
            }

            // 塞入 Cube List
            for (CubesItem cubes : bones.getCubes()) {
            	List<Float> uv = cubes.getUv();
                List<Float> size = cubes.getSize();
                boolean mirror = cubes.isMirror();
                float inflate = cubes.getInflate();
                model.texOffs(uv.get(0).intValue(), uv.get(1).intValue()).addBox(convertOrigin(bones, cubes, 0), convertOrigin(bones, cubes, 1), convertOrigin(bones, cubes, 2),
                        size.get(0), size.get(1), size.get(2), inflate, mirror);
            }
        }
    }
    
    protected void setParentModelRender(ModelRenderer model,String name, String parent) {
        if (parent != null) {
            modelMap.get(parent).addChild(model);
        }else if(name.equals("head")||name.equals("bipedHead")){
        	this.head.addChild(model);
        }else if(name.equals("body")||name.equals("bipedBody")){
        	this.body.addChild(model);
        }else if(name.equals("armRight")||name.equals("bipedRightArm")){
        	model.setPos(0, 0, 0);
        	this.rightArm.addChild(model);
        }else if(name.equals("armLeft")||name.equals("bipedLeftArm")){
        	model.setPos(0, 0, 0);
        	this.leftArm.addChild(model);
        }else if(name.equals("legRight")||name.equals("bipedRightLeg")){
        	model.setPos(0, 0, 0);
        	this.rightLeg.addChild(model);
        }else if(name.equals("legLeft")||name.equals("bipedLeftLeg")){
        	model.setPos(0, 0, 0);
        	this.leftLeg.addChild(model);
        }
    }
    
    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    /**
     * 基岩版的旋转中心计算方式和 Java 版不太一样，需要进行转换
     * <p>
     * 如果有父模型
     * <li>x，z 方向：本模型坐标 - 父模型坐标
     * <li>y 方向：父模型坐标 - 本模型坐标
     * <p>
     * 如果没有父模型
     * <li>x，z 方向不变
     * <li>y 方向：24 - 本模型坐标
     *
     * @param index 是 xyz 的哪一个，x 是 0，y 是 1，z 是 2
     */
    private float convertPivot(BonesItem bones, int index) {
        if (bones.getParent() != null) {
            if (index == 1) {
                return indexBones.get(bones.getParent()).getPivot().get(index) - bones.getPivot().get(index);
            }
			return bones.getPivot().get(index) - indexBones.get(bones.getParent()).getPivot().get(index);
        }
		if (index == 1) {
		    return 24 - bones.getPivot().get(index);
		}
		return bones.getPivot().get(index);
    }

    /**
     * 基岩版和 Java 版本的方块起始坐标也不一致，Java 是相对坐标，而且 y 值方向不一致。
     * 基岩版是绝对坐标，而且 y 方向朝上。
     * 其实两者规律很简单，但是我找了一下午，才明白咋回事。
     * <li>如果是 x，z 轴，那么只需要方块起始坐标减去旋转点坐标
     * <li>如果是 y 轴，旋转点坐标减去方块起始坐标，再减去方块的 y 长度
     *
     * @param index 是 xyz 的哪一个，x 是 0，y 是 1，z 是 2
     */
    private float convertOrigin(BonesItem bones, CubesItem cubes, int index) {
        if (index == 1) {
            return bones.getPivot().get(index) - cubes.getOrigin().get(index) - cubes.getSize().get(index);
        }
		return cubes.getOrigin().get(index) - bones.getPivot().get(index);
    }

    /**
     * 基岩版用的是度，Java 版用的是弧度，这个转换很简单
     */
    private float convertRotation(float degree) {
        return (float) (degree * Math.PI / 180);
    }

}
