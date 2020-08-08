package cn.mcmod_mmf.mmlib.util;

import javax.annotation.Nullable;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

public final class WorldUtil {
	private static final WorldUtil instance = new WorldUtil();
	private WorldUtil() {
		// TODO Auto-generated constructor stub
	}
    public boolean isItemFuel(ItemStack stack){
        return TileEntityFurnace.getItemBurnTime(stack) > 0;
    }
    
    @Nullable
    public BlockPos.MutableBlockPos findGround(World world, BlockPos pos, boolean ignoreLeaves, boolean stopOnFluid, boolean useWorldHeight){
        return findGround(world, pos, ignoreLeaves, stopOnFluid, useWorldHeight, 8);
    }

    @Nullable
    public BlockPos.MutableBlockPos findGround(World world, BlockPos pos, boolean ignoreLeaves, boolean stopOnFluid, boolean useWorldHeight, int offset) {
        if (useWorldHeight)
            pos = world.getHeight(pos);
        if (pos.getY() <= 4) {
            return null;
        }
        BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos(pos);
        if (position.getY() > 0){
            int yOrigin = position.getY();
            do {
                IBlockState state = world.getBlockState(position);
                if (stopOnFluid && (state.getBlock() instanceof BlockLiquid || state.getBlock() instanceof IFluidBlock))
                   return position.move(EnumFacing.UP);

                if (!state.getBlock().isReplaceable(world, position) && (!ignoreLeaves || !state.getBlock().isLeaves(state, world, position)))
                    return position.move(EnumFacing.UP);
            }
            while (yOrigin - position.getY() < 40 && position.move(EnumFacing.DOWN).getY() > 0);
        }
        return null;
    }
	public static WorldUtil getInstance() {
		return instance;
	}

}
