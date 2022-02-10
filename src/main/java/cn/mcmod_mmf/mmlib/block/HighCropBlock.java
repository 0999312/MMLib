package cn.mcmod_mmf.mmlib.block;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

@SuppressWarnings("deprecation")
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
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(UPPER, false));
    }
    
    public BooleanProperty getUpperProperty() {
        return UPPER;
    }
    
    public int getGrowUpperAge() {
        return 3;
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
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos downpos = pos.below();   
        if(worldIn.getBlockState(downpos).is(this))
            return  !worldIn.getBlockState(downpos).getValue(this.getUpperProperty())
                    && (worldIn.getRawBrightness(pos, 0) >= 8 || worldIn.canSeeSky(pos))
                    && this.getAge(worldIn.getBlockState(downpos))>= this.getGrowUpperAge();
        return super.canSurvive(state, worldIn, pos);
    }
    
    @Override
    public BlockState getStateForAge(int age) {
        return super.getStateForAge(age);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        float f = getGrowthSpeed(this, worldIn, pos);
        int age = this.getAge(state);
        if (worldIn.getRawBrightness(pos, 0) >= 9) {
           if (age < this.getMaxAge()) {
              if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0)) {
                  worldIn.setBlock(pos, this.getStateForAge(age + 1).setValue(this.getUpperProperty(), state.getValue(this.getUpperProperty())), 2);
                 ForgeHooks.onCropsGrowPost(worldIn, pos, state);
              }
           }
        }
        if(state.getValue(this.getUpperProperty()))
            return;
        if (age >= this.getGrowUpperAge()) {
            if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0)) {
                if (this.defaultBlockState().canSurvive(worldIn, pos.above()) && worldIn.isEmptyBlock(pos.above())) {
                    worldIn.setBlockAndUpdate(pos.above(), this.defaultBlockState().setValue(this.getUpperProperty(), true));
                    ForgeHooks.onCropsGrowPost(worldIn, pos, state);
                }
            }
        }
    }
    @Override
    public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
        BlockState upperState = worldIn.getBlockState(pos.above());
        if (upperState.is(this)) {
            return !(this.isMaxAge(upperState));
        }
        if (state.getValue(this.getUpperProperty())) {
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
            if (state.getValue(this.getUpperProperty())) {
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
                    worldIn.setBlock(pos.above(), this.defaultBlockState()
                            .setValue(this.getUpperProperty(), true)
                            .setValue(this.getAgeProperty(), remainingGrowth), 3);
                }
            }
        }
    }

}
