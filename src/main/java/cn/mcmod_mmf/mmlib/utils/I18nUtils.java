package cn.mcmod_mmf.mmlib.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class I18nUtils {
    
    public static Component chanceComponent(float chance){
        return new TranslatableComponent("mm_lib.jei.chance", chance < 0.01 ? "<1" : (int) (chance * 100))
                .withStyle(ChatFormatting.GOLD);
    }
    
}
