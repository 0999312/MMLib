package cn.mcmod_mmf.mmlib.register;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import cn.mcmod_mmf.mmlib.Main;
import cn.mcmod_mmf.mmlib.recipe.UniversalFluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class MMLibRegistries {
	public static final IForgeRegistry<UniversalFluid> UNIVERSAL_FLUID = GameRegistry.findRegistry(UniversalFluid.class);

	static {
		// Make sure all public static final fields have values, should stop people from
		// prematurely loading this class.
		try {
			int publicStaticFinal = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL;

			for (Field field : MMLibRegistries.class.getFields()) {
				if (!field.getType().isAssignableFrom(IForgeRegistry.class)) {
					Main.getLogger().warn("Weird field? (Not a registry) {}", field);
					continue;
				}
				if ((field.getModifiers() & publicStaticFinal) != publicStaticFinal) {
					Main.getLogger().warn("Weird field? (not Public Static Final) {}", field);
					continue;
				}
				if (field.get(null) == null) {
					throw new RuntimeException(
							"Oh nooo! Someone tried to use the registries before they exist. Now everything is broken!");
				}
			}
		} catch (Exception e) {
			Main.getLogger().fatal("Fatal error! This is likely a programming mistake.", e);
			throw new RuntimeException(e);
		}
	}
}
