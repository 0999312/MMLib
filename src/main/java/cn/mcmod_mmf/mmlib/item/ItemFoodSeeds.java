package cn.mcmod_mmf.mmlib.item;

import cn.mcmod_mmf.mmlib.item.info.FoodInfo;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.minecraft.core.NonNullList;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;

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
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (ModList.get().isLoaded("tfc")) {
            if (allowdedIn(category)) {
                items.add(FoodCapability.setStackNonDecaying(new ItemStack(this)));
            }
        }else super.fillItemCategory(category, items);
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
