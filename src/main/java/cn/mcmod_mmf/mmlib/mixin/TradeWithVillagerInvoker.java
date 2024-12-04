package cn.mcmod_mmf.mmlib.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.TradeWithVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;

@Mixin(value = TradeWithVillager.class)
public interface TradeWithVillagerInvoker {
	@Invoker(value = "throwHalfStack")
	public static void throwHalfStackInvoker(Villager villager, Set<Item> stack, LivingEntity entity) {
		throw new AssertionError();
	}
}
