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

public class BlockRegister {
	public static void register(String modid, Block block, Item itemBlock, String string) {
		registerNoItem(modid, block, string);
		if (itemBlock != null) {
			itemBlock.setRegistryName(modid, string);
			itemBlock.setUnlocalizedName(modid + "." + string);
			ForgeRegistries.ITEMS.register(itemBlock);
		}
	}

	public static void registerNoItem(String modid, Block block, String string) {
		block.setRegistryName(modid, string);
		block.setUnlocalizedName(modid + "." + string);

		ForgeRegistries.BLOCKS.register(block);
	}

	public static Block registerFluidBlock(String modid, Fluid fluid, Block fluidBlock, String name) {
		fluidBlock.setRegistryName(new ResourceLocation(modid, name));
		ForgeRegistries.BLOCKS.register(fluidBlock);
		fluid.setBlock(fluidBlock);
		return fluidBlock;
	}

	@SideOnly(Side.CLIENT)
	public static void registerFluidBlockRendering(String modid, Block block, String name) {
		final ModelResourceLocation fluidLocation = new ModelResourceLocation(modid + ":fluids", name);
		ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return fluidLocation;
			}
		});
	}

	@SideOnly(Side.CLIENT)
	public static void registerRender(Block block) {
		ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(),
				"inventory");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);
	}

	@SideOnly(Side.CLIENT)
	public static void registerCakeRender(String modid, Block block, String name) {
		JSON_Creator.genCake(modid, block.getRegistryName().toString().substring(1 + modid.length()), name,
				"json_create");
		ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);
	}

	@SideOnly(Side.CLIENT)
	public static void registerRender(String modid, Block block, String name) {
		JSON_Creator.genBlock(modid, block.getRegistryName().toString().substring(1 + modid.length()), name,
				"json_create");
		ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);
	}
}
