package cn.mcmod_mmf.mmlib.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.data.LanguageProvider;

public abstract class AbstractLangProvider extends LanguageProvider {
    private final String mod_id;
    public AbstractLangProvider(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
        this.mod_id = modid;
    }
    
    public void addItemGroup(ItemGroup group, String name) {
        add(group.getDisplayName().getString(), name);
    }

    public void addAdvTitle(String advancementTitle, String name) {
        add("advancement." + advancementTitle + ".title", name);
    }

    public void addAdvDesc(String advancementTitle, String name) {
        add("advancement." + advancementTitle + ".desc", name);
    }

    public void addSubtitle(String category, String subtitleName, String name) {
        add("subtitles." + category + "." + subtitleName, name);
    }

    public void addBiome(RegistryKey<Biome> biomeKey, String name) {
        add("biome." + biomeKey.location().getNamespace() + "." + biomeKey.location().getPath(), name);
    }

    public void addDeath(String deathName, String name) {
        add("death.attack." + deathName, name);
    }
    
    public void addTooltip(String tooltipName, String name) {
        add("tooltip."+ getModid() + tooltipName, name);
    }

    public String getModid() {
        return mod_id;
    }

}
