package cn.mcmod_mmf.mmlib.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import cn.mcmod_mmf.mmlib.client.model.pojo.BedrockModelPOJO;
import cn.mcmod_mmf.mmlib.client.model.pojo.BonesItem;
import cn.mcmod_mmf.mmlib.client.model.pojo.CubesItem;
import cn.mcmod_mmf.mmlib.client.model.pojo.Description;

import java.util.Collections;
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
    public AxisAlignedBB renderBoundingBox;
    /**
     * 存储 ModelRender 子模型的 HashMap
     */
    protected final HashMap<String, ModelRenderer> modelMap = Maps.newHashMap();
    
    public HashMap<String, ModelRenderer> getModelMap() {
		return (HashMap<String, ModelRenderer>) Collections.unmodifiableMap(modelMap);
	}
    
    /**
     * 存储 Bones 的 HashMap，主要是给后面寻找父骨骼进行坐标转换用的
     */
    protected final HashMap<String, BonesItem> indexBones = Maps.newHashMap();
    /**
     * 哪些模型需要渲染。加载进父骨骼的子骨骼是不需要渲染的
     * 大概用不着这个，所以注释掉了。
     */
//    protected final List<ModelRenderer> shouldRender = Lists.newLinkedList();
    
    public ModelBipedJson(BedrockModelPOJO pojo) {
    	super(1F);
        //清理Biped骨架确保后续加载时骨架为空。
        this.hat = new ModelRenderer(this);
        this.head = new ModelRenderer(this);
        this.body = new ModelRenderer(this);
        this.leftArm = new ModelRenderer(this);
        this.rightArm = new ModelRenderer(this);
        this.leftLeg = new ModelRenderer(this);
        this.rightLeg = new ModelRenderer(this);
        
        if (pojo.getFormatVersion().equals("1.10.0")) {
            loadLegacyModel(pojo);
        }else if (pojo.getFormatVersion().equals("1.12.0")) {
            loadNewModel(pojo);
        }
    }
    
    private void loadNewModel(BedrockModelPOJO pojo) {
        assert pojo.getGeometryModelNew() != null;

        Description description = pojo.getGeometryModelNew().getDescription();
        // 材质的长度、宽度
        texWidth = description.getTextureWidth();
        texHeight = description.getTextureHeight();

        List<Float> offset = description.getVisibleBoundsOffset();
        float offsetX = offset.get(0);
        float offsetY = offset.get(1);
        float offsetZ = offset.get(2);
        float width = description.getVisibleBoundsWidth() / 2.0f;
        float height = description.getVisibleBoundsHeight() / 2.0f;
        renderBoundingBox = new AxisAlignedBB(offsetX - width, offsetY - height, offsetZ - width, offsetX + width, offsetY + height, offsetZ + width);

        // 往 indexBones 里面注入数据，为后续坐标转换做参考
        for (BonesItem bones : pojo.getGeometryModelNew().getBones()) {
            // 塞索引，这是给后面坐标转换用的
            indexBones.put(bones.getName(), bones);
            // 塞入新建的空 ModelRenderer 实例
            // 因为后面添加 parent 需要，所以先塞空对象，然后二次遍历再进行数据存储
            modelMap.put(bones.getName(), new ModelRenderer(this));
        }

        // 开始往 ModelRenderer 实例里面塞数据
        for (BonesItem bones : pojo.getGeometryModelNew().getBones()) {
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
            for (CubesItem cube : bones.getCubes()) {
                List<Float> uv = cube.getUv();
                List<Float> size = cube.getSize();
                @Nullable List<Float> cubeRotation = cube.getRotation();
                boolean mirror = cube.isMirror();
                float inflate = cube.getInflate();

                // 当做普通 cube 存入
                if (cubeRotation == null) {
                    model.cubes.add(new ModelFloatBox(uv.get(0), uv.get(1),
                            convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                            size.get(0), size.get(1), size.get(2), inflate, inflate, inflate, mirror,
                            texWidth, texHeight));
                }
                // 创建 Cube ModelRender
                else {
                    ModelRenderer cubeRenderer = new ModelRenderer(this);
                    cubeRenderer.setPos(convertPivot(bones, cube, 0), convertPivot(bones, cube, 1), convertPivot(bones, cube, 2));
                    setRotationAngle(cubeRenderer, convertRotation(cubeRotation.get(0)), convertRotation(cubeRotation.get(1)), convertRotation(cubeRotation.get(2)));
                    cubeRenderer.cubes.add(new ModelFloatBox(uv.get(0), uv.get(1),
                            convertOrigin(cube, 0), convertOrigin(cube, 1), convertOrigin(cube, 2),
                            size.get(0), size.get(1), size.get(2), inflate, inflate, inflate, mirror,
                            texWidth, texHeight));

                    // 添加进父骨骼中
                    model.addChild(cubeRenderer);
                }
            }
        }
    }

    private void loadLegacyModel(BedrockModelPOJO pojo) {
        assert pojo.getGeometryModelLegacy() != null;
        // 材质的长度、宽度
        texWidth = pojo.getGeometryModelLegacy().getTextureWidth();
        texHeight = pojo.getGeometryModelLegacy().getTextureHeight();
        
        List<Float> offset = pojo.getGeometryModelLegacy().getVisibleBoundsOffset();
        float offsetX = offset.get(0);
        float offsetY = offset.get(1);
        float offsetZ = offset.get(2);
        float width = pojo.getGeometryModelLegacy().getVisibleBoundsWidth() / 2.0f;
        float height = pojo.getGeometryModelLegacy().getVisibleBoundsHeight() / 2.0f;
        renderBoundingBox = new AxisAlignedBB(offsetX - width, offsetY - height, offsetZ - width, offsetX + width, offsetY + height, offsetZ + width);
        
        // 往 indexBones 里面注入数据，为后续坐标转换做参考
        for (BonesItem bones : pojo.getGeometryModelLegacy().getBones()) {
            // 塞索引，这是给后面坐标转换用的
            indexBones.put(bones.getName(), bones);
            // 塞入新建的空 ModelRenderer 实例
            // 因为后面添加 parent 需要，所以先塞空对象，然后二次遍历再进行数据存储
            modelMap.put(bones.getName(), new ModelRenderer(this));
        }

        // 开始往 ModelRenderer 实例里面塞数据
        for (BonesItem bones : pojo.getGeometryModelLegacy().getBones()) {
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
            for (CubesItem cube : bones.getCubes()) {
                List<Float> uv = cube.getUv();
                List<Float> size = cube.getSize();
                boolean mirror = cube.isMirror();
                float inflate = cube.getInflate();

                model.cubes.add(new ModelFloatBox(uv.get(0), uv.get(1),
                        convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                        size.get(0), size.get(1), size.get(2), inflate, inflate, inflate, mirror,
                        texWidth, texHeight));
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
            } else {
                return bones.getPivot().get(index) - indexBones.get(bones.getParent()).getPivot().get(index);
            }
        } else {
            if (index == 1) {
                return 24 - bones.getPivot().get(index);
            } else {
                return bones.getPivot().get(index);
            }
        }
    }

    private float convertPivot(BonesItem parent, CubesItem cube, int index) {
        assert cube.getPivot() != null;
        if (index == 1) {
            return parent.getPivot().get(index) - cube.getPivot().get(index);
        } else {
            return cube.getPivot().get(index) - parent.getPivot().get(index);
        }
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
    private float convertOrigin(BonesItem bone, CubesItem cube, int index) {
        if (index == 1) {
            return bone.getPivot().get(index) - cube.getOrigin().get(index) - cube.getSize().get(index);
        } else {
            return cube.getOrigin().get(index) - bone.getPivot().get(index);
        }
    }

    private float convertOrigin(CubesItem cube, int index) {
        assert cube.getPivot() != null;
        if (index == 1) {
            return cube.getPivot().get(index) - cube.getOrigin().get(index) - cube.getSize().get(index);
        } else {
            return cube.getOrigin().get(index) - cube.getPivot().get(index);
        }
    }

    /**
     * 基岩版用的是度，Java 版用的是弧度，这个转换很简单
     */
    private float convertRotation(float degree) {
        return (float) (degree * Math.PI / 180);
    }
}
