package cn.mcmod_mmf.mmlib.utils;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;

public class VillagerUtils {
	private static final Map<Item, Integer> FOOD_POINTS = Maps.newHashMap(Villager.FOOD_POINTS);
	
	public static void addFoodToVillage(Item item, int food) {
		FOOD_POINTS.put(item, food);
	}
	
	public static Map<Item, Integer> getFoodPoints(){
		return ImmutableMap.copyOf(FOOD_POINTS);
	}
}
