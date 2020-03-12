package cn.mcmod_mmf.mmlib.item;

import cn.mcmod_mmf.mmlib.util.RecipesUtil;
import cn.mcmod_mmf.mmlib.util.TagPropertyAccessor.TagPropertyInteger;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemMetaDurability extends ItemBase {
	public static final TagPropertyInteger amount = new TagPropertyInteger("amount");
	private final int max_amount;
	private final ItemStack container;
	public ItemMetaDurability(String modid, String name,int max_amount, ItemStack container, String[] subNames) {
		super(modid, name, 1, subNames);
		this.max_amount=max_amount;
		this.container = container;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return amount.get(RecipesUtil.getItemTagCompound(stack),0)>0;
	} 
	 
	 @Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return (double)amount.get(RecipesUtil.getItemTagCompound(stack),0)/4.0F;
	}
	
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
    	NBTTagCompound tag = RecipesUtil.getItemTagCompound(itemStack);
        int dmg = amount.get(tag,0);
        if (dmg < getMaxAmount()) {
            ItemStack stack = itemStack.copy();
            NBTTagCompound tag_result = RecipesUtil.getItemTagCompound(stack);
            amount.add(tag_result, 1);
            return stack;
        }
		return getContainer();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
            return true;
    }

	public int getMaxAmount() {
		return max_amount;
	}

	public ItemStack getContainer() {
		return container;
	}
}
