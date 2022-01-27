package cn.mcmod_mmf.mmlib.block;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

public class HighCropBlock extends BaseCropBlock {
    public static final BooleanProperty UPPER = BooleanProperty.create("upper");
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { 
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), 
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D), 
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),  
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),  
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D), 
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };
    public HighCropBlock(Properties proper, Supplier<? extends ItemLike> seed) {
        super(proper, seed);
        this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0).setValue(UPPER, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, UPPER);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        
        return context.getLevel().getBlockState(pos.below()).is(this)
                ? super.getStateForPlacement(context).setValue(UPPER, true)
                : super.getStateForPlacement(context);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return (worldIn.getRawBrightness(pos, 0) >= 8 || worldIn.canSeeSky(pos))
                && ((worldIn.getBlockState(pos.below()).is(this) 
                && !worldIn.getBlockState(pos.below()).getValue(UPPER) 
                && this.getAge(worldIn.getBlockState(pos.below()))>= 3)
                || worldIn.getBlockState(pos.below()).is(Blocks.FARMLAND));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return (state.is(this) && !state.getValue(UPPER) && this.getAge(state)>= 3) || state.is(Blocks.FARMLAND);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        super.randomTick(state, worldIn, pos, rand);
        if (state.getValue(UPPER)) {
            return;
        }
        int age = this.getAge(state);
        if (age >= 3 && age <= this.getMaxAge()) {
            float chance = 10;
            if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25.0F / chance) + 1) == 0)) {

            }
            if (this.defaultBlockState().canSurvive(worldIn, pos.above()) && worldIn.isEmptyBlock(pos.above())) {
                worldIn.setBlockAndUpdate(pos.above(), this.defaultBlockState().setValue(UPPER, true));
                ForgeHooks.onCropsGrowPost(worldIn, pos, state);
            }
        }
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
        BlockState upperState = worldIn.getBlockState(pos.above());
        if (upperState.is(this)) {
            return !(this.isMaxAge(upperState));
        }
        if (state.getValue(UPPER)) {
            return !(this.isMaxAge(state));
        }
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level worldIn, Random rand, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel worldIn, Random rand, BlockPos pos, BlockState state) {
        int ageGrowth = Math.min(this.getAge(state) + this.getBonemealAgeIncrease(worldIn), 15);
        if (ageGrowth <= this.getMaxAge()) {
            worldIn.setBlockAndUpdate(pos, state.setValue(AGE, ageGrowth));
        } else {
            worldIn.setBlockAndUpdate(pos, state.setValue(AGE, this.getMaxAge()));
            if (state.getValue(UPPER)) {
                return;
            }
            BlockState top = worldIn.getBlockState(pos.above());
            if (top.is(this)) {
                BonemealableBlock growable = (BonemealableBlock) worldIn.getBlockState(pos.above()).getBlock();
                if (growable.isValidBonemealTarget(worldIn, pos.above(), top, false)) {
                    growable.performBonemeal(worldIn, worldIn.random, pos.above(), top);
                }
            } else {
                int remainingGrowth = ageGrowth - this.getMaxAge() - 1;
                if (this.defaultBlockState().canSurvive(worldIn, pos.above()) && worldIn.isEmptyBlock(pos.above())) {
                    worldIn.setBlock(pos.above(), this.defaultBlockState().setValue(UPPER, true).setValue(this.getAgeProperty(), remainingGrowth), 2);
                }
            }
        }
    }

}
