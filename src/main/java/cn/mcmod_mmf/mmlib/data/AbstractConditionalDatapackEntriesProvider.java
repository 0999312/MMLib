package cn.mcmod_mmf.mmlib.data;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.DataPackRegistriesHooks;

public abstract class AbstractConditionalDatapackEntriesProvider implements DataProvider {
	private static final Logger LOGGER = LogUtils.getLogger();
	private final PackOutput output;
	private final CompletableFuture<HolderLookup.Provider> registries;
	private final java.util.function.Predicate<String> namespacePredicate;

	public AbstractConditionalDatapackEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries,
			Set<String> modIds) {
		this.namespacePredicate = modIds == null ? namespace -> true : modIds::contains;
		this.registries = registries;
		this.output = output;
	}

	public AbstractConditionalDatapackEntriesProvider(PackOutput output, CompletableFuture<Provider> registries,
			RegistrySetBuilder datapackEntriesBuilder, Set<String> modIds) {
		this(output, registries.thenApply(r -> constructDatapackRegistries(r, datapackEntriesBuilder)), modIds);
	}

	private static HolderLookup.Provider constructDatapackRegistries(HolderLookup.Provider original,
			RegistrySetBuilder datapackEntriesBuilder) {
		var builderKeys = new HashSet<>(datapackEntriesBuilder.getEntryKeys());
		DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().filter(data -> !builderKeys.contains(data.key()))
				.forEach(data -> datapackEntriesBuilder.add(data.key(), context -> {
				}));
		return datapackEntriesBuilder.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY),
				original);
	}

	public abstract <T> Map<ResourceKey<T>, List<ICondition>> getConditions();

	public CompletableFuture<?> run(CachedOutput pOutput) {
		return this.registries.thenCompose((p_256533_) -> {
			DynamicOps<JsonElement> dynamicops = RegistryOps.create(JsonOps.INSTANCE, p_256533_);
			return CompletableFuture.allOf(net.minecraftforge.registries.DataPackRegistriesHooks
					.getDataPackRegistriesWithDimensions().flatMap((p_256552_) -> {
						return this.dumpRegistry(pOutput, p_256533_, dynamicops, p_256552_).stream();
					}).toArray((p_255809_) -> {
						return new CompletableFuture[p_255809_];
					}));
		});
	}

	private <T> Optional<CompletableFuture<?>> dumpRegistry(CachedOutput pOutput, HolderLookup.Provider pRegistries,
			DynamicOps<JsonElement> pOps, RegistryDataLoader.RegistryData<T> pRegistryData) {
		ResourceKey<? extends Registry<T>> resourcekey = pRegistryData.key();
		return pRegistries.lookup(resourcekey).map((p_255847_) -> {
			PackOutput.PathProvider packoutput$pathprovider = this.output.createPathProvider(
					PackOutput.Target.DATA_PACK,
					net.minecraftforge.common.ForgeHooks.prefixNamespace(resourcekey.location()));
			return CompletableFuture.allOf(p_255847_.listElements()
					.filter(holder -> this.namespacePredicate.test(holder.key().location().getNamespace()))
					.map((reference) -> {
						JsonArray conditions = new JsonArray();
						if (getConditions().containsKey(reference.key())) {
							for (ICondition c : getConditions().get(reference.key()))
								conditions.add(CraftingHelper.serialize(c));
						}
						return dumpConditionalValue(packoutput$pathprovider.json(reference.key().location()), pOutput,
								pOps, pRegistryData.elementCodec(), reference.value(), conditions);
					}).toArray((p_256279_) -> {
						return new CompletableFuture[p_256279_];
					}));
		});
	}

	private static <E> CompletableFuture<?> dumpConditionalValue(Path pValuePath, CachedOutput pOutput,
			DynamicOps<JsonElement> pOps, Encoder<E> pEncoder, E pValue, JsonArray conditions) {

		Optional<JsonElement> optional = pEncoder.encodeStart(pOps, pValue).resultOrPartial((p_255999_) -> {
			LOGGER.error("Couldn't serialize element {}: {}", pValuePath, p_255999_);
		});

		if (optional.isPresent()) {
			var result = optional.get().getAsJsonObject();
			if (!conditions.isJsonNull() && !conditions.isEmpty())
				result.add("forge:conditions", conditions);
			return DataProvider.saveStable(pOutput, result, pValuePath);
		}

		return CompletableFuture.completedFuture((Object) null);
	}

}
