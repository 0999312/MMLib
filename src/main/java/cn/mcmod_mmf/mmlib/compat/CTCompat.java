package cn.mcmod_mmf.mmlib.compat;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import cn.mcmod_mmf.mmlib.Main;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional.Method;

public class CTCompat {
	private static final CTCompat instance = new CTCompat();
	private CTCompat() {
		
	}
	public static CTCompat getInstance() {
		return instance;
	}
	public List<IAction> actions = new ArrayList<IAction>();
	public void Init() {
		if (Loader.isModLoaded("crafttweaker"))
            doDelayTask();
		actions = null;
	}
	public void addAction(IAction action) {
        actions.add(action);
    }

    @Method(modid = "crafttweaker")
    public void doDelayTask() {
        for (IAction act : actions) {
            CraftTweakerAPI.apply(act);
            if (act.describe() != null)
            	Main.getLogger().log(Level.INFO, act.describe());
        }
        actions.clear();
    }
}
