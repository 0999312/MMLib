package cn.mcmod_mmf.mmlib.recipe;

import com.google.gson.JsonObject;

import cn.mcmod_mmf.mmlib.utils.DataGenUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class AbstractRecipeSerializer<T extends AbstractRecipe> implements RecipeSerializer<T> {
    private final Class<T> recipeClass;

    public AbstractRecipeSerializer(Class<T> recipeClass) {
        this.recipeClass = recipeClass;
    }
    @Override
    public T fromJson(ResourceLocation recipeID, JsonObject recipeJson) {
        T result = DataGenUtil.NETWORK_GSON.fromJson(recipeJson, recipeClass);
        result.setId(recipeID);
        return result;
    }

    @Override
    public T fromNetwork(ResourceLocation recipeID, FriendlyByteBuf buffer) {
        T result = DataGenUtil.NETWORK_GSON.fromJson(buffer.readUtf(), recipeClass);
        result.setId(recipeID);
        return result;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        buffer.writeUtf(DataGenUtil.NETWORK_GSON.toJson(recipe));
    }

    public JsonObject toJson(T recipe) {
        return DataGenUtil.NETWORK_GSON.toJsonTree(recipe).getAsJsonObject();
    }
}
