package cn.mcmod_mmf.mmlib.block.entity;

import cn.mcmod_mmf.mmlib.CommonTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Copy and improve from Farmer's Delight, add require tags for other magic jobs.
 */
public interface HeatableBlockEntity {

    /**
     * Checks for heat sources below the block. If it can, it also checks for conducted heat.
     */
    default boolean isHeated(Level level, BlockPos pos) {
        BlockState stateBelow = level.getBlockState(pos.below());

        if (this.heatSourceTag().contains(stateBelow.getBlock())) {
            if (stateBelow.hasProperty(BlockStateProperties.LIT))
                return stateBelow.getValue(BlockStateProperties.LIT);
            return true;
        }

        if (!this.requiresDirectHeat() && this.heatConductorTag().contains(stateBelow.getBlock())) {
            BlockState stateFurtherBelow = level.getBlockState(pos.below(2));
            if (this.heatSourceTag().contains(stateFurtherBelow.getBlock())) {
                if (stateFurtherBelow.hasProperty(BlockStateProperties.LIT))
                    return stateFurtherBelow.getValue(BlockStateProperties.LIT);
                return true;
            }
        }

        return false;
    }
    
    default Tag.Named<Block> heatSourceTag(){
        return CommonTags.HEAT_SOURCES;
    }
    
    default Tag.Named<Block> heatConductorTag(){
        return CommonTags.HEAT_CONDUCTORS;
    }
    
    /**
     * Determines if this block can only be heated directly, excluding conductors.
     */
    default boolean requiresDirectHeat() {
        return false;
    }
}
