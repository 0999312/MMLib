package cn.mcmod_mmf.mmlib.recipe;

import java.util.Map;
import java.util.Map.Entry;

import cn.mcmod_mmf.mmlib.util.RecipesUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public abstract class ItemToItemRecipeBase {
	public Map<Object, ItemStack> RecipesList;
    
	public void addRecipes(Object main, ItemStack result) {
		RecipesList.put(main, result);
	}

	public ItemStack getResultItemStack(ItemStack stack) {
		for (Entry<Object, ItemStack> entry2 : RecipesList.entrySet()) {
			if (entry2.getKey() instanceof ItemStack) {
				if (ItemStack.areItemsEqual(stack, (ItemStack) entry2.getKey())) {
					return entry2.getValue();
				}
			} else if (entry2.getKey() instanceof String) {
				String dict = (String) entry2.getKey();
				NonNullList<ItemStack> ore = OreDictionary.getOres(dict);
				if (!ore.isEmpty() && RecipesUtil.getInstance().containsMatch(true, ore, stack))
					return entry2.getValue();
			}
		}
		return ItemStack.EMPTY;
	}
	
	public void ClearRecipe(Object input) {
		RecipesList.remove(input);
	}

	public void ClearAllRecipe() {
		RecipesList.clear();
	}
}