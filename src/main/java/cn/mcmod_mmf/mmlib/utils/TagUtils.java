package cn.mcmod_mmf.mmlib.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class TagUtils {
    public static IOptionalNamedTag<Item> modItemTag(String modid, String path) {
        return ItemTags.createOptional(new ResourceLocation(modid, path));
    }

    public static IOptionalNamedTag<Block> modBlockTag(String modid, String path) {
        return BlockTags.createOptional(new ResourceLocation(modid, path));
    }
    
    public static IOptionalNamedTag<EntityType<?>> modEntityTag(String modid, String path) {
        return EntityTypeTags.createOptional(new ResourceLocation(modid, path));
    }

    public static IOptionalNamedTag<Fluid> modFluidTag(String modid, String path) {
        return FluidTags.createOptional(new ResourceLocation(modid, path));
    }
    
    public static IOptionalNamedTag<Item> forgeItemTag(String path) {
        return ItemTags.createOptional(new ResourceLocation("forge", path));
    }
    
    public static IOptionalNamedTag<Block> forgeBlockTag(String path) {
        return BlockTags.createOptional(new ResourceLocation("forge", path));
    }
    
    public static IOptionalNamedTag<EntityType<?>> forgeEntityTag(String path) {
        return EntityTypeTags.createOptional(new ResourceLocation("forge", path));
    }

    public static IOptionalNamedTag<Fluid> forgeFluidTag(String path) {
        return FluidTags.createOptional(new ResourceLocation("forge", path));
    }
}
