package cn.mcmod_mmf.mmlib.level;

import java.util.ArrayList;
import java.util.List;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public class StructuresUtils {
	public static void addBuildingToPool(
			Registry<StructureTemplatePool> templatePoolRegistry, 
			Registry<StructureProcessorList> processorListRegistry, 
			ResourceLocation poolRL, String nbtPieceRL, int weight) {
		StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
		if (pool == null) return;

		ResourceLocation emptyProcessor = new ResourceLocation("empty");
		Holder<StructureProcessorList> processorHolder = 
				processorListRegistry.getHolderOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, emptyProcessor));

		SinglePoolElement piece = SinglePoolElement.single(nbtPieceRL, processorHolder)
				.apply(StructureTemplatePool.Projection.RIGID);

		for (int i = 0; i < weight; i++) {
			pool.templates.add(piece);
		}

		List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
		listOfPieceEntries.add(new Pair<>(piece, weight));
		pool.rawTemplates = listOfPieceEntries;
	}
}
