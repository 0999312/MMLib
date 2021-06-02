package cn.mcmod_mmf.mmlib;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SPlaySoundPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.time.LocalDateTime;
import java.time.Month;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("mmlib")
public class Main {
	private static final Logger LOGGER = LogManager.getLogger();
	
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,"mmlib");
    public static final RegistryObject<SoundEvent> presented_by_zaia = SOUNDS.register("presented_by_zaia", () -> new SoundEvent(new ResourceLocation("mmlib", "presented_by_zaia")));
	
    public Main() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		MinecraftForge.EVENT_BUS.register(this);
		SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MMLibConfig.COMMON_CONFIG);
	}

	private void setup(final FMLCommonSetupEvent event) {
		LOGGER.info("Presented by Zaia");
	}

	public static Logger getLogger() {
		return LOGGER;
	}

	@SubscribeEvent
	public void welcomeUsing(PlayerLoggedInEvent event) {
		boolean april_first = false;
		LocalDateTime now = LocalDateTime.now();
		if (now.getMonth() == Month.APRIL && now.getDayOfMonth() == 1)
			april_first = true;
		if (MMLibConfig.INFO.get()) {
			String key = april_first ? "mm_lib.info.welcome_foolish" : "mm_lib.info.welcome";
			ITextComponent component = new TranslationTextComponent(key, event.getPlayer().getName().getString());
			event.getPlayer().sendMessage(component, event.getPlayer().getUUID());
			if (april_first) {
				if (event.getPlayer() instanceof ServerPlayerEntity) {
					((ServerPlayerEntity) event.getPlayer()).connection.send(
							new SPlaySoundPacket(presented_by_zaia.getId(), SoundCategory.PLAYERS, event.getPlayer().position(), 1F, 1F)
					);
				}
			}
		}

	}
}
