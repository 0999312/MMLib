package cn.mcmod_mmf.mmlib.item.info;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;

import net.minecraft.potion.EffectInstance;

public class FoodInfo {
	private final String name;
	private final int amount;
	private final float calories;
	private final boolean isWolfFood;
	private final boolean isAlwaysEat;
	private final int eatTime;
	//TFC-TNG FoodHandler parameters
	private final float water;
	private final float grain;
	private final float fruit;
	private final float veg;
	private final float meat;
	private final float dairy;
	private final boolean isNonDecaying;
	private final float decayModifier;
	private final boolean heatable;
	private final float heatCapacity;
	private final float cookingTemp;
	
	private final List<Pair<Supplier<EffectInstance>, Float>> effects = Lists.newArrayList();
	
	public FoodInfo(String name,int amount,float calories,boolean iswolffood) {
		this(name,amount, calories, iswolffood, 1f, 1f,0f,0f,0f,0f,1f,0f,0f);
	}
	
	public FoodInfo(String name,int amount,float calories,boolean iswolffood,boolean isalways,int eatTime) {
		this(name,amount, calories, iswolffood, isalways, eatTime, 1f, 1f,0f,0f,0f,0f,1f,0f,0f);
	}
	
	public FoodInfo(String name,int amount,float calories,boolean iswolffood, float water, float grain, float fruit, float veg, float meat, float dairy, float decayModifier, float heatCapacity, float cookingTemp) {
		this(name,amount, calories, iswolffood, false, 32, water, grain, fruit, veg, meat, dairy, decayModifier, heatCapacity, cookingTemp);
	}
	
	public FoodInfo(String name,int amount,float calories,boolean iswolffood,boolean isalways,int time, float water, float grain, float fruit, float veg, float meat, float dairy, float decayModifier, float heatCapacity, float cookingTemp) {
		this.name = name;
		this.amount = amount;
		this.calories = calories;
		this.isWolfFood = iswolffood;
		this.isAlwaysEat = isalways;
		this.eatTime = time;
		
		//TFC-TNG FoodHandler parameters
		this.water = water;
		this.grain = grain;
		this.fruit = fruit;
		this.veg = veg;
		this.meat = meat;
		this.dairy = dairy;
		this.decayModifier = decayModifier;
		this.isNonDecaying = decayModifier <= 0;
		this.heatable = cookingTemp >= 0;
		this.heatCapacity = heatCapacity;
		this.cookingTemp = cookingTemp;
	}

	public int getAmount() {
		return amount;
	}
	
	public boolean isWolfFood() {
		return isWolfFood;
	}
	
	public float getCalories() {
		return calories;
	}

	public float getDecayModifier() {
		return decayModifier;
	}

	public float getWater() {
		return water;
	}
	
	public float[] getNutrients() {
		return new float[] { grain, fruit, veg, meat, dairy };
	}

	public boolean isHeatable() {
		return heatable;
	}
	
	public boolean isNeverDecay() {
		return isNonDecaying;
	}

	public float getHeatCapacity() {
		return heatCapacity;
	}

	public float getCookingTemp() {
		return cookingTemp;
	}

	public String getName() {
		return name;
	}

	public boolean isAlwaysEat() {
		return isAlwaysEat;
	}
	
	public int getEatTime() {
		return eatTime;
	}
	
	public FoodInfo addEffect(Supplier<EffectInstance> effectIn, float probability) {
		this.effects.add(Pair.of(effectIn, probability));
		return this;
	}
	
	public List<Pair<Supplier<EffectInstance>, Float>> getEffects() {
		return effects;
	}


}
