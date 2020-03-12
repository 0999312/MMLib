package cn.mcmod_mmf.mmlib;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Config(modid="mm_lib")
@EventBusSubscriber(modid="mm_lib")
public class MMLibConfig {
    @Config.LangKey("mm_lib.config.welcome_info")
    @Config.RequiresMcRestart
    @Config.Comment("Whether to enable Welcome Info.")
    public static boolean info=true;
}
