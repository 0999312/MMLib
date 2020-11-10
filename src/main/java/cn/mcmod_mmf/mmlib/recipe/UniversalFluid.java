package cn.mcmod_mmf.mmlib.recipe;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class UniversalFluid {
	private static final UniversalFluid instance = new UniversalFluid();
	private UniversalFluid() {
		
	}
	public static UniversalFluid getInstance() {
		return instance;
	}
	private final Map<String, List<Fluid>> FLUID_MAP = Maps.newHashMap();
	private final Map<String, List<Fluid>> READONLY_MAP = Collections.unmodifiableMap(FLUID_MAP);

	public void addFluid(String name, Fluid... input) {
		if (FLUID_MAP.containsKey(name)) {
			for(Fluid fluid : input)
			FLUID_MAP.get(name).add(fluid);
			
			return;
		}
		List<Fluid> fluids = Lists.newArrayList(input[0]);
		for(int i=1;i<input.length;i++) {
			fluids.add(input[i]);
		}
		FLUID_MAP.put(name, fluids);
	}

	public void removeFluid(String name, Fluid input) {
		FLUID_MAP.get(name).remove(input);
	}
	public void removeFluid(String name, FluidStack input) {
		removeFluid(name, input.getFluid());
	}
	public void removeFluidList(String name) {
		FLUID_MAP.get(name).clear();
	}
	public void clearAll() {
		FLUID_MAP.clear();
	}
	
	public Map<String, List<Fluid>> getFluidMap() {
		return READONLY_MAP;
	}
	
}
