package cn.mcmod_mmf.mmlib.register;

import cn.mcmod_mmf.mmlib.util.JSON_Creator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BlockRegister {
	private static final BlockRegister instance = new BlockRegister();

	private BlockRegister() {
	}

	public static BlockRegister getInstance() {
		return instance;
	}
	public void register(String modid, Block block, Item itemBlock, String string) {
		registerNoItem(modid, block, string);
		if (itemBlock != null) {
			StringBuilder itembuilder = new StringBuilder(modid);
			itemBlock.setRegistryName(modid, string);
			itemBlock.setUnlocalizedName(itembuilder.append('.').append(string).toString());
			ForgeRegistries.ITEMS.register(itemBlock);
		}
	}

	public void registerNoItem(String modid, Block block, String string) {
		StringBuilder blockbuilder = new StringBuilder(modid);
		block.setRegistryName(modid, string);
		block.setUnlocalizedName(blockbuilder.append('.').append(string).toString());

		ForgeRegistries.BLOCKS.register(block);
	}

	public Block registerFluidBlock(String modid, Fluid fluid, Block fluidBlock, String name) {
		fluidBlock.setRegistryName(new ResourceLocation(modid, name));
		ForgeRegistries.BLOCKS.register(fluidBlock);
		fluid.setBlock(fluidBlock);
		return fluidBlock;
	}

	@SideOnly(Side.CLIENT)
	public void registerFluidBlockRendering(String modid, Block block, String name) {
		final ModelResourceLocation fluidLocation = new ModelResourceLocation(modid + ":fluids", name);
		ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return fluidLocation;
			}
		});
	}
	@SideOnly(Side.CLIENT)
	public void registerRender(Block block,int meta) {
		ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(),
				"inventory");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, model);
	}
	@SideOnly(Side.CLIENT)
	public void registerRender(Block block) {
		registerRender(block, 0);
	}

//	TODO:WIP
//	@SideOnly(Side.CLIENT)
//	public void registerMetaBlockRender(Block block) {
//		ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(),
//				"inventory");
//		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);
//	}

	@SideOnly(Side.CLIENT)
	public void registerCakeRender(String modid, Block block, String name) {
		JSON_Creator.getInstance().genCake(modid, block.getRegistryName().toString().substring(1 + modid.length()), name,
				"json_create");
		ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);
	}

	@SideOnly(Side.CLIENT)
	public void registerRender(String modid, Block block, String name) {
		JSON_Creator.getInstance().genBlock(modid, block.getRegistryName().toString().substring(1 + modid.length()), name,
				"json_create");
		ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);
	}
}
