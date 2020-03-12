package cn.mcmod_mmf.mmlib.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBase extends Block {
	public BlockBase(Material material) {
		super(material);
	}

	@Override
	public Block setSoundType(SoundType sound) {
		return super.setSoundType(sound);
	}
}
