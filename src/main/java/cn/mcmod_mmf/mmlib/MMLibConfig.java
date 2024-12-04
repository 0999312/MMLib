package cn.mcmod_mmf.mmlib;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MMLibConfig {
    public static ModConfigSpec COMMON_CONFIG;
    public static ModConfigSpec.BooleanValue INFO;

    static {
    	ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
        COMMON_BUILDER.comment("General settings").push("general");
        INFO = COMMON_BUILDER.comment("Whether to enable Welcome Info.").define("welcome_info", false);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
