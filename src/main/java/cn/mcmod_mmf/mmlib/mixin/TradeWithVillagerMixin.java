package cn.mcmod_mmf.mmlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cn.mcmod_mmf.mmlib.utils.VillagerUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.TradeWithVillager;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;

@Mixin(value = TradeWithVillager.class)
public abstract class TradeWithVillagerMixin {
	@Inject(method = "tick", at = @At(value = "TAIL"))
	private void onTick(ServerLevel level, Villager owner, long gameTime, CallbackInfo ci) {
		Villager villager = (Villager)owner.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        if (!(owner.distanceToSqr(villager) > 5.0)) {
            if (owner.hasExcessFood() && (owner.getVillagerData().getProfession() == VillagerProfession.FARMER || villager.wantsMoreFood())) {
            	TradeWithVillagerInvoker.throwHalfStackInvoker(owner, VillagerUtils.getFoodPoints().keySet(), villager);
            }
    	}
	}
}
