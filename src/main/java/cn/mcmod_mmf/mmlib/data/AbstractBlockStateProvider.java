package cn.mcmod_mmf.mmlib.data;

import java.util.function.Supplier;

import cn.mcmod_mmf.mmlib.Main;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class AbstractBlockStateProvider extends BlockStateProvider {

    public AbstractBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    public ResourceLocation texture(String name) {
        return modLoc("block/" + name);
    }

    public String name(Supplier<? extends Block> block) {
        return block.get().getRegistryName().getPath();
    }

    public void block(Supplier<? extends Block> block) {
        simpleBlock(block.get());
    }

    public void log(Supplier<? extends RotatedPillarBlock> block) {
        logBlock(block.get());
    }

    public void log(Supplier<? extends RotatedPillarBlock> block, String name) {
        axisBlock(block.get(), texture(name));
    }

    private void crossBlock(Supplier<? extends Block> block, ModelFile model) {
        getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(model).build());
    }

    public void torchBlock(Supplier<? extends Block> block, Supplier<? extends Block> wall) {
        ModelFile torch = models().torch(name(block), texture(name(block)));
        ModelFile torchwall = models().torchWall(name(wall), texture(name(block)));
        simpleBlock(block.get(), torch);
        getVariantBuilder(wall.get()).forAllStates(state -> ConfiguredModel.builder().modelFile(torchwall)
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 90) % 360).build());
    }

    public void crossBlock(Supplier<? extends Block> block) {
        crossBlock(block, models().cross(name(block), texture(name(block))));
    }

    public void stairs(Supplier<? extends StairBlock> block, Supplier<? extends Block> fullBlock) {
        stairsBlock(block.get(), texture(name(fullBlock)));
    }

    public void slab(Supplier<? extends SlabBlock> block, Supplier<? extends Block> fullBlock) {
        slabBlock(block.get(), texture(name(fullBlock)), texture(name(fullBlock)));
    }

    public void fence(Supplier<? extends FenceBlock> block, Supplier<? extends Block> fullBlock) {
        fenceBlock(block.get(), texture(name(fullBlock)));
        fenceColumn(block, name(fullBlock));
    }

    private void fenceColumn(Supplier<? extends FenceBlock> block, String side) {
        String baseName = block.get().getRegistryName().toString();
        fourWayBlock(block.get(), models().fencePost(baseName + "_post", texture(side)),
                models().fenceSide(baseName + "_side", texture(side)));
    }

    public void door(Supplier<? extends DoorBlock> block, String name) {
        doorBlock(block.get(), texture(name + "_door_bottom"), texture(name + "_door_top"));
    }

    public void trapdoor(Supplier<? extends TrapDoorBlock> block, String name) {
        trapdoorBlock(block.get(), texture(name + "_trapdoor"), true);
    }

    public void stageBlock(Supplier<? extends Block> block, IntegerProperty ageProperty, Property<?>... ignored) {
        getVariantBuilder(block.get()).forAllStatesExcept(state -> {
            int ageSuffix = state.getValue(ageProperty);
            String stageName = name(block) + "_stage" + ageSuffix;
            return ConfiguredModel.builder().modelFile(models().crop(stageName, texture(stageName))).build();
        }, ignored);
    }
    
    public void facingSlabBlock(Supplier<? extends SlabBlock> block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        facingSlabBlock(block, 
                models().withExistingParent(name(block), new ResourceLocation(Main.MODID, "block/facing_slab"))
                .texture("side", side)
                .texture("bottom", bottom)
                .texture("top", top), 
                models().withExistingParent(name(block) + "_top", new ResourceLocation(Main.MODID, "block/facing_slab_top"))
                .texture("side", side)
                .texture("bottom", bottom)
                .texture("top", top),
                models().withExistingParent(name(block) + "_double", new ResourceLocation(Main.MODID, "block/facing_block"))
                .texture("side", side)
                .texture("bottom", bottom)
                .texture("top", top)
        );
    }

    public void facingSlabBlock(Supplier<? extends SlabBlock> block, ModelFile bottom, ModelFile top, ModelFile doubleslab) {
        getVariantBuilder(block.get())
            .forAllStates(state -> ConfiguredModel.builder()
                .modelFile(
                        state.getValue(SlabBlock.TYPE) == SlabType.DOUBLE ? doubleslab : 
                            state.getValue(SlabBlock.TYPE) == SlabType.BOTTOM ? bottom : top)
                .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                .build()
            );
    }
}
