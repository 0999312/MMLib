package cn.mcmod_mmf.mmlib.utils;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class TagUtils {
    public static TagKey<Item> modItemTag(String modid, String path) {
        return ItemTags.create(new ResourceLocation(modid, path));
    }

    public static TagKey<Block> modBlockTag(String modid, String path) {
        return BlockTags.create(new ResourceLocation(modid, path));
    }
    
    public static TagKey<EntityType<?>> modEntityTag(String modid, String path) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(modid, path));
    }

    public static TagKey<Fluid> modFluidTag(String modid, String path) {
        return FluidTags.create(new ResourceLocation(modid, path));
    }
    
    public static TagKey<Item> forgeItemTag(String path) {
        return ItemTags.create(new ResourceLocation("forge", path));
    }
    
    public static TagKey<Block> forgeBlockTag(String path) {
        return BlockTags.create(new ResourceLocation("forge", path));
    }
    
    public static TagKey<EntityType<?>> forgeEntityTag(String path) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("forge", path));
    }

    public static TagKey<Fluid> forgeFluidTag(String path) {
        return FluidTags.create(new ResourceLocation("forge", path));
    }
}
