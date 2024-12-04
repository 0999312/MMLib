package cn.mcmod_mmf.mmlib.data;

import java.util.Locale;
import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public abstract class AbstractItemModelProvider extends ItemModelProvider {

	private final String modid;
	
    public AbstractItemModelProvider(PackOutput generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
        this.modid=modid;
    }

    public void sign(Supplier<? extends SignBlock> sign) {
		withExistingParent(blockName(sign), mcLoc("item/generated"))
				.texture("layer0", modLoc("item/" + blockName(sign)));
	}

    public void woodenFence(Supplier<? extends Block> fence, Supplier<? extends Block> block) {
		getBuilder(BuiltInRegistries.BLOCK.getKey(fence.get()).getPath())
				.parent(getExistingFile(mcLoc("block/fence_inventory")))
				.texture("texture", "block/" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath());
	}

	public ItemModelBuilder torchItem(Supplier<Block> item) {
		return withExistingParent(BuiltInRegistries.BLOCK.getKey(item.get()).getPath(), mcLoc("item/generated"))
				.texture("layer0", modLoc("block/" + BuiltInRegistries.BLOCK.getKey(item.get()).getPath()));
	}

	public ItemModelBuilder generated(String name, ResourceLocation... layers) {
		return buildItem(name, "item/generated", 0, layers);
	}

	public ItemModelBuilder buildItem(String name, String parent, int emissivity, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, parent);
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		if (emissivity > 0)
			builder = builder.customLoader(ItemLayerModelBuilder::begin).emissive(emissivity, emissivity, 0).renderType("minecraft:translucent", 0).end();
		return builder;
	}

	public ItemModelBuilder tool(String name, ResourceLocation... layers) {
		return buildItem(name, "item/handheld", 0, layers);
	}

	public ItemModelBuilder singleTexTool(Supplier<? extends Item> item) {
		return tool(itemPath(item).getPath(), prefix("item/" + itemPath(item).getPath()));
	}

	public ItemModelBuilder singleTexRodTool(Supplier<? extends Item> item) {
		return toolRod(itemPath(item).getPath(), prefix("item/" + itemPath(item).getPath()));
	}

	public ItemModelBuilder toolRod(String name, ResourceLocation... layers) {
		return buildItem(name, "item/handheld_rod", 0, layers);
	}

	public ItemModelBuilder singleTex(Supplier<? extends ItemLike> item) {
		return generated(itemPath(item).getPath(), prefix("item/" + itemPath(item).getPath()));
	}

	public ItemModelBuilder emmisiveTex(Supplier<? extends Item> item) {
		return singleTex(item).customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).renderType("minecraft:translucent", 0).end();
	}

	public ItemModelBuilder glowBowItem(Supplier<? extends Item> item) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(item.get());
		buildItem(id.getPath() + "_pulling_0", mcLoc("item/bow").toString(), 15, modLoc("item/" + id.getPath() + "_pulling_0"));
		buildItem(id.getPath() + "_pulling_1", mcLoc("item/bow").toString(), 15, modLoc("item/" + id.getPath() + "_pulling_1"));
		buildItem(id.getPath() + "_pulling_2", mcLoc("item/bow").toString(), 15, modLoc("item/" + id.getPath() + "_pulling_2"));
		return withExistingParent(id.getPath(), mcLoc("item/bow"))
				.customLoader(ItemLayerModelBuilder::begin).emissive(15, 15, 0).renderType("minecraft:translucent", 0).end()
				.texture("layer0", modLoc("item/" + id.getPath()))
				.override().predicate(ResourceLocation.parse("pulling"), 1).model(getExistingFile(modLoc("item/" + id.getPath() + "_pulling_0"))).end()
				.override().predicate(ResourceLocation.parse("pulling"), 1).predicate(ResourceLocation.parse("pull"), 0.65F).model(getExistingFile(modLoc("item/" + id.getPath() + "_pulling_1"))).end()
				.override().predicate(ResourceLocation.parse("pulling"), 1).predicate(ResourceLocation.parse("pull"), 0.9F).model(getExistingFile(modLoc("item/" + id.getPath() + "_pulling_2"))).end();
	}

	public ItemModelBuilder bowItem(Supplier<? extends Item> item) {
		ResourceLocation id = BuiltInRegistries.ITEM.getKey(item.get());
		withExistingParent(id.getPath() + "_pulling_0", mcLoc("item/bow")).texture("layer0", modLoc("item/" + id.getPath() + "_pulling_0"));
		withExistingParent(id.getPath() + "_pulling_1", mcLoc("item/bow")).texture("layer0", modLoc("item/" + id.getPath() + "_pulling_1"));
		withExistingParent(id.getPath() + "_pulling_2", mcLoc("item/bow")).texture("layer0", modLoc("item/" + id.getPath() + "_pulling_2"));
		return withExistingParent(id.getPath(), mcLoc("item/bow"))
				.texture("layer0", modLoc("item/" + id.getPath()))
				.override().predicate(ResourceLocation.parse("pulling"), 1).model(getExistingFile(modLoc("item/" + id.getPath() + "_pulling_0"))).end()
				.override().predicate(ResourceLocation.parse("pulling"), 1).predicate(ResourceLocation.parse("pull"), 0.65F).model(getExistingFile(modLoc("item/" + id.getPath() + "_pulling_1"))).end()
				.override().predicate(ResourceLocation.parse("pulling"), 1).predicate(ResourceLocation.parse("pull"), 0.9F).model(getExistingFile(modLoc("item/" + id.getPath() + "_pulling_2"))).end();
	}

	public void woodenButton(Supplier<? extends Block> button, String variant) {
		getBuilder(BuiltInRegistries.BLOCK.getKey(button.get()).getPath())
				.parent(getExistingFile(mcLoc("block/button_inventory")))
				.texture("texture", "block/wood/planks_" + variant + "_0");
	}

	public void woodenFence(Block fence, String variant) {
		getBuilder(BuiltInRegistries.BLOCK.getKey(fence).getPath())
				.parent(getExistingFile(mcLoc("block/fence_inventory")))
				.texture("texture", "block/wood/planks_" + variant + "_0");
	}

	public ItemModelBuilder wall(Supplier<? extends WallBlock> wall, Supplier<? extends Block> fullBlock) {
		return wallInventory(BuiltInRegistries.BLOCK.getKey(wall.get()).getPath(), texture(blockName(fullBlock)));
	}

	public ItemModelBuilder toBlock(Supplier<? extends Block> b) {
		return toBlockModel(b, BuiltInRegistries.BLOCK.getKey(b.get()).getPath());
	}

	public ItemModelBuilder toBlockModel(Supplier<? extends Block> b, String model) {
		return toBlockModel(b, prefix("block/" + model));
	}

	public ItemModelBuilder toBlockModel(Supplier<? extends Block> b, ResourceLocation model) {
		return withExistingParent(BuiltInRegistries.BLOCK.getKey(b.get()).getPath(), model);
	}

	public ItemModelBuilder itemBlockFlat(Supplier<? extends Block> block) {
		return itemBlockFlat(block, blockName(block));
	}

	public ItemModelBuilder itemBlockFlat(Supplier<? extends Block> block, String name) {
		return withExistingParent(blockName(block), mcLoc("item/generated"))
				.texture("layer0", modLoc("block/" + name));
	}

	public ItemModelBuilder egg(Supplier<Item> item) {
		return withExistingParent(BuiltInRegistries.ITEM.getKey(item.get()).getPath(), mcLoc("item/template_spawn_egg"));
	}

	public String blockName(Supplier<? extends Block> block) {
		return BuiltInRegistries.BLOCK.getKey(block.get()).getPath();
	}
	
	public ResourceLocation texture(String name) {
		return modLoc("block/" + name);
	}

	public ResourceLocation itemPath(Supplier<? extends ItemLike> item) {
		return BuiltInRegistries.ITEM.getKey(item.get().asItem());
	}

	public String getModid() {
		return this.modid;
	}

	public ResourceLocation prefix(String name) {
		return ResourceLocation.fromNamespaceAndPath(getModid(), name.toLowerCase(Locale.ROOT));
	}
}
