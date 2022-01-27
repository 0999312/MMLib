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
    // TFC-TNG FoodHandler parameters
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

    private final List<Pair<Supplier<EffectInstance>, Float>> effects;
    
    public static FoodInfo.Builder builder() {
        return new FoodInfo.Builder();
    }
    
    private FoodInfo(FoodInfo.Builder builder) {
        this.name = builder.name;
        this.amount = builder.amount;
        this.calories = builder.calories;
        this.isWolfFood = builder.isWolfFood;
        this.isAlwaysEat = builder.isAlwaysEat;
        this.eatTime = builder.eatTime;
        this.effects = builder.effects;
        // TFC-TNG FoodHandler parameters
        this.water = builder.water;
        this.grain = builder.grain;
        this.fruit = builder.fruit;
        this.veg = builder.veg;
        this.meat = builder.meat;
        this.dairy = builder.dairy;
        this.decayModifier = builder.decayModifier;
        this.heatCapacity = builder.heatCapacity;
        this.cookingTemp = builder.cookingTemp;
        this.isNonDecaying = decayModifier <= 0;
        this.heatable = cookingTemp >= 0;

    }
    
    public static class Builder {
        private String name;
        private int amount;
        private float calories;
        private boolean isWolfFood = false;
        private boolean isAlwaysEat = false;
        private int eatTime = 32;
        // TFC-TNG FoodHandler parameters
        private float water;
        private float grain;
        private float fruit;
        private float veg;
        private float meat;
        private float dairy;
        private float decayModifier;
        private float heatCapacity;
        private float cookingTemp;
        
        private final List<Pair<Supplier<EffectInstance>, Float>> effects = Lists.newArrayList();
        
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder calories(float calories) {
            this.calories = calories;
            return this;
        }
        
        public Builder amountAndCalories(int amount, float calories) {
            this.amount(amount);
            this.calories(calories);
            return this;
        }

        public Builder wolfFood() {
            this.isWolfFood = true;
            return this;
        }

        public Builder alwaysEat() {
            this.isAlwaysEat = true;
            return this;
        }

        public Builder eatTime(int eatTime) {
            this.eatTime = eatTime;
            return this;
        }

        public Builder water(float water) {
            this.water = water;
            return this;
        }

        public Builder grain(float grain) {
            this.grain = grain;
            return this;
        }

        public Builder fruit(float fruit) {
            this.fruit = fruit;
            return this;
        }

        public Builder vegatable(float veg) {
            this.veg = veg;
            return this;
        }

        public Builder meat(float meat) {
            this.meat = meat;
            return this;
        }

        public Builder dairy(float dairy) {
            this.dairy = dairy;
            return this;
        }
        
        public Builder nutrients(float all) {
            this.grain(all).fruit(all).vegatable(all).grain(all).grain(all);
            return this;
        }
        
        public Builder nutrients(float grain,float fruit,float veg,float meat,float dairy) {
            this.grain(grain).fruit(fruit).vegatable(veg).grain(meat).grain(dairy);
            return this;
        }

        public Builder decayModifier(float decayModifier) {
            this.decayModifier = decayModifier;
            return this;
        }

        public Builder heatCapacity(float heatCapacity) {
            this.heatCapacity = heatCapacity;
            return this;
        }

        public Builder cookingTemp(float cookingTemp) {
            this.cookingTemp = cookingTemp;
            return this;
        }

        public Builder addEffect(Supplier<EffectInstance> effectIn, float probability) {
            this.effects.add(Pair.of(effectIn, probability));
            return this;
        }
        
        public FoodInfo build() {
            return new FoodInfo(this);
        }

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

    public List<Pair<Supplier<EffectInstance>, Float>> getEffects() {
        return effects;
    }

}
