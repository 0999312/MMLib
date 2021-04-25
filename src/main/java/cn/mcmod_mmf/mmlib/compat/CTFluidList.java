package cn.mcmod_mmf.mmlib.compat;

import cn.mcmod_mmf.mmlib.recipe.UniversalFluid;
import cn.mcmod_mmf.mmlib.register.MMLibRegistries;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mmlib.universalFluidList")
@ZenRegister
public class CTFluidList {
	@ZenMethod
	public static void RemoveFluid(String name, ILiquidStack input_fluid) {
		CTCompat.getInstance().actions.add(new Removal(name, CraftTweakerMC.getLiquidStack(input_fluid)));
	}

	@ZenMethod
	public static void AddFluid(String name, ILiquidStack input_fluid) {
		CTCompat.getInstance().actions.add(new Addition(name, CraftTweakerMC.getLiquidStack(input_fluid).getFluid()));
	}

	@ZenMethod
	public static void AddFluids(String name, ILiquidStack[] input_fluid) {
		FluidStack[] fluids = CraftTweakerMC.getLiquidStacks(input_fluid);
		for(int i=0;i<fluids.length;i++) {
			CTCompat.getInstance().actions.add(new Addition(name, fluids[i].getFluid()));
		}
	}

	@ZenMethod
	public static void ClearFluidMap(String name) {
		CTCompat.getInstance().actions.add(new ClearFluidList(name));
	}

	private static final class Removal implements IAction {
		private final String name;
		private final FluidStack itemInput;

		private Removal(String name, FluidStack itemInput) {
			this.name = name;
			this.itemInput = itemInput;
		}

		@Override
		public void apply() {
			MMLibRegistries.UNIVERSAL_FLUID.getValue(new ResourceLocation(name)).removeFluid(itemInput.getFluid());
		}

		@Override
		public String describe() {
			return "Removing fluid "+itemInput.getLocalizedName()+" from universal fluid "+name;
		}
	}

	private static final class Addition implements IAction {
		private final String name;
		private final Fluid[] itemInput;

		private Addition(String name, Fluid itemInput) {
			this.name = name;
			this.itemInput = new Fluid[] {itemInput};
		}
		
		private Addition(String name, Fluid[] itemInput) {
			this.name = name;
			this.itemInput = itemInput;
		}

		@Override
		public void apply() {
			if(!MMLibRegistries.UNIVERSAL_FLUID.containsKey(new ResourceLocation(name)))
				MMLibRegistries.UNIVERSAL_FLUID.register(new UniversalFluid().setRegistryName(name));
			UniversalFluid.get(name).addFluid(itemInput);
		}

		@Override
		public String describe() {
			return "Adding fluids "+itemInput[0].getName()+" for universal fluid"+name;
		}
	}

	private static final class ClearFluidList implements IAction {
		private final String name;

		private ClearFluidList(String name) {
			this.name = name;
		}

		@Override
		public void apply() {
			MMLibRegistries.UNIVERSAL_FLUID.getValue(new ResourceLocation(name)).getFluidList().clear();
		}

		@Override
		public String describe() {
			return "Removing all fluid in universal fluid" + name;
		}
	}

}
