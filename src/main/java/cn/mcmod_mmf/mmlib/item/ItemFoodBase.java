package cn.mcmod_mmf.mmlib.item;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemFoodBase extends Item {
    private final FoodInfo info;

    public ItemFoodBase(Item.Properties prop, FoodInfo info) {
        super(prop);
        this.info = info;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return super.initCapabilities(stack, nbt);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World level, LivingEntity entity) {
        ItemStack itemstack = super.finishUsingItem(stack, level, entity);
        if (stack.getCount() > 0) {
            if (entity instanceof PlayerEntity) {
                PlayerEntity entityplayer = (PlayerEntity) entity;
                if (entityplayer.abilities.instabuild)
                    return itemstack;
                if (!entityplayer.addItem(this.getContainerItem(stack)))
                    entityplayer.drop(this.getContainerItem(stack), true);
            }
            return itemstack;
        }
        return entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.instabuild ? itemstack : this.getContainerItem(stack);
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
    public Food getFoodProperties() {
        Food.Builder food = new Food.Builder().nutrition(getFoodInfo().getAmount())
                .saturationMod(getFoodInfo().getCalories());
        if (getFoodInfo().isAlwaysEat())
            food.alwaysEat();
        if (getFoodInfo().getEatTime() <= 16)
            food.fast();
        this.getFoodInfo().getEffects().forEach((k) -> food.effect(k.getFirst(), k.getSecond()));

        return food.build();
    }

    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        if (this.getFoodInfo() != null)
            return this.getFoodInfo().getEatTime();
        return super.getUseDuration(p_77626_1_);
    }

    public FoodInfo getFoodInfo() {
        return info;
    }

}
