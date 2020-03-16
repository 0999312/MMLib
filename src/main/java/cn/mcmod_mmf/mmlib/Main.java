package cn.mcmod_mmf.mmlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.oredict.OreDictionary;

@EventBusSubscriber
@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, dependencies = "required-after:forge@[14.23.5.2847,);after:tfc;")
public class Main {
	public static final String MODID = "mm_lib";
	public static final String NAME = "Mysterious Mountain Lib";
	public static final String VERSION = "@version@";

	private static final Logger logger = LogManager.getLogger(NAME);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger.info("Presented by Zaia");
		registerVanillaFoods();
		if(Loader.isModLoaded("tfc"))
			;
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
	private static void registerVanillaFoods() {
		  OreDictionary.registerOre("listAllchickenraw", Items.CHICKEN);
		  OreDictionary.registerOre("listAllegg", Items.EGG);
		  OreDictionary.registerOre("listAllchickencooked", Items.COOKED_CHICKEN);
		  OreDictionary.registerOre("listAllporkraw", Items.PORKCHOP);
		  OreDictionary.registerOre("listAllporkcooked", Items.COOKED_PORKCHOP);
		  OreDictionary.registerOre("listAllbeefraw", Items.BEEF);
		  OreDictionary.registerOre("listAllbeefcooked", Items.COOKED_BEEF);
		  OreDictionary.registerOre("listAllmuttonraw", Items.MUTTON);
		  OreDictionary.registerOre("listAllmuttoncooked", Items.COOKED_MUTTON);
		  OreDictionary.registerOre("listAllrabbitraw", Items.RABBIT);
		  OreDictionary.registerOre("listAllrabbitcooked", Items.COOKED_RABBIT);
		  OreDictionary.registerOre("bread", Items.BREAD);
		  OreDictionary.registerOre("foodBread", Items.BREAD);
		  OreDictionary.registerOre("cropCarrot", Items.CARROT);
		  OreDictionary.registerOre("cropPotato", Items.POTATO);
		  OreDictionary.registerOre("cropPumpkin", Blocks.PUMPKIN);
		  OreDictionary.registerOre("cropWheat", Items.WHEAT);
		  OreDictionary.registerOre("listAllmushroom", Blocks.RED_MUSHROOM);
		  OreDictionary.registerOre("listAllmushroom", Blocks.BROWN_MUSHROOM);
		  OreDictionary.registerOre("cropBeet", Items.BEETROOT);
		  OreDictionary.registerOre("listAllgrain", Items.WHEAT);
		  OreDictionary.registerOre("cropApple", Items.APPLE);
		  OreDictionary.registerOre("listAllfruit", Items.APPLE);
		  OreDictionary.registerOre("listAllfruit", Items.CHORUS_FRUIT);
		  OreDictionary.registerOre("listAllfruit", Items.MELON);
		  OreDictionary.registerOre("listAllmeatraw", Items.BEEF);
		  OreDictionary.registerOre("listAllmeatraw", Items.CHICKEN);
		  OreDictionary.registerOre("listAllmeatraw", Items.PORKCHOP);
		  OreDictionary.registerOre("listAllmeatraw", Items.MUTTON);
		  OreDictionary.registerOre("listAllmeatraw", Items.RABBIT);
		  OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_BEEF);
		  OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_CHICKEN);
		  OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_PORKCHOP);
		  OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_MUTTON);
		  OreDictionary.registerOre("listAllmeatcooked", Items.COOKED_RABBIT);
		  OreDictionary.registerOre("listAllfishraw", new ItemStack(Items.FISH, 1, 32767));
		  OreDictionary.registerOre("listAllfishfresh", new ItemStack(Items.FISH, 1, 32767));
		  OreDictionary.registerOre("listAllfishcooked", Items.COOKED_FISH);
		  OreDictionary.registerOre("listAllfishcooked", new ItemStack(Items.COOKED_FISH, 1, 1));
		  OreDictionary.registerOre("salmonRaw", new ItemStack(Items.FISH, 1));
		  OreDictionary.registerOre("listAllveggie", Items.CARROT);
		  OreDictionary.registerOre("listAllveggie", Items.POTATO);
		  OreDictionary.registerOre("listAllveggie", Blocks.PUMPKIN);
		  OreDictionary.registerOre("listAllveggie", Items.BEETROOT);
		  OreDictionary.registerOre("listAllsugar", Items.SUGAR);
		  OreDictionary.registerOre("cropBeet", Items.BEETROOT);
		  OreDictionary.registerOre("seedBeet", Items.BEETROOT_SEEDS);
		  OreDictionary.registerOre("listAllwater", Items.WATER_BUCKET);
		  OreDictionary.registerOre("listAllmilk", Items.MILK_BUCKET);
		  OreDictionary.registerOre("listAllsugar", Items.SUGAR);
		  if(!Loader.isModLoaded("sakura"))
			  OreDictionary.registerOre("sakuraLeaves", new ItemStack(Items.DYE,1,9));
	}
	
}
