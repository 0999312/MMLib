package cn.mcmod_mmf.mmlib.item;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;

public class ItemFoodSeeds extends ItemNameBlockItem implements IFoodLike{
    private final FoodInfo info;
    public ItemFoodSeeds(Block block, Item.Properties prop, FoodInfo info) {
        super(block, prop);
        this.info = info;
    }
    
    @Override
    public boolean isEdible() {
        return this.info != null;
    }
    
    @Override
    public FoodProperties getFoodProperties() {
        FoodProperties.Builder food = new FoodProperties.Builder().nutrition(getFoodInfo().getAmount()).saturationMod(getFoodInfo().getCalories());
        if (getFoodInfo().isAlwaysEat())
            food.alwaysEat();
        if (getFoodInfo().getEatTime() <= 16)
            food.fast();
        this.getFoodInfo().getEffects().forEach((k) -> food.effect(k.getFirst(), k.getSecond()));

        return food.build();
    }
    
    @Override
    public FoodInfo getFoodInfo() {
        return info;
    }
}
