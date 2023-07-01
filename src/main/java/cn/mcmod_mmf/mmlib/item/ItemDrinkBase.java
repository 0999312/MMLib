package cn.mcmod_mmf.mmlib.item;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class ItemDrinkBase extends ItemFoodBase {

    public ItemDrinkBase(Item.Properties prop, FoodInfo info) {
        super(prop, info);
    }

    @Override
    public FoodProperties getFoodProperties() {
        return super.getFoodProperties();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
    
    @Override
    public FoodInfo getFoodInfo() {
        return super.getFoodInfo();
    }
}
