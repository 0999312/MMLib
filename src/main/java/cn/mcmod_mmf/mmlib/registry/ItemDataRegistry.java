package cn.mcmod_mmf.mmlib.registry;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface ItemDataRegistry {
	public ResourceLocation getID();
	
	public ItemStack getItem(Item item);
	
	public ItemStack getItem();
	
	public <T extends ItemDataRegistry> Codec<T> getCodec();
}
