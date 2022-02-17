package cn.mcmod_mmf.mmlib.fluid;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import cn.mcmod_mmf.mmlib.utils.DataGenUtil;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidHelper {

    public static JsonElement serializeFluidStack(FluidStack stack) {
        JsonObject json = new JsonObject();
        json.addProperty("fluid", stack.getFluid().getRegistryName().toString());
        json.addProperty("amount", stack.getAmount());
        if (stack.hasTag())
            json.addProperty("nbt", stack.getTag().toString());
        return json;
    }

    public static FluidStack deserializeFluidStack(JsonObject json) {
        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
        if (fluid == null)
            throw new JsonSyntaxException("Unknown fluid '" + id + "'");
        int amount = GsonHelper.getAsInt(json, "amount");
        FluidStack stack = new FluidStack(fluid, amount);

        if (!json.has("nbt"))
            return stack;

        try {
            JsonElement element = json.get("nbt");
            stack.setTag(TagParser.parseTag(element.isJsonObject() ? DataGenUtil.GSON.toJson(element) : GsonHelper.convertToString(element, "nbt")));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return stack;
    }
}
