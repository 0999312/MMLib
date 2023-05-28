package cn.mcmod_mmf.mmlib.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import cn.mcmod_mmf.mmlib.client.model.pojo.CubesItem;
import cn.mcmod_mmf.mmlib.fluid.FluidIngredient;
import cn.mcmod_mmf.mmlib.json.AbstractSerializer.ChanceResultSerializer;
import cn.mcmod_mmf.mmlib.json.AbstractSerializer.FluidIngredientSerializer;
import cn.mcmod_mmf.mmlib.json.AbstractSerializer.FluidStackSerializer;
import cn.mcmod_mmf.mmlib.json.AbstractSerializer.IngredientSerializer;
import cn.mcmod_mmf.mmlib.json.AbstractSerializer.ItemStackSerializer;
import cn.mcmod_mmf.mmlib.json.NonNullListDeserializer;
import cn.mcmod_mmf.mmlib.recipe.ChanceResult;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.nio.file.Path;

public class DataGenUtil {
    public static final Gson DATA_GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(CubesItem.class, new CubesItem.Deserializer()).create();
    public static final Gson NETWORK_GSON = new GsonBuilder().disableHtmlEscaping().enableComplexMapKeySerialization()
            .registerTypeAdapter(ItemStack.class, ItemStackSerializer.getInstance())
            .registerTypeAdapter(Ingredient.class, IngredientSerializer.getInstance())
            .registerTypeAdapter(FluidStack.class, FluidStackSerializer.getInstance())
            .registerTypeAdapter(ChanceResult.class, ChanceResultSerializer.getInstance())
            .registerTypeAdapter(FluidIngredient.class, FluidIngredientSerializer.getInstance())
            .registerTypeAdapter(NonNullList.class, NonNullListDeserializer.getInstance())
            .excludeFieldsWithoutExposeAnnotation().create();

    public static Path createPath(Path path, String namespace, String type, String name) {
        StringBuilder builder = new StringBuilder("data/").append(namespace).append('/').append(type).append('/')
                .append(type).append(".json");
        return path.resolve(builder.toString());
    }

    // Thanks Commoble
    public static final Codec<Ingredient> INGREDIENT_CODEC = Codec.PASSTHROUGH.comapFlatMap(dynamic -> {
        try {
            Ingredient ingredient = Ingredient.fromJson(dynamic.convert(JsonOps.INSTANCE).getValue());
            return DataResult.success(ingredient);
        } catch (Exception e) {
            return DataResult.error(e.getMessage());
        }
    }, ingredient -> new Dynamic<JsonElement>(JsonOps.INSTANCE, ingredient.toJson()));
}
