package cn.mcmod_mmf.mmlib.fluid;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import cn.mcmod_mmf.mmlib.utils.DataGenUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidHelper {
    private static final NumberFormat nf = NumberFormat.getIntegerInstance();
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
            stack.setTag(TagParser.parseTag(element.isJsonObject() ? DataGenUtil.DATA_GSON.toJson(element) : GsonHelper.convertToString(element, "nbt")));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }

        return stack;
    }
    
    public static List<Component> getTooltip(FluidStack fluidStack, int capacityMb, TooltipFlag tooltipFlag) {
        List<Component> tooltip = new ArrayList<>();
        Fluid fluidType = fluidStack.getFluid();
        if (fluidType == null) {
            return tooltip;
        }

        Component displayName = fluidStack.getDisplayName();
        tooltip.add(displayName);

        int amount = fluidStack.getAmount();
        if (capacityMb > 0) {
            TranslatableComponent amountString = new TranslatableComponent("mm_lib.tooltip.liquid.amount.with.capacity", nf.format(amount), nf.format(capacityMb));
            tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
        } else {
            TranslatableComponent amountString = new TranslatableComponent("mm_lib.tooltip.liquid.amount", nf.format(amount));
            tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
        }

        return tooltip;
    }
}