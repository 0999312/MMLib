package cn.mcmod_mmf.mmlib.compat;

import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import cn.mcmod_mmf.mmlib.item.food.FoodInfo;
import cn.mcmod_mmf.mmlib.item.food.ItemFoodBase;
import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.capability.food.FoodData;
import net.dries007.tfc.api.capability.food.FoodHandler;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class TFCCompat {
	private static final TFCCompat INSTANCE = new TFCCompat();

	private TFCCompat() {
	}

	public static TFCCompat getInstance() {
		return INSTANCE;
	}

	public List<Pair<ItemStack, FoodInfo>> FOOD_INFO_MAP = Lists.newArrayList();

	public void addFoodInfo(List<ItemStack> stackList, int amount, float calories, float water, float grain,
			float fruit, float veg, float meat, float dairy, float decayModifier, float heatCapacity,
			float cookingTemp) {
		for (ItemStack stack : stackList) {
			if (stack.getItem().getRegistryName().getResourceDomain().equalsIgnoreCase("tfc")
					|| stack.getItem() instanceof ItemFoodBase ||!(stack.getItem() instanceof ItemFood||stack.getItem() instanceof ItemSeedFood))
				continue;
			FOOD_INFO_MAP.add(Pair.of(stack, new FoodInfo(stack.getUnlocalizedName().substring(5), amount, calories,
					false, water, grain, fruit, veg, meat, dairy, decayModifier, heatCapacity, cookingTemp)));
		}
	}

	public void addFoodInfo(ItemStack stack, int amount, float calories, float water, float grain, float fruit,
			float veg, float meat, float dairy, float decayModifier, float heatCapacity, float cookingTemp) {
		FOOD_INFO_MAP.add(Pair.of(stack, new FoodInfo(stack.getUnlocalizedName().substring(5), amount, calories, false,
				water, grain, fruit, veg, meat, dairy, decayModifier, heatCapacity, cookingTemp)));
	}

	public void addFoodInfo(ItemStack stack, FoodInfo info) {
		FOOD_INFO_MAP.add(Pair.of(stack, info));
	}

	public void addInfo() {
		if (!Loader.isModLoaded("tfc"))
			return;

		FOOD_INFO_MAP.forEach((entry) -> {
			CapabilityFood.CUSTOM_FOODS.put(IIngredient.of(entry.getLeft()),
					() -> new FoodHandler(null,
							new FoodData(entry.getRight().getAmount(), entry.getRight().getWater(),
									entry.getRight().getCalories(), entry.getRight().getNutrients(),
									entry.getRight().getDecayModifier())));
		});
	}
}
