package cn.mcmod_mmf.mmlib.item;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;

public class ItemRegister {
	
	public static void RegisterFoods(DeferredRegister<Item> register,FoodInfo... infos) {
		for(FoodInfo info : infos) {
			Food.Builder food = new Food.Builder().nutrition(info.getAmount()).saturationMod(info.getCalories());
			if(info.isAlwaysEat())
				food.alwaysEat();
			if(info.isFastEat())
				food.fast();
			register.register(info.getName(), ()->new ItemFoodBase(new Item.Properties().food(food.build())));
		}
	}
	
}
