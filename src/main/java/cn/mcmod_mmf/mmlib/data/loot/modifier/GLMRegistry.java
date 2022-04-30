package cn.mcmod_mmf.mmlib.data.loot.modifier;

import cn.mcmod_mmf.mmlib.Main;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GLMRegistry {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister
            .create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, Main.MODID);
    public static final RegistryObject<AddLootTableModifier.Serializer> ADD_LOOT_TABLE = GLM.register("add_loot_table",
            AddLootTableModifier.Serializer::new);
}
