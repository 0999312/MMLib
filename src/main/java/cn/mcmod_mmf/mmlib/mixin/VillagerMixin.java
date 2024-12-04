package cn.mcmod_mmf.mmlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cn.mcmod_mmf.mmlib.utils.VillagerUtils;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@Mixin(value = Villager.class)
public abstract class VillagerMixin implements VillagerInvoker{
	@Inject(method = "wantsToPickUp", at = @At(value = "TAIL"), cancellable = true)
	private void onWantsToPickUp(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
		Item item = stack.getItem();
		boolean result = (VillagerUtils.getFoodPoints().containsKey(item)) && 
				((Villager) (Object) this).getInventory().canAddItem(stack);
		ci.setReturnValue(result);
	}

	@Inject(method = "countFoodPointsInInventory", at = @At(value = "TAIL"), cancellable = true)
	private void onCountFoodPointsInInventory(CallbackInfoReturnable<Integer> ci) {
		SimpleContainer simplecontainer = ((Villager) (Object) this).getInventory();
		int result = VillagerUtils.getFoodPoints().entrySet().stream()
				.mapToInt(p_186300_ -> simplecontainer.countItem(p_186300_.getKey()) * p_186300_.getValue()).sum();
		ci.setReturnValue(result);
	}

	@Shadow 
	private int foodLevel;
	
	@Inject(method = "eatUntilFull", at = @At(value = "HEAD"), cancellable = true)
	private void onEatUntilFull(CallbackInfo ci) {
		Villager villager = ((Villager) (Object) this);
        if (this.hungryInvoker() && this.countFoodPointsInInventoryInvoker() != 0) {
            for (int i = 0; i < villager.getInventory().getContainerSize(); i++) {
                ItemStack itemstack = villager.getInventory().getItem(i);
                if (!itemstack.isEmpty()) {
                    Integer integer = VillagerUtils.getFoodPoints().get(itemstack.getItem());
                    if (integer != null) {
                        int j = itemstack.getCount();

                        for (int k = j; k > 0; k--) {
                        	this.foodLevel = this.foodLevel + integer;
                        	villager.getInventory().removeItem(i, 1);
                            if (!this.hungryInvoker()) {
                                ci.cancel();
                            }
                        }
                    }
                }
            }
        }
	}
	

}
