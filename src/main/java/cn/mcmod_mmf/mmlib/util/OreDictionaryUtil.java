package cn.mcmod_mmf.mmlib.util;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryUtil {
	private static final OreDictionaryUtil instance = new OreDictionaryUtil();

	private OreDictionaryUtil() {
	}

	public static OreDictionaryUtil getInstance() {
		return instance;
	}

	public final String listAllfiber = "listAllfiber";
	public final String listAllcookie = "listAllcookie";
	public final String listAllsoda = "listAllsoda";
	public final String listAlltofu = "listAlltofu";
	public final String listAllyogurt = "listAllyogurt";
	public final String listAlljuice = "listAlljuice";
	public final String foodBread = "foodBread";
	public final String listAllveggie = "listAllveggie";
	public final String listAllseed = "listAllseed";
	public final String listAllgrain = "listAllgrain";
	public final String listAllfruit = "listAllfruit";
	public final String listAllmeatraw = "listAllmeatraw";
	public final String listAllmeatcooked = "listAllmeatcooked";
	public final String listAllfishfresh = "listAllfishfresh";
	public final String listAllfishraw = "listAllfishraw";
	public final String listAllfishcooked = "listAllfishcooked";
	public final String listAllmushroom = "listAllmushroom";
	public final String listAllegg = "listAllegg";
	public final String listAllchickenraw = "listAllchickenraw";
	public final String listAllchickencooked = "listAllchickencooked";
	public final String listAllporkraw = "listAllporkraw";
	public final String listAllporkcooked = "listAllporkcooked";
	public final String listAllbeefraw = "listAllbeefraw";
	public final String listAllbeefcooked = "listAllbeefcooked";
	public final String listAllmuttonraw = "listAllmuttonraw";
	public final String listAllmuttoncooked = "listAllmuttoncooked";
	public final String listAllturkeyraw = "listAllturkeyraw";
	public final String listAllturkeycooked = "listAllturkeycooked";
	public final String listAllrabbitraw = "listAllrabbitraw";
	public final String listAllrabbitcooked = "listAllrabbitcooked";
	public final String listAllvenisonraw = "listAllvenisonraw";
	public final String listAllvenisoncooked = "listAllvenisoncooked";
	public final String listAllduckraw = "listAllduckraw";
	public final String listAllduckcooked = "listAllduckcooked";
	public final String listAllheavycream = "listAllheavycream";
	public final String listAllicecream = "listAllicecream";
	public final String listAllwater = "listAllwater";
	public final String listAllmilk = "listAllmilk";
	public final String listAllsugar = "listAllsugar";
	public final String listAllgreenveggie = "listAllgreenveggie";
	public final String listAllberry = "listAllberry";
	public final String listAllherb = "listAllherb";
	public final String listAllrootveggie = "listAllrootveggie";
	public final String listAllpepper = "listAllpepper";
	public final String listAllcitrus = "listAllcitrus";
	public final String listAllsmoothie = "listAllsmoothie";
	public final String listAllsoup = "listAllsoup";

	public void registerOreFromOreDict(String name, String list) {
		if (OreDictionary.doesOreNameExist(list))
			registerOredictionaryList(name, OreDictionary.getOres(list));
	}

	public void registerOredictionaryList(String name, List<ItemStack> items) {
		items.forEach((item) -> {
			OreDictionary.registerOre(name, item);
		});
	}

}
