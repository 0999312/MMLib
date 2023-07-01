package cn.mcmod_mmf.mmlib;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.IEventBus;
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

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import cn.mcmod_mmf.mmlib.data.loot.modifier.GLMRegistry;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Main.MODID)
public class Main {
    public static final String MODID = "mysterious_mountain_lib";

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
            MODID);
    public static final RegistryObject<SoundEvent> presented_by_zaia = SOUNDS.register("presented_by_zaia",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "presented_by_zaia")));

    public Main() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        SOUNDS.register(modEventBus);
        GLMRegistry.GLM.register(modEventBus);
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
            Component component = Component.translatable(key, event.getEntity().getName().getString());
            event.getEntity().displayClientMessage(component, false);
            if (april_first) {
                if (event.getEntity() instanceof ServerPlayer player) {
                    player.connection.send(new ClientboundSoundPacket(
                            presented_by_zaia.getHolder().get(), SoundSource.PLAYERS, player.getX(),
                            player.getY(), player.getZ(), 1F, 1F, player.getRandom().nextLong()));
                }
            }
        }
    }
}
