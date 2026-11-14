package cn.mcmod_mmf.mmlib.item;

import org.jetbrains.annotations.Nullable;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemFoodSeeds extends ItemNameBlockItem implements IFoodLike{
    private final FoodInfo info;
    public ItemFoodSeeds(Block block, Item.Properties prop, FoodInfo info) {
        super(block, prop.food( 
     		new FoodProperties(info.getAmount(), info.getCalories(), info.isAlwaysEat(), info.getEatTime(),  
     				Optional.empty(), List.of()) 
     		));
        this.info = info;
    }
    
	@Override
	public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
		FoodProperties.Builder food = new FoodProperties.Builder().nutrition(getFoodInfo().getAmount())
		      .saturationModifier(getFoodInfo().getCalories());
		if (getFoodInfo().isAlwaysEat())
		  food.alwaysEdible();
		if (getFoodInfo().getEatTime() <= 16)
		  food.fast();
		this.getFoodInfo().getEffects().forEach((k) -> food.effect(k.getFirst(), k.getSecond()));
		
		return food.build();
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity p_344979_) {
	    if (this.getFoodInfo() != null)
	    	return this.getFoodInfo().getEatTime();
		return super.getUseDuration(stack, p_344979_);
	}
    
    @Override
    public FoodInfo getFoodInfo() {
        return info;
    }

	@Override
	public boolean shouldAddEffectTooltips() {
		return this.info != null;
	}
}
