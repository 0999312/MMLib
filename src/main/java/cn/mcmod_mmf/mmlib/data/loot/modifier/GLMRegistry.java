package cn.mcmod_mmf.mmlib.data.loot.modifier;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;

import cn.mcmod_mmf.mmlib.Main;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class GLMRegistry {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Main.MODID);
    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ADD_LOOT_TABLE = GLM.register("add_loot_table", AddLootTableModifier.CODEC);
}
