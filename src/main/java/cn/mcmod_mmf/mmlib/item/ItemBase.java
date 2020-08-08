package cn.mcmod_mmf.mmlib.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemBase extends Item {
	private final String modid;
	private final String[] subNames;
	public Item containerItem;
	public ItemBase(String modid, String name, int stackSize, String... subNames) {
		StringBuilder namebuilder = new StringBuilder(modid);
		this.setUnlocalizedName(namebuilder.append('.').append(name).toString());
		this.setHasSubtypes(subNames!=null&&subNames.length > 0);
		this.setMaxStackSize(stackSize);
		this.modid=modid;
		this.subNames = subNames!=null&&subNames.length > 0?subNames: null;
		this.setMaxDamage(0);
		this.setNoRepair();
	}
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if(this.isInCreativeTab(tab))
			if(getSubNames()!=null) {
				for(int i = 0; i < getSubNames().length; i++)
						list.add(new ItemStack(this, 1, i));
			}
			else list.add(new ItemStack(this));
	}

	public String[] getSubNames() {
		return subNames;
	}
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if(getSubNames()!=null) {
			StringBuilder name_builder = new StringBuilder("item.");
			String subName = stack.getMetadata() < getSubNames().length?name_builder.append(modid).append('.').append(getSubNames()[stack.getMetadata()]).toString(): "";
			return subName;
		}
		return this.getUnlocalizedName();
	}

	public ItemBase setContainerItem(Item containerItem) {
		this.containerItem = containerItem;
		return this;
	}

}
