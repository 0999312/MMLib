package cn.mcmod_mmf.mmlib.client.model.bedrock;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import cn.mcmod_mmf.mmlib.client.model.pojo.BedrockModelPOJO;
import cn.mcmod_mmf.mmlib.client.model.pojo.BonesItem;
import cn.mcmod_mmf.mmlib.client.model.pojo.CubesItem;
import cn.mcmod_mmf.mmlib.client.model.pojo.Description;
import cn.mcmod_mmf.mmlib.client.model.pojo.FaceUVsItem;
import cn.mcmod_mmf.mmlib.utils.ClientUtil;
import net.minecraft.world.phys.AABB;

public interface BedrockModel {
    public BedrockModelPOJO getBedrockModelPOJO();
    public void setBedrockModelPOJO(BedrockModelPOJO pojo);
    public HashMap<String, BedrockPart> getModelMap();
    public HashMap<String, BonesItem> getIndexBones();
    public List<BedrockPart> getShouldRender();
    public AABB getRenderBoundingBox();
    public void setRenderBoundingBox(AABB aabb);

    public default boolean needRefresh(BedrockModelPOJO pojo) {
        // if not same object, refresh it.
        return this.getBedrockModelPOJO() != pojo;
    }

    public default void loadModel(BedrockModelPOJO pojo) {
        this.getModelMap().clear();
        this.getIndexBones().clear();
        this.getShouldRender().clear();
        String formatVersion = pojo.getFormatVersion();
        if (formatVersion.equals(BedrockVersion.LEGACY.getVersion())) {
            loadLegacyModel(pojo);
        } else if (formatVersion.equals(BedrockVersion.NEW.getVersion())) {
            loadNewModel(pojo);
        }
        this.setBedrockModelPOJO(pojo);
    }

    public default void loadNewModel(BedrockModelPOJO pojo) {
        assert pojo.getGeometryModelNew() != null;
        pojo.getGeometryModelNew().deco();

        Description description = pojo.getGeometryModelNew().getDescription();

        int texWidth = description.getTextureWidth();
        int texHeight = description.getTextureHeight();

        List<Float> offset = description.getVisibleBoundsOffset();
        float offsetX = offset.get(0);
        float offsetY = offset.get(1);
        float offsetZ = offset.get(2);
        float width = description.getVisibleBoundsWidth() / 2.0f;
        float height = description.getVisibleBoundsHeight() / 2.0f;
        this.setRenderBoundingBox(new AABB(offsetX - width, offsetY - height, offsetZ - width, offsetX + width, offsetY + height, offsetZ + width));

        for (BonesItem bones : pojo.getGeometryModelNew().getBones()) {
            this.getIndexBones().put(bones.getName(), bones);
            this.getModelMap().put(bones.getName(), new BedrockPart());
        }

        for (BonesItem bones : pojo.getGeometryModelNew().getBones()) {
            String name = bones.getName();
            @Nullable List<Float> rotation = bones.getRotation();
            @Nullable String parent = bones.getParent();
            BedrockPart model = this.getModelMap().get(name);
            model.mirror = bones.isMirror();

            model.setPos(convertPivot(bones, 0), convertPivot(bones, 1), convertPivot(bones, 2));

            if (rotation != null) {
                setRotationAngle(model, ClientUtil.convertRotation(rotation.get(0)), ClientUtil.convertRotation(rotation.get(1)), ClientUtil.convertRotation(rotation.get(2)));
            }

            if (parent != null) {
                this.getModelMap().get(parent).addChild(model);
            } else {
                this.getShouldRender().add(model);
            }

            if (bones.getCubes() == null) {
                continue;
            }

            for (CubesItem cube : bones.getCubes()) {
                List<Float> uv = cube.getUv();
                @Nullable FaceUVsItem faceUv = cube.getFaceUv();
                List<Float> size = cube.getSize();
                @Nullable List<Float> cubeRotation = cube.getRotation();
                boolean mirror = cube.isMirror();
                float inflate = cube.getInflate();

                if (cubeRotation == null) {
                    if (faceUv == null) {
                        model.cubes.add(new BedrockCubeBox(uv.get(0), uv.get(1),
                                convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                                size.get(0), size.get(1), size.get(2), inflate, mirror,
                                texWidth, texHeight));
                    } else {
                        model.cubes.add(new BedrockCubePerFace(
                                convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                                size.get(0), size.get(1), size.get(2), inflate,
                                texWidth, texHeight, faceUv));
                    }
                }

                else {
                    BedrockPart cubeRenderer = new BedrockPart();
                    cubeRenderer.setPos(convertPivot(bones, cube, 0), convertPivot(bones, cube, 1), convertPivot(bones, cube, 2));
                    setRotationAngle(cubeRenderer, ClientUtil.convertRotation(cubeRotation.get(0)), ClientUtil.convertRotation(cubeRotation.get(1)), ClientUtil.convertRotation(cubeRotation.get(2)));
                    if (faceUv == null) {
                        cubeRenderer.cubes.add(new BedrockCubeBox(uv.get(0), uv.get(1),
                                convertOrigin(cube, 0), convertOrigin(cube, 1), convertOrigin(cube, 2),
                                size.get(0), size.get(1), size.get(2), inflate, mirror,
                                texWidth, texHeight));
                    } else {
                        cubeRenderer.cubes.add(new BedrockCubePerFace(
                                convertOrigin(cube, 0), convertOrigin(cube, 1), convertOrigin(cube, 2),
                                size.get(0), size.get(1), size.get(2), inflate,
                                texWidth, texHeight, faceUv));
                    }

                    model.addChild(cubeRenderer);
                }
            }
        }
    }

    public default void loadLegacyModel(BedrockModelPOJO pojo) {
        assert pojo.getGeometryModelLegacy() != null;
        pojo.getGeometryModelLegacy().deco();

        int texWidth = pojo.getGeometryModelLegacy().getTextureWidth();
        int texHeight = pojo.getGeometryModelLegacy().getTextureHeight();

        List<Float> offset = pojo.getGeometryModelLegacy().getVisibleBoundsOffset();
        float offsetX = offset.get(0);
        float offsetY = offset.get(1);
        float offsetZ = offset.get(2);
        float width = pojo.getGeometryModelLegacy().getVisibleBoundsWidth() / 2.0f;
        float height = pojo.getGeometryModelLegacy().getVisibleBoundsHeight() / 2.0f;
        this.setRenderBoundingBox(new AABB(offsetX - width, offsetY - height, offsetZ - width, offsetX + width, offsetY + height, offsetZ + width));

        for (BonesItem bones : pojo.getGeometryModelLegacy().getBones()) {
            this.getIndexBones().put(bones.getName(), bones);
            this.getModelMap().put(bones.getName(), new BedrockPart());
        }

        for (BonesItem bones : pojo.getGeometryModelLegacy().getBones()) {
            String name = bones.getName();
            @Nullable List<Float> rotation = bones.getRotation();
            @Nullable String parent = bones.getParent();
            BedrockPart model = this.getModelMap().get(name);

            model.mirror = bones.isMirror();

            model.setPos(convertPivot(bones, 0), convertPivot(bones, 1), convertPivot(bones, 2));

            if (rotation != null) {
                setRotationAngle(model, ClientUtil.convertRotation(rotation.get(0)), ClientUtil.convertRotation(rotation.get(1)), ClientUtil.convertRotation(rotation.get(2)));
            }

            if (parent != null) {
                this.getModelMap().get(parent).addChild(model);
            } else {
                this.getShouldRender().add(model);
            }

            if (bones.getCubes() == null) {
                continue;
            }

            for (CubesItem cube : bones.getCubes()) {
                List<Float> uv = cube.getUv();
                List<Float> size = cube.getSize();
                boolean mirror = cube.isMirror();
                float inflate = cube.getInflate();

                model.cubes.add(new BedrockCubeBox(uv.get(0), uv.get(1),
                        convertOrigin(bones, cube, 0), convertOrigin(bones, cube, 1), convertOrigin(bones, cube, 2),
                        size.get(0), size.get(1), size.get(2), inflate, mirror,
                        texWidth, texHeight));
            }
        }
    }

    public default void setRotationAngle(BedrockPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public default float convertPivot(BonesItem bones, int index) {
        if (bones.getParent() != null) {
            if (index == 1) {
                return getIndexBones().get(bones.getParent()).getPivot().get(index) - bones.getPivot().get(index);
            } else {
                return bones.getPivot().get(index) - getIndexBones().get(bones.getParent()).getPivot().get(index);
            }
        } else {
            if (index == 1) {
                return 24 - bones.getPivot().get(index);
            } else {
                return bones.getPivot().get(index);
            }
        }
    }

    public default float convertPivot(BonesItem parent, CubesItem cube, int index) {
        assert cube.getPivot() != null;
        if (index == 1) {
            return parent.getPivot().get(index) - cube.getPivot().get(index);
        } else {
            return cube.getPivot().get(index) - parent.getPivot().get(index);
        }
    }

    public default float convertOrigin(BonesItem bone, CubesItem cube, int index) {
        if (index == 1) {
            return bone.getPivot().get(index) - cube.getOrigin().get(index) - cube.getSize().get(index);
        } else {
            return cube.getOrigin().get(index) - bone.getPivot().get(index);
        }
    }

    public default float convertOrigin(CubesItem cube, int index) {
        assert cube.getPivot() != null;
        if (index == 1) {
            return cube.getPivot().get(index) - cube.getOrigin().get(index) - cube.getSize().get(index);
        } else {
            return cube.getOrigin().get(index) - cube.getPivot().get(index);
        }
    }

    public default BedrockPart getChild(String partName) {
        return this.getModelMap().get(partName);
    }
}
