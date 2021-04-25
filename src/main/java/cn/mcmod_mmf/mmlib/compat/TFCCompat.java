package cn.mcmod_mmf.mmlib.compat;

import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import cn.mcmod_mmf.mmlib.item.food.ItemFoodBase;
import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import cn.mcmod_mmf.mmlib.item.info.HeatInfo;
import cn.mcmod_mmf.mmlib.item.info.MetalItemInfo;
import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.capability.food.FoodData;
import net.dries007.tfc.api.capability.food.FoodHandler;
import net.dries007.tfc.api.capability.forge.CapabilityForgeable;
import net.dries007.tfc.api.capability.forge.ForgeableHeatableHandler;
import net.dries007.tfc.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.heat.ItemHeatHandler;
import net.dries007.tfc.api.capability.metal.CapabilityMetalItem;
import net.dries007.tfc.api.capability.metal.MetalItemHandler;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional.Method;

public class TFCCompat {
	private static final TFCCompat INSTANCE = new TFCCompat();

	private TFCCompat() {
	}

	public static TFCCompat getInstance() {
		return INSTANCE;
	}

	public List<Pair<ItemStack, FoodInfo>> FOOD_INFO_MAP = Lists.newArrayList();
	public List<Pair<ItemStack, HeatInfo>> HEAT_INFO_MAP = Lists.newArrayList();
	public List<Pair<ItemStack, MetalItemInfo>> METAL_ITEM_INFO_MAP = Lists.newArrayList();
	
    @Method(modid="tfc")
	public void addMetalItemInfo(List<ItemStack> stackList, String metal, int amount, boolean melt) {
		for (ItemStack stack : stackList) {
			if (stack.getItem().getRegistryName().getResourceDomain().equalsIgnoreCase("tfc"))
				continue;
			METAL_ITEM_INFO_MAP.add(Pair.of(stack, new MetalItemInfo(metal, amount, melt)));
		}
	}
    @Method(modid="tfc")
	public void addMetalItemInfo(ItemStack stack, String metal, int amount, boolean melt) {
    	METAL_ITEM_INFO_MAP.add(Pair.of(stack, new MetalItemInfo(metal, amount, melt)));
	}
    @Method(modid="tfc")
	public void addMetalItemInfo(ItemStack stack, MetalItemInfo info) {
    	METAL_ITEM_INFO_MAP.add(Pair.of(stack, info));
	}
	
    @Method(modid="tfc")
	public void addHeatInfo(List<ItemStack> stackList, boolean forgeable, float heatCapacity, float cookingTemp) {
		for (ItemStack stack : stackList) {
			if (stack.getItem().getRegistryName().getResourceDomain().equalsIgnoreCase("tfc"))
				continue;
			HEAT_INFO_MAP.add(Pair.of(stack, new HeatInfo(heatCapacity, cookingTemp, forgeable)));
		}
	}
    @Method(modid="tfc")
	public void addHeatInfo(ItemStack stack, boolean forgeable, float heatCapacity, float cookingTemp) {
		HEAT_INFO_MAP.add(Pair.of(stack, new HeatInfo(heatCapacity, cookingTemp, forgeable)));
	}
    @Method(modid="tfc")
	public void addHeatInfo(ItemStack stack, HeatInfo info) {
		HEAT_INFO_MAP.add(Pair.of(stack, info));
	}
    @Method(modid="tfc")
	public void addFoodInfo(List<ItemStack> stackList, int amount, float calories, float water, float grain,
			float fruit, float veg, float meat, float dairy, float decayModifier, float heatCapacity,
			float cookingTemp) {
		for (ItemStack stack : stackList) {
			if (stack.getItem().getRegistryName().getResourceDomain().equalsIgnoreCase("tfc")
					|| stack.getItem() instanceof ItemFoodBase
					|| !(stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemSeedFood))
				continue;
			FOOD_INFO_MAP.add(Pair.of(stack, new FoodInfo(stack.getUnlocalizedName().substring(5), amount, calories,
					false, water, grain, fruit, veg, meat, dairy, decayModifier, heatCapacity, cookingTemp)));
		}
	}
    @Method(modid="tfc")
	public void addFoodInfo(ItemStack stack, int amount, float calories, float water, float grain, float fruit,
			float veg, float meat, float dairy, float decayModifier, float heatCapacity, float cookingTemp) {
		FOOD_INFO_MAP.add(Pair.of(stack, new FoodInfo(stack.getUnlocalizedName().substring(5), amount, calories, false,
				water, grain, fruit, veg, meat, dairy, decayModifier, heatCapacity, cookingTemp)));
	}
    @Method(modid="tfc")
	public void addFoodInfo(ItemStack stack, FoodInfo info) {
		FOOD_INFO_MAP.add(Pair.of(stack, info));
	}
    @Method(modid="tfc")
	public void addInfo() {
		if (!Loader.isModLoaded("tfc"))
			return;

		FOOD_INFO_MAP.forEach((entry) -> {
	        if (CapabilityFood.CUSTOM_FOODS.get(IIngredient.of(entry.getKey())) != null)
	        	return;
			CapabilityFood.CUSTOM_FOODS.put(IIngredient.of(entry.getLeft()),
					() -> new FoodHandler(null,
							new FoodData(entry.getRight().getAmount(), entry.getRight().getWater(),
									entry.getRight().getCalories(), entry.getRight().getNutrients(),
									entry.getRight().getDecayModifier())));
		});
		HEAT_INFO_MAP.forEach((entry) -> {
	        if (CapabilityForgeable.CUSTOM_ITEMS.get(IIngredient.of(entry.getKey())) != null)
	        	return;
			if (entry.getRight().isForgeable()) {
				CapabilityForgeable.CUSTOM_ITEMS.put(IIngredient.of(entry.getLeft()),
						() -> new ForgeableHeatableHandler(null, entry.getRight().getHeatCapacity(),
								entry.getRight().getMeltTemp()));
			} else {
				CapabilityItemHeat.CUSTOM_ITEMS.put(IIngredient.of(entry.getLeft()), () -> new ItemHeatHandler(null,
						entry.getRight().getHeatCapacity(), entry.getRight().getMeltTemp()));
			}
		});
		METAL_ITEM_INFO_MAP.forEach((entry) -> {
			if (CapabilityMetalItem.CUSTOM_METAL_ITEMS.get(IIngredient.of(entry.getLeft())) != null)
	        	return;
	        Metal metal = TFCRegistries.METALS.getValuesCollection().stream()
	                .filter(x -> x.getRegistryName().getResourcePath().equalsIgnoreCase(entry.getValue().getMetal())).findFirst().orElse(null);
	            if (metal == null)
	                throw new IllegalArgumentException("Metal specified not found!");
			CapabilityMetalItem.CUSTOM_METAL_ITEMS.put(IIngredient.of(entry.getLeft()),
					() -> new MetalItemHandler(metal, entry.getValue().getAmount(), entry.getValue().isCanMelt()));
		});
	}
}
