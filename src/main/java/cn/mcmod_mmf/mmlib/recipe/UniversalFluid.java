package cn.mcmod_mmf.mmlib.recipe;

import java.util.List;

import com.google.common.collect.Lists;

import cn.mcmod_mmf.mmlib.Main;
import cn.mcmod_mmf.mmlib.register.MMLibRegistries;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class UniversalFluid extends Impl<UniversalFluid> {

	public static UniversalFluid get(String name) {
        String modid;
        String path;
        
        String str[] = name.split(":",2);
        if(str.length == 2){
            modid = str[0];
            path = str[1];
        }else{
            modid = Main.MODID;
            path = name;
        }
        
		return UniversalFluid.get(modid,path);
	}

	public static UniversalFluid get(String modid,String name) {
		
		return UniversalFluid.get(new ResourceLocation(modid,name));
	}

	
	public static UniversalFluid get(ResourceLocation name) {
		try {
			if (MMLibRegistries.UNIVERSAL_FLUID.getValue(name) == null) {
				throw new RuntimeException(String.format("We DO NOT have this fluid:%s , Maybe next time.", name.toString()));
			}
			
		} catch (Exception e) {
			Main.getLogger().fatal("Fatal error! This is likely a programming mistake.", e);
			throw new RuntimeException(e);
		}
		return MMLibRegistries.UNIVERSAL_FLUID.getValue(name);
	}
	private final NonNullList<Fluid> fluidList = NonNullList.create();
	
	public UniversalFluid() {
		
	}
	
	public UniversalFluid(Fluid... fluids) {
		for(Fluid fluid:fluids) {
			this.fluidList.add(fluid);
		}
	}
	
	public NonNullList<Fluid> getFluidList() {
		return fluidList;
	}
	
	public void addFluid(Fluid... fluids) {
		for(Fluid fluid:fluids) {
			addFluid(fluid);
		}
	}
	
	public void addFluid(Fluid fluid) {
		this.fluidList.add(fluid);
	}
	
	public void removeFluid(Fluid... fluids) {
		for(Fluid fluid:fluids) {
			removeFluid(fluid);
		}
	}
	
	public void removeFluid(Fluid fluid) {
		this.fluidList.remove(fluid);
	}
	
	public boolean hasFluid(Fluid target) {
		boolean flag = false;
		for(Fluid fluid : getFluidList()) {
			if(fluid.equals(target)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	public List<FluidStack> getFluidList(int amount) {
		List<FluidStack> list = Lists.newArrayList();
		fluidList.forEach((k) -> list.add(new FluidStack(k, amount)));
		return list;
	}
	
}
