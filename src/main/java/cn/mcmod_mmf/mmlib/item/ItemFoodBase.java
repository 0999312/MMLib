package cn.mcmod_mmf.mmlib.item;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemFoodBase extends Item {
    private final FoodInfo info;

    public ItemFoodBase(Item.Properties prop, FoodInfo info) {
        super(prop.food(new Food.Builder().nutrition(info.getAmount()).saturationMod(info.getCalories()).build()));
        this.info = info;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return super.initCapabilities(stack, nbt);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack p_77654_1_, World p_77654_2_, LivingEntity p_77654_3_) {

        ItemStack itemstack = super.finishUsingItem(p_77654_1_, p_77654_2_, p_77654_3_);
        if (p_77654_1_.getCount() > 0) {
            if (p_77654_3_ instanceof PlayerEntity) {
                PlayerEntity entityplayer = (PlayerEntity) p_77654_3_;
                if (entityplayer.abilities.instabuild)
                    return itemstack;
                if (!entityplayer.addItem(this.getContainerItem(p_77654_1_)))
                    entityplayer.drop(this.getContainerItem(p_77654_1_), true);
            }
            return itemstack;
        }
        return p_77654_3_ instanceof PlayerEntity && ((PlayerEntity) p_77654_3_).abilities.instabuild ? itemstack
                : this.getContainerItem(p_77654_1_);
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
