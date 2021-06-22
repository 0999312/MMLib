package cn.mcmod_mmf.mmlib.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class TileEntityMachineBase extends TileEntityEnergyBase implements ITickableTileEntity {

	public TileEntityMachineBase(TileEntityType<?> p_i48289_1_) {
		super(p_i48289_1_);
	}

	protected int processTime;
	protected int maxTime;

	/*
	 * The working condition for a machine, e.g. a proper recipe
	 */
	public abstract boolean canWork();

	/*
	 * What will the machine do when working, like draining energy, or something
	 * else
	 */
	public abstract void onWork();

	/*
	 * What will the machine do if the working condition is not met, like resetting
	 * working status
	 */
	public abstract void failed();

	/*
	 * What will the machine do if a working cycle is finished, like output items.
	 */
	public abstract void done();

	/*
	 * A general function for the machine, if there's anything needed
	 */
	public void general() {
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = super.getUpdateTag();
		tag.putInt("processTime", this.processTime);
		tag.putInt("totalTime", this.maxTime);

		return tag;
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		super.handleUpdateTag(state, tag);
		this.processTime = tag.getInt("processTime");
		this.maxTime = tag.getInt("totalTime");
	}

	@Override
	public void tick() {
		if (this.level != null && !this.level.isClientSide) {
			if (canWork()) {
				onWork();
				if (processTime >= maxTime) {
					processTime = 0;
					done();
				}
			} else
				failed();
			general();
		}
	}

	public int getMaxTime() {
		return maxTime;
	}

}
