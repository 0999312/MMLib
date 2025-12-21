package cn.mcmod_mmf.mmlib;

import cn.mcmod_mmf.mmlib.utils.TagUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CommonTags {
    // Blocks that can heat up cooking workstations.
    public static final TagKey<Block> HEAT_SOURCES = TagUtils.modBlockTag(Main.MODID, "heat_sources");
    
    // Vanilla tags but not in 1.20.1
    public static final TagKey<Item> CHICKEN_FOOD = TagUtils.modItemTag("minecraft", "chicken_food");
    public static final TagKey<Item> PIG_FOOD = TagUtils.modItemTag("minecraft", "pig_food");
    public static final TagKey<Item> STRIDER_FOOD = TagUtils.modItemTag("minecraft", "strider_food");
    public static final TagKey<Item> CAT_FOOD = TagUtils.modItemTag("minecraft", "cat_food");
    public static final TagKey<Item> PARROT_FOOD = TagUtils.modItemTag("minecraft", "parrot_food");
    public static final TagKey<Item> HORSE_FOOD = TagUtils.modItemTag("minecraft", "horse_food");

    // Blocks that can transfer heat to cooking workstations, if directly above a heat source.
    public static final TagKey<Block> HEAT_CONDUCTORS = TagUtils.modBlockTag(Main.MODID, "heat_conductors");
}
