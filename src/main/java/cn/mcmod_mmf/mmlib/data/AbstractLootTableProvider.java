package cn.mcmod_mmf.mmlib.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ForgeLootTableProvider;

public abstract class AbstractLootTableProvider extends ForgeLootTableProvider {

//    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> tables = ImmutableList
//            .of(Pair.of(FishingProvider::new, LootParameterSets.FISHING),
//                Pair.of(ChestProvider::new, LootParameterSets.CHEST),
//                Pair.of(EntityProvider::new, LootParameterSets.ENTITY),
//                Pair.of(BlockProvider::new, LootParameterSets.BLOCK),
//                Pair.of(PiglinBarteringProvider::new, LootParameterSets.PIGLIN_BARTER),
//                Pair.of(GiftProvider::new, LootParameterSets.GIFT));

    public AbstractLootTableProvider(DataGenerator gen) {
        super(gen);
    }

}
