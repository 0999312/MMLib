package cn.mcmod_mmf.mmlib.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import cn.mcmod_mmf.mmlib.fluid.FluidHelper;
import cn.mcmod_mmf.mmlib.fluid.FluidIngredient;
import cn.mcmod_mmf.mmlib.recipe.ChanceResult;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class AbstractSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {
    public static class ItemStackSerializer extends AbstractSerializer<ItemStack> {
        private static final ItemStackSerializer INSTANCE = new ItemStackSerializer();

        private ItemStackSerializer() {
        }

        @Override
        public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject objectResult = new JsonObject();
            objectResult.addProperty("item", ForgeRegistries.ITEMS.getKey(src.getItem()).toString());
            if (src.getCount() > 1) {
                objectResult.addProperty("count", src.getCount());
            }
            if (src.hasTag()) {
                objectResult.add("nbt", JsonParser.parseString(src.getTag().toString()));
            }
            return objectResult;
        }

        @Override
        public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return CraftingHelper.getItemStack(json.getAsJsonObject(), true);
        }

        public static ItemStackSerializer getInstance() {
            return INSTANCE;
        }
    }

    public static class ChanceResultSerializer extends AbstractSerializer<ChanceResult> {
        private static final ChanceResultSerializer INSTANCE = new ChanceResultSerializer();

        private ChanceResultSerializer() {
        }

        @Override
        public JsonElement serialize(ChanceResult src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject objectResult = new JsonObject();
            objectResult.addProperty("item", ForgeRegistries.ITEMS.getKey(src.stack().getItem()).toString());
            if (src.stack().getCount() > 1) {
                objectResult.addProperty("count", src.stack().getCount());
            }
            if (src.stack().hasTag()) {
                objectResult.add("nbt", JsonParser.parseString(src.stack().getTag().toString()));
            }
            if (src.chance() != 1)
                objectResult.addProperty("chance", src.chance());
            return objectResult;
        }

        @Override
        public ChanceResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();
            return new ChanceResult(CraftingHelper.getItemStack(jsonObj, true),
                    jsonObj.has("chance") ? GsonHelper.getAsFloat(jsonObj.getAsJsonObject(), "chance") : 1.0F);
        }

        public static ChanceResultSerializer getInstance() {
            return INSTANCE;
        }
    }

    public static class IngredientSerializer extends AbstractSerializer<Ingredient> {
        private static final IngredientSerializer INSTANCE = new IngredientSerializer();

        private IngredientSerializer() {
        }

        @Override
        public JsonElement serialize(Ingredient src, Type typeOfSrc, JsonSerializationContext context) {
            return src.toJson();
        }

        @Override
        public Ingredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return Ingredient.fromJson(json);
        }

        public static IngredientSerializer getInstance() {
            return INSTANCE;
        }
    }

    public static class FluidStackSerializer extends AbstractSerializer<FluidStack> {
        private static final FluidStackSerializer INSTANCE = new FluidStackSerializer();

        private FluidStackSerializer() {
        }

        @Override
        public JsonElement serialize(FluidStack src, Type typeOfSrc, JsonSerializationContext context) {
            return FluidHelper.serializeFluidStack(src);
        }

        @Override
        public FluidStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return FluidHelper.deserializeFluidStack(json.getAsJsonObject());
        }

        public static FluidStackSerializer getInstance() {
            return INSTANCE;
        }
    }

    public static class FluidIngredientSerializer extends AbstractSerializer<FluidIngredient> {
        private static final FluidIngredientSerializer INSTANCE = new FluidIngredientSerializer();

        private FluidIngredientSerializer() {
        }

        @Override
        public JsonElement serialize(FluidIngredient src, Type typeOfSrc, JsonSerializationContext context) {
            return src.serialize();
        }

        @Override
        public FluidIngredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return FluidIngredient.deserialize(json);
        }

        public static FluidIngredientSerializer getInstance() {
            return INSTANCE;
        }
    }
}
