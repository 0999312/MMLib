package cn.mcmod_mmf.mmlib.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public abstract class AbstractLootTableProvider extends LootTableProvider {

    public AbstractLootTableProvider(DataGenerator gen) {
        super(gen);
    }
    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
        // [VanillaCopy] super, but remove call that checks that all vanilla tables are accounted for, because we aren't vanilla.
        // Except validation issues occur when attempting to generate loot tables from other loot tables (see: EntityLootTables)
        //map.forEach((id, builder) -> LootTableManager.validate(validationtracker, id, builder));
    }

    @Override
    protected abstract List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables();

    @Override
    public abstract String getName();
}
