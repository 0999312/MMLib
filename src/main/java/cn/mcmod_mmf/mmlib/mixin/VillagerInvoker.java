package cn.mcmod_mmf.mmlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.npc.Villager;

@Mixin(value = Villager.class)
public interface VillagerInvoker {
	
	@Invoker(value = "countFoodPointsInInventory")
	public int countFoodPointsInInventoryInvoker();
	
	@Invoker(value = "hungry")
	public boolean hungryInvoker();

}
