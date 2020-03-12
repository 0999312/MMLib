package cn.mcmod_mmf.mmlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@EventBusSubscriber
@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, dependencies = "required-after:forge@[14.23.5.2847,);")
public class Main {
	public static final String MODID = "mm_lib";
	public static final String NAME = "Mysterious Mountain Lib";
	public static final String VERSION = "@version@";

	private static final Logger logger = LogManager.getLogger(NAME);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger.info("Presented by Zaia");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
	}

	public static Logger getLogger() {
		return logger;
	}
	
	@SubscribeEvent
	public static void welcomeUsing(PlayerLoggedInEvent event) {
        if (MMLibConfig.info) {
    		String json = I18n.translateToLocal("mm_lib.info.welcome");
    		json = json.replaceAll("%name%", event.player.getName());
    		ITextComponent component = ITextComponent.Serializer.jsonToComponent(json);
            event.player.sendMessage(component);
        }
	}
}
