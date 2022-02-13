package cn.mcmod_mmf.mmlib;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.time.LocalDateTime;
import java.time.Month;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main {
    public static final String MODID = "mmlib";

    private static final Logger LOGGER = LogManager.getLogger();

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
            MODID);
    public static final RegistryObject<SoundEvent> presented_by_zaia = SOUNDS.register("presented_by_zaia",
            () -> new SoundEvent(new ResourceLocation(MODID, "presented_by_zaia")));

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
            Component component = new TranslatableComponent(key, event.getPlayer().getName().getString());
            event.getPlayer().sendMessage(component, event.getPlayer().getUUID());
            if (april_first) {
                if (event.getPlayer() instanceof ServerPlayer) {
                    ((ServerPlayer) event.getPlayer()).connection.send(new ClientboundSoundPacket(
                            presented_by_zaia.get(), SoundSource.PLAYERS, event.getPlayer().getX(),
                            event.getPlayer().getY(), event.getPlayer().getZ(), 1F, 1F));
                }
            }
        }

    }
}
