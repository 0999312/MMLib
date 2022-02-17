package cn.mcmod_mmf.mmlib.fluid;

import java.util.function.Consumer;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class ConsumerFluidTank extends FluidTank {

	private Consumer<FluidStack> updateCallback;

	public ConsumerFluidTank(int capacity, Consumer<FluidStack> updateCallback) {
		super(capacity);
		this.updateCallback = updateCallback;
	}

	@Override
	protected void onContentsChanged() {
		super.onContentsChanged();
		updateCallback.accept(getFluid());
	}

	@Override
	public void setFluid(FluidStack stack) {
		super.setFluid(stack);
		updateCallback.accept(stack);
	}

}
