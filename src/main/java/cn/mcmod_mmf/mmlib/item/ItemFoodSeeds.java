package cn.mcmod_mmf.mmlib.item;

import java.util.List;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
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
    
    @Override
    public void appendHoverText(ItemStack itemStack, Level level, List<Component> tooltips, TooltipFlag flag) {
    	super.appendHoverText(itemStack, level, tooltips, flag);
    	if(!this.shouldAddEffectTooltips())
    		return;
    	
    	if(!this.getFoodInfo().getEffects().isEmpty()) {
    		this.addEffectTooltips(tooltips);
    	}
    }

	@Override
	public boolean shouldAddEffectTooltips() {
		return this.info != null;
	}
}
