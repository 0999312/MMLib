package cn.mcmod_mmf.mmlib;

import net.minecraftforge.common.ForgeConfigSpec;

public class MMLibConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.BooleanValue INFO;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("General settings").push("general");
        INFO = COMMON_BUILDER.comment("Whether to enable Welcome Info.").define("welcome_info", false);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
