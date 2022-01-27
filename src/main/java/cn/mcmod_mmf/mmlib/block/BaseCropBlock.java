package cn.mcmod_mmf.mmlib.block;

import java.util.function.Supplier;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BaseCropBlock extends CropBlock {
    private final Supplier<? extends ItemLike> seedItem;

    public BaseCropBlock(Properties proper, Supplier<? extends ItemLike> seed) {
        super(proper);
        this.seedItem = seed;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return this.seedItem.get();
    }

    public BlockState withAge(int age) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), age);
    }
}
