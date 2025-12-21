package cn.mcmod_mmf.mmlib.item;

import java.util.List;
import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class ItemFoodBase extends Item implements IFoodLike {
    private final FoodInfo info;
    private final FoodProperties finalFoodProperties;
    public ItemFoodBase(Item.Properties prop, FoodInfo info) {
        super(prop);
        this.info = info;
        FoodProperties.Builder food = new FoodProperties.Builder().nutrition(getFoodInfo().getAmount())
                .saturationMod(getFoodInfo().getCalories());
        if (info.isAlwaysEat())
            food.alwaysEat();
        if (info.getEatTime() <= 16)
            food.fast();
        this.info.getEffects().forEach((k) -> food.effect(k.getFirst(), k.getSecond()));
        this.finalFoodProperties = food.build();
    }

    @Override
    public boolean isEdible() {
        return this.info != null;
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
    public FoodProperties getFoodProperties() {
        return this.finalFoodProperties;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        if (this.getFoodInfo() != null)
            return this.getFoodInfo().getEatTime();
        return super.getUseDuration(stack);
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
