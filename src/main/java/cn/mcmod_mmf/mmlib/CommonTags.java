package cn.mcmod_mmf.mmlib;

import cn.mcmod_mmf.mmlib.utils.TagUtils;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags.IOptionalNamedTag;

public class CommonTags {
    // Blocks that can heat up cooking workstations.
    public static final IOptionalNamedTag<Block> HEAT_SOURCES = TagUtils.modBlockTag(Main.MODID, "heat_sources");

    // Blocks that can transfer heat to cooking workstations, if directly above a heat source.
    public static final IOptionalNamedTag<Block> HEAT_CONDUCTORS = TagUtils.modBlockTag(Main.MODID, "heat_conductors");
}
