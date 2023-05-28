package cn.mcmod_mmf.mmlib.item;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

public class ItemFoodBase extends Item implements IFoodLike {
    private final FoodInfo info;

    public ItemFoodBase(Item.Properties prop, FoodInfo info) {
        super(prop);
        this.info = info;
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (ModList.get().isLoaded("tfc")) {
            if (allowdedIn(category)) {
                items.add(FoodCapability.setStackNonDecaying(new ItemStack(this)));
            }
        }else super.fillItemCategory(category, items);
    }

    @Override
    public boolean isEdible() {
        return this.info != null;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return super.initCapabilities(stack, nbt);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack itemstack = super.finishUsingItem(stack, level, entity);
        if (stack.getCount() > 0) {
            if (entity instanceof Player) {
                Player entityplayer = (Player) entity;
                if (entityplayer.getAbilities().instabuild)
                    return itemstack;
                if (!entityplayer.addItem(this.getContainerItem(stack)))
                    entityplayer.drop(this.getContainerItem(stack), true);
            }
            return itemstack;
        }
        return entity instanceof Player && ((Player) entity).getAbilities().instabuild ? itemstack
                : this.getContainerItem(stack);
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
        FoodProperties.Builder food = new FoodProperties.Builder().nutrition(getFoodInfo().getAmount())
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

    @Override
    public FoodInfo getFoodInfo() {
        return info;
    }

}
