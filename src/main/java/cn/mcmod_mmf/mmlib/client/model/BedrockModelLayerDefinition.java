package cn.mcmod_mmf.mmlib.client.model;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.mcmod_mmf.mmlib.client.model.pojo.BedrockModelPOJO;
import cn.mcmod_mmf.mmlib.client.model.pojo.BonesItem;
import cn.mcmod_mmf.mmlib.client.model.pojo.CubesItem;
import cn.mcmod_mmf.mmlib.client.model.pojo.Description;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class BedrockModelLayerDefinition {
    
    public static LayerDefinition createLayer(MeshDefinition meshdefinition, BedrockModelPOJO pojo) {
        if (pojo.getFormatVersion().equals("1.10.0")) {
            return loadLegacyModel(meshdefinition, pojo);
        } else if (pojo.getFormatVersion().equals("1.12.0")) {
            return loadNewModel(meshdefinition, pojo);
        }
        return null;
    }
    
    private static LayerDefinition loadNewModel(MeshDefinition meshdefinition, BedrockModelPOJO pojo) {
        PartDefinition partdefinition = meshdefinition.getRoot();
        HashMap<String, BonesItem> indexBones = Maps.newHashMap();
        List<String> shouldRender = Lists.newLinkedList();
        // 存储 PartDefinition 子模型的 HashMap
        HashMap<String, PartDefinition> modelMap = Maps.newHashMap();

        assert pojo.getGeometryModelNew() != null;
        pojo.getGeometryModelNew().deco();

        Description description = pojo.getGeometryModelNew().getDescription();
        
        // 材质的长度、宽度
        int texWidth = description.getTextureWidth();
        int texHeight = description.getTextureHeight();

        // 往 indexBones 里面注入数据，为后续坐标转换做参考
        for (BonesItem bones : pojo.getGeometryModelNew().getBones()) {
            // 塞索引，这是给后面坐标转换用的
            indexBones.put(bones.getName(), bones);
            // 塞入新建的空 PartDefinition 实例
            // 因为后面添加 parent 需要，所以先塞空对象，然后二次遍历再进行数据存储
            modelMap.put(bones.getName(), partdefinition.getChild(bones.getName()));
        }

        // 开始往 ModelRenderer 实例里面塞数据
        for (BonesItem bones : pojo.getGeometryModelNew().getBones()) {
            // 骨骼名称，注意因为后面动画的需要，头部、手部、腿部等骨骼命名必须是固定死的
            String name = bones.getName();
            // 旋转点，可能为空
            @Nullable
            List<Float> rotation = bones.getRotation();
            // 父骨骼的名称，可能为空
            @Nullable
            String parent = bones.getParent();
            // 塞进 HashMap 里面的模型对象
            PartDefinition model = modelMap.get(name);
            PartPose pose = null;
            // Nullable 检查，设置旋转参数
            if (rotation != null) {
                pose = PartPose.offsetAndRotation(convertPivot(indexBones,bones, 0), convertPivot(indexBones,bones, 1),
                        convertPivot(indexBones,bones, 2), convertRotation(rotation.get(0)), convertRotation(rotation.get(1)),
                        convertRotation(rotation.get(2)));
            } else {
                pose = PartPose.offset(convertPivot(indexBones,bones, 0), convertPivot(indexBones,bones, 1), convertPivot(indexBones,bones, 2));
            }

            CubeListBuilder cubeBuilder = CubeListBuilder.create();

            // Null 检查，进行父骨骼绑定
            if (parent != null) {
                modelMap.get(parent).addOrReplaceChild(name, cubeBuilder, pose);
            } else if(partdefinition.getChild("head")!=null){
                if (name.equals("head") || name.equals("bipedHead"))
                    partdefinition.getChild("head").addOrReplaceChild(name, cubeBuilder, pose);
            } else if(partdefinition.getChild("hat")!=null){
                if (name.equals("hat"))
                    partdefinition.getChild("hat").addOrReplaceChild(name, cubeBuilder, pose);
            } else if(partdefinition.getChild("body")!=null){
                if (name.equals("body") || name.equals("bipedBody"))
                    partdefinition.getChild("body").addOrReplaceChild(name, cubeBuilder, pose);
            } else if(partdefinition.getChild("right_arm")!=null){
                if (name.equals("armRight") || name.equals("bipedRightArm"))
                    partdefinition.getChild("right_arm").addOrReplaceChild(name, cubeBuilder, pose);
            } else if(partdefinition.getChild("left_arm")!=null){
                if (name.equals("armLeft") || name.equals("bipedLeftArm"))
                    partdefinition.getChild("left_arm").addOrReplaceChild(name, cubeBuilder, pose);
            } else if(partdefinition.getChild("right_leg")!=null){
                if (name.equals("legRight") || name.equals("bipedRightLeg"))
                    partdefinition.getChild("right_leg").addOrReplaceChild(name, cubeBuilder, pose);
            } else if(partdefinition.getChild("left_leg")!=null){
                if (name.equals("legLeft") || name.equals("bipedLeftLeg"))
                    partdefinition.getChild("left_leg").addOrReplaceChild(name, cubeBuilder, pose);
            } else {
                // 没有父骨骼的模型才进行渲染
                shouldRender.add(name);
            }

            // 我的天，Cubes 还能为空……
            if (bones.getCubes() == null) {
                continue;
            }

            // 塞入 Cube List
            for (CubesItem cube : bones.getCubes()) {
                List<Float> uv = cube.getUv();
                List<Float> size = cube.getSize();
                @Nullable
                List<Float> cubeRotation = cube.getRotation();
                boolean mirror = cube.isMirror();
                float inflate = cube.getInflate();

                // 当做普通 cube 存入
                if (cubeRotation == null) {
                    cubeBuilder.texOffs(uv.get(0).intValue(), uv.get(1).intValue()).mirror(mirror).addBox(
                            convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                            size.get(0), size.get(1), size.get(2), new CubeDeformation(inflate, inflate, inflate));
                }
                // 创建 Cube ModelRender
                else {
                    String cubeName = name + "_cube_" + convertPivot(bones, cube, 0) + '_'
                            + convertPivot(bones, cube, 1) + '_' + convertPivot(bones, cube, 2) + '_'
                            + convertRotation(cubeRotation.get(0)) + '_' + convertRotation(cubeRotation.get(1)) + '_'
                            + convertRotation(cubeRotation.get(2)) + '_' + convertOrigin(bones, cube, 0) + '_'
                            + convertOrigin(bones, cube, 1) + '_' + convertOrigin(bones, cube, 2) + '_' + size.get(0)
                            + '_' + size.get(1) + '_' + size.get(2) + '_' + inflate;

                    PartPose cubePose = PartPose.offsetAndRotation(convertPivot(bones, cube, 0),
                            convertPivot(bones, cube, 1), convertPivot(bones, cube, 2),
                            convertRotation(cubeRotation.get(0)), convertRotation(cubeRotation.get(1)),
                            convertRotation(cubeRotation.get(2)));

                    // 添加进父骨骼中
                    model.addOrReplaceChild(cubeName,
                            CubeListBuilder.create().texOffs(uv.get(0).intValue(), uv.get(1).intValue()).mirror(mirror)
                                    .addBox(convertOrigin(cube, 0), convertOrigin(cube, 1), convertOrigin(cube, 2),
                                            size.get(0), size.get(1), size.get(2),
                                            new CubeDeformation(inflate, inflate, inflate)),
                            cubePose);
                }
            }
        }
        return LayerDefinition.create(meshdefinition, texWidth, texHeight);
    }

    private static LayerDefinition loadLegacyModel(MeshDefinition meshdefinition, BedrockModelPOJO pojo) {
        PartDefinition partdefinition = meshdefinition.getRoot();
        HashMap<String, BonesItem> indexBones = Maps.newHashMap();
        List<String> shouldRender = Lists.newLinkedList();
        // 存储 PartDefinition 子模型的 HashMap
        HashMap<String, PartDefinition> modelMap = Maps.newHashMap();
        assert pojo.getGeometryModelLegacy() != null;

        // 材质的长度、宽度
        int texWidth = pojo.getGeometryModelLegacy().getTextureWidth();
        int texHeight = pojo.getGeometryModelLegacy().getTextureHeight();

        // 往 indexBones 里面注入数据，为后续坐标转换做参考
        for (BonesItem bones : pojo.getGeometryModelLegacy().getBones()) {
            // 塞索引，这是给后面坐标转换用的
            indexBones.put(bones.getName(), bones);
            // 塞入新建的空 PartDefinition 实例
            // 因为后面添加 parent 需要，所以先塞空对象，然后二次遍历再进行数据存储
            modelMap.put(bones.getName(), partdefinition.addOrReplaceChild(bones.getName(), CubeListBuilder.create(), PartPose.ZERO));
        }

        // 开始往 ModelRenderer 实例里面塞数据
        for (BonesItem bones : pojo.getGeometryModelLegacy().getBones()) {
            // 骨骼名称，注意因为后面动画的需要，头部、手部、腿部等骨骼命名必须是固定死的
            String name = bones.getName();
            // 旋转点，可能为空
            @Nullable
            List<Float> rotation = bones.getRotation();
            // 父骨骼的名称，可能为空
            @Nullable
            String parent = bones.getParent();

            PartPose pose = null;
            // Nullable 检查，设置旋转参数
            if (rotation != null) {
                pose = PartPose.offsetAndRotation(convertPivot(indexBones, bones, 0), convertPivot(indexBones,bones, 1),
                        convertPivot(indexBones,bones, 2), convertRotation(rotation.get(0)), convertRotation(rotation.get(1)),
                        convertRotation(rotation.get(2)));
            } else {
                pose = PartPose.offset(convertPivot(indexBones,bones, 0), convertPivot(indexBones,bones, 1), convertPivot(indexBones,bones, 2));
            }

            CubeListBuilder cubeBuilder = CubeListBuilder.create();

            // Null 检查，进行父骨骼绑定
            if (parent != null) {
                modelMap.get(parent).addOrReplaceChild(name, cubeBuilder, pose);
            } else {
                // 没有父骨骼的模型才进行渲染
                shouldRender.add(name);
            }

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
                cubeBuilder.texOffs(uv.get(0).intValue(), uv.get(1).intValue()).mirror(mirror).addBox(
                        convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                        size.get(0), size.get(1), size.get(2), new CubeDeformation(inflate, inflate, inflate));
            }
        }
        return LayerDefinition.create(meshdefinition, texWidth, texHeight);
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
    private static float convertPivot(HashMap<String, BonesItem> indexBones, BonesItem bones, int index) {
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

    private static float convertPivot(BonesItem parent, CubesItem cube, int index) {
        assert cube.getPivot() != null;
        if (index == 1) {
            return parent.getPivot().get(index) - cube.getPivot().get(index);
        } else {
            return cube.getPivot().get(index) - parent.getPivot().get(index);
        }
    }

    /**
     * 基岩版和 Java 版本的方块起始坐标也不一致，Java 是相对坐标，而且 y 值方向不一致。 基岩版是绝对坐标，而且 y 方向朝上。
     * 其实两者规律很简单，但是我找了一下午，才明白咋回事。
     * <li>如果是 x，z 轴，那么只需要方块起始坐标减去旋转点坐标
     * <li>如果是 y 轴，旋转点坐标减去方块起始坐标，再减去方块的 y 长度
     *
     * @param index 是 xyz 的哪一个，x 是 0，y 是 1，z 是 2
     */
    private static float convertOrigin(BonesItem bone, CubesItem cube, int index) {
        if (index == 1) {
            return bone.getPivot().get(index) - cube.getOrigin().get(index) - cube.getSize().get(index);
        } else {
            return cube.getOrigin().get(index) - bone.getPivot().get(index);
        }
    }

    private static float convertOrigin(CubesItem cube, int index) {
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
    private static float convertRotation(float degree) {
        return (float) (degree * Math.PI / 180);
    }
}
