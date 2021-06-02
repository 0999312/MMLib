package cn.mcmod_mmf.mmlib.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemFoodBase extends Item {

	public ItemFoodBase(Properties p_i48487_1_) {
		super(p_i48487_1_);
	}
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		// TODO TFC-TNG Compat
		return super.initCapabilities(stack, nbt);
	}
}
