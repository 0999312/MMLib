package cn.mcmod_mmf.mmlib.data.loot;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.fml.util.ObfuscationReflectionHelper;

public abstract class AbstartctBlockLoot extends BlockLootSubProvider {
    
	private final Set<Block> knownBlocks = new HashSet<>();
	// [VanillaCopy] super
	public static final float[] DEFAULT_SAPLING_DROP_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
	public static final float[] RARE_SAPLING_DROP_RATES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};

	private static final Set<Item> EXPLOSION_RESISTANT = Set.of();


	protected AbstartctBlockLoot(HolderLookup.Provider p_344943_) {
		super(EXPLOSION_RESISTANT, FeatureFlags.REGISTRY.allFlags(), p_344943_);
	}


    @Override
    protected void add(Block block, LootTable.Builder builder) {
        super.add(block, builder);
        knownBlocks.add(block);
    }
    
    public abstract void addTables();
    
    @Override
    protected void generate() {
        this.addTables();
    }

	// [VanillaCopy] super.droppingWithChancesAndSticks, but non-silk touch parameter can be an item instead of a block
	public LootTable.Builder silkAndStick(Block block, ItemLike nonSilk, float... nonSilkFortune) {
		LootItemCondition.Builder NOT_SILK_TOUCH_OR_SHEARS = ObfuscationReflectionHelper.getPrivateValue(BlockLootSubProvider.class, null, "HAS_NO_SHEARS_OR_SILK_TOUCH");
		return createSilkTouchOrShearsDispatchTable(block, applyExplosionCondition(block, LootItem.lootTableItem(nonSilk.asItem())).when(BonusLevelTableCondition.bonusLevelFlatChance(this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), nonSilkFortune))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(NOT_SILK_TOUCH_OR_SHEARS).add(applyExplosionDecay(block, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).when(BonusLevelTableCondition.bonusLevelFlatChance(this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
	}

	public void registerEmpty(Block b) {
		add(b, LootTable.lootTable());
	}

	public void registerSlab(Block b) {
		add(b, createSlabItemTable(b));
	}

    @Override
    public Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }
}
