package cn.mcmod_mmf.mmlib.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBase extends Block {
	public final boolean isFull;
	private boolean isBase;
	public BlockBase(Material material, boolean isfull) {
		super(material);
		isFull=isfull;
	}

	public BlockBase setBeaconBase(boolean isBase) {
		this.isBase = isBase;
		return this;
	}
	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
		return isBase;
	}
	@Override
	public Block setSoundType(SoundType sound) {
		return super.setSoundType(sound);
	}
	@Override
	public boolean isFullCube(IBlockState state) {
		return isFull;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return isFull?BlockRenderLayer.SOLID:BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return isFull;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return isFull;
	}


}
