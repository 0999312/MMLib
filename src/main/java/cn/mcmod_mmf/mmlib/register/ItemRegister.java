package cn.mcmod_mmf.mmlib.register;

import cn.mcmod_mmf.mmlib.item.ItemBase;
import cn.mcmod_mmf.mmlib.item.food.ItemFoodBase;
import cn.mcmod_mmf.mmlib.util.JSON_Creator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ItemRegister {
	private static final ItemRegister instance = new ItemRegister();

	private ItemRegister() {
	}

	public static ItemRegister getInstance() {
		return instance;
	}
	public void register(String modid, Item item) {
		ForgeRegistries.ITEMS.register(item.setRegistryName(item.getUnlocalizedName().substring(6+modid.length())));
	}

	@SideOnly(Side.CLIENT)
	public void registerRender(ItemBase item) {
		registerRender(item, false);
	}

	@SideOnly(Side.CLIENT)
	public void registerRender(ItemFoodBase item) {
		registerRender(item, false);
	}

	@SideOnly(Side.CLIENT)
	public void registerRender(ItemBase item, boolean json_create) {
		String name;
		final String modid = item.getRegistryName().getResourceDomain();
		final int length = item.getSubNames().length;
		
		for (int i = 0; i < length; i++) {
			name = item.getSubNames()[i];
			if (json_create)
				JSON_Creator.getInstance().genItem(modid, name, name, "json_create");
			ModelResourceLocation model = new ModelResourceLocation(
					new ResourceLocation(modid, name), "inventory");
			ModelLoader.setCustomModelResourceLocation(item, i, model);
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerRender(ItemFoodBase item, boolean json_create) {
		String name;
		final String modid = item.getRegistryName().getResourceDomain();
		final int length = item.getInfo().length;
		for (int i = 0; i < length; i++) {
			name = item.getInfo()[i].getName();
			if (json_create)
				JSON_Creator.getInstance().genItem(modid, name, name, "json_create");
			ModelResourceLocation model = new ModelResourceLocation(
					new ResourceLocation(modid, name), "inventory");
			ModelLoader.setCustomModelResourceLocation(item, i, model);
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerRender(Item item, int meta, String name) {
		ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, model);
	}

	@SideOnly(Side.CLIENT)
	public void registerRender(Item item, int meta, String name, String textureName) {
		JSON_Creator.getInstance().genItem(item.getRegistryName().getResourceDomain(), name, textureName, "json_create");
		ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, model);
	}

	@SideOnly(Side.CLIENT)
	public void registerRender(Item item, String textureName) {
		JSON_Creator.getInstance().genItem(item.getRegistryName().getResourceDomain(), item.getRegistryName().getResourcePath(),
				textureName, "json_create");
		ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);
	}

	@SideOnly(Side.CLIENT)
	public void registerRender(Item item) {
		ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);
	}
}
