package cn.mcmod_mmf.mmlib.data;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class AbstractRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public AbstractRecipeProvider(PackOutput gen) {
        super(gen);

    }

    protected final void foodCooking(Supplier<? extends ItemLike> material, Supplier<? extends ItemLike> result, float xp, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(material.get()), RecipeCategory.FOOD, result.get(), xp, 200).unlockedBy("has_item", has(material.get())).save(consumer, new ResourceLocation(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(), "smelting_" + ForgeRegistries.ITEMS.getKey(result.get().asItem()).getPath()));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(material.get()), RecipeCategory.FOOD, result.get(), xp, 100).unlockedBy("has_item", has(material.get())).save(consumer, new ResourceLocation(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),"smoking_" + ForgeRegistries.ITEMS.getKey(result.get().asItem()).getPath()));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(material.get()), RecipeCategory.FOOD, result.get(), xp, 600).unlockedBy("has_item", has(material.get())).save(consumer, new ResourceLocation(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),"campfire_cooking_" + ForgeRegistries.ITEMS.getKey(result.get().asItem()).getPath()));
    }

    protected final void foodCookingButNoCampfire(Supplier<? extends ItemLike> material, Supplier<? extends ItemLike> result, float xp, Consumer<FinishedRecipe> consumer) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(material.get()), RecipeCategory.FOOD, result.get(), xp, 200).unlockedBy("has_item", has(material.get())).save(consumer, new ResourceLocation(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),"smelting_" + ForgeRegistries.ITEMS.getKey(result.get().asItem()).getPath()));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(material.get()), RecipeCategory.FOOD, result.get(), xp, 100).unlockedBy("has_item", has(material.get())).save(consumer, new ResourceLocation(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),"smoking_" + ForgeRegistries.ITEMS.getKey(result.get().asItem()).getPath()));
    }

    protected final void foodCooking(Supplier<? extends ItemLike> material, Supplier<? extends ItemLike> result, float xp, Consumer<FinishedRecipe> consumer, String recipeName) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(material.get()), RecipeCategory.FOOD, result.get(), xp, 200).unlockedBy("has_item", has(material.get())).save(consumer, new ResourceLocation(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),"smelting_" + recipeName));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(material.get()), RecipeCategory.FOOD, result.get(), xp, 100).unlockedBy("has_item", has(material.get())).save(consumer, new ResourceLocation(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),"smoking_" + recipeName));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(material.get()), RecipeCategory.FOOD, result.get(), xp, 600).unlockedBy("has_item", has(material.get())).save(consumer, new ResourceLocation(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),"campfire_cooking_" + recipeName));
    }

    public static void cuttingRecipe(Consumer<FinishedRecipe> consumer, Supplier<? extends ItemLike> cuttingItem, Supplier<? extends ItemLike> result, int count) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(cuttingItem.get()), RecipeCategory.BUILDING_BLOCKS, result.get(), count).unlockedBy("has_item", has(cuttingItem.get())).save(consumer, new ResourceLocation(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),"cutting_" + ForgeRegistries.ITEMS.getKey(result.get().asItem()).getPath()));
    }

    protected final void helmetItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
                .pattern("###")
                .pattern("# #")
                .define('#', material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locEquip(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(), name));
    }

    protected final void chestplateItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locEquip(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),name));
    }

    protected final void leggingsItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .define('#', material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locEquip(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),name));
    }

    protected final void bootsItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
                .pattern("# #")
                .pattern("# #")
                .define('#', material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locEquip(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),name));
    }

    protected final void pickaxeItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material, TagKey<Item> handle) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result.get())
                .pattern("###")
                .pattern(" X ")
                .pattern(" X ")
                .define('#', material.get())
                .define('X', handle)
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locEquip(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),name));
    }

    protected final void swordItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material, TagKey<Item> handle) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result.get())
                .pattern("#")
                .pattern("#")
                .pattern("X")
                .define('#', material.get())
                .define('X', handle)
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locEquip(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),name));
    }

    protected final void axeItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material, TagKey<Item> handle) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result.get())
                .pattern("##")
                .pattern("#X")
                .pattern(" X")
                .define('#', material.get())
                .define('X', handle)
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locEquip(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),name));
    }

    protected final void shovelItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material, TagKey<Item> handle) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result.get())
                .pattern("#")
                .pattern("X")
                .pattern("X")
                .define('#', material.get())
                .define('X', handle)
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locEquip(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),name));
    }

    protected final void hoeItem(Consumer<FinishedRecipe> consumer, String name, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material, TagKey<Item> handle) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result.get())
                .pattern("##")
                .pattern(" X")
                .pattern(" X")
                .define('#', material.get())
                .define('X', handle)
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, locEquip(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),name));
    }

    protected final void tofuBlockItem(Consumer<FinishedRecipe> consumer, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get())
                .pattern("##")
                .pattern("##")
                .define('#', material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer);
    }

    protected final void tofuBlockItem(Consumer<FinishedRecipe> consumer, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material, String name) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get())
                .pattern("##")
                .pattern("##")
                .define('#', material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer, new ResourceLocation(ForgeRegistries.ITEMS.getKey(result.get().asItem()).getNamespace(),name));
    }

    protected final void ladderItem(Consumer<FinishedRecipe> consumer, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result.get(), 6)
                .pattern("# #")
                .pattern("###")
                .pattern("# #")
                .define('#', material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer);
    }

    protected final void decorationTofuBlockItem(Consumer<FinishedRecipe> consumer, Supplier<? extends ItemLike> result, Supplier<? extends ItemLike> material) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, result.get(), 4)
                .pattern("##")
                .pattern("##")
                .define('#', material.get())
                .unlockedBy("has_item", has(material.get()))
                .save(consumer);
    }

    public void makeStairs(Consumer<FinishedRecipe> consumer, Supplier<? extends ItemLike> stairsOut, Supplier<? extends ItemLike> blockIn) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, stairsOut.get(), 4)
                .pattern("M  ")
                .pattern("MM ")
                .pattern("MMM")
                .define('M', blockIn.get())
                .unlockedBy("has_item", has(blockIn.get())).save(consumer);
    }

    public void makeStairsCraftingOrCutting(Consumer<FinishedRecipe> consumer, Supplier<? extends ItemLike> stairsOut, Supplier<? extends ItemLike> blockIn) {
        makeStairs(consumer, stairsOut, blockIn);
        cuttingRecipe(consumer, blockIn, stairsOut, 2);
    }

    public void makeSlab(Consumer<FinishedRecipe> consumer, Supplier<? extends ItemLike> slabOut, Supplier<? extends ItemLike> blockIn) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, slabOut.get(), 6)
                .pattern("MMM")
                .define('M', blockIn.get())
                .unlockedBy("has_item", has(blockIn.get())).save(consumer);
    }

    public void makeSlabCraftingOrCutting(Consumer<FinishedRecipe> consumer, Supplier<? extends ItemLike> slabOut, Supplier<? extends ItemLike> blockIn) {
        makeSlab(consumer, slabOut, blockIn);
        cuttingRecipe(consumer, blockIn, slabOut, 2);
    }

    public void makeSolidFence(Consumer<FinishedRecipe> consumer, Supplier<? extends ItemLike> fenceOut, Supplier<? extends ItemLike> blockIn) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, fenceOut.get(), 6)
                .pattern("MMM")
                .pattern("MMM")
                .define('M', blockIn.get())
                .unlockedBy("has_item", has(blockIn.get())).save(consumer);
    }

    public void makeTorch(Consumer<FinishedRecipe> consumer, Supplier<? extends ItemLike> torchOut, Supplier<? extends ItemLike> itemIn) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, torchOut.get(), 4)
                .pattern("C")
                .pattern("M")
                .define('C', ItemTags.COALS)
                .define('M', itemIn.get())
                .unlockedBy("has_item", has(itemIn.get())).save(consumer);
    }

    public void makeDoor(Consumer<FinishedRecipe> consumer, Supplier<? extends Block> doorOut, Supplier<? extends Block> plankIn) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, doorOut.get(), 3)
                .pattern("PP")
                .pattern("PP")
                .pattern("PP")
                .define('P', plankIn.get())
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(plankIn.get().asItem()).getPath(), has(plankIn.get())).save(consumer);
    }

    public void makeTrapdoor(Consumer<FinishedRecipe> consumer, Supplier<? extends Block> trapdoorOut, Supplier<? extends ItemLike> plankIn) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, trapdoorOut.get(), 2)
                .pattern("PPP")
                .pattern("PPP")
                .define('P', plankIn.get())
                .unlockedBy("has_" + ForgeRegistries.ITEMS.getKey(plankIn.get().asItem()).getPath(), has(plankIn.get())).save(consumer);
    }

    public void makeWoodFence(Consumer<FinishedRecipe> consumer, Block fenceOut, Block blockIn) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, fenceOut, 3)
                .pattern("MSM")
                .pattern("MSM")
                .define('M', blockIn)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn).getPath(), has(blockIn)).save(consumer);
    }

    public void makeFenceGate(Consumer<FinishedRecipe> consumer, Block fenceOut, Block blockIn) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, fenceOut)
                .pattern("SMS")
                .pattern("SMS")
                .define('M', blockIn)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_" + ForgeRegistries.BLOCKS.getKey(blockIn).getPath(), has(blockIn)).save(consumer);
    }


    protected final ResourceLocation locEquip(String modid, String name) {
        return new ResourceLocation(modid,"equipment/" + name);
    }
}
