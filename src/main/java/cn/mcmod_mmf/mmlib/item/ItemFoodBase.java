package cn.mcmod_mmf.mmlib.item;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemFoodBase extends Item implements IFoodLike {
    private final FoodInfo info;

    public ItemFoodBase(Item.Properties prop, FoodInfo info) {
        super(prop.food(
        		new FoodProperties(info.getAmount(), info.getCalories(), info.isAlwaysEat(), info.getEatTime(), 
        				Optional.empty(), List.of())
        		));
        this.info = info;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack itemstack = super.finishUsingItem(stack, level, entity);
        if (stack.getCount() > 0) {
            if (entity instanceof Player) {
                Player entityplayer = (Player) entity;
                if (entityplayer.getAbilities().instabuild)
                    return itemstack;
                if (!entityplayer.addItem(this.getCraftingRemainingItem(stack)))
                    entityplayer.drop(this.getCraftingRemainingItem(stack), true);
            }
            return itemstack;
        }
        return entity instanceof Player && ((Player) entity).getAbilities().instabuild ? itemstack
                : this.getCraftingRemainingItem(stack);
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return super.getDrinkingSound();
    }

    @Override
    public SoundEvent getEatingSound() {
        return super.getEatingSound();
    }
    
    @Override
    public FoodInfo getFoodInfo() {
        return info;
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
	public boolean shouldAddEffectTooltips() {
		return this.info != null;
	}

}
