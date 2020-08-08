package cn.mcmod_mmf.mmlib;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.objects.blocks.plants.BlockPlantTFC;
import net.dries007.tfc.objects.items.food.ItemFoodTFC;
import net.dries007.tfc.types.DefaultPlants;
import net.dries007.tfc.util.agriculture.Food;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.oredict.OreDictionary;

@EventBusSubscriber
@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, dependencies = "required-after:forge@[14.23.5.2847,);after:tfc@[1.0.6.133,);")
public class Main {
	public static final String MODID = "mm_lib";
	public static final String NAME = "Mysterious Mountain Lib";
	public static final String VERSION = "@version@";

	private static final Logger logger = LogManager.getLogger(NAME);
	public static final Calendar CALENDER = Calendar.getInstance();
	
	public static final SoundEvent ZAIA_ENTERPRISE = new SoundEvent(new ResourceLocation("mm_lib", "presented_by_zaia"));
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger.info("Presented by Zaia");
		registerVanillaFoods();
        EntityRegistry.registerModEntity(new ResourceLocation("mmlib:seat_block"), EntitySeat.class, "SeatBlock", 0, this, 80, 1, false);
	}
	@SubscribeEvent
	public static void onSoundEvenrRegistration(RegistryEvent.Register<SoundEvent> event) {
	    event.getRegistry().register(ZAIA_ENTERPRISE.setRegistryName(new ResourceLocation("mm_lib", "presented_by_zaia")));
	}
	@EventHandler
	public void init(FMLInitializationEvent event) {
		if(Loader.isModLoaded("tfc"))
			registerTFCOreDict();
	}

	public static Logger getLogger() {
		return logger;
	}
	
	@SubscribeEvent
	public static void welcomeUsing(PlayerLoggedInEvent event) {
    	boolean april_first = false;
    	if (CALENDER.get(2) + 1 == 4 && CALENDER.get(5) == 1)
    		april_first = true;
        if (MMLibConfig.info) {
    		String json = april_first?I18n.translateToLocal("mm_lib.info.welcome_foolish"):I18n.translateToLocal("mm_lib.info.welcome");
    		json = json.replaceAll("%name%", event.player.getName());
    		ITextComponent component = ITextComponent.Serializer.jsonToComponent(json);
            event.player.sendMessage(component);
        }
        if(april_first){
        	if(event.player instanceof EntityPlayerMP){
        		((EntityPlayerMP)event.player).connection.sendPacket(new SPacketCustomSound(ZAIA_ENTERPRISE.getRegistryName().toString(), SoundCategory.PLAYERS, event.player.posX, event.player.posY, event.player.posZ, 1F, 1F));
        	}
        }
	}
	@Method(modid = "tfc")
	private void registerTFCOreDict(){
		OreDictionary.registerOre("listAllchickenraw", ItemFoodTFC.get(Food.CHICKEN));
		OreDictionary.registerOre("listAllchickencooked", ItemFoodTFC.get(Food.COOKED_CHICKEN));
		OreDictionary.registerOre("listAllporkraw", ItemFoodTFC.get(Food.PORK));
		OreDictionary.registerOre("listAllporkcooked", ItemFoodTFC.get(Food.COOKED_PORK));
		OreDictionary.registerOre("listAllbeefraw", ItemFoodTFC.get(Food.BEEF));
		OreDictionary.registerOre("listAllbeefcooked", ItemFoodTFC.get(Food.COOKED_BEEF));
		OreDictionary.registerOre("listAllmuttonraw", ItemFoodTFC.get(Food.MUTTON));
		OreDictionary.registerOre("listAllmuttoncooked", ItemFoodTFC.get(Food.COOKED_MUTTON));
		OreDictionary.registerOre("listAllrabbitraw", ItemFoodTFC.get(Food.RABBIT));
		OreDictionary.registerOre("listAllrabbitcooked", ItemFoodTFC.get(Food.COOKED_RABBIT));
		
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.CHICKEN));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_CHICKEN));
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.PORK));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_PORK));
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.BEEF));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_BEEF));
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.MUTTON));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_MUTTON));
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.RABBIT));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_RABBIT));
		
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.BEAR));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_BEAR));
		OreDictionary.registerOre("listAllfishraw", ItemFoodTFC.get(Food.CALAMARI));
		OreDictionary.registerOre("listAllfishcooked", ItemFoodTFC.get(Food.COOKED_CALAMARI));
		OreDictionary.registerOre("listAllfishfresh", ItemFoodTFC.get(Food.CALAMARI));
		OreDictionary.registerOre("foodCalamariraw", ItemFoodTFC.get(Food.CALAMARI));
		OreDictionary.registerOre("foodCalamaricooked", ItemFoodTFC.get(Food.COOKED_CALAMARI));
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.HORSE_MEAT));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_HORSE_MEAT));
		
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.PHEASANT));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_PHEASANT));
		OreDictionary.registerOre("listAllturkeyraw", ItemFoodTFC.get(Food.PHEASANT));
		OreDictionary.registerOre("listAllturkeycooked", ItemFoodTFC.get(Food.COOKED_PHEASANT));
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.WOLF));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_WOLF));
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.VENISON));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_VENISON));
		OreDictionary.registerOre("listAllvenisonraw", ItemFoodTFC.get(Food.VENISON));
		OreDictionary.registerOre("listAllvenisoncooked", ItemFoodTFC.get(Food.COOKED_VENISON));
		
		OreDictionary.registerOre("listAllduckraw", ItemFoodTFC.get(Food.DUCK));
		OreDictionary.registerOre("listAllduckcooked", ItemFoodTFC.get(Food.COOKED_DUCK));
		OreDictionary.registerOre("listAllmuttonraw", ItemFoodTFC.get(Food.CHEVON));
		OreDictionary.registerOre("listAllmuttoncooked", ItemFoodTFC.get(Food.COOKED_CHEVON));
		
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.DUCK));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_DUCK));
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.CHEVON));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_CHEVON));
		OreDictionary.registerOre("slabCobblestone", new ItemStack(Blocks.STONE_SLAB,1,3));
		OreDictionary.registerOre("listAllmeatraw", ItemFoodTFC.get(Food.GRAN_FELINE));
		OreDictionary.registerOre("listAllmeatcooked", ItemFoodTFC.get(Food.COOKED_GRAN_FELINE));
		
		OreDictionary.registerOre("listAllfishraw", ItemFoodTFC.get(Food.FISH));
		OreDictionary.registerOre("listAllfishfresh", ItemFoodTFC.get(Food.FISH));
		OreDictionary.registerOre("listAllfishcooked", ItemFoodTFC.get(Food.COOKED_FISH));
		
		OreDictionary.registerOre("cropWheat", ItemFoodTFC.get(Food.WHEAT_GRAIN));
		OreDictionary.registerOre("cropRice", ItemFoodTFC.get(Food.RICE_GRAIN));
		OreDictionary.registerOre("cropRye", ItemFoodTFC.get(Food.RYE_GRAIN));
		OreDictionary.registerOre("cropCorn", ItemFoodTFC.get(Food.MAIZE));
		OreDictionary.registerOre("cropOats", ItemFoodTFC.get(Food.OAT_GRAIN));
		OreDictionary.registerOre("cropBarley", ItemFoodTFC.get(Food.BARLEY_GRAIN));
		
		OreDictionary.registerOre("foodFlour", ItemFoodTFC.get(Food.WHEAT_FLOUR));
		OreDictionary.registerOre("foodFlour", ItemFoodTFC.get(Food.RICE_FLOUR));
		OreDictionary.registerOre("foodFlour", ItemFoodTFC.get(Food.RYE_FLOUR));
		OreDictionary.registerOre("foodFlour", ItemFoodTFC.get(Food.CORNMEAL_FLOUR));
		OreDictionary.registerOre("foodFlour", ItemFoodTFC.get(Food.OAT_FLOUR));
		OreDictionary.registerOre("foodFlour", ItemFoodTFC.get(Food.BARLEY_FLOUR));
		
		OreDictionary.registerOre("listAllgrain", ItemFoodTFC.get(Food.WHEAT_GRAIN));
		OreDictionary.registerOre("listAllgrain", ItemFoodTFC.get(Food.BARLEY_GRAIN));
		OreDictionary.registerOre("listAllgrain", ItemFoodTFC.get(Food.RICE_GRAIN));
		OreDictionary.registerOre("listAllgrain", ItemFoodTFC.get(Food.OAT_GRAIN));
		OreDictionary.registerOre("listAllgrain", ItemFoodTFC.get(Food.RYE_GRAIN));
		OreDictionary.registerOre("listAllgrain", ItemFoodTFC.get(Food.MAIZE));
		
		OreDictionary.registerOre("foodDough", ItemFoodTFC.get(Food.WHEAT_DOUGH));
		OreDictionary.registerOre("foodDough", ItemFoodTFC.get(Food.RICE_DOUGH));
		OreDictionary.registerOre("foodDough", ItemFoodTFC.get(Food.RYE_DOUGH));
		OreDictionary.registerOre("foodDough", ItemFoodTFC.get(Food.CORNMEAL_DOUGH));
		OreDictionary.registerOre("foodDough", ItemFoodTFC.get(Food.OAT_DOUGH));
		OreDictionary.registerOre("foodDough", ItemFoodTFC.get(Food.BARLEY_DOUGH));
		OreDictionary.registerOre("bread", ItemFoodTFC.get(Food.BARLEY_BREAD));
		OreDictionary.registerOre("foodBread", ItemFoodTFC.get(Food.BARLEY_BREAD));
		OreDictionary.registerOre("bread", ItemFoodTFC.get(Food.CORNBREAD));
		OreDictionary.registerOre("foodBread", ItemFoodTFC.get(Food.CORNBREAD));
		OreDictionary.registerOre("bread", ItemFoodTFC.get(Food.OAT_BREAD));
		OreDictionary.registerOre("foodBread", ItemFoodTFC.get(Food.OAT_BREAD));
		OreDictionary.registerOre("bread", ItemFoodTFC.get(Food.RICE_BREAD));
		OreDictionary.registerOre("foodBread", ItemFoodTFC.get(Food.RICE_BREAD));
		OreDictionary.registerOre("bread", ItemFoodTFC.get(Food.RYE_BREAD));
		OreDictionary.registerOre("foodBread", ItemFoodTFC.get(Food.RYE_BREAD));
		OreDictionary.registerOre("bread", ItemFoodTFC.get(Food.WHEAT_BREAD));
		OreDictionary.registerOre("foodBread", ItemFoodTFC.get(Food.WHEAT_BREAD));
		
		OreDictionary.registerOre("cropCarrot", ItemFoodTFC.get(Food.CARROT));
		OreDictionary.registerOre("cropPotato", ItemFoodTFC.get(Food.POTATO));
		OreDictionary.registerOre("cropTomato", ItemFoodTFC.get(Food.TOMATO));
		OreDictionary.registerOre("cropOnion", ItemFoodTFC.get(Food.ONION));
		OreDictionary.registerOre("cropGarlic", ItemFoodTFC.get(Food.GARLIC));
		OreDictionary.registerOre("cropSeaweed", ItemFoodTFC.get(Food.SEAWEED));
		OreDictionary.registerOre("cropCabbage", ItemFoodTFC.get(Food.CABBAGE));
		OreDictionary.registerOre("cropSoybean", ItemFoodTFC.get(Food.SOYBEAN));
		OreDictionary.registerOre("cropWinterSquash", ItemFoodTFC.get(Food.SQUASH));
		OreDictionary.registerOre("cropBellpepper", ItemFoodTFC.get(Food.GREEN_BELL_PEPPER));
		OreDictionary.registerOre("cropBellpepper", ItemFoodTFC.get(Food.RED_BELL_PEPPER));
		OreDictionary.registerOre("cropBellpepper", ItemFoodTFC.get(Food.YELLOW_BELL_PEPPER));
		OreDictionary.registerOre("cropBeet", ItemFoodTFC.get(Food.BEET));
		
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.CARROT));
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.POTATO));
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.TOMATO));
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.ONION));
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.GARLIC));
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.SEAWEED));
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.CABBAGE));
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.SOYBEAN));
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.SQUASH));
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.GREEN_BELL_PEPPER));
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.RED_BELL_PEPPER));
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.YELLOW_BELL_PEPPER));
		OreDictionary.registerOre("listAllveggie", ItemFoodTFC.get(Food.BEET));

		OreDictionary.registerOre("listAllmushroom", BlockPlantTFC.get(TFCRegistries.PLANTS.getValue(DefaultPlants.PORCINI)));

		OreDictionary.registerOre("cropApple", ItemFoodTFC.get(Food.RED_APPLE));
		OreDictionary.registerOre("cropApple", ItemFoodTFC.get(Food.GREEN_APPLE));
		OreDictionary.registerOre("cropBanana", ItemFoodTFC.get(Food.BANANA));
		OreDictionary.registerOre("cropLemon", ItemFoodTFC.get(Food.LEMON));
		OreDictionary.registerOre("cropOrange", ItemFoodTFC.get(Food.ORANGE));
		OreDictionary.registerOre("cropPlum", ItemFoodTFC.get(Food.PLUM));
		OreDictionary.registerOre("cropStrawberry", ItemFoodTFC.get(Food.STRAWBERRY));
		OreDictionary.registerOre("cropCherry", ItemFoodTFC.get(Food.CHERRY));
		OreDictionary.registerOre("cropBlackberry", ItemFoodTFC.get(Food.BLACKBERRY));
		OreDictionary.registerOre("cropBlueberry", ItemFoodTFC.get(Food.BLUEBERRY));
		OreDictionary.registerOre("cropGooseberry", ItemFoodTFC.get(Food.GOOSEBERRY));
		OreDictionary.registerOre("cropRaspberry", ItemFoodTFC.get(Food.RASPBERRY));
		OreDictionary.registerOre("cropCranberry", ItemFoodTFC.get(Food.CRANBERRY));
		OreDictionary.registerOre("cropElderberry", ItemFoodTFC.get(Food.ELDERBERRY));
		
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.RED_APPLE));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.GREEN_APPLE));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.BANANA));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.LEMON));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.ORANGE));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.PLUM));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.CHERRY));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.STRAWBERRY));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.BLACKBERRY));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.BLUEBERRY));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.GOOSEBERRY));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.RASPBERRY));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.CRANBERRY));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.ELDERBERRY));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.BUNCH_BERRY));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.CLOUD_BERRY));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.WINTERGREEN_BERRY));
		OreDictionary.registerOre("listAllfruit", ItemFoodTFC.get(Food.SNOW_BERRY));
		
		OreDictionary.registerOre("listAllberry", ItemFoodTFC.get(Food.STRAWBERRY));
		OreDictionary.registerOre("listAllberry", ItemFoodTFC.get(Food.BLACKBERRY));
		OreDictionary.registerOre("listAllberry", ItemFoodTFC.get(Food.BLUEBERRY));
		OreDictionary.registerOre("listAllberry", ItemFoodTFC.get(Food.GOOSEBERRY));
		OreDictionary.registerOre("listAllberry", ItemFoodTFC.get(Food.RASPBERRY));
		OreDictionary.registerOre("listAllberry", ItemFoodTFC.get(Food.CRANBERRY));
		OreDictionary.registerOre("listAllberry", ItemFoodTFC.get(Food.ELDERBERRY));
		OreDictionary.registerOre("listAllberry", ItemFoodTFC.get(Food.BUNCH_BERRY));
		OreDictionary.registerOre("listAllberry", ItemFoodTFC.get(Food.CLOUD_BERRY));
		OreDictionary.registerOre("listAllberry", ItemFoodTFC.get(Food.WINTERGREEN_BERRY));
		OreDictionary.registerOre("listAllberry", ItemFoodTFC.get(Food.SNOW_BERRY));
		OreDictionary.registerOre("foodCheese", ItemFoodTFC.get(Food.CHEESE));
		
	}
	private void registerVanillaFoods() {
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
		  OreDictionary.registerOre("slabCobblestone", new ItemStack(Blocks.STONE_SLAB,1,3));
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
		  OreDictionary.registerOre("blockMossy", Blocks.MOSSY_COBBLESTONE);
		  if(!Loader.isModLoaded("sakura"))
			  OreDictionary.registerOre("sakuraLeaves", new ItemStack(Items.DYE,1,9));
	}
	
}
