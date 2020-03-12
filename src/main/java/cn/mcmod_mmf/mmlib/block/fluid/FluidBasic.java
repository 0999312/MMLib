package cn.mcmod_mmf.mmlib.block.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidBasic extends Fluid {
	public FluidBasic(String modid, String name) {
		super(modid + "." + name, new ResourceLocation(modid, "blocks/" + name + "_still"),
				new ResourceLocation(modid, "blocks/" + name + "_flow"));
	}

}