package cn.mcmod_mmf.mmlib.item;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;

public class ItemDrinkBase extends ItemFoodBase {

	public ItemDrinkBase(Item.Properties prop,FoodInfo info) {
		super(prop, info);
	}
	@Override
	public Food getFoodProperties() {
		return super.getFoodProperties();
	}
	@Override
	public UseAction getUseAnimation(ItemStack p_77661_1_) {
		return p_77661_1_.getItem().isEdible() ? UseAction.DRINK : UseAction.NONE;
	}
}
