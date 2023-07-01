package cn.mcmod_mmf.mmlib.recipe;

import com.google.gson.annotations.Expose;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public abstract class AbstractRecipe implements Recipe<RecipeWrapper> {
    protected ResourceLocation id;
    @Expose
    public String group;
    @Expose
    public float experience;
    @Expose
    public int recipeTime;

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }
    
    public float getExperience() {
        return experience;
    }

    public int getRecipeTime() {
        return recipeTime;
    }

}
